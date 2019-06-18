package com.example.uidesign;

public class Behavior {
    private String action;
    private int value;

    public Behavior(String action, int value){
        this.action = action;
        this.value = value;

    }

    public String getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
