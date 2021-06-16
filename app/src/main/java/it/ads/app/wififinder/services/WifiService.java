package it.ads.app.wififinder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import it.ads.app.wififinder.recievers.WifiBroadcastReciever;

/**
 * Service to periodically scan for wifi device in background
 */
public class WifiService extends Service {
    String TAG = "8888";
    WifiManager wifiManager;
    WifiBroadcastReciever wifiReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiReceiver, intentFilter);
        wifiReceiver = new WifiBroadcastReciever();
        wifiManager.startScan();
        Log.e(TAG, "Service: onCreate()");
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
