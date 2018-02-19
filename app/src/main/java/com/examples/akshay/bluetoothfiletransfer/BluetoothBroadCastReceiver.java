package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ash on 19/2/18.
 */

public class BluetoothBroadCastReceiver extends BroadcastReceiver {

    BluetoothAdapter bluetoothAdapter;

    public BluetoothBroadCastReceiver() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
            switch (state) {
                case BluetoothAdapter.STATE_CONNECTED:
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    break;
                case BluetoothAdapter.STATE_DISCONNECTING:
                    break;
                case BluetoothAdapter. STATE_TURNING_ON:
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_OFF:
                    break;
                case BluetoothAdapter.STATE_ON:
                    break;
                default:
                    break;
            }
        }
    }
}
