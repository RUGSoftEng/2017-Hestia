package com.rugged.application.hestia;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientInteractionController {
    ArrayList<Device> devices;
    private final static String TAG = "ClntInterController";
    private String path;

    public ClientInteractionController(String path){
        this.path = path;

        try {
            devices = new DeviceListRetriever(path).execute().get();
        } catch (InterruptedException e) {
            Log.e(TAG,e.toString());
        } catch (ExecutionException e) {
            Log.e(TAG,e.toString());
        }

    }

    public ArrayList<Device> getDevices(){
        assert(devices != null);
        return devices;
    }
/*
    setActivatorState(int devId, int actId, T newState){
    }*/
}
