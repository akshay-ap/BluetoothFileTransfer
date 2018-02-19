package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.examples.akshay.bluetoothfiletransfer.Constants.REQUEST_ENABLE_BT;

public class Server extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===Server";
    TextView textViewBluetoothState;
    BluetoothAdapter mBluetoothAdapter;
    private boolean isBluetoothOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        setupUI();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            isBluetoothOn = true;
            textViewBluetoothState.setText(R.string.STATE_OFF);
            Log.d(TAG, "OFF");

        } else {
            isBluetoothOn =false;
            textViewBluetoothState.setText(R.string.STATE_ON);
            Log.d(TAG, "ON");


        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void setupUI(){
        textViewBluetoothState = findViewById(R.id.activity_server_textview_bluetooth_state);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }




}
