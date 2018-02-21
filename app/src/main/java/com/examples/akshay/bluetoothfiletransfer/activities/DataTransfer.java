package com.examples.akshay.bluetoothfiletransfer.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_DATA;

public class DataTransfer extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "===DataTransfer";
    EditText editTextInput;
    TextView textViewDataReceived;
    BroadcastReceiver broadcastReceiver;
    Button buttonSend;
    IntentFilter intentFilter;
    DataTransferTask dataTransferTask;
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
        textViewDataReceived = findViewById(R.id.activity_data_transfer_text_received);

        buttonSend = findViewById(R.id.activity_data_transfer_button_send);
        buttonSend.setOnClickListener(this);
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
        dataTransferTask = new DataTransferTask(SocketHolder.getBluetoothSocket(),handler);
        if(!(dataTransferTask.getStatus() == AsyncTask.Status.RUNNING)) {
            dataTransferTask.execute();
        }
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
                if(dataTransferTask != null) {
                    toSend = toSend + "";
                    dataTransferTask.write(toSend.getBytes());
                }
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
