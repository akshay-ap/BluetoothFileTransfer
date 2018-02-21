package com.examples.akshay.bluetoothfiletransfer.Tasks;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.examples.akshay.bluetoothfiletransfer.SocketHolder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ash on 20/2/18.
 *
 */

public class DataTransferTask extends AsyncTask {
    private static final String TAG = "===DataTransferTask";
    //private static volatile DataTransferTask dataTransferThreadSingleton;
    BluetoothSocket socket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler handler;
    public DataTransferTask(BluetoothSocket socket, Handler handler) {
        this.socket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.handler = handler;
        try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            //mState = STATE_CONNECTED;

        if (SocketHolder.getMODE() == 0) {
            if (this.socket == null) {
                Log.d(TAG, "Won't work...socket is null");

            } else {
                Log.d(TAG, "will work...socket is set");

            }
        } else if (SocketHolder.getMODE() == 1) {
            if (this.socket == null) {
                Log.d(TAG, "won't work...socket is null");

            } else {
                Log.d(TAG, "will work...serversocket is set");
            }
        } else {
            Log.d(TAG, "Invalid socket state :" +  SocketHolder.getMODE());

        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        while (!isCancelled()) {
            try {
                Thread.sleep(1000);
                byte[] buffer = new byte[1024];
                int bytes;
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    if(bytes == -1) {
                        continue;
                    }
                    String text = new String(buffer);
                    text = text.substring(0,bytes);
                    Log.d(DataTransferTask.TAG,text);
                    Message msg = new Message();
                    msg.obj = text;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    //connectionLost();
                    break;
                } catch (Exception e) {
                    Log.e(DataTransferTask.TAG,e.toString());
                }
                Log.d(DataTransferTask.TAG," in run()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

    public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
}
