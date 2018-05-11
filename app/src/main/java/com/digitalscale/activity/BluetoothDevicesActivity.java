package com.digitalscale.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalscale.BT.BTClientThread;
import com.digitalscale.BT.BTConstant;
import com.digitalscale.BT.BTDeviceConnectThread;
import com.digitalscale.BT.BTServerConnectThread;
import com.digitalscale.R;
import com.digitalscale.model.BTDeviceItem;
import com.digitalscale.tools.Constant;
import com.digitalscale.utility.BluetoothUtility;
import com.digitalscale.utility.FormulaUtility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.digitalscale.utility.BluetoothUtility.getBluetoothAdapter;

@EActivity(R.layout.activity_bluetooth_devices)
public class BluetoothDevicesActivity extends MasterActivity {

    @ViewById(R.id.switchBluetoothDeviceOnOff)
    Switch switchBluetoothDeviceOnOff;

    @ViewById(R.id.tvBluetoothDeviceOnOff)
    TextView tvBluetoothDeviceOnOff;

    @ViewById(R.id.tvBluetoothDeviceNoDeviceFound)
    TextView tvBluetoothDeviceNoDeviceFound;

    @ViewById(R.id.tvBluetoothDeviceScanDevice)
    TextView tvBluetoothDeviceScanDevice;

    @ViewById(R.id.lvBluetoothDeviceList)
    ListView lvBluetoothDeviceList;

    @ViewById(R.id.llBluetoothDeviceList)
    LinearLayout llBluetoothDeviceList;

    @ViewById(R.id.tvBluetoothOffMessage)
    TextView tvBluetoothOffMessage;

    @ViewById(R.id.tvTapToScanDevice)
    TextView tvTapToScanDevice;

    @ViewById(R.id.ivBluetoothDeviceBack)
    ImageView ivBluetoothDeviceBack;

    @ViewById(R.id.llScanning)
    LinearLayout llScanning;

    @ViewById(R.id.tvBluetoothDeviceConnectionStatus)
    public static TextView tvBluetoothDeviceConnectionStatus;

    ArrayList<String> allDevices;
    ArrayList<BluetoothDevice> devices;
    ArrayAdapter<String> bluetoothDeviceAdapter;
    Set<BluetoothDevice> pairedDevices;

    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    public BluetoothDevice deviceToConnect;

    private BluetoothSocket curBTSocket = null;
    public static BluetoothSocket forResetDeviceSocket = null;

    BTClientThread connectThread;
    BTDeviceConnectThread deviceConnectThread;
    BTServerConnectThread serverConnectThread;

    public static int weightReadingInGram;

    public static boolean isAddEditFoodActivityCreated = false;

    public static boolean isBluetoothDeviceActivityCreated = false;

    public static boolean isSocketConnected = false;

    private boolean from_basic_setting;


    @AfterViews
    public void init() {

        isBluetoothDeviceActivityCreated = true;

        renderViews();

        initialization();

        from_basic_setting = getIntent().getBooleanExtra("from_basic_setting", false);

        checkBTCompatibility();

        registerBroadcastReceiver();

        listener();

        BTOnOff();
    }

    /* Check bluetooth is ON / OFF*/
    private void BTOnOff() {
        if (mBluetoothAdapter.isEnabled()) {
            switchBluetoothDeviceOnOff.setChecked(true);
        } else {
            switchBluetoothDeviceOnOff.setChecked(false);
        }
    }

    /**
     * Check device support bluetooth or / not
     */
    private void checkBTCompatibility() {
        // Phone does not support Bluetooth so let the user know and exit.
        if (mBluetoothAdapter == null) {
            toastMessage(getString(R.string.toast_msg_phone_does_not_support_bt));
            finish();
        } else {
            //toastMessage("Your phone supports Bluetooth ");
        }
    }

    private void registerBroadcastReceiver() {
        // Broadcast when new BT device found
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReciever, filter);

