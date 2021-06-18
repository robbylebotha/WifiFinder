package it.ads.app.wififinder.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import it.ads.app.wififinder.Networking.SendData;
import it.ads.app.wififinder.Notifications;
import it.ads.app.wififinder.R;
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
    Notifications notification;

    @Override
    public void onCreate() {
        super.onCreate();
        notification = new Notifications(getApplicationContext());
        Log.i(TAG, "Service: started onCreate()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(2, notification.makeServiceNotification());
        }else {
            startForeground(1, new Notification());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: Ping....");
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Notifications notification = new Notifications(getApplicationContext());
        Log.e(TAG, "Service: onStartCommand()");
        List<ScanResult>  results = wifiManager.getScanResults();
        if(!results.isEmpty()){
            notification.makePopupNotification("Found "+results.size()+" devices");
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
