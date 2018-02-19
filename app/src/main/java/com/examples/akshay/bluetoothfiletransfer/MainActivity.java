package com.examples.akshay.bluetoothfiletransfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "===MainActivity";
    Button buttonClient;
    Button buttonServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
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
            break;
        case R.id.main_activity_button_server:
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
