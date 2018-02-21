package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ash on 21/2/18.
 */

public class FileSenderTask extends AsyncTask {

    private static final String TAG = "===FileSenderTask";
    OutputStream outputStream;
    String filePath;
    BluetoothSocket bluetoothSocket;
    public FileSenderTask(String path) {
    Log.d(FileSenderTask.TAG,"Object created");
    this.bluetoothSocket = SocketHolder.getBluetoothSocket();
    this.filePath = path;
        try {
            this.outputStream = bluetoothSocket.getOutputStream();
            Log.d(FileSenderTask.TAG,"obtained outputStream");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(FileSenderTask.TAG,"Failed to obtain outputStream");
            Log.d(FileSenderTask.TAG,e.toString());
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        write();
        try {
            Log.d(FileSenderTask.TAG,"trying to close the outputStream...");
            outputStream.flush();
            //outputStream.close();

        } catch (IOException e) {
            Log.d(FileSenderTask.TAG,"outputStream closing failed");
            e.printStackTrace();
            Log.d(FileSenderTask.TAG,e.toString());
        }
        return null;
    }


    public void write() {
        try {
            //String path = Environment.getExternalStorageDirectory()+"/"+filePath;

            Log.d(FileSenderTask.TAG,"Trying to open : "+ filePath);
            File file = new File(filePath);

            FileInputStream fileInputStream = new FileInputStream(file);
            if(!file.exists()) {
                Log.d(FileSenderTask.TAG,"File does not exist");
                return;
            } else {
                Log.d(FileSenderTask.TAG,"File exists");

            }
            //outputStream.write();
        } catch (IOException e) {
            Log.e(FileSenderTask.TAG, "Exception during write", e);
            Log.d(FileSenderTask.TAG,e.toString());
        }
    }

    @Override
    protected void onPostExecute(Object o) {

        Log.d(FileSenderTask.TAG,"Task ececution completed");

        super.onPostExecute(o);
    }
}
