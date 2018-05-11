package com.digitalscale.BT;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Vishal Gadhiya on 5/2/2017.
 */

public class BTServerConnectThread extends Thread {

    private BluetoothSocket bTSocket;
    Handler mHandler;
    public static final String TAG = "ServerConnectThread";
    Activity activity;

    public BluetoothSocket acceptConnection(Activity activity,BluetoothAdapter bTAdapter, UUID mUUID, Handler mHandler) {

        BluetoothServerSocket temp = null;
        this.mHandler = mHandler;
        this.activity = activity;
        try {
            temp = bTAdapter.listenUsingRfcommWithServiceRecord("Service_Name", mUUID);
        } catch (IOException e) {
            Log.d(TAG, "Could not get a BluetoothServerSocket:" + e.toString());
        }
        while (true) {
            try {
                if (temp != null)
                bTSocket = temp.accept();
            } catch (IOException e) {
                Log.d(TAG, "Could not accept an incoming connection.");

                /*this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.tvExp.setText("Could not accept an incoming connection.");
                        MainActivity.tvExp.setVisibility(View.VISIBLE);
                    }
                });*/
                break;
            }
            if (bTSocket != null) {
                try {
                    temp.close();

                    byte[] bytes0 = "Connected".getBytes();
                    byte[] buffer0 = new byte[1024];
                    mHandler.obtainMessage(BTConstant.MESSAGE_SERVER_CONNECTED, 0, -1, bytes0)
                            .sendToTarget();

                } catch (IOException e) {
                    Log.d(TAG, "Could not close ServerSocket:" + e.toString());
                }
                break;
            }
        }

        return bTSocket;
    }

    public void closeConnection() {
        if (bTSocket != null) {
            try {
                bTSocket.close();
            } catch (IOException e) {
                Log.d(TAG, "Could not close connection:" + e.toString());
            }
        }
    }
}
