package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_SOCKET;


/**
 * Created by ash on 20/2/18.
 *
 */

public class ConnectThread extends Thread {
    private static final String TAG = "===ConnectThread";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    public ConnectThread(Context context,BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        this.mBluetoothAdapter = bluetoothAdapter;
        this.mmDevice = device;
        this.context = context;
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.MY_UUID));
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            Log.d(ConnectThread.TAG,"Trying to connect to server...");
            mmSocket.connect();
            Log.d(ConnectThread.TAG, "Connected...");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket);
        //DataTransferService dataTransferService = new DataTransferService(mmSocket);
        //startService()
        SocketHolder.setMODE(0);
        SocketHolder.setBluetoothSocket(mmSocket);
        //DataTransferService dataTransferService = new DataTransferService(mmSocket);
        Intent intent = new Intent(context,DataTransferService.class);
        context.startService(intent);


    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}