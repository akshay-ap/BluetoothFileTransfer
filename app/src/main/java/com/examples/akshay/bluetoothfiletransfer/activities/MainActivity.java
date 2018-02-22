package com.examples.akshay.bluetoothfiletransfer.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.examples.akshay.bluetoothfiletransfer.R;

import java.util.ArrayList;

import static android.content.pm.PackageManager.MATCH_ALL;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.examples.akshay.bluetoothfiletransfer.Constants.PERMISSIONS_REQUEST;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===MainActivity";
    Button buttonClient;
    Button buttonServer;
    private static boolean  isBluetoothAvailable;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        if(!isBluetoothAvailable) {
            buttonServer.setEnabled(false);
            buttonClient.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            makeToast(getString(R.string.bluetooth_not_supported));

            isBluetoothAvailable = false;
        } else {
            isBluetoothAvailable = true;
            makeToast(getString(R.string.bluetooth_supported));
        }

        checkPermissions();

    }

    private void setupUI() {
        buttonClient = findViewById(R.id.main_activity_button_client);
        buttonClient.setOnClickListener(this);

        buttonServer = findViewById(R.id.main_activity_button_server);
        buttonServer.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
    switch (view.getId()) {
        case R.id.main_activity_button_client:
            startActivity(new Intent(this,Server.class));
            break;
        case R.id.main_activity_button_server:
            startActivity(new Intent(this,Client.class));
            break;
        default:
            break;
        }
    }

    private void makeToast(String toToast) {
        if(toToast == null ) {
            toToast = "null";
        }
        Toast.makeText(MainActivity.this,toToast,Toast.LENGTH_SHORT).show();
    }

    public void checkPermissions() {
        // Here, thisActivity is the current activity

        boolean read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;

        ArrayList<String> arrayListPermissions = new ArrayList<>();

        if(!read) {
            arrayListPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.d(MainActivity.TAG,"READ_EXTERNAL_STORAGE permission needed");
        }
        if(!write) {
            arrayListPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.d(MainActivity.TAG,"WRITE_EXTERNAL_STORAGE permission needed");

        }
        if(!location) {
            arrayListPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            Log.d(MainActivity.TAG,"ACCESS_COARSE_LOCATION permission needed");
        }

        if(arrayListPermissions.size()!=0) {
            buttonClient.setEnabled(false);
            buttonServer.setEnabled(false);
            String[] permissions = new String[arrayListPermissions.size()];
            for (int i=0;i<arrayListPermissions.size();i++) {
                permissions[i] = arrayListPermissions.get(i);
            }

            ActivityCompat.requestPermissions(this, permissions ,PERMISSIONS_REQUEST);
            Log.d(MainActivity.TAG,"Requesting permissions");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int all = 0;
        if(requestCode == PERMISSIONS_REQUEST) {
            Log.d(MainActivity.TAG,"Number of permissions : "+ permissions.length);
            for (int res : grantResults) {
                if(res == PERMISSION_GRANTED) all++;
            }

            if(all == grantResults.length) {
                buttonClient.setEnabled(true);
                buttonServer.setEnabled(true);
                Log.d(MainActivity.TAG,"All permissions granted");
            } else {
                Log.d(MainActivity.TAG,"permissions denied : " + (grantResults.length-all));
                buttonClient.setEnabled(false);
                buttonServer.setEnabled(false);
            }

        }
    }


}
