package com.rugged.application.hestia.backend.frontend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import hestia.backend.NetworkHandler;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;
import hestia.backend.models.RequiredInfo;
import hestia.backend.models.deserializers.DeviceDeserializer;
import hestia.backend.models.deserializers.RequiredInfoDeserializer;

import static android.R.attr.type;

/**
 * Created by lars on 12-6-17.
 */

public class MockServerCollectionsInteractor extends ServerCollectionsInteractor{

    private ArrayList<Device> devices;
    private ArrayList<String> collections;
    private ArrayList<String> plugins;
    private RequiredInfo requiredInfo;

    public String setIP = "";
    public String setUsername = "";

    public ArrayList<Device> getDevices() throws IOException, ComFaultException {
        return devices;
    }

    public void addDevice(RequiredInfo info) throws IOException, ComFaultException {
        setIP = info.getInfo().get("ip");
        setUsername = info.getInfo().get("username");
    }

    public MockServerCollectionsInteractor(NetworkHandler handler) {
        super(handler);

        // Init collections
        collections = new ArrayList<>();
        collections.add("CollectionOne");
        collections.add("CollectionTwo");

        // Init plugins
        plugins = new ArrayList<>();
        plugins.add("PluginOne");
        plugins.add("PluginTwo");

        //Init requiredInfo
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ip", "The ip should be entered here");
        hashMap.put("username", "The username as registerd");
        requiredInfo = new RequiredInfo("Collection", "Plugin", hashMap);

        //init devices
        devices = new ArrayList<>();
        ArrayList<Activator> activators =  new ArrayList<>();
        ActivatorState<Boolean>  lockState = new ActivatorState<>(true, "bool");
        Activator activatorLock = new Activator("idAct1", 0, lockState, "Switch");
        activators.add(activatorLock);
        Device lock = new Device("id", "name", "Lock", activators, handler);

        ArrayList<Activator> activators_sliders =  new ArrayList<>();
        ActivatorState<Boolean> lightState = new ActivatorState<>(true, "bool");
        Activator activatorLight = new Activator("idAct2", 0, lightState, "Switch");
        activators.add(activatorLight);
        ActivatorState<Float> sliderState = new ActivatorState<>(0.5f, "float");
        Activator sliderActivator = new Activator("idAct3", 1, sliderState, "Slider");
        activators.add(sliderActivator);
        Device light = new Device("id2", "Porch", "light", activators_sliders, handler);

        devices.add(lock);
        devices.add(light);

    }

    public ArrayList<String> getCollections(){
        return collections;
    }

    public ArrayList<String> getPlugins(String collection){
        return plugins;
    }

    public RequiredInfo getRequiredInfo(String collection, String plugin){
        return requiredInfo;
    }

}
