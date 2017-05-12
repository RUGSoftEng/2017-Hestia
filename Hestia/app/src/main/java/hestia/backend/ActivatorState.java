package hestia.backend;

/**
 * Wrapper class for the different state fields. The activator state has a type T, which is
 * inferred using a custom JSON deserializer.
 * @see ActivatorDeserializer
 * @param <T> Type of the state of the activator. This can be a boolean (for a switch) or a float
 *           (for a slider), or something else, depending on the Activator.
 */

public class ActivatorState<T> {
    private T rawState;
    private String type;

    /**
     * Creates an ActivatorState with the specified rawState and type.
     * @param rawState the value of the ActivatorState, based on the type
     * @param type the type of the activator.
     */
    public ActivatorState(T rawState, String type) {
        this.rawState = rawState;
        this.type = type;
    }

    /**
     * Returns the rawState, which is the actual value of the activator's state.
     * @return the rawState of the activator's state
     */
    public T getRawState() {
        return this.rawState;
    }

    /**
     * Replaces the current rawState of the activator's state with the specified one.
     * @param rawState the new rawState of the activator's state
     */
    public void setRawState(T rawState){
        this.rawState = rawState;
    }

    /**
     * Returns the type of the activator's state.
     * @return the type of the activator's state
     */
    public String getType(){
        return this.type;
    }

    /**
     * Replaces the current type of the activator's state with the specified one.
     * @param type the new type of the activator's state
     */
    public void setType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return this.type + " - " + this.rawState.toString();
    }
}
