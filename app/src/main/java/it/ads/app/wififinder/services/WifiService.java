package it.ads.app.wififinder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import it.ads.app.wififinder.Networking.SendData;
import it.ads.app.wififinder.Notifications;
import it.ads.app.wififinder.models.DeviceData;
import it.ads.app.wififinder.recievers.WifiBroadcastReciever;

/**
 * Service to periodically scan for wifi device in background
 * @author RLB
 */
public class WifiService extends Service {
    String TAG = "8888";
    WifiManager wifiManager;
    WifiBroadcastReciever wifiReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service: started onCreate()");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: Ping....");
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Notifications notification = new Notifications(getApplicationContext());
//        wifiReceiver = new WifiBroadcastReciever(wifiManager);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        registerReceiver(wifiReceiver, intentFilter);
//        wifiManager.startScan();
        Log.e(TAG, "Service: onCreate()");
        List<ScanResult>  results = wifiManager.getScanResults();
        if(!results.isEmpty()){
            notification.makeNotification("Found "+results.size()+" devices");
            ArrayList<DeviceData> deviceData = new ArrayList<>();
            for (ScanResult scanResult : results) {
                Log.i(TAG, "BSSID: "+scanResult.BSSID+"\nName: "+scanResult.SSID
                        +"\nCap: "+scanResult.capabilities+"\nLevel"+scanResult.level);
                deviceData.add(new DeviceData(scanResult.SSID, scanResult.BSSID,
                        String.valueOf(scanResult.level)));
                SendData send = new SendData(getApplicationContext());
                send.sendDeviceListToServer(deviceData);

            }
        }else{
            Log.i(TAG, "Service: no results found yet");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service: onDestroy()");
    }


}
