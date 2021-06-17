package it.ads.app.wififinder;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import it.ads.app.wififinder.Interfaces.Scanner;
import it.ads.app.wififinder.recievers.WifiBroadcastReciever;

public class WifiScanner implements Scanner {
    WifiBroadcastReciever wifiReceiver;
    WifiManager wifiManager;
    Context context;
    final String TAG = "8888";

    public WifiScanner(Context context){
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }
    @Override
    public void startScanner() {

        if (!isEnabled()) {
            Toast.makeText(context, "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }else{
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            context.registerReceiver(wifiReceiver, intentFilter);
            // start broadcast receiver
            wifiReceiver = new WifiBroadcastReciever(wifiManager);
            wifiManager.startScan();

            Log.i(TAG, "Wifi scanner started");
        }
    }

    @Override
    public void stopScanner() {
        context.unregisterReceiver(wifiReceiver);
        Log.i(TAG, "Wifi broadcast unregistered");
    }

    @Override
    public boolean isEnabled() {
        return wifiManager.isWifiEnabled();
    }

    @Override
    public void scannerError(String error) {
        Log.i(TAG, error.toString());
    }
}
