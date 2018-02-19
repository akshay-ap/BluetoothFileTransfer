package com.examples.akshay.bluetoothfiletransfer;

/**
 * Created by ash on 19/2/18.
 */

public class CustomBluetoothDevice {
    private String name;
    private String MAC;

    public CustomBluetoothDevice(String name, String MAC) {
        this.name = name;
        this.MAC = MAC;
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
