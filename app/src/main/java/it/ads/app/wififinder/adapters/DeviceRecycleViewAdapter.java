package it.ads.app.wififinder.adapters;

import android.app.Activity;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.ads.app.wififinder.R;
import it.ads.app.wififinder.models.DeviceData;

public class DeviceRecycleViewAdapter extends RecyclerView.Adapter<DeviceRecycleViewAdapter.DeviceViewHolder> {
    Activity context;
    ArrayList<DeviceData> deviceDataArrayList;
    private int lastPosition = -1 ;
    String TAG = "8888";

    public DeviceRecycleViewAdapter(Activity context, ArrayList<DeviceData> deviceArrayList) {
        this.context = context;
        this.deviceDataArrayList = deviceArrayList;
        Log.e(TAG, "RecyclerAdapter init: List Size- "+getItemCount());
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_listview,parent,false);
        return new DeviceViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceRecycleViewAdapter.DeviceViewHolder holder, int position) {

        DeviceData device =deviceDataArrayList.get(position);
        DeviceViewHolder viewHolder = holder;

        viewHolder.deviceName.setText(device.getDeviceName());
        viewHolder.deviceID.setText(device.getDeviceID());
        viewHolder.deviceStrength.setText(device.getDeviceStrength());

        //some custom animation because lists are boring.
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return deviceDataArrayList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        TextView deviceID;
        ImageView imgDevice;
        TextView deviceStrength;

        public DeviceViewHolder(@NonNull View currentItemView) {
            super(currentItemView);
            deviceName = currentItemView.findViewById(R.id.txtDeviceName);
            deviceID = currentItemView.findViewById(R.id.txtDeviceAddress);
            deviceStrength = currentItemView.findViewById(R.id.txtDeviceStrength);
            imgDevice = currentItemView.findViewById(R.id.imgDevice);


        }
    }
}
