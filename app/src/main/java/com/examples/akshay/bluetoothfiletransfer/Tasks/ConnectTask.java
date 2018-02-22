package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.Constants;
import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by ash on 20/2/18.
 *
 */

public class ConnectTask extends AsyncTask {
    private static final String TAG = "===ConnectTask";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    public ConnectTask(Context context,BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
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


    @Override
    protected Object doInBackground(Object[] objects) {

        // Cancel discovery because it otherwise slows down the connection.
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            Log.d(ConnectTask.TAG,"Trying to connect to server...");
            mmSocket.connect();
            Log.d(ConnectTask.TAG, "Connected...");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.d(TAG, "Could not close the client socket", closeException);
            }
            return null;
        }

        SocketHolder.setMODE(0);
        SocketHolder.setBluetoothSocket(mmSocket);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.e(TAG, "onPostExecute()");

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