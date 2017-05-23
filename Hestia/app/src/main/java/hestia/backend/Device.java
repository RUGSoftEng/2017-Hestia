package hestia.backend;

import java.util.ArrayList;

/**
 * Represents the internal representation of the device class on the client. The device contains an
 * id with which we can reference its remote version on the server. The name string contains the
 * local name of the device, for instance "Front door lock". The type string is used to denote the
 * type of the device so a GUI can be generated with the right icons at the correct location.
 * <p>
 *     Finally, there is a list of activators. These activators represent all the actions which can
 *     be performed remotely on the device. An activator can be, for instance,
 *     an On/Off switch (Toggle), or an intensity slider.
 * </p>
 * @see Activator
 */

public class Device {
    private String deviceId;
    private String name;
    private String type;
    private ArrayList<Activator> activators;

    public Device(String deviceId, String name, String type, ArrayList<Activator> activator) {
        this.deviceId = deviceId;
        this.name = name;
        this.type = type;
        this.activators = activator;
    }

    public String getId() {
        return deviceId;
    }

    public void setId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the main toggle activator, which has the rank 0.
     * @return the toggle activator.
     */
    public Activator getToggle() {
        Activator toggle = null;
        for(Activator activator : activators) {
            Integer rank = activator.getRank();
            if(rank == 0) {
                toggle = activator;
                break;
            }
        }
        return toggle;
    }

    /**
     * This method will return all activators which need to be implemented in the UI as sliders.
     * @see hestia.UI.ExpandableListAdapter
     * @return the activators if the array is not empty, null otherwise
     */
    public ArrayList<Activator> getSliders() {
        ArrayList<Activator> sliders = new ArrayList<>();
        for(Activator activator : activators){
            String type = activator.getState().getType();
            if(type.equals("float")){
                sliders.add(activator);
            }
        }
        return sliders;
    }

    /**
     * Gets the complete list of activators.
     * @return the list of activators
     */
    public ArrayList<Activator> getActivators() {
        return activators;
    }

    /**
     * Sets the local activators to a different list.
     * @param activators the list of activators which will be set
     */
    public void setActivators(ArrayList<Activator> activators) {
        this.activators = activators;
    }

    public String toString(){
        return name +" "+ deviceId + " " + activators + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;

        Device device = (Device) o;

        if (!getId().equals(device.getId())) return false;
        if (!getName().equals(device.getName())) return false;
        if (!getType().equals(device.getType())) return false;
        return getActivators().equals(device.getActivators());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getType().hashCode();
        result = 31 * result + getActivators().hashCode();
        return result;
    }
}
