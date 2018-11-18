package util;

import java.util.ArrayList;
import java.util.List;

public class InputFile {
    private int numFloors;
    private int numMainCorridors;
    private int numSubCorridors;
    private List<Action> actions = new ArrayList<>();

    private String mode;

    public int getNumFloors() {
        return numFloors;
    }

    public void setNumFloors(int numFloors) {
        this.numFloors = numFloors;
    }

    public int getNumMainCorridors() {
        return numMainCorridors;
    }

    public void setNumMainCorridors(int numMainCorridors) {
        this.numMainCorridors = numMainCorridors;
    }

    public int getNumSubCorridors() {
        return numSubCorridors;
    }

    public void setNumSubCorridors(int numSubCorridors) {
        this.numSubCorridors = numSubCorridors;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setEvents(List<Action> actions) {
        this.actions = actions;
    }

}
