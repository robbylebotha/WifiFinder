package it.ads.app.wififinder.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.ads.app.wififinder.models.DeviceData;

public class DeviceDataViewModel extends ViewModel {
    MutableLiveData<ArrayList<DeviceData>> mutableDeviceData = new MutableLiveData<>();
    ArrayList<DeviceData> tempDeviceData =new ArrayList<>();
    String TAG = "8888";

    public MutableLiveData<ArrayList<DeviceData>> getDeviceMutableLiveData() {
        return mutableDeviceData;
    }

    /**
     * Add a list of devices
     * @param deviceData ArrayList of devices
     */
    public void addDeviceList(ArrayList<DeviceData> deviceData){
        mutableDeviceData.setValue(deviceData);
        Log.e(TAG, "Added device to mutable list\n"+mutableDeviceData.getValue().size());
    }

    /**
     * Add a single device to list
     * @param device device object
     */
    public void addDevice(DeviceData device){
        tempDeviceData.add(device);
        mutableDeviceData.setValue(tempDeviceData);
        Log.e(TAG, "Added device to mutable list\n"+mutableDeviceData.getValue().size());
    }

    public int listSize(){
        return mutableDeviceData.getValue().size();
    }

    public void clear(){
        mutableDeviceData.getValue().clear();
    }
}
