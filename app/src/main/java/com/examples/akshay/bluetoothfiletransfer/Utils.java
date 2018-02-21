package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.activities.Server;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by ash on 19/2/18.
 */

public class Utils {

    public static  ArrayList<BluetoothDevice> getPairedDevices(BluetoothAdapter mBluetoothAdapter) {
        ArrayList<BluetoothDevice> arrayListpairedDevices = new ArrayList<BluetoothDevice>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice curDevice : pairedDevices) {
                arrayListpairedDevices.add(curDevice);
            }
        }
        return arrayListpairedDevices;
    }
}
