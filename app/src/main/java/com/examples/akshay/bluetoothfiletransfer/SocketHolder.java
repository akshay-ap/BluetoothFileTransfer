package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ash on 20/2/18.
 *
 */

public class SocketHolder {

    /*
    *
    * MODE 0 = Client
    * MODE 1 = Server
    * MODE -1 = None
    *
    * */

    private static int MODE = -1;
    private static BluetoothSocket bluetoothSocket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private static boolean inputStreamLock = false;
    private static boolean outputStreamLock = false;

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static void setInputStream() {
        try {
            SocketHolder.inputStream = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OutputStream getOutputStream() {
        return outputStream;
    }

    public static void setOutputStream() {
        try {
            SocketHolder.outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