        // Broadcast when new BT device state change like "ON","OFF","PAIRED","UNPAIRED".
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, filter1);
    }

    private void initialization() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Click on Back
     */
    @Click
    public void ivBluetoothDeviceBack() {
        if (from_basic_setting) {
            startActivity(new Intent(BluetoothDevicesActivity.this, MainActivity_.class));
        } else
        finish();
    }

    /* Click on "Bluetooth" text to scan device */
    @Click
    public void tvBluetoothDeviceScanDevice() {
        startDiscovery();
    }


    private void listener() {
        switchBluetoothDeviceOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // TODO: Bluetooth ON
                    tvBluetoothDeviceOnOff.setText(getString(R.string.lbl_on));
                    tvBluetoothOffMessage.setVisibility(View.GONE);
                    llBluetoothDeviceList.setVisibility(View.VISIBLE);
                    if (!isSocketConnected) {
                        BluetoothUtility.turnOn(BluetoothDevicesActivity.this);
                        llScanning.setVisibility(View.VISIBLE);
                        startDiscovery();
                    }
                } else {
                    // TODO: Bluetooth OFF
                    tvBluetoothDeviceOnOff.setText(getString(R.string.lbl_off));
                    tvBluetoothOffMessage.setVisibility(View.VISIBLE);
                    llBluetoothDeviceList.setVisibility(View.GONE);
                    llScanning.setVisibility(View.GONE);
                    if (BluetoothUtility.turnOff()) {
                        //toastMessage("Bluetooth is OFF");
                    }

                }
            }
        });
    }

    /**
     * Find all views id
     */
    private void renderViews() {
        tvBluetoothDeviceOnOff = (TextView) findViewById(R.id.tvBluetoothDeviceOnOff);
        tvBluetoothOffMessage = (TextView) findViewById(R.id.tvBluetoothOffMessage);
        switchBluetoothDeviceOnOff = (Switch) findViewById(R.id.switchBluetoothDeviceOnOff);
        lvBluetoothDeviceList = (ListView) findViewById(R.id.lvBluetoothDeviceList);
        llBluetoothDeviceList = (LinearLayout) findViewById(R.id.llBluetoothDeviceList);
        ivBluetoothDeviceBack = (ImageView) findViewById(R.id.ivBluetoothDeviceBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BluetoothUtility.REQUEST_ENABLE_BT) {
                debugLog("Bluetooth enable");
                BluetoothUtility.doDiscovery(llScanning);
            } else {
                debugLog("Error in Bluetooth on");
            }
        } else {
            switchBluetoothDeviceOnOff.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isBluetoothDeviceActivityCreated = false;
        // Unregister broadcast listeners
        unregisterReceiver(bReciever);
        unregisterReceiver(mPairReceiver);
        if (getBluetoothAdapter().isDiscovering())
            getBluetoothAdapter().cancelDiscovery();
    }

    /* Broadcast when bluetooth device found*/
    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice curDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(curDevice);
            }
            Log.i(TAG, "All BT Devices : " + devices.size());
            if (devices.size() > 0) {
                showPairedList();
            }
        }
    };

    /* Broadcast when BT device paired / unpaired*/
    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    toastMessage(getString(R.string.toast_weight_plate_paired));
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    toastMessage(getString(R.string.toast_weight_plate_un_paired));
                } else if (state == BluetoothAdapter.STATE_ON) {
                    //toastMessage("ON");
                } else if (state == BluetoothAdapter.STATE_OFF) {
                }

            }
        }
    };

    private void showPairedList() {
        List<String> tempDevices = new ArrayList<String>();

        for (BluetoothDevice b : devices) {
            BTDeviceItem btDeviceItem = new BTDeviceItem();
            String paired = "Paired";
            if (b.getBondState() != 12) {
                paired = "Not Paired";
                btDeviceItem.setPaired(false);
            } else {
                btDeviceItem.setPaired(true);
            }
            btDeviceItem.setDeviceName(b.getName());
            tempDevices.add(b.getName() + "  [ " + paired + " ]");
        }

        if (allDevices == null) {
            allDevices = new ArrayList<String>();
        } else
            allDevices.clear();

        allDevices.addAll(tempDevices);

        if (bluetoothDeviceAdapter == null) {
            bluetoothDeviceAdapter = new ArrayAdapter<>(BluetoothDevicesActivity.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, allDevices);
            lvBluetoothDeviceList.setAdapter(bluetoothDeviceAdapter);

            lvBluetoothDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    try {
                        startAsServer();
                        deviceToConnect = devices.get(position);
                        bluetoothDeviceAdapter = null;
                        Log.i(TAG, "Connecting to device :" + deviceToConnect.getName());
                        toastMessage(getString(R.string.toast_connecting_to_device) + " " + deviceToConnect.getName());

                        //Now this is not the server...
                        killServerThread();

                        //Connect to the other device which is a server...
                        connectAsClient();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(BluetoothDevicesActivity.this, getString(R.string.toast_off_bt_and_on), Toast.LENGTH_LONG).show();
                    }
                }
            });


        } else {
            bluetoothDeviceAdapter.notifyDataSetChanged();
        }
    }

    public void connectAsClient() {
        debugLog("Connecting for online Bluetooth devices...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (deviceToConnect != null) {
                    if (connectThread != null) {
                        connectThread.cancel();
                        connectThread = null;
                        Log.d("TAG", "deviceToConnect.getName() >> " + deviceToConnect.getName());
                        if (deviceToConnect.getUuids() != null)
                            Log.d("TAG", "deviceToConnect.getUuids()[0].getUuid() >> " + deviceToConnect.getUuids()[0].getUuid());
                    }
                    connectThread = new BTClientThread();
                    curBTSocket = connectThread.connect(BluetoothDevicesActivity.this, mBluetoothAdapter, deviceToConnect, BTConstant.MY_UUID, mHandler);
                    forResetDeviceSocket = curBTSocket;
                    if (connectThread != null)
                    connectThread.start();
                }
            }
        }).start();


    }

    public void killServerThread() {
        if (serverConnectThread != null) {
            serverConnectThread.closeConnection();
            serverConnectThread = null;
        }
    }

    public void startAsServer() {
        debugLog("Listening for online Bluetooth devices...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverConnectThread = new BTServerConnectThread();
                curBTSocket = serverConnectThread.acceptConnection(BluetoothDevicesActivity.this, mBluetoothAdapter, BTConstant.MY_UUID, mHandler);
                forResetDeviceSocket = curBTSocket;
                Log.d(TAG, "curBTSocket >> " + curBTSocket);
            }
        }).start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llScanning.setVisibility(View.GONE);
            Log.d(TAG, "Handler message >> " + msg);
            byte[] buf = (byte[]) msg.obj;

            switch (msg.what) {
                case BTConstant.MESSAGE_WRITE:
                    // construct a string from the buffer
                    String writeMessage = new String(buf);
                    Log.i(TAG, "Write Message : " + writeMessage);
                    //toastMessage("Message Sent : " + writeMessage);
                    break;
                case BTConstant.MESSAGE_READ:
                    isSocketConnected = true;
                    final String deviceName = new String(buf);

                    if (msg.arg2 == 0)
                        BluetoothDevicesActivity.weightReadingInGram = msg.arg1 * (-1);
                    else
                        BluetoothDevicesActivity.weightReadingInGram = msg.arg1;

                    debugLog(TAG,"BluetoothDevicesActivity.weightReadingInGram >> " + BluetoothDevicesActivity.weightReadingInGram);
                    tvBluetoothDeviceConnectionStatus.setText(getString(R.string.lbl_Connected));
                    tvBluetoothDeviceConnectionStatus.setTextColor(ContextCompat.getColor(BluetoothDevicesActivity.this, R.color.white));
                    debugLog("BluetoothDevicesActivity.isAddEditFoodActivityCreated >> " + BluetoothDevicesActivity.isAddEditFoodActivityCreated);
                    if (BluetoothDevicesActivity.isAddEditFoodActivityCreated) {
                        setStatusConnected();
                    }
                    break;
                case BTConstant.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = new String(buf);
                    toastMessage("Connected to " + mConnectedDeviceName);

                    // Send "0x0" command to device for device always on
                    sendCommand(0x0);
                    isSocketConnected = true;
                    tvBluetoothDeviceConnectionStatus.setText("Weight Plate : "+getString(R.string.lbl_Connected));
                    cancelDiscovery();
                    llScanning.setVisibility(View.GONE);
                    startActivity(new Intent(BluetoothDevicesActivity.this, MainActivity_.class).putExtra("OPEN_DIARY_FM", true));
                    break;
                case BTConstant.MESSAGE_SERVER_CONNECTED:
                    toastMessage("CONNECTED");
                    Log.i(TAG, "Connected...");
                    //linSendMessage.setVisibility(View.VISIBLE);
                    break;
                case BTConstant.MESSAGE_SOCKET_DISCONNECTED:
                    isSocketConnected = false;
                    tvBluetoothDeviceConnectionStatus.setText("Weight Plate : "+getString(R.string.lbl_disconnected));
                    if (BluetoothDevicesActivity.isAddEditFoodActivityCreated) {
                        setStatusDisconnected();
                    }
                    break;
            }
        }
    };

    private void setStatusDisconnected() {
        AddFoodActivity.tv_add_food_bt_status.setText(getString(R.string.lbl_disconnected));
        AddFoodActivity.tv_add_food_bt_status.setTextColor(ContextCompat.getColor(BluetoothDevicesActivity.this, R.color.colorFontGry));
        AddFoodActivity.iv_add_food_bt_image.setImageResource(R.drawable.ic_bluetooth_gry);
        AddFoodActivity.resetButtonVisibleInvisible();
    }

    private void setStatusConnected() {
        String selectedUnit = AddFoodActivity.tv_add_food_unit.getText().toString();
        AddFoodActivity.tvAddFoodWeightReading.setText(FormulaUtility.convertOldToNewUnit(Constant.UNIT_GM, selectedUnit, String.valueOf(BluetoothDevicesActivity.weightReadingInGram)));
        AddFoodActivity.tv_add_food_bt_status.setText(getString(R.string.lbl_Connected));
        AddFoodActivity.tv_add_food_bt_status.setTextColor(ContextCompat.getColor(BluetoothDevicesActivity.this, R.color.white));
        AddFoodActivity.iv_add_food_bt_image.setBackgroundResource(R.drawable.ic_bluetooth);
        AddFoodActivity.resetButtonVisibleInvisible();
    }

    public void sendMessageToDevice() {
        deviceConnectThread = new BTDeviceConnectThread(BluetoothDevicesActivity.this, curBTSocket, mHandler);
        deviceConnectThread.start();
        String edt = "Hi";/*edtMessage.getText().toString().trim();*/
        String message = convertStringToHex(edt);
        Log.d(TAG, "Conver string to hex >> " + message);
        if (message.length() > 0) {
            byte[] send = /*message*/"0x0".getBytes();
            Log.d(TAG, "Send >> " + send);
            deviceConnectThread.write(send);
        }
    }

    private String convertStringToHex(String string) {
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            newString.append(String.format("%x ", (byte) (string.charAt(i))));
        }
        return newString.toString();
    }

    private void startDiscovery() {
        //toastMessage("Starting Discovery...");
        if (!mBluetoothAdapter.isDiscovering()) {
            getPairedDevices();
            mBluetoothAdapter.startDiscovery();
        }
    }

    private void cancelDiscovery() {
        //toastMessage("Cancelling Discovery...");
        if (mBluetoothAdapter.isDiscovering()) {
            unregisterReceiver(bReciever);
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private void getPairedDevices() {

        if (devices == null)
            devices = new ArrayList<BluetoothDevice>();
        else
            devices.clear();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice curDevice : pairedDevices) {
                devices.add(curDevice);
            }
            Log.i(TAG, "Paired Number of Devices : " + pairedDevices.size());
            showPairedList();
        }
    }

    /* Send command to device in HEX format string */
    public void sendCommand(int command) {
        if (deviceConnectThread == null) {
            deviceConnectThread = new BTDeviceConnectThread(BluetoothDevicesActivity.this, curBTSocket, mHandler);
            deviceConnectThread.start();
        }

        Log.d(TAG, "command >> " + command);
        //if (command.length() > 0) {
        //byte[] send = /*message*/"0xFE".getBytes();
        //Log.d(TAG,"Send >> "+ 0x30);
        deviceConnectThread.write(command);//}
    }

    public BTDeviceConnectThread getDeviceConnectThread() {
        if (deviceConnectThread != null)
            return deviceConnectThread;
        else
            return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBluetoothDeviceActivityCreated = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isBluetoothDeviceActivityCreated = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (from_basic_setting) {
            startActivity(new Intent(BluetoothDevicesActivity.this, MainActivity_.class));
        } else
            finish();
    }
}
