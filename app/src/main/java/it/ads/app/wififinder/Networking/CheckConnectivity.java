package it.ads.app.wififinder.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class CheckConnectivity {
    Context context;
    private String TAG = "kopo88";

    public CheckConnectivity(Context context){
        this.context = context;
    }

    /**
     * Check if device has network connection
     * @return boolean
     */
    public boolean isConnected(){

        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        Log.i(TAG, "getNetworkStatus: "+connected);

        return connected;
    }
}
