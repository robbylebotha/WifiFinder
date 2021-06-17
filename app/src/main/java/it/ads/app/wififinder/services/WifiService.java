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

import java.util.List;

import it.ads.app.wififinder.Notifications;
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
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Notifications notification = new Notifications(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiReceiver = new WifiBroadcastReciever(wifiManager);
        registerReceiver(wifiReceiver, intentFilter);
        wifiManager.startScan();
        Log.e(TAG, "Service: onCreate()");
        List<ScanResult>  results = wifiManager.getScanResults();
        if(!results.isEmpty()){
            notification.makeNotification(getApplicationContext());
        }else{
            Log.i(TAG, "Service: no results found yet");
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        Log.e(TAG, "Service: onDestroy()");
    }


}
