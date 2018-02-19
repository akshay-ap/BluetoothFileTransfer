package com.examples.akshay.bluetoothfiletransfer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.examples.akshay.bluetoothfiletransfer.Constants.PERMISSIONS_REQUEST_LOCATION;
import static com.examples.akshay.bluetoothfiletransfer.Constants.REQUEST_ENABLE_BT;

public class Server extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===Server";
    TextView textViewBluetoothState;
    Button buttonViewPairedDevices;
    Button buttonStartScanDevices;
    Button buttonStopScanDevices;

    RecyclerView recyclerViewPairedDevices;
    RecyclerView recyclerViewScannedDevices;
    BluetoothDeviceAdapter  bluetoothDeviceAdapterPairedDevices;
    BluetoothDeviceAdapter bluetoothDeviceAdapterScanedDevices;

    ArrayList<CustomBluetoothDevice> arrayListPairedDevices;
    ArrayList<CustomBluetoothDevice> arrayListScannedDevices = new ArrayList<>();

    BluetoothAdapter mBluetoothAdapter;

    IntentFilter filter;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        setupUI();

        arrayListPairedDevices = new ArrayList<>();
        arrayListScannedDevices = new ArrayList<>();

        bluetoothDeviceAdapterPairedDevices = new BluetoothDeviceAdapter(arrayListPairedDevices);
        bluetoothDeviceAdapterScanedDevices = new BluetoothDeviceAdapter(arrayListScannedDevices);

        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    CustomBluetoothDevice customBluetoothDevice = new CustomBluetoothDevice(deviceName,deviceHardwareAddress);
                    arrayListScannedDevices.add(customBluetoothDevice);
                    bluetoothDeviceAdapterScanedDevices.setArrayListCustomBluetoothDevice(arrayListScannedDevices);
                    recyclerViewScannedDevices.setAdapter(bluetoothDeviceAdapterScanedDevices);

                    Log.d(Server.TAG,"Found device : " + deviceName + " " + deviceHardwareAddress);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Log.d(Server.TAG,"ACTION_DISCOVERY_STARTED");
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.d(Server.TAG,"ACTION_DISCOVERY_FINISHED");
                }
            }
        };
        checkLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiver, filter);
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
        unregisterReceiver(mReceiver);
    }

    private void setupUI(){
        buttonViewPairedDevices = findViewById(R.id.activity_server_button_view_paired_devices);
        buttonViewPairedDevices.setOnClickListener(this);

        buttonStartScanDevices = findViewById(R.id.activity_server_button_start_scan_devices);
        buttonStartScanDevices.setOnClickListener(this);

        buttonStopScanDevices = findViewById(R.id.activity_server_button_stop_scan_devices);
        buttonStopScanDevices.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManagerPairedDevices = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManagerScannedDevices = new LinearLayoutManager(getApplicationContext());

        textViewBluetoothState = findViewById(R.id.activity_server_textview_bluetooth_state);

        recyclerViewPairedDevices = findViewById(R.id.activity_server_recycler_view_paired_devices);
        recyclerViewPairedDevices.setLayoutManager(layoutManagerPairedDevices);
        recyclerViewPairedDevices.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPairedDevices.setAdapter(bluetoothDeviceAdapterPairedDevices);

        recyclerViewScannedDevices = findViewById(R.id.activity_server_recycler_view_scanned_devices);
        recyclerViewScannedDevices.setLayoutManager(layoutManagerScannedDevices);
        recyclerViewScannedDevices.setItemAnimator(new DefaultItemAnimator());
        recyclerViewScannedDevices.setAdapter(bluetoothDeviceAdapterScanedDevices);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_server_button_view_paired_devices:
                Log.d(Server.TAG,"Clicked View Paired Devices");
                arrayListPairedDevices = getPairedDevices();
                bluetoothDeviceAdapterPairedDevices.setArrayListCustomBluetoothDevice(arrayListPairedDevices);
                //bluetoothDeviceAdapterPairedDevices.notifyDataSetChanged();
                recyclerViewPairedDevices.setAdapter(bluetoothDeviceAdapterPairedDevices);
                break;

            case R.id.activity_server_button_start_scan_devices:
                arrayListScannedDevices.clear();
                Log.d(Server.TAG," Scan start click...");
                mBluetoothAdapter.startDiscovery();
                break;

            case R.id.activity_server_button_stop_scan_devices:
                Log.d(Server.TAG," Scan stop click...");
                if(mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    Log.d(Server.TAG,"calling mBluetoothAdapter.cancelDiscovery()");
                } else {
                    Log.d(Server.TAG,"mBluetoothAdapter.isDiscovering() is false...cannot mBluetoothAdapter.cancelDiscovery()");
                }
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

    public void checkLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
                buttonStopScanDevices.setEnabled(false);
                buttonStartScanDevices.setEnabled(false);
                buttonViewPairedDevices.setEnabled(false);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSIONS_REQUEST_LOCATION);
                Log.d(Server.TAG,"Requesting location permission");
        } else {
            Log.d(Server.TAG,"Location permission granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Log.d(Server.TAG,"PERMISSION_GRANTED");
                buttonStopScanDevices.setEnabled(true);
                buttonStartScanDevices.setEnabled(true);
                buttonViewPairedDevices.setEnabled(true);
            } else {
                Log.d(Server.TAG,"PERMISSION_DENIED");
                buttonStopScanDevices.setEnabled(false);
                buttonStartScanDevices.setEnabled(false);
                buttonViewPairedDevices.setEnabled(false);
            }
        }

    }
}
