package hestia.backend.refactoring;

import android.util.Log;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import hestia.backend.Activator;
import hestia.backend.ActivatorDeserializer;
import hestia.backend.BackendInteractor;
import hestia.backend.Device;

public class GetDevicesRequest extends GetRequest<ArrayList<Device>> {

    private final String TAG = "GetDevicesRequest";

    public GetDevicesRequest(String path) {
        super(path);
    }

    @Override
    protected void registerTypeAdapter(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
    }

    @Override
    protected void onPostExecute(HttpURLConnection urlConnection) {
        ArrayList<Device> devices = super.getReturnValue();
        if(devices != null) {
            BackendInteractor.getInstance().setDevices(devices);
        } else {
            Log.e(TAG, "DEVICES ARRAY IS NULL");
        }
    }
}
