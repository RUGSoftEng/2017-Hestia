package hestia.backend;

import android.app.Activity;
import android.app.Application;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import hestia.backend.requests.DeleteRequest;
import hestia.backend.requests.GetDevicesRequest;
import hestia.backend.requests.GetPluginInformationRequest;
import hestia.backend.requests.PostRequest;

/**
 * A singleton class which handles interaction between front and back-end. The facade pattern is
 * used to achieve this. During execution, there is a single NetworkHandler accessible
 * throughout the entire app through the HestiaApplication class.
 * @see hestia.UI.HestiaApplication
 */

public class NetworkHandler extends Application{
    private static NetworkHandler instance;
    private Cache cache = Cache.getInstance();

    /**
     * The empty constructor, which can not be accessed from the outside,
     * because we want a singleton behavior.
     */
    private NetworkHandler(){}

    /**
     * Returns the single instance of NetworkHandler.
     * If there was no instance of this class created previously,
     * then it will create one and return it
     * @return the single instance of NetworkHandler
     */
    public static NetworkHandler getInstance(){
        if(instance == null){
            instance = new NetworkHandler();
        }
        return instance;
    }

    /**
     * Deletes a device from the server by starting a DeleteRequest,
     * which will send a DELETE request to the server.
     * @param device the device to be deleted.
     * @see DeleteRequest
     */
    public void deleteDevice(Device device) {
        String id = device.getId();
        String path = this.getPath() + "devices/" + id;
        new DeleteRequest(path).execute();
        this.updateDevices();
    }

    /**
     * Adds a device to the server, by starting a GetPluginInformationRequest,
     * which will send a GET request to the server and, based on the data returned from
     * the GET request, will do a POST request with additional information.
     * @param collection the collection that has/manufactured the device. (e.g. Philips)
     * @param pluginName the name of the plugin the contains data of the device to be added
     * @param activity the current activity
     * @see GetPluginInformationRequest
     */
    public void addDevice(String collection, String pluginName, Activity activity) {
        String path = this.getPath() + "plugins/" + collection + "/plugins/" + pluginName;
        new GetPluginInformationRequest(path, activity).execute();
    }

    /**
     * Executes a POST request, sending a jsonObject containing all information
     * needed to add a device to the server.
     * @param requiredInfo the jsonObject containing the information relevant to adding a new device.
     */
    public void postDevice(JsonObject requiredInfo) {
        String path = NetworkHandler.getInstance().getPath() + "devices/";
        new PostRequest(path, requiredInfo).execute();
        NetworkHandler.getInstance().updateDevices();
    }

    /**
     * Updates the current list of devices by running the GetDevicesRequest, which
     * will execute a GET request for the list of devices from the server.
     */
    public void updateDevices(){
        String devicesPath = this.getPath() + "devices/";
        new GetDevicesRequest(devicesPath).execute();
    }

    /**
     * This method implements the HTTP POST method for changing the state of an activator on a
     * device as an AsyncTask.
     * @param device The target device for the post
     * @param activator The target activator for the post
     * @param newActivatorState The new state object to be used by the post
     * @see PostRequest
     */
    public void setActivatorState(Device device, Activator activator, ActivatorState newActivatorState){
        activator.setState(newActivatorState);
        String deviceId = device.getId();
        String activatorId = activator.getId();
        String path = this.getPath() + "devices/" + deviceId + "/activators/" + activatorId;
        JsonObject newState = new JsonObject();
        JsonPrimitive newStateValue = this.getNewStateValue(newActivatorState);
        newState.add("state", newStateValue);
        new PostRequest(path, newState).execute();
    }

    /**
     * Creates a JsonPrimitive containing the raw value of the new state.
     * The raw state can be either a Boolean or a Number. In case the state is something else,
     * it will return a String.
     * @param newActivatorState the new state of the device
     * @return JsonPrimitive containing the rawValue
     */
    public JsonPrimitive getNewStateValue(ActivatorState newActivatorState) {
        switch (newActivatorState.getType().toLowerCase()) {
            case "bool":
                return new JsonPrimitive(Boolean.valueOf(String.valueOf(newActivatorState.getRawState())));
            case "float":
                return new JsonPrimitive(Float.valueOf(String.valueOf(newActivatorState.getRawState())));
            default:
                return new JsonPrimitive(String.valueOf(newActivatorState.getRawState()));
        }
    }

    /**
     * Returns the path to the main page of the server.
     * This consists of the server's IP address and port number.
     * @return the path to the main page of the server
     */
    public String getPath(){
        return "http://" + cache.getIp() + ":" + cache.getPort() + "/";
    }
}
