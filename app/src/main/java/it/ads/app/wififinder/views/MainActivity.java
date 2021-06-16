package it.ads.app.wififinder.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import it.ads.app.wififinder.Networking.CheckConnectivity;
import it.ads.app.wififinder.R;
import it.ads.app.wififinder.adapters.DeviceRecycleViewAdapter;
import it.ads.app.wififinder.models.DeviceData;
import it.ads.app.wififinder.recievers.WifiBroadcastReciever;
import it.ads.app.wififinder.viewmodels.DeviceDataViewModel;

public class MainActivity extends AppCompatActivity {
    WifiBroadcastReciever wifiReceiver;
    WifiManager wifiManager;
    String TAG = "8888";
    private final int REQUEST_CODE_ASK_PERMISSIONS = 1;
//    LinearLayout progressBar;
    CheckConnectivity checkConnectivity;
    RecyclerView deviceRecycleViewer;
    DeviceDataViewModel deviceViewModel;
    DeviceRecycleViewAdapter deviceRecycleViewAdapter;
    MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab =  findViewById(R.id.btn_refresh);
        deviceRecycleViewer = findViewById(R.id.recycler_main);
        context = this;

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        checkConnectivity = new CheckConnectivity(getApplicationContext());

        //set adapter and observer
        deviceViewModel = ViewModelProviders.of(context).get(DeviceDataViewModel.class);
        deviceRecycleViewer.setLayoutManager(new LinearLayoutManager(context));
        deviceViewModel.getDeviceMutableLiveData().observe(context,deviceListUpdateObserver);


        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(getApplicationContext(), "Turning WiFi ON...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }else{
            // start broadcast receiver
            wifiReceiver = new WifiBroadcastReciever(wifiManager);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "refreshed");
                scanForWifi();
            }
        });
    }

    //set adapter and listen to updates on the live data
    Observer<ArrayList<DeviceData>> deviceListUpdateObserver = new Observer<ArrayList<DeviceData>>() {
        @Override
        public void onChanged(ArrayList<DeviceData> list) {
            deviceRecycleViewAdapter = new DeviceRecycleViewAdapter(context, list);
            deviceRecycleViewer.setAdapter(deviceRecycleViewAdapter);
            deviceRecycleViewAdapter.notifyDataSetChanged();
            Log.e(TAG,"Observer");
        }
    };


    /**
     * Start scanning for wifi devices
     */
    private void scanForWifi(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            registerReceiver(wifiReceiver, intentFilter);
            wifiManager.startScan();
        }
    }

    /**
     * From API level 23 (Android 6.0 Marshmallow) we need to ask Run-time
     * permission from the user-end
     */
    private void locationPermission(){

        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }else{
//                progressBar.setVisibility(View.VISIBLE);
                wifiManager.startScan();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(wifiReceiver);
    }

    public void onResume(){
        super.onResume();
        scanForWifi();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        scanForWifi();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    progressBar.setVisibility(View.VISIBLE);
                    wifiManager.startScan();
                } else {
                    // Permission for location Denied
                    Toast.makeText( this,"Well cant help you then!" ,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}