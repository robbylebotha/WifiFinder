package it.ads.app.wififinder.viewmodels;

import android.net.wifi.ScanResult;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.ads.app.wififinder.Networking.SendData;
import it.ads.app.wififinder.R;
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

    /**
     * Get size of list
     * @return int size
     */
    public int listSize(){
        return mutableDeviceData.getValue().size();
    }

    /**
     * Clear list
     */
    public void clear(){
        mutableDeviceData.getValue().clear();
    }

}
