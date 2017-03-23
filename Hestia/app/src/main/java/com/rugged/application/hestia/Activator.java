package com.rugged.application.hestia;

public class Activator<T> {
    private int activatorId;
    private T state;
    private String name;
    private String stateType;

    private String requiredInfo;


    public Activator(int id, T state, String name, String type) {
        this.activatorId = id;
        this.state = state;
        this.name = name;
        this.stateType = type;
    }

    public int getId() {
        return activatorId;
    }

    public void setId(int id) {
        this.activatorId = id;
    }


    public T getState() {

        return state;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return stateType;
    }

    public void setType(String type) {
        this.stateType = type;
    }

    public String toString() {
        return name + " " + state;
    }
}
