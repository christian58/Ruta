package com.academiamoviles.tracklogcopilototck.model;

import java.util.List;

/**
 * Created by veraj on 5/08/2018.
 */

public class Stadistics {

    Integer speed;
    Integer promExcess;
    String duration;
    List<Coordinates> listCoordinates;

    public Stadistics(Integer speed, Integer promExcess, String duration, List<Coordinates> listCoordinates) {
        this.speed = speed;
        this.promExcess = promExcess;
        this.duration = duration;
        this.listCoordinates = listCoordinates;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getPromExcess() {
        return promExcess;
    }

    public void setPromExcess(Integer promExcess) {
        this.promExcess = promExcess;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<Coordinates> getListCoordinates() {
        return listCoordinates;
    }

    public void setListCoordinates(List<Coordinates> listCoordinates) {
        this.listCoordinates = listCoordinates;
    }
}
