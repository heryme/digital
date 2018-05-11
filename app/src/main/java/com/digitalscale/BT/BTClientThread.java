package com.digitalscale.BT;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Vishal Gadhiya on 5/2/2017.
 */

public class BTClientThread extends Thread {

    private BluetoothSocket bTSocket = null;
    public static final String TAG = "ClientThread";
    Activity activity;

    public BluetoothSocket connect(Activity activity, BluetoothAdapter bTAdapter, BluetoothDevice bTDevice, UUID mUUID, Handler mHandler) {
        this.activity = activity;
        try {
            bTSocket = bTDevice.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            Log.d(TAG, "Could not create RFCOMM socket:" + e.toString());
            return bTSocket;
        }

        bTAdapter.cancelDiscovery();


        try {
            bTSocket.connect();
        } catch (Exception e) {
            Log.d(TAG, "Could not connect: " + e.toString());
            mHandler.obtainMessage(BTConstant.MESSAGE_SOCKET_DISCONNECTED, 0, -1, bTDevice.getName().getBytes())
                    .sendToTarget();
            try {
                bTSocket.close();
                Log.d(TAG, "Socket Close");
            } catch (IOException close) {
                Log.d(TAG, "Could not close connection:" + e.toString());
                return bTSocket;
            }
            return bTSocket;
        } /*catch (Exception e) {
            e.printStackTrace();
        }*/

        byte[] bytes = bTDevice.getName().getBytes();
        byte[] buffer = new byte[1024];
        mHandler.obtainMessage(BTConstant.MESSAGE_DEVICE_NAME, 0, -1, bytes)
                .sendToTarget();

        return bTSocket;
    }

    public boolean cancel() {
        if (bTSocket != null) {
            try {
                bTSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "Could not close connection:" + e.toString());
                return false;
            }
        }
        return true;
    }
}
