package com.examples.akshay.bluetoothfiletransfer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ash on 19/2/18.
 */

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    public ArrayList<CustomBluetoothDevice> getArrayListCustomBluetoothDevice() {
        return arrayListCustomBluetoothDevice;
    }

    public void setArrayListCustomBluetoothDevice(ArrayList<CustomBluetoothDevice> arrayListCustomBluetoothDevice) {
        this.arrayListCustomBluetoothDevice = arrayListCustomBluetoothDevice;
    }

    ArrayList<CustomBluetoothDevice> arrayListCustomBluetoothDevice;
    private static final String TAG = "===BluetoothDA";

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "===ViewHolder";
        public TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(ViewHolder.TAG,"ViewHolder()");
            name = itemView.findViewById(R.id.card_device_layout_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.d(ViewHolder.TAG,"ViewHolder()...click");

        }
    }

    public BluetoothDeviceAdapter(ArrayList<CustomBluetoothDevice> customBluetoothDevice) {
        Log.d(BluetoothDeviceAdapter.TAG,"BluetoothDeviceAdapter()");
        this.arrayListCustomBluetoothDevice = customBluetoothDevice;
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
        CustomBluetoothDevice customBluetoothDevice = arrayListCustomBluetoothDevice.get(position);
        holder.name.setText(customBluetoothDevice.getName());

    }

    @Override
    public int getItemCount() {

        ///Log.d(BluetoothDeviceAdapter.TAG,"getItemCount() : " + arrayListCustomBluetoothDevice.size());
        return arrayListCustomBluetoothDevice.size();
    }



}
