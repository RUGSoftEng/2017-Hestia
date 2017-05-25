package hestia.backend.models.deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import hestia.backend.models.RequiredInfo;

/**
 * Created by Lars on 25-5-2017.
 */

public class RequiredInfoDeserializer implements JsonDeserializer<RequiredInfo> {

    @Override
    public RequiredInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonInfo = json.getAsJsonObject();

        String collection = jsonInfo.get("collection").getAsString();
        String plugin = jsonInfo.get("plugin_name").getAsString();

        JsonObject required_info_json = jsonInfo.get("required_info").getAsJsonObject();
        Gson gson = new Gson();
        HashMap<String, String> required_info = gson.fromJson(required_info_json, new TypeToken<HashMap<String, String>>(){}.getType());

        return new RequiredInfo(collection, plugin, required_info);
    }
}

/**
 * Deserializes a JSON object, creating a Device.
 * @param json the JSON object to be deserialized
 * @param typeOfT the type of the Object to deserialize to
 * @param context the current context of application
 * @return a deserialized object of the specified type Device
 * @throws com.google.gson.JsonParseException if the JSON is not in the expected format.
 *
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
 } {*/