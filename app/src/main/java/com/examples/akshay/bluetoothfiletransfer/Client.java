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
import android.widget.Button;
import android.widget.TextView;

import static com.examples.akshay.bluetoothfiletransfer.Constants.REQUEST_ENABLE_BT;

public class Client extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===Client";
    TextView textViewBluetoothState;
    Button buttonMakeDiscoverable;
    BluetoothAdapter mBluetoothAdapter;
    private boolean isBluetoothOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
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
        textViewBluetoothState = findViewById(R.id.activity_client_textview_bluetooth_state);

        buttonMakeDiscoverable = findViewById(R.id.activity_client_make_discoverable);
        buttonMakeDiscoverable.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_client_make_discoverable:
                Log.d(Client.TAG," Make discoverable click");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                break;
            default:
                break;
        }
    }




}
