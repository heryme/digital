package com.digitalscale.BT;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Vishal Gadhiya on 5/2/2017.
 */

public class BTDeviceConnectThread extends Thread {

    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;
    Handler mHandler;
    public static final String TAG = "DeviceConnectThread";
    Activity activity;
    int mState;

    // Positive : 1 Negative : 0
    int PNValue = 1;

    public BTDeviceConnectThread(Activity activity, BluetoothSocket socket, Handler mHandler) {
        this.activity = activity;
        this.mHandler = mHandler;
        mSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();

            Log.d(TAG, "tmpIn >> " + tmpIn);
            Log.d(TAG, "tmpOut >> " + tmpOut);

        } catch (IOException e) {
            Log.d(TAG, "<---------------- I / O stream exceptopn ----------->");
            e.printStackTrace();
        }

        mInStream = tmpIn;
        mOutStream = tmpOut;

        mState = BTConstant.STATE_CONNECTED;

    }

    public void run() {

        byte[] buffer = new byte[2];  // buffer store for the stream
        int bytes = 0; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                //
                Log.d(TAG, " START ");

                bytes = mInStream.read(buffer);
                Log.d(TAG, "byte >> " + Arrays.toString(buffer));
                String bit = BTHexUtil.byteToBit(buffer);
                Log.d(TAG, "Bit " + bit);
                int data = Integer.parseInt(bit.substring(0, 1), 2);
                Log.d(TAG, "Data = " + data);
                if (data == 1) {
                    Log.d(TAG, "It's +ve value ");
                    PNValue = 1;
                } else {
                    Log.d(TAG, "It's Negative value ");
                    PNValue = 0;
                }

                final int battryPower = Integer.parseInt(bit.substring(2, 3), 2);
                Log.d(TAG, "battryPower = " + battryPower);

                final int Weight = Integer.parseInt(bit.substring(4), 2);

                if (PNValue == 0)
                    Log.d(TAG, "Weight " + Weight * (-1));
                else
                    Log.d(TAG, "Weight " + Weight);

                /*activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.tvReadableData.setText("Weight :" + Weight);
                        MainActivity.tvReadableData.setVisibility(View.VISIBLE);
                    }
                });*/

                // Send the obtained bytes to the UI Activity
                mHandler.obtainMessage(BTConstant.MESSAGE_READ, Weight, PNValue, buffer)
                        .sendToTarget();

            } catch (Exception e) {
                Log.d(TAG, "Message read exception \n");
                e.printStackTrace();
                mHandler.obtainMessage(BTConstant.MESSAGE_SOCKET_DISCONNECTED, bytes, -1, buffer)
                        .sendToTarget();

                break;
            }
        }

    }

    public String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            //mOutStream.write(bytes);
            mOutStream.write(0xFE);
            mHandler.obtainMessage(BTConstant.MESSAGE_WRITE, 0, -1, bytes)
                    .sendToTarget();
        } catch (IOException e) {
            Log.i(TAG, "Write Error : " + e.toString());
        }
    }

    public void write(int hexValue) {
        try {
            //mOutStream.write(bytes);
            Log.d(TAG, "hexValue >> " + hexValue);
            mOutStream.write(hexValue);
            /*mHandler.obtainMessage(Constants.MESSAGE_WRITE, 0, -1, hexValue)
                    .sendToTarget();*/
        } catch (IOException e) {
            Log.i(TAG, "Write Error : " + e.toString());
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
        }
    }
}
