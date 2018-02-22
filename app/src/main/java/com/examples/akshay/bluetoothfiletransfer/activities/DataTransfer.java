package com.examples.akshay.bluetoothfiletransfer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.akshay.bluetoothfiletransfer.Constants;
import com.examples.akshay.bluetoothfiletransfer.Tasks.DataTransferTask;
import com.examples.akshay.bluetoothfiletransfer.R;
import com.examples.akshay.bluetoothfiletransfer.SocketHolder;
import com.examples.akshay.bluetoothfiletransfer.Tasks.FileReceiverTask;
import com.examples.akshay.bluetoothfiletransfer.Tasks.FileSenderTask;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.io.IOException;

import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_DATA;

public class DataTransfer extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "===DataTransfer";
    EditText editTextInput;
    EditText editTextFileInput;
    TextView textViewDataReceived;
    BroadcastReceiver broadcastReceiver;
    Button buttonSend;
    Button buttonTest1;
    Button buttonTest2;
    FileReceiverTask fileReceiverTask;
    IntentFilter intentFilter;
    //DataTransferTask dataTransferTask;
    FileSenderTask fileSenderTask;
    static Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer);

        setupUI();

        broadcastReceiver = getBroadCastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.DATA_TRANSFER_ACTION);
    }

    private void setupUI() {
        editTextInput = findViewById(R.id.activity_data_transfer_edittext_input);
        editTextFileInput = findViewById(R.id.activity_data_transfer_edittext_file_input);

        textViewDataReceived = findViewById(R.id.activity_data_transfer_text_received);

        buttonSend = findViewById(R.id.activity_data_transfer_button_send);
        buttonSend.setOnClickListener(this);


        buttonTest1 = findViewById(R.id.activity_data_transfer_test_1);
        buttonTest1.setOnClickListener(this);

        buttonTest2 = findViewById(R.id.activity_data_transfer_test_2);
        buttonTest2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                final String text = (String)message.obj;
                Log.d(DataTransfer.TAG,text);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewDataReceived.setText(text);
                    }
                });
                return false;
            }
        });
        //dataTransferTask = new DataTransferTask(SocketHolder.getBluetoothSocket(),handler);
        //if(!(dataTransferTask.getStatus() == AsyncTask.Status.RUNNING)) {
        //    dataTransferTask.execute();
        //}
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        //dataTransferTask.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_data_transfer_button_send:
                Log.d(DataTransfer.TAG,"Attempting to send data");
                String toSend = editTextInput.getText().toString();
//                if(dataTransferTask != null) {
//                    toSend = toSend + "";
//                    dataTransferTask.write(toSend.getBytes());
//                }
                break;
            case R.id.activity_data_transfer_test_1:
                /*Log.d(DataTransfer.TAG,"test click");
                String filePath = editTextFileInput.getText().toString();
                Toast.makeText(DataTransfer.this, "FILE: " + filePath, Toast.LENGTH_SHORT).show();
                FileSenderTask fileSenderTask = new FileSenderTask(filePath);
                fileSenderTask.execute();*/
                /*try {
                    Log.d(DataTransfer.TAG,"Trying to close socket..." + SocketHolder.getBluetoothSocket().isConnected());
                    SocketHolder.getBluetoothSocket().close();
                    Log.d(DataTransfer.TAG,"closed socket...");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(DataTransfer.TAG,"Failed to close socket...");
                    Log.d(DataTransfer.TAG,e.toString());
                }*/

                if(fileReceiverTask == null || fileReceiverTask.getStatus() == AsyncTask.Status.FINISHED) {
                    fileReceiverTask = new FileReceiverTask();
                    fileReceiverTask.execute();
                } else {
                    Toast.makeText(DataTransfer.this,"Already running another task",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.activity_data_transfer_test_2:
                String path = String.valueOf(Environment.getExternalStorageDirectory());
                new ChooserDialog().with(this)
                    .withStartFile(path)
                    .withChosenListener(new ChooserDialog.Result() {
                        @Override
                        public void onChoosePath(String path, File pathFile) {
                            Toast.makeText(DataTransfer.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                            if(fileSenderTask == null || fileSenderTask.getStatus() == AsyncTask.Status.FINISHED) {
                                fileSenderTask = new FileSenderTask(path);
                                fileSenderTask.execute();
                            }  else {
                                Toast.makeText(DataTransfer.this,"Already in sending task is running",Toast.LENGTH_SHORT).show();
                                Log.d(DataTransfer.TAG,"already sending task is running");
                            }

                        }
                    })
                    .build()
                    .show();
                break;

            default:
                break;
        }
    }

    private BroadcastReceiver getBroadCastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(DataTransfer.TAG,"broadCastReceived...");
                String data = intent.getStringExtra(DATA_TRANSFER_DATA);
                textViewDataReceived.setText(data);

            }
        };
    }
}
