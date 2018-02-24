package com.examples.akshay.bluetoothfiletransfer.activities;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.examples.akshay.bluetoothfiletransfer.InstanceProviderAPI;
import com.examples.akshay.bluetoothfiletransfer.R;

import java.util.Arrays;

public class CollectSend extends AppCompatActivity {

    public static final String TAG = "===CollectSend";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_send);

        Cursor mCursor = getDataFromContentProvider();

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
