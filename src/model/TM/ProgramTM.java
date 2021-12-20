package model.TM;/*
author :Himal
version : 0.0.1
*/

import javafx.scene.layout.HBox;

public class ProgramTM {
    String id;
    String name;
    String duration;
    double fee;
    HBox action;

    public ProgramTM() {
    }

    public ProgramTM(String id, String name, String duration, double fee, HBox action) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.fee = fee;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public HBox getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }


}
