package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ash on 21/2/18.
 */

public class FileSenderTask extends AsyncTask {

    OutputStream outputStream;
    String filePath;
    BluetoothSocket bluetoothSocket;
    public FileSenderTask(String path, BluetoothSocket bluetoothSocket) {
    this.bluetoothSocket = bluetoothSocket;
    this.filePath = path;
        try {
            this.outputStream = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        return null;
    }
}
