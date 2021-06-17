package it.ads.app.wififinder.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
        Log.i(TAG, "Service: started onCreate()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }else {
            startForeground(1, new Notification());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "it.ads.app.wififinder";
        String channelName = "Wifi Finder Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_wifi)
                .setContentTitle("Wifi Finder running in background...")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: Ping....");
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Notifications notification = new Notifications(getApplicationContext());
        Log.e(TAG, "Service: onStartCommand()");
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
