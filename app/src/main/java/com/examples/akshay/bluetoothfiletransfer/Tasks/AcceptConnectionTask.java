package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.IOException;
import java.util.UUID;

import static com.examples.akshay.bluetoothfiletransfer.Constants.MY_UUID;
import static com.examples.akshay.bluetoothfiletransfer.Constants.NAME;

/**
 * Created by ash on 20/2/18.
 */

public class AcceptConnectionTask extends AsyncTask {
    private static final String TAG = "===AccpeThread";
    private final BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    public AcceptConnectionTask(Context context, BluetoothAdapter bluetoothAdapter) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        this.context = context;
        this.mBluetoothAdapter = bluetoothAdapter;
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString(MY_UUID));
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;

    }
    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                Log.d(AcceptConnectionTask.TAG,"Trying to accpet connections...");
                socket = mmServerSocket.accept();
                Log.d(AcceptConnectionTask.TAG,"Accepted Connection");

            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                SocketHolder.setMODE(1);
                SocketHolder.setBluetoothSocket(socket);

                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                Log.d(AcceptConnectionTask.TAG,"sokcet object is null");
            }
        }
        return null;
    }
}