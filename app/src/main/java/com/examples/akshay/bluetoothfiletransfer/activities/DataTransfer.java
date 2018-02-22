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
import com.examples.akshay.bluetoothfiletransfer.R;
import com.examples.akshay.bluetoothfiletransfer.Tasks.FileReceiverTask;
import com.examples.akshay.bluetoothfiletransfer.Tasks.FileSenderTask;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;

import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_DATA;

public class DataTransfer extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "===DataTransfer";


    BroadcastReceiver broadcastReceiver;
    Button buttonTest1;
    Button buttonTest2;
    FileReceiverTask fileReceiverTask;
    IntentFilter intentFilter;
    FileSenderTask fileSenderTask;
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

        buttonTest1 = findViewById(R.id.activity_data_transfer_test_1);
        buttonTest1.setOnClickListener(this);

        buttonTest2 = findViewById(R.id.activity_data_transfer_test_2);
        buttonTest2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_data_transfer_test_1:

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
            }
        };
    }
}
