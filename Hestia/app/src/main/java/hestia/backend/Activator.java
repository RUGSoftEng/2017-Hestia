package hestia.backend;

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
    private int rank;
    private ActivatorState state;
    private String name;

    /**
     * Creates an Activator with the specified id, state and name.
     * @param activatorId the id of the activator
     * @param state the current state of the activator, this is a wrapper around a generic type
     * @param name the name of the activator
     */
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

    public void setState(ActivatorState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Activator)) return false;

        Activator activator = (Activator) o;

        if (getRank() != activator.getRank()) return false;
        if (!activatorId.equals(activator.activatorId)) return false;
        if (!getState().equals(activator.getState())) return false;
        return getName().equals(activator.getName());

    }

    @Override
    public int hashCode() {
        int result = activatorId.hashCode();
        result = 31 * result + getRank();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + " " + state;
    }
}
