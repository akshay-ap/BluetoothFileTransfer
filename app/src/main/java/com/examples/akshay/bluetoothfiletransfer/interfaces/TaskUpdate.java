package com.examples.akshay.bluetoothfiletransfer.interfaces;

/**
 * Created by ash on 22/2/18.
 */

public interface TaskUpdate {
    void TaskCompleted();
    void TaskStarted();
    void TaskProgressPublish(long Update);
}
