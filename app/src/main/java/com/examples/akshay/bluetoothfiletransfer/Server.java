package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import static com.examples.akshay.bluetoothfiletransfer.Constants.REQUEST_ENABLE_BT;

public class Server extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===Server";
    TextView textViewBluetoothState;
    Button buttonViewPairedDevices;
    RecyclerView recyclerViewPairedDevices;
    BluetoothDeviceAdapter bluetoothDeviceAdapter;
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
        buttonViewPairedDevices = findViewById(R.id.activity_server_button_view_paired_devices);
        buttonViewPairedDevices.setOnClickListener(this);


        textViewBluetoothState = findViewById(R.id.activity_server_textview_bluetooth_state);
        recyclerViewPairedDevices = findViewById(R.id.activity_server_recycler_view_paired_devices);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_server_button_view_paired_devices:
                Log.d(Server.TAG,"Clicked View Paired Devices");

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerViewPairedDevices.setLayoutManager(mLayoutManager);
                recyclerViewPairedDevices.setItemAnimator(new DefaultItemAnimator());
                ArrayList<CustomBluetoothDevice> pairedDevices = getPairedDevices();
                bluetoothDeviceAdapter = new BluetoothDeviceAdapter(pairedDevices);
                recyclerViewPairedDevices.setAdapter(bluetoothDeviceAdapter);

                bluetoothDeviceAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private ArrayList<CustomBluetoothDevice> getPairedDevices(){
        ArrayList<CustomBluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {

                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                CustomBluetoothDevice customBluetoothDevice = new CustomBluetoothDevice(deviceName,deviceHardwareAddress);
                bluetoothDeviceArrayList.add(customBluetoothDevice);

                Log.d(Server.TAG,"Paired : " + deviceName + " " + deviceHardwareAddress);
            }
        }
        return bluetoothDeviceArrayList;
    }



}
