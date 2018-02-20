package com.examples.akshay.bluetoothfiletransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_DATA;
import static com.examples.akshay.bluetoothfiletransfer.Constants.DATA_TRANSFER_SOCKET;

public class DataTransfer extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "===DataTransfer";
    EditText editTextInput;
    TextView textViewDataReceived;
    BroadcastReceiver broadcastReceiver;
    Button buttonSend;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer);

        setupUI();

        broadcastReceiver = getBroadCastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.DATA_TRANSFER_ACTION);
        DataTransferThread dataTransferThread = DataTransferThread.getInstance(SocketHolder.getBluetoothSocket());
        dataTransferThread.execute();
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
            case R.id.activity_data_transfer_button_send:
                Log.d(DataTransfer.TAG,"Attempting to send data");
                String toSend = editTextInput.getText().toString();
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
