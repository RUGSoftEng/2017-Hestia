package hestia.backend;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A singleton class acts as a temporary memory, storing the data regarding the list of devices,
 * IP address, or port number. During execution, there is a single Cache accessible.
 */
public class Cache {
    /**
     * We use a CopyOnWriteArrayList to avoid ConcurrentModificationExceptions if
     * a listener attempts to remove itself during event notification.
     */
    private final CopyOnWriteArrayList<DevicesChangeListener> listeners = new CopyOnWriteArrayList<>();
    private static Cache instance;
    private ArrayList<Device> devices = new ArrayList<>();
    private String ip = "82.73.173.179";
    private int port = 8000;

    /**
     * The empty constructor, which can not be accessed from the outside,
     * because we want a singleton behavior.
     */
    private Cache() {}

    /**
     * Returns the single instance of Cache.
     * If there was no instance of this class created previously,
     * then it will create one and return it
     * @return the single instance of Cache
     */
    public static Cache getInstance(){
        if(instance == null){
            instance = new Cache();
        }
        return instance;
    }

    public ArrayList<Device> getDevices(){
        return devices;
    }

    /**
     * Replaces the current list of devices with the specified one and will fire a change event.
     * @param devices the new list of devices
     */
    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
        fireChangeEvent();
    }

    public String getIp(){
        return this.ip;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public int getPort(){
        return port;
    }

    public void setPort(int port){
        this.port = port;
    }

    /**
     * Triggers a change event. The change is propagated to all listeners.
     * @see hestia.UI.DeviceListFragment
     */
    private void fireChangeEvent() {
        DevicesEvent evt = new DevicesEvent(this);
        for (DevicesChangeListener listener : listeners) {
            listener.changeEventReceived(evt);
        }
    }

    /**
     * Adds a DeviceChangeListener to the list of listeners.
     * @param listener the listener to be added to the list of listeners.
     */
    public void addDevicesChangeListener(DevicesChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a DeviceChangeListener from the the list of listeners.
     * @param listener the listener to be removed from the list of listeners.
     */
    public void removeDevicesChangeListener(DevicesChangeListener listener) {
        this.listeners.remove(listener);
    }

    public CopyOnWriteArrayList<DevicesChangeListener> getListeners(){
        return this.listeners;
    }

}
