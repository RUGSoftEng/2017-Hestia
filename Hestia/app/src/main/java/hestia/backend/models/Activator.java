package hestia.backend.models;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;

import hestia.backend.ComFaultException;
import hestia.backend.NetworkHandler;

/**
 * This class represents a single activator on a device. A single device can have multiple activators.
 * The activator has an id so that we can reference it on the server and
 * a rank so that they can be ordered properly, based on their rank.
 * Furthermore there is a string name and a field state of the type ActivatorState,
 * which reflects the current state of the activator
 * @see Device
 * @see ActivatorState
 */
public class Activator {
    private String activatorId;
    private Integer rank;
    private ActivatorState state;
    private String name;
    public Device device;
    private NetworkHandler handler;

    public Activator(String activatorId, Integer rank, ActivatorState state, String name) {
        this.activatorId = activatorId;
        this.rank = rank;
        this.state = state;
        this.name = name;
    }

    public Activator(String activatorId, Integer rank, ActivatorState state, String name, Device device, NetworkHandler handler) {
        this.activatorId = activatorId;
        this.rank = rank;
        this.state = state;
        this.name = name;
        this.device = device;
        this.handler = handler;
    }

    public String getId() {
        return activatorId;
    }

    public void setId(String activatorId) {
        this.activatorId = activatorId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public ActivatorState getState() {
        return state;
    }

    public void setState(ActivatorState state) throws IOException, ComFaultException {
        String path = "devices/"+device.getId()+"/activators/"+activatorId;
        JsonObject send = new JsonObject();
        send.add("state", state.getRawStateJSON());
        JsonElement payload = handler.POST(send, path);

        Log.d("Activator", payload.getAsString());

        if(payload.isJsonObject()) {
            JsonObject result = payload.getAsJsonObject();
            if(result.has("error")) {
                String error = result.get("error").getAsString();
                String message = result.get("message").getAsString();
                throw new ComFaultException(error, message);
            }

        }
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NetworkHandler getHandler() {
        return this.handler;
    }

    public void setHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activator)) return false;

        Activator activator = (Activator) o;

        if (!activatorId.equals(activator.activatorId)) return false;
        if (!getRank().equals(activator.getRank())) return false;
        if (!getState().equals(activator.getState())) return false;
        if (!getName().equals(activator.getName())) return false;
        if (!device.equals(activator.device)) return false;
        return getHandler().equals(activator.getHandler());
    }

    @Override
    public int hashCode() {
        int result = activatorId.hashCode();
        result = 31 * result + getRank().hashCode();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + device.hashCode();
        result = 31 * result + getHandler().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + " " + state;
    }
}
