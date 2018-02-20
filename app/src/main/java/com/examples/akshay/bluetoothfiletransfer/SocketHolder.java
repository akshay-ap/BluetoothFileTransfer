package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

/**
 * Created by ash on 20/2/18.
 */

public class SocketHolder {

    public static int MODE = -1;
    public static BluetoothSocket bluetoothSocket;

    public static int getMODE() {
        return MODE;
    }

    public static void setMODE(int MODE) {
        SocketHolder.MODE = MODE;
    }

    public static BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public static void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        SocketHolder.bluetoothSocket = bluetoothSocket;
    }

    public SocketHolder() {
    }
}
