package com.examples.akshay.bluetoothfiletransfer.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.akshay.bluetoothfiletransfer.R;
import com.examples.akshay.bluetoothfiletransfer.activities.Client;

import java.util.ArrayList;

/**
 * Created by ash on 19/2/18.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private static final String TAG = "===BluetoothDA";
    ArrayList<BluetoothDevice> arrayListBluetoothDevice;
    private Context context;

    public BluetoothDeviceAdapter(ArrayList<BluetoothDevice> bluetoothDevice, Context context) {
        Log.d(BluetoothDeviceAdapter.TAG,"BluetoothDeviceAdapter()");
        this.context = context;
        this.arrayListBluetoothDevice = bluetoothDevice;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "===ViewHolder";
        public TextView name;
        public int id;
        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(ViewHolder.TAG,"ViewHolder()");
            name = itemView.findViewById(R.id.card_device_layout_name);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            Log.d(ViewHolder.TAG,"ViewHolder()...click" + getAdapterPosition());
            BluetoothDevice bluetoothDevice= arrayListBluetoothDevice.get(getAdapterPosition());
            Client.setBluetoothDeviceSelected(bluetoothDevice,context);
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(BluetoothDeviceAdapter.TAG,"onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_device_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(BluetoothDeviceAdapter.TAG,"onBindViewHolder()");
        BluetoothDevice bluetoothDevice = arrayListBluetoothDevice.get(position);
        holder.name.setText(bluetoothDevice.getName());
    }

    @Override
    public int getItemCount() {
        return arrayListBluetoothDevice.size();
    }

    public ArrayList<BluetoothDevice> getArrayListBluetoothDevice() {
        return arrayListBluetoothDevice;
    }

    public void setArrayListBluetoothDevice(ArrayList<BluetoothDevice> arrayListBluetoothDevice) {
        this.arrayListBluetoothDevice = arrayListBluetoothDevice;
    }

}
