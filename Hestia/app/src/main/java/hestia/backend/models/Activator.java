package hestia.backend.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rugged.application.hestia.R;

import java.io.IOException;

import hestia.UI.HestiaApplication;
import hestia.backend.NetworkHandler;
import hestia.backend.exceptions.ComFaultException;

/**
 * This class represents a single activator on a device. A single device can have multiple activators.
 * The activator has an id so that we can reference it on the server and
 * a rank so that they can be ordered properly, based on their rank.
 * Furthermore there is a string name and a field state of the type ActivatorState,
 * which reflects the current state of the activator
 *
 * @see Device
 * @see ActivatorState
 * @see NetworkHandler
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
        String path = "devices/" + device.getId() + "/activators/" + activatorId;
        JsonObject send = new JsonObject();
        send.add("state", state.getRawStateJSON());
        JsonElement payload = handler.POST(send, path);

        if (payload != null && payload.isJsonObject()) {
            JsonObject result = payload.getAsJsonObject();
            if (result.has("error")) {
                String error = result.get("error").getAsString();
                throw new ComFaultException(error);
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
    public boolean equals(Object object) {
        if (!(object instanceof Activator)) return false;
        Activator activator = (Activator) object;
        return (this == activator || (this.getId().equals(activator.getId()) &&
                this.getRank().equals(activator.getRank()) &&
                this.getState().equals(activator.getState()) &&
                this.getName().equals(activator.getName()) &&
                this.getHandler().equals(activator.getHandler())));
    }

    @Override
    public int hashCode() {
        int multiplier = Integer.valueOf(HestiaApplication.getContext().getString(R.string.hashCodeMultiplier));
        int result = getId().hashCode();
        result = result * multiplier + getRank().hashCode();
        result = result * multiplier + getState().hashCode();
        result = result * multiplier + getName().hashCode();
        result = result * multiplier + getHandler().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + " " + state;
    }
}
