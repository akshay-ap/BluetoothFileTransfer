package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
            startActivity(new Intent(this,Client.class));
            break;
        case R.id.main_activity_button_server:
            startActivity(new Intent(this,Server.class));
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

}
