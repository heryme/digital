package com.digitalscale.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.BluetoothDevicesActivity;
import com.digitalscale.model.BTDeviceItem;

import java.util.ArrayList;

/**
 * Created by Vishal Gadhiya on 5/26/2017.
 */

public class BluetoothDeviceAdapter extends BaseAdapter {

    private static final String TAG = BluetoothDeviceAdapter.class.getSimpleName();

    private ArrayList<BTDeviceItem> deviceList;
    private Context context;
    private LayoutInflater inflater;
    BluetoothDevicesActivity bluetoothDevicesActivity;

    public BluetoothDeviceAdapter(Context context, ArrayList<BTDeviceItem> deviceList)
    {
        this.context = context;
        this.deviceList = deviceList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bluetoothDevicesActivity = new BluetoothDevicesActivity();
    }
    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.row_bluetooth_device, null);
        }

        TextView tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        final Button btnPair = (Button) view.findViewById(R.id.btnPairDevice);

        final BTDeviceItem device = (BTDeviceItem) getItem(i);
        if (device != null) {
            tvDeviceName.setText(device.getDeviceName());
            if (device.getDeviceName().equalsIgnoreCase("Weight Plate")) {
                if (device.getIsPaired()) {
                    btnPair.setText("Paired");
                } else {
                    btnPair.setText("Pair");
                }
                btnPair.setVisibility(View.VISIBLE);
            } else {
                btnPair.setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }

    private void debugLog(String log) {
        Log.d(TAG, log);
    }
}
