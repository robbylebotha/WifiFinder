package it.ads.app.wififinder.Networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.ads.app.wififinder.R;
import it.ads.app.wififinder.models.DeviceData;
import it.ads.app.wififinder.Interfaces.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Class to send device data to server
 * @author R
 */
public class SendData {
    String TAG = "8888";
    Retrofit retrofit;
    ApiInterface client;
    Context context;
    CheckConnectivity checkConnectivity;

    public SendData(Context context){

        this.context = context;
        checkConnectivity = new CheckConnectivity(context);
    }

    /**
     * Send device data to server
     * @param deviceData ArrayList of device data object
     */
    public void sendDeviceListToServer(ArrayList<DeviceData> deviceData){
        if(checkConnectivity.isConnected()){
        }else{
            Toast.makeText(context,
                    R.string.check_network,Toast.LENGTH_LONG).show();
        }
//        Log.i(TAG, "sendWifiData");
        JSONArray jsonArray = new JSONArray();
        //create json array of devices
        for(DeviceData device : deviceData){
            JSONObject obj = new JSONObject();

            try {
                obj.put("name", device.getDeviceName());
                obj.put("id", device.getDeviceID());
                obj.put("strength", device.getDeviceStrength());
                jsonArray.put(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "sendWifiData:\n"+jsonArray.toString());
        retrofit = ApiClient.getInstance();
        client = retrofit.create(ApiInterface.class);

        Call<JSONArray> call = client.sendInfo(jsonArray);

        call.enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {

                assert response.body() != null;
                Log.i(TAG, "Server: Success updating server\n"+response.toString());
            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {
                Log.i(TAG, "Server: Failed to update server\n"+t.getCause());

            }
        });
    }
}
