package hestia.backend.requests;

import android.util.Log;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import hestia.backend.Activator;
import hestia.backend.ActivatorDeserializer;
import hestia.backend.Cache;
import hestia.backend.Device;

/**
 * This class will do a GET request, expecting an ArrayList containing the list of devices.
 * @see GetRequest
 */

public class GetDevicesRequest extends GetRequest<ArrayList<Device>> {

    public GetDevicesRequest(String path) {
        super(path);
    }

    @Override
    protected void setRegisterTypeAdapter(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
    }

    @Override
    protected Type getReturnType() {
        return new TypeToken<ArrayList<Device>>(){}.getType();
    }

    @Override
    protected void onPostExecute(HttpURLConnection urlConnection) {
        ArrayList<Device> devices = super.getReturnValue();
        if(devices != null) {
            Cache.getInstance().setDevices(devices);
        } else {
            Log.e("GetDevicesRequest", "DEVICES ARRAY IS NULL");
        }
    }
}
