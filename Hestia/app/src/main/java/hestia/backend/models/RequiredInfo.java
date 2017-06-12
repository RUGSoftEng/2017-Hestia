package hestia.backend.models;

import java.util.HashMap;

/**
 * This class holds the data that needs to be sent to the server in order to add a new device.
 * It contains a field for collection, a field for the plugin and a field "info".
 * Here, "info" is a HashMap and holds key-value pairs containing additional information
 * specific to each device. For instance, a Philips Hue Light may need to know the IP address
 * and port number of the bridge.
 */
public class RequiredInfo {
    private String collection;
    private String plugin;
    private HashMap<String, String> info;

    public RequiredInfo(String collection, String plugin, HashMap<String, String> info) {
        this.collection = collection;
        this.plugin = plugin;
        this.info = info;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }
}
