package com.examples.akshay.bluetoothfiletransfer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DataTransfer extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "===DataTransfer";
    EditText editTextInput;
    TextView textViewDataReceived;
    Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer);
    }

    private void setupUI() {
        editTextInput = findViewById(R.id.activity_data_transfer_edittext_input);
        textViewDataReceived = findViewById(R.id.activity_data_transfer_text_received);

        buttonSend = findViewById(R.id.activity_data_transfer_button_send);
        buttonSend.setOnClickListener(this);
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
}
