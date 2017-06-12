package hestia.backend.models.deserializers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import hestia.backend.models.RequiredInfo;

public class RequiredInfoDeserializer implements JsonDeserializer<RequiredInfo> {

    /**
     * Deserializes a JSON object, creating a RequiredInfo.
     *
     * @param json    the JSON object to be deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the current context of application
     * @return a deserialized object of the specified type RequiredInfo
     * @throws com.google.gson.JsonParseException if the JSON is not in the expected format.
     */
    @Override
    public RequiredInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonInfo = json.getAsJsonObject();

        String collection = jsonInfo.get("collection").getAsString();
        String plugin = jsonInfo.get("plugin_name").getAsString();

        JsonObject required_info_json = jsonInfo.get("required_info").getAsJsonObject();
        Gson gson = new Gson();
        HashMap<String, String> required_info = gson.fromJson(required_info_json, new TypeToken<HashMap<String, String>>() {
        }.getType());

        return new RequiredInfo(collection, plugin, required_info);
    }
}