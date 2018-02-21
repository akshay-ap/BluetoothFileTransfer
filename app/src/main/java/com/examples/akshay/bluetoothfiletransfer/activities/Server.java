package com.examples.akshay.bluetoothfiletransfer.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.akshay.bluetoothfiletransfer.Constants;
import com.examples.akshay.bluetoothfiletransfer.Tasks.AcceptConnectionTask;
import com.examples.akshay.bluetoothfiletransfer.R;
import com.examples.akshay.bluetoothfiletransfer.Utils;


import java.util.ArrayList;
import java.util.Set;

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
    AlertDialog alertDialog;
    ArrayList<BluetoothDevice> pairedDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server);
        setupUI();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        broadcastReceiver = getBraodCastReceiver();
        intentFilter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Accepting connections");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(Server.TAG,"Cancel button Clicked");

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_server_make_discoverable:
                Log.d(Server.TAG," Make discoverable click");
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, Constants.DISCOVERABLE_DURATION);
                startActivity(discoverableIntent);
                break;
            case R.id.activity_server_accept_connections:
                //buttonAccpet.setEnabled(false);
                Log.d(Server.TAG,"accept() click...");
                if(!alertDialog.isShowing()) {
                    //waitingDialog.setMessage("Accepting connections");
                    alertDialog.show();
                } else {
                    Log.e(Server.TAG,"Error... this should not happen click activity_server_accept_connections");
                }
                AcceptConnectionTask acceptConnectionTask = new AcceptConnectionTask(this,mBluetoothAdapter);
                acceptConnectionTask.execute();

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
                setState();

                String action = intent.getAction();
                if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                    Log.d(Server.TAG,"Connection State Changed");

                } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    Log.d(Server.TAG,"BluetoothDevice.ACTION_ACL_CONNECTED");
                    if(alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }

                    //For logging purpose
                    ArrayList<BluetoothDevice> Connecteddevices = Utils.getPairedDevices(mBluetoothAdapter);
                    for (BluetoothDevice d : Connecteddevices) {
                        Log.d(Server.TAG,d.getName() + " " + d.getAddress() + " " + d.getBondState());
                    }

                    Toast.makeText(Server.this,R.string.device_connected,Toast.LENGTH_SHORT).show();

                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    Log.d(Server.TAG,"BluetoothDevice.ACTION_ACL_DISCONNECTED");
                    Toast.makeText(Server.this,R.string.device_disconnected,Toast.LENGTH_SHORT).show();

                } else if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                    Log.d(Server.TAG,"BluetoothDevice.ACTION_SCAN_MODE_CHANGED");
                }
            }
        };
        return  mReceiver;
    }

    public void setState() {
        int state = mBluetoothAdapter.getState();
        switch (state) {
            case BluetoothAdapter.STATE_OFF:
                textViewBluetoothState.setText(R.string.STATE_OFF);
                break;

            case BluetoothAdapter.STATE_TURNING_ON:
                textViewBluetoothState.setText(R.string.STATE_TURNING_ON);
                break;

            case BluetoothAdapter.STATE_ON:
                textViewBluetoothState.setText(R.string.STATE_ON);
                break;

            case BluetoothAdapter.STATE_TURNING_OFF:
                if(mBluetoothAdapter.isDiscovering()) {
                    Log.d(Server.TAG,"Cancelling discovery");
                    mBluetoothAdapter.cancelDiscovery();
                }
                textViewBluetoothState.setText(R.string.STATE_TURNING_OFF);
                break;

            default:
                textViewBluetoothState.setText(R.string.unknown);
                break;
        }

        int scanMode = mBluetoothAdapter.getScanMode();
        switch (scanMode) {
            case BluetoothAdapter.SCAN_MODE_NONE:
                Log.d(Server.TAG,"BluetoothAdapter.SCAN_MODE_NONE");

                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                Log.d(Server.TAG,"BluetoothAdapter.SCAN_MODE_CONNECTABLE");

                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                Log.d(Server.TAG,"BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE");

                break;
            default:
                Log.d(Server.TAG,"This should never occur");
                break;
        }
    }

}
