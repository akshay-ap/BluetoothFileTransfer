package com.examples.akshay.bluetoothfiletransfer;

import android.bluetooth.BluetoothDevice;

/**
 * Created by ash on 19/2/18.
 */

public class CustomBluetoothDevice {
    private String name;
    private String MAC;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    private BluetoothDevice bluetoothDevice;
    public CustomBluetoothDevice(BluetoothDevice bluetoothDevice,String name, String MAC) {
        this.name = name;
        this.MAC = MAC;
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

}
