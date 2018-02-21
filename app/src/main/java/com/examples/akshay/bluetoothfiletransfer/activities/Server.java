package com.examples.akshay.bluetoothfiletransfer.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.examples.akshay.bluetoothfiletransfer.Threads.AcceptThread;
import com.examples.akshay.bluetoothfiletransfer.R;

import static com.examples.akshay.bluetoothfiletransfer.Constants.REQUEST_ENABLE_BT;

public class Server extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===Server";
    TextView textViewBluetoothState;
    Button buttonMakeDiscoverable;
    Button buttonAccpet;
    BroadcastReceiver broadcastReceiver;
    Button buttonDataTransfer;
    BluetoothAdapter mBluetoothAdapter;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server);
        setupUI();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        broadcastReceiver = getBraodCastReceiver();
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,intentFilter);
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            textViewBluetoothState.setText(R.string.STATE_OFF);
            Log.d(TAG, "OFF");

        } else {
            textViewBluetoothState.setText(R.string.STATE_ON);
            Log.d(TAG, "ON");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);

    }

    private void setupUI(){
        textViewBluetoothState = findViewById(R.id.activity_server_textview_bluetooth_state);

        buttonMakeDiscoverable = findViewById(R.id.activity_server_make_discoverable);
        buttonMakeDiscoverable.setOnClickListener(this);

        buttonAccpet = findViewById(R.id.activity_server_accept_connections);
        buttonAccpet.setOnClickListener(this);

        buttonDataTransfer = findViewById(R.id.activity_server_transfer_data);
        buttonDataTransfer.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_server_make_discoverable:
                Log.d(Server.TAG," Make discoverable click");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                break;
            case R.id.activity_server_accept_connections:
                Log.d(Server.TAG,"accept() click...");
                AcceptThread acceptThread = new AcceptThread(this,mBluetoothAdapter);
                acceptThread.run();
                break;
            case R.id.activity_server_transfer_data:
                Log.d(Server.TAG," click transfer data");
                Intent intent = new Intent(this,DataTransfer.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private BroadcastReceiver getBraodCastReceiver() {
        BroadcastReceiver mReceiver;
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.d(Server.TAG,"onReceive()");

                String action = intent.getAction();
                if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                    Log.d(Server.TAG,"Connection State Changed");
                }
            }
        };
        return  mReceiver;
    }
}
