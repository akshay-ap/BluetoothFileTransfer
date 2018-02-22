package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.examples.akshay.bluetoothfiletransfer.MetaData;
import com.examples.akshay.bluetoothfiletransfer.SocketHolder;
import com.examples.akshay.bluetoothfiletransfer.interfaces.TaskUpdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by ash on 21/2/18.
 * Used to perform sending file in background.
 * Assume Socket is established.
 * Make sure to instantiate only one instance of this class.
 */

public class FileSenderTask extends AsyncTask {

    private static final String TAG = "===FileSenderTask";
    OutputStream outputStream;
    String filePath;
    BluetoothSocket bluetoothSocket;
    TaskUpdate taskUpdate;
    MetaData metaData;
    public FileSenderTask(String path, TaskUpdate taskUpdate) {

    Log.d(FileSenderTask.TAG,"Object created");
    this.taskUpdate = taskUpdate;

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
        taskUpdate.TaskStarted();
        if(!SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(FileSenderTask.TAG,"Socket is closed... Can't perform file sending Task...");
            return null;
        }

        //Receive metaData
        try {

            Log.d(FileSenderTask.TAG,"obtain file object for : "+ filePath);
            File file = new File(filePath);

            if(!file.exists()) {
                Log.d(FileSenderTask.TAG,"File does not exist");
                return null ;
            } else {
                Log.d(FileSenderTask.TAG,"File exists");
            }

            ObjectOutputStream objectOutputStream;
            objectOutputStream = new ObjectOutputStream(outputStream);

            metaData = new MetaData(file.length(),file.getName());
            Log.d(FileSenderTask.TAG,"Trying to send : " + metaData.toString());
            objectOutputStream.writeObject(metaData);


            if(!SocketHolder.getBluetoothSocket().isConnected()) {
                Log.d(FileSenderTask.TAG,"Socket is closed... Can't perform file sending on stream...");
                return null;
            }

            Log.d(FileSenderTask.TAG,"fileInputStream open: "+ filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            int loop = 0;
            long totalRead = 0;
            long toRead = metaData.getDataSize();
            while ((bytesRead = fileInputStream.read(buffer)) > 0)
            {
                loop++;
                outputStream.write(buffer, 0, bytesRead);
                totalRead = totalRead + bytesRead;
                if(totalRead % 1024*10 == 0) {
                    taskUpdate.TaskProgressPublish((long)((float)totalRead/toRead*100));
                }
            }

            Log.d(FileSenderTask.TAG,"Loop iterations run : " + loop);
            Log.d(FileSenderTask.TAG,"trying to close the fileInputStream...");
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(FileSenderTask.TAG,e.toString());

        }

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        taskUpdate.TaskCompleted(metaData.getFname());

        if(SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(FileSenderTask.TAG,"BluetoothSocket is connected");

        } else {
            Log.d(FileSenderTask.TAG,"BluetoothSocket is ****NOT*** connected");
        }

        Log.d(FileSenderTask.TAG,"Task execution completed");

    }
}
