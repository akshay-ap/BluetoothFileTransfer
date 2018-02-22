package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.MetaData;
import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

/**
 * Created by ash on 21/2/18.
 * Make sure to instantiate only one instance of this class.
 * Assume Socket connection is established over bluetooth.
 */

public class FileReceiverTask extends AsyncTask {

    private static final String TAG = "===FileReceiverTask";

    InputStream inputStream;
    //String filePath;
    public FileReceiverTask() {
        Log.d(FileReceiverTask.TAG,"Object created");

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

        if(!SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(FileReceiverTask.TAG,"Socket is closed... Can't perform file receiving Task...");
            return null;
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            MetaData metaData = (MetaData)objectInputStream.readObject();
            Log.d(FileReceiverTask.TAG," Variable value received : " + metaData.toString() );
            //objectInputStream.close();

            String receivePath = String.valueOf(Environment.getExternalStorageDirectory()) +"/"+ metaData.getFname();

            File file = new File(receivePath);
            if(!file.exists()) {
                Log.d(FileReceiverTask.TAG, "File does not exist : " + receivePath);
            }  else {
                Log.d(FileReceiverTask.TAG,"File already exists : " + receivePath);
            }

            OutputStream outputStreamWriteToFile = new FileOutputStream(file);
            Log.d(FileReceiverTask.TAG,"outputStreamWriteToFile created");

            byte[] buffer = new byte[1024];
            int read;
            int totalRead = 0;
            int loop = 0;
            if(! SocketHolder.getBluetoothSocket().isConnected()) {
                Log.d(FileReceiverTask.TAG,"Socket is closed... Can't perform file receiving from stream...");
                return null;
            }
            while ((read = inputStream.read(buffer)) != -1) {

                totalRead = totalRead + read;
                outputStreamWriteToFile.write(buffer,0,read);
                loop++;
                Log.d(FileReceiverTask.TAG,"loop iterations : " + loop + " bytes read: " + totalRead);

                //This the most important part...
                if(totalRead == metaData.getDataSize()) {
                    Log.d(FileReceiverTask.TAG,"breaking from loop");
                    break;
                }
            }

            Log.d(FileReceiverTask.TAG,"loop iterations : " + loop + " bytes read: " + totalRead);
            Log.d(FileReceiverTask.TAG,"outputStreamWriteToFile closing");
            outputStreamWriteToFile.close();
            Log.d(FileReceiverTask.TAG,"outputStreamWriteToFile closed");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(FileReceiverTask.TAG,e.toString() );

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(FileReceiverTask.TAG,e.toString() );
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if(SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(FileReceiverTask.TAG,"BluetoothSocket is connected");

        } else {
            Log.d(FileReceiverTask.TAG,"BluetoothSocket is ****NOT*** connected");
        }
        Log.d(FileReceiverTask.TAG,"Task execution completed");

    }
}
