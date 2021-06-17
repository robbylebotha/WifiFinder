package it.ads.app.wififinder.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import it.ads.app.wififinder.Notifications;
import it.ads.app.wififinder.R;
import it.ads.app.wififinder.recievers.WifiBroadcastReciever;

import static android.os.Build.VERSION_CODES.R;

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
        getApplicationContext().registerReceiver(wifiReceiver, intentFilter);
        wifiReceiver = new WifiBroadcastReciever();
        wifiManager.startScan();
        Log.e(TAG, "Service: onCreate()");
        notification.makeNotification(getApplicationContext());
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
