package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ash on 21/2/18.
 */

public class FileReceiverTask extends AsyncTask {

    private static final String TAG = "===FileReceiverTask";

    InputStream inputStream;
    String filePath;
    public FileReceiverTask(String name) {
        Log.d(FileReceiverTask.TAG,"Object created");
        this.filePath = name;

        try {

            Log.d(FileReceiverTask.TAG,"trying to get inputStream");
            inputStream = SocketHolder.getBluetoothSocket().getInputStream();
            Log.d(FileReceiverTask.TAG,"obtained inputStream");

        } catch (IOException e) {
            Log.d(FileReceiverTask.TAG,"Failed to obtain input stream");
            Log.d(FileReceiverTask.TAG,e.toString());
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
        File file = new File(filePath);
        if(!file.exists()) {
            Log.d(FileReceiverTask.TAG,"File does not exist : " + filePath);
            Log.d(FileReceiverTask.TAG,"Trying to create file: " + filePath);
            /*if(file.createNewFile()) {
                //File creation successful
                Log.d(FileReceiverTask.TAG,"File creation successsful");
            } else {
                //File creation failed
                Log.d(FileReceiverTask.TAG,"File creation failed");
                return null;
            }*/

        } else {
            Log.d(FileReceiverTask.TAG,"File already exists : " + filePath);
        }

            Log.d(FileReceiverTask.TAG,"Getting OutputStream : ");

            OutputStream output = new FileOutputStream(file);
            Log.d(FileReceiverTask.TAG,"outputStream for file created : ");

            byte[] buffer = new byte[1024];
            int read;
            int loop = 0;
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer,0,read);
                loop++;
                //Log.d(FileReceiverTask.TAG,"in loop");

            }
            Log.d(FileReceiverTask.TAG,"Loop iterations run : " + loop);

            Log.d(FileReceiverTask.TAG,"OutputStream of File closing");

            //output.flush();
            output.close();
            Log.d(FileReceiverTask.TAG,"OutputStream of File closed");

            //inputStream.close();
            //Log.d(FileReceiverTask.TAG,"inputStream closed");

        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d(FileReceiverTask.TAG,"Task execution completed");

    }
}
