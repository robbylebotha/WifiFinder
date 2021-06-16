package it.ads.app.wififinder.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import it.ads.app.wififinder.Networking.SendData;
import it.ads.app.wififinder.R;
import it.ads.app.wififinder.adapters.CustomAdapter;
import it.ads.app.wififinder.models.DeviceData;
import it.ads.app.wififinder.viewmodels.DeviceDataViewModel;

/**
 * Listen to wifi system broadcast
 */
public class WifiBroadcastReciever extends BroadcastReceiver {
    WifiManager wifiManager;
    ArrayList<DeviceData> deviceData;
    String TAG = "8888";
    DeviceDataViewModel deviceDataViewModel;

    public WifiBroadcastReciever(WifiManager wifiManager){

        this.wifiManager = wifiManager;

    }

    @Override
    /**
     * App has received a message from the system
     */
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "Wifi onReceive "+action);

        boolean detectedWifiDevice = intent.getBooleanExtra(wifiManager.EXTRA_RESULTS_UPDATED, false);
        Log.i(TAG, "Scan Complete! Intent Extra: "+detectedWifiDevice);
        deviceDataViewModel = ViewModelProviders.of((FragmentActivity) context).get(DeviceDataViewModel.class);

        if (detectedWifiDevice) {
            List<ScanResult> wifiList = wifiManager.getScanResults();
            Log.i(TAG, "Found device: "+wifiList.size());
            deviceData = new ArrayList<>();
            for (ScanResult scanResult : wifiList) {
                Log.i(TAG, "BSSID: "+scanResult.BSSID+"\nName: "+scanResult.SSID
                        +"\nCap: "+scanResult.capabilities+"\nLevel"+scanResult.level);
                deviceData.add(new DeviceData(scanResult.SSID, scanResult.BSSID,
                        String.valueOf(scanResult.level)));


                //Add 1 more device to list because no other wifi devices available
                deviceData.add(new DeviceData("Test1", "8888",
                        "-50"));
                //add device to mutable list
                deviceDataViewModel.addDeviceList(deviceData);

            }
                Log.i(TAG, "New List: "+deviceData.size());

                if(!deviceData.isEmpty()){
                    //Set listview
                    SendData send = new SendData(context);
                    send.sendDeviceListToServer(deviceData);

                }else{
                    Toast.makeText(context,
                            R.string.no_device_found, Toast.LENGTH_LONG).show();
                }
        }else{
            Log.i(TAG, "Found no devices serious problem");
            Toast.makeText(context,
                    R.string.no_device_found, Toast.LENGTH_LONG).show();
        }
    }
}
