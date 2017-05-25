package hestia.backend.models.deserializers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import hestia.backend.models.Activator;
import hestia.backend.models.Device;

/**
 * A JSON deserializer for the Device class.
 * It implements Google's JsonDeserializer interface, and it is used by GSON to deserialize the
 * activators in a device.
 * @see Device
 */

public class DeviceDeserializer implements JsonDeserializer<Device> {

    /**
     * Deserializes a JSON object, creating a Device.
     * @param json the JSON object to be deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the current context of application
     * @return a deserialized object of the specified type Device
     * @throws JsonParseException if the JSON is not in the expected format.
     */
    @Override
    public Device deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonDevices = json.getAsJsonObject();
        Log.i("JSONOBJECT - DEVICES",jsonDevices.toString());

        String deviceId = jsonDevices.get("deviceId").getAsString();
        String  type = jsonDevices.get("type").getAsString();
        String name = jsonDevices.get("name").getAsString();

        JsonArray jsonActivators = jsonDevices.get("activators").getAsJsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
        Gson gson = gsonBuilder.create();

        Type typeToken = new TypeToken<ArrayList<Activator>>(){}.getType();
        ArrayList<Activator> activators = gson.fromJson(jsonActivators, typeToken);

        Device device = new Device(deviceId, name, type, activators);
        this.connectActivatorsToDevice(activators, device);

        return new Device(deviceId, name, type, activators);
    }

    private void connectActivatorsToDevice(ArrayList<Activator> activators, Device device) {
        for(Activator activator : activators) {
            activator.setDevice(device);
        }
    }
}
