package hestia.backend.models.deserializers;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;

/**
 * A JSON deserializer for the Activator class.
 * It implements Google's JsonDeserializer interface, and it is used by GSON to deserialize the
 * activators in a device.
 * @see Activator
 */

public class ActivatorDeserializer implements JsonDeserializer<Activator> {

    /**
     * Deserializes a JSON object, creating an Activator.
     * @param json the JSON object to be deserialized
     * @param typeOfT the type of the Object to deserialize to
     * @param context the current context of application
     * @return a deserialized object of the specified type Activator
     * @throws JsonParseException if the JSON is not in the expected format.
     */
    @Override
    public Activator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = (JsonObject) json;
        Log.i("JSONOBJECT - ACTIVATORS",jsonObject.toString());
        String stateType = jsonObject.get("type").getAsString();
        String rawState = jsonObject.get("state").getAsString();
        ActivatorState state = null;

        switch (stateType.toLowerCase()) {
            case "bool":
                state = new ActivatorState<Boolean>(Boolean.parseBoolean(rawState),"bool");
                break;
            case "float" :
                state = new ActivatorState<Float>(Float.parseFloat(rawState),"float");
                break;
            default : break;
        }

        String activatorId = jsonObject.get("activatorId").getAsString();
        Integer rank = jsonObject.get("rank").getAsInt();
        String name = jsonObject.get("name").getAsString();

        return new Activator(activatorId, rank, state, name);
    }
}
