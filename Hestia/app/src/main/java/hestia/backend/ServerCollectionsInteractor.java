package hestia.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Device;
import hestia.backend.models.deserializers.DeviceDeserializer;
import hestia.backend.models.RequiredInfo;
import hestia.backend.models.deserializers.RequiredInfoDeserializer;

/**
 * A singleton class acts as a temporary memory, storing the data regarding the list of devices,
 * IP address, or port number. During execution, there is a single ServerCollectionsInteractor accessible.
 */
public class ServerCollectionsInteractor implements Serializable{
    private NetworkHandler handler;

    public ServerCollectionsInteractor(NetworkHandler handler){
        this.handler = handler;
    }

    public ArrayList<Device> getDevices() throws IOException, ComFaultException {
        String endpoint = "devices/";
        JsonElement payload = handler.GET(endpoint);
        if(payload.isJsonArray()) {
            JsonArray jsonArray = payload.getAsJsonArray();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Device.class, new DeviceDeserializer(handler));
            Gson gson = gsonBuilder.create();

            Type type = new TypeToken<ArrayList<Device>>(){}.getType();
            ArrayList<Device> devices = gson.fromJson(jsonArray, type);
            this.connectDevicesToHandler(devices);
            return devices;
        } else {
            JsonObject jsonObject = payload.getAsJsonObject();
            String error = jsonObject.get("error").getAsString();
            String message = jsonObject.get("message").getAsString();
            throw new ComFaultException(error, message);
        }
    }

    public void addDevice(RequiredInfo info) throws IOException, ComFaultException {
        JsonObject send = new JsonObject();
        send.addProperty("collection", info.getCollection());
        send.addProperty("plugin_name", info.getPlugin());
        JsonObject required = new JsonObject();
        for(String key : info.getInfo().keySet()){
            required.addProperty(key, info.getInfo().get(key));
        }
        send.add("required_info", required);
        String endpoint = "devices/";
        JsonElement payload = handler.POST(send, endpoint);
        if(payload.isJsonObject()) {
            JsonObject object = payload.getAsJsonObject();
            if(object.has("error")) {
                String error = object.get("error").getAsString();
                String message = object.get("message").getAsString();
                throw new ComFaultException(error, message);
            }
        }
    }

    public void removeDevice(Device device) throws IOException, ComFaultException {
        String endpoint = "devices/" + device.getId();
        JsonElement payload = handler.DELETE(endpoint);
        if(payload != null && payload.isJsonObject()) {
            JsonObject jsonObject = payload.getAsJsonObject();
            if(jsonObject.has("error")) {
                String error = jsonObject.get("error").getAsString();
                String message = jsonObject.get("message").getAsString();
                throw new ComFaultException(error, message);
            }
        }
    }

    public ArrayList<String> getCollections() throws IOException, ComFaultException {
        JsonElement object = handler.GET("plugins");
        return ParseInfo(object);
    }

    public ArrayList<String> getPlugins(String collection) throws IOException, ComFaultException {
        JsonElement object = handler.GET("plugins/" + collection);
        return ParseInfo(object);
    }

    public RequiredInfo getRequiredInfo(String collection, String plugin) throws IOException, ComFaultException {
        JsonElement rawObject = handler.GET("plugins/" + collection + "/plugins/" + plugin);
        if (rawObject.isJsonObject()) {
            JsonObject object = rawObject.getAsJsonObject();
            if(object.has("error")) {
                String error = object.get("error").getAsString();
                String message = object.get("message").getAsString();
                throw new ComFaultException(error, message);
            } else {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(RequiredInfo.class, new RequiredInfoDeserializer());
                Gson gson = gsonBuilder.create();

                RequiredInfo requiredInfo = gson.fromJson(object, RequiredInfo.class);
                return requiredInfo;
            }
        }
        return null;
    }

    private ArrayList<String> ParseInfo(JsonElement element) throws ComFaultException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        if(element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            ArrayList<String> list = gson.fromJson(array, new TypeToken<ArrayList<String>>() {
            }.getType());
            return list;
        } else if (element.getAsJsonObject().has("error")){
            ComFaultException comFaultException=gson.fromJson(element,ComFaultException.class);
            throw comFaultException;
        }
        return new ArrayList<>();
    }

    public NetworkHandler getHandler() {
        return handler;
    }

    public void setHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    private void connectDevicesToHandler(ArrayList<Device> devices) {
        for(Device device : devices) {
            device.setHandler(this.handler);
        }
    }
}
