package com.digitalscale.utility;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Method;

/**
 * Created by Vishal Gadhiya on 5/26/2017.
 */

public class BluetoothUtility {

    private static final String TAG = BluetoothUtility.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    private static BluetoothAdapter bluetoothAdapter = null;
    public static BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null)
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return bluetoothAdapter;
    }

    public static boolean checkBluetoothCompatibility() {
        // Phone does not support Bluetooth so let the user know and exit.
        if (getBluetoothAdapter() == null) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean turnOff() {
        return getBluetoothAdapter().disable();
    }

    public static void turnOn(Activity activity) {
        if (!getBluetoothAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    /**
     * Start device discover with the BluetoothAdapter
     */
    public static void doDiscovery(LinearLayout linearLayout) {
        debugLog("doDiscovery()");

        // Indicate scanning in the title
        linearLayout.setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (getBluetoothAdapter().isDiscovering()) {
            getBluetoothAdapter().cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        getBluetoothAdapter().startDiscovery();
    }


    /**
     * Paired device
     *
     * @param btDevice
     * @return
     * @throws Exception
     */
    public static boolean createBond(BluetoothDevice btDevice)
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * Unpaired Device
     *
     * @param btDevice
     * @return
     * @throws Exception
     */
    public static boolean removeBond(BluetoothDevice btDevice)
            throws Exception {
        Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    private static void debugLog(String log) {
        Log.d(TAG, log);
    }
}
