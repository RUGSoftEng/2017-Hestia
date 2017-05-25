package hestia.backend.models;

import java.util.HashMap;

public class RequiredInfo {
    private String collection;
    private String plugin;
    private HashMap<String, String> info;

    public RequiredInfo(String collection, String plugin, HashMap<String, String> info){
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
