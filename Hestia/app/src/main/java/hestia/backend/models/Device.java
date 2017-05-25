package hestia.backend.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;

import hestia.backend.ComFaultException;
import hestia.backend.NetworkHandler;

/**
 * Represents the internal representation of the device class on the client. The device contains an
 * id with which we can reference its remote version on the server. The name string contains the
 * local name of the device, for instance "Front door lock". The type string is used to denote the
 * type of the device so a GUI can be generated with the right icons at the correct location.
 * <p>
 *     Finally, there is a list of activators. These activators represent all the actions which can
 *     be performed remotely on the device. An activator can be, for instance,
 *     an On/Off switch (Toggle), or an intensity slider.
 * </p>
 * @see Activator
 */

public class Device {
    private String deviceId;
    private String name;
    private String type;
    private ArrayList<Activator> activators;
    private NetworkHandler handler;

    public Device(String deviceId, String name, String type, ArrayList<Activator> activator) {
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.activators = activator;
    }

    public Device(String deviceId, String name, String type, ArrayList<Activator> activator, NetworkHandler handler) {
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.activators = activator;
        this.handler = handler;
    }

    public String getId() {
        return deviceId;
    }

    public void setId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NetworkHandler getHandler() {
        return handler;
    }

    public void setHandler(NetworkHandler handler) {
        this.handler = handler;
        for(Activator activator : activators) {
            activator.setHandler(handler);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IOException, ComFaultException {
        String path = "devices/"+deviceId;
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        JsonElement payload = handler.PUT(object, path);
        if(payload.getAsJsonObject().has("error")){
            GsonBuilder gsonBuilder=new GsonBuilder();
            Gson gson = gsonBuilder.create();
            ComFaultException comFaultException=gson.fromJson(payload,ComFaultException.class);
            throw comFaultException;
        }else {
            this.name = name;
        }
    }

    public ArrayList<Activator> getActivators() {
        return activators;
    }

    public void setActivators(ArrayList<Activator> activators) {
        this.activators = activators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (!getId().equals(device.getId())) return false;
        if (!getName().equals(device.getName())) return false;
        if (!getType().equals(device.getType())) return false;
        if (!getActivators().equals(device.getActivators())) return false;
        return getHandler().equals(device.getHandler());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getActivators().hashCode();
        result = 31 * result + getHandler().hashCode();
        return result;
    }

    public String toString(){
        return name +" "+ deviceId + " " + activators + "\n";
    }

    /* Everything below should be done in the frontend
    /**
     * Returns the main toggle activator, which has the rank 0.
     * @return the toggle activator.

    public Activator getToggle() {
        Activator toggle = null;
        for(Activator activator : activators) {
            Integer rank = activator.getRank();
            if(rank == 0) {
                toggle = activator;
                break;
            }
        }
        return toggle;
    }

    /**
     * This method will return all activators which need to be implemented in the UI as sliders.
     * @see hestia.UI.Activities.Home.ExpandableDeviceList
     * @return the activators if the array is not empty, null otherwise

    public ArrayList<Activator> getSliders() {
        ArrayList<Activator> sliders = new ArrayList<>();
        for(Activator activator : activators){
            String type = activator.getState().getType();
            if(type.equals("float")){
                sliders.add(activator);
            }
        }
        return sliders;
    }*/
}
