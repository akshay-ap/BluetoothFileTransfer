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
import com.examples.akshay.bluetoothfiletransfer.dto.Instance;

import java.util.ArrayList;

/**
 * Created by ash on 19/2/18.
 */

public class FormInstanceAdapter extends RecyclerView.Adapter<FormInstanceAdapter.ViewHolder> {

    private static final String TAG = "===BluetoothDA";
    ArrayList<Instance> arrayListFromInstance;
    private Context context;

    public FormInstanceAdapter(ArrayList<Instance> arrayListFromInstance, Context context) {
        Log.d(FormInstanceAdapter.TAG,"BluetoothDeviceAdapter()");
        this.context = context;
        this.arrayListFromInstance = arrayListFromInstance;
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
            Instance instance= arrayListFromInstance.get(getAdapterPosition());
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(FormInstanceAdapter.TAG,"onCreateViewHolder()");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_device_layout,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(FormInstanceAdapter.TAG,"onBindViewHolder()");
        Instance instance = arrayListFromInstance.get(position);
        holder.name.setText(instance.getDisplayName());
    }

    @Override
    public int getItemCount() {
        return arrayListFromInstance.size();
    }

    public ArrayList<Instance> getArrayListBluetoothDevice() {
        return arrayListFromInstance;
    }

    public void setArrayListInstanceAdapter(ArrayList<Instance> arrayListFromInstance) {
        this.arrayListFromInstance = arrayListFromInstance;
    }

}
