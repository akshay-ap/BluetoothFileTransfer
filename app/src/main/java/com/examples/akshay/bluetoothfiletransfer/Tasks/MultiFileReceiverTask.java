package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.Constants;
import com.examples.akshay.bluetoothfiletransfer.MetaData;
import com.examples.akshay.bluetoothfiletransfer.MetaMetaData;
import com.examples.akshay.bluetoothfiletransfer.SocketHolder;
import com.examples.akshay.bluetoothfiletransfer.interfaces.TaskUpdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.spec.ECField;

/**
 * Created by ash on 21/2/18.
 * Make sure to instantiate only one instance of this class.
 * Assume Socket connection is established over bluetooth.
 */

public class MultiFileReceiverTask extends AsyncTask {

    private static final String TAG = "===MultiFileRTask";

    InputStream inputStream;
    TaskUpdate taskUpdate;
    MetaData metaData;
    MetaMetaData metaMetaData;
    //String filePath;
    public MultiFileReceiverTask(TaskUpdate taskUpdate) {
        Log.d(MultiFileReceiverTask.TAG,"Object created");
        this.taskUpdate = taskUpdate;
        try {

            Log.d(MultiFileReceiverTask.TAG,"trying to get inputStream");
            inputStream = SocketHolder.getBluetoothSocket().getInputStream();
            Log.d(MultiFileReceiverTask.TAG,"obtained inputStream");

        } catch (IOException e) {
            Log.d(MultiFileReceiverTask.TAG,"Failed to obtain input stream");
            Log.d(MultiFileReceiverTask.TAG,e.toString());
        }

    }

    @Override
    protected Object doInBackground(Object[] objects) {
        taskUpdate.TaskStarted();
        if(!SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(MultiFileReceiverTask.TAG,"Socket is closed... Can't perform file receiving Task...");
            return null;
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            metaMetaData = (MetaMetaData)objectInputStream.readObject();

            int numberOfFiles = metaMetaData.getNumberOfFiles();
            Log.d(MultiFileReceiverTask.TAG,"Number of files to receive = :" + numberOfFiles);

            for(int i = 0 ;i< numberOfFiles ;i++) {
                metaData = (MetaData)objectInputStream.readObject();
                Log.d(MultiFileReceiverTask.TAG," Variable value received : " + metaData.toString() );
                //objectInputStream.close();
                Log.d(MultiFileReceiverTask.TAG,"Trying to recevie file :" + i);

                //Make directory for form...
                String formDirectoryPath = String.valueOf(Environment.getExternalStorageDirectory()) + Constants.RECEIVED_FORM_SAVE_PATH + metaData.getFname();
                formDirectoryPath  = formDirectoryPath.substring(0,formDirectoryPath.length() - 4);
                File dir = new File(formDirectoryPath);

                if(!dir.exists()) {
                    if(dir.mkdir()) {
                        Log.d(MultiFileReceiverTask.TAG,"Created directory");
                    } else {
                        Log.d(MultiFileReceiverTask.TAG,"Failed to created directory");
                        taskUpdate.TaskError("Failed to create directory : " + formDirectoryPath);
                        return null;
                    }
                } else {
                    Log.d(MultiFileReceiverTask.TAG,"Directory already exists");
                }


                String receivePath = formDirectoryPath +"/" + metaData.getFname();

                File file = new File(receivePath);
                if(!file.exists()) {
                    Log.d(MultiFileReceiverTask.TAG, "File does not exist : " + receivePath);
                }  else {
                    Log.d(MultiFileReceiverTask.TAG,"File already exists : " + receivePath);
                }

                OutputStream outputStreamWriteToFile = new FileOutputStream(file);
                Log.d(MultiFileReceiverTask.TAG,"outputStreamWriteToFile created");

                int read;
                long totalRead = 0;
                long toRead = metaData.getDataSize();
                int loop = 0;
                if(! SocketHolder.getBluetoothSocket().isConnected()) {
                    Log.d(MultiFileReceiverTask.TAG,"Socket is closed... Can't perform file receiving from stream...");
                    return null;
                }
                byte[] buffer;

                if(toRead > 1024 )
                {
                    buffer = new byte[1024];
                } else {
                    buffer = new byte[(int) toRead];
                }


                while ((read = inputStream.read(buffer)) != -1) {

                    totalRead = totalRead + read;
                    outputStreamWriteToFile.write(buffer,0,read);
                    loop++;
                    Log.d(MultiFileReceiverTask.TAG,"loop iterations : " + loop + " bytes read: " + totalRead);
                    if(totalRead % 1024 == 0) {
                        taskUpdate.TaskProgressPublish(metaData.getFname() + String.valueOf((float)totalRead/toRead*100) + "%");
                    }

                    if((toRead - totalRead) < 1024) {
                        buffer = new byte[(int) (toRead-totalRead)];
                    }
                    //This the most important part...
                    if(totalRead == metaData.getDataSize()) {
                        Log.d(MultiFileReceiverTask.TAG,"breaking from loop");
                        break;
                    }
                }

                Log.d(MultiFileReceiverTask.TAG,"loop iterations : " + loop + " bytes read: " + totalRead);
                Log.d(MultiFileReceiverTask.TAG,"outputStreamWriteToFile closing");
                outputStreamWriteToFile.close();
                Log.d(MultiFileReceiverTask.TAG,"outputStreamWriteToFile closed");
            }



        } catch (IOException e) {
            e.printStackTrace();
            Log.d(MultiFileReceiverTask.TAG,e.toString() );
            taskUpdate.TaskError(e.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            taskUpdate.TaskError(e.toString());
            Log.d(MultiFileReceiverTask.TAG,e.toString() );
        } catch (Exception e) {
            taskUpdate.TaskError(e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        taskUpdate.TaskCompleted(this.getClass().getSimpleName()+": Completed");
        if(SocketHolder.getBluetoothSocket().isConnected()) {
            Log.d(MultiFileReceiverTask.TAG,"BluetoothSocket is connected");

        } else {
            Log.d(MultiFileReceiverTask.TAG,"BluetoothSocket is ****NOT*** connected");
        }
        Log.d(MultiFileReceiverTask.TAG,"Task execution completed");

    }
}
