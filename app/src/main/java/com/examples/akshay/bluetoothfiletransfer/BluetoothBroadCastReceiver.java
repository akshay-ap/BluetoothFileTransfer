package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ash on 19/2/18.
 */

public class BluetoothBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "===BluetoothBR";
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
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_DISCONNECTING:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter. STATE_TURNING_ON:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_OFF:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(BluetoothBroadCastReceiver.TAG,"");

                    break;
                default:
                    Log.d(BluetoothBroadCastReceiver.TAG,"unknown");

                    break;
            }
        }
    }
}
