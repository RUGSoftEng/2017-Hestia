package com.rugged.application.hestia;

import java.util.ArrayList;

/**
 * This class contains the clients internal representation of the peripheral connected to the
 * server.
 */
public class Peripheral {
    private String type;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}