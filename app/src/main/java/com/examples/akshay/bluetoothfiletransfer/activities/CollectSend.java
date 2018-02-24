package com.examples.akshay.bluetoothfiletransfer.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.examples.akshay.bluetoothfiletransfer.InstanceProviderAPI;
import com.examples.akshay.bluetoothfiletransfer.R;
import com.examples.akshay.bluetoothfiletransfer.adapters.FormInstanceAdapter;
import com.examples.akshay.bluetoothfiletransfer.dto.Instance;

import java.util.ArrayList;
import java.util.Arrays;

public class CollectSend extends AppCompatActivity {

    RecyclerView recyclerViewPairedDevices;
    FormInstanceAdapter formInstanceAdapter;
    Cursor mCursor;
    public static final String TAG = "===CollectSend";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_send);

        formInstanceAdapter = new FormInstanceAdapter(new ArrayList<Instance>(),CollectSend.this);
        mCursor = getDataFromContentProvider();

        setupUI();

        ArrayList<Instance> instances = new ArrayList<>();
        while (mCursor.moveToNext()) {

            Instance.Builder builder = new Instance.Builder();
            String displayName = mCursor.getString(mCursor.getColumnIndex(InstanceProviderAPI.InstanceColumns.DISPLAY_NAME));
            String instancePath = mCursor.getString(mCursor.getColumnIndex(InstanceProviderAPI.InstanceColumns.INSTANCE_FILE_PATH));
            builder.displayName(displayName);
            builder.instanceFilePath(instancePath);

            Log.d(CollectSend.TAG,"Instance displayname : "+displayName +" path : "+instancePath);
            instances.add(builder.build());
        }

        formInstanceAdapter.setArrayListInstanceAdapter(instances);
        recyclerViewPairedDevices.setAdapter(formInstanceAdapter);


    }
    private void setupUI() {
        recyclerViewPairedDevices = findViewById(R.id.activity_collect_send_recycler_view_from_instances);
        RecyclerView.LayoutManager layoutManagerPairedDevices = new LinearLayoutManager(getApplicationContext());
        recyclerViewPairedDevices.setLayoutManager(layoutManagerPairedDevices);
        recyclerViewPairedDevices.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPairedDevices.setAdapter(formInstanceAdapter);
    }

    private Cursor getDataFromContentProvider() {
        Cursor mCursor= getContentResolver().query(InstanceProviderAPI.InstanceColumns.CONTENT_URI,null,null,null,null);

        if (null == mCursor) {
            Log.d(CollectSend.TAG," mCursor is null");
            Toast.makeText(CollectSend.this,"Cursor is null",Toast.LENGTH_SHORT).show();
        } else if (mCursor.getCount() < 1) {

            Log.d(CollectSend.TAG," mCursor is empty");
            Toast.makeText(CollectSend.this,"Cursor is empty",Toast.LENGTH_SHORT).show();
        } else {
            Log.d(CollectSend.TAG," mCursor is non-empty");
            Toast.makeText(CollectSend.this,"Cursor is non-empty",Toast.LENGTH_SHORT).show();
            Log.d(CollectSend.TAG," mCursor no. of cloumns : " +mCursor.getColumnCount());
            String cloumnNames [] = mCursor.getColumnNames();
            Log.d(CollectSend.TAG," mCursor cloumns : " +  Arrays.toString(cloumnNames));
        }
        return  mCursor;
    }
}
