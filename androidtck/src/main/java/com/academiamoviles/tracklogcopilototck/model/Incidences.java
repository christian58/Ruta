package com.academiamoviles.tracklogcopilototck.model;

import java.util.Date;

/**
 * Created by veraj on 2/08/2018.
 */

public class Incidences {

    Integer idIncidences;
    Integer idPlates_has_paths;
    Integer speed;
    Integer excess;
    Double latitude;
    Double longitude;
    Date register;
    Integer idgroup;

    public Incidences(Integer idPlates_has_paths, Integer speed, Integer excess, Double latitude, Double longitude, Date register, Integer idgroup) {
        this.idPlates_has_paths = idPlates_has_paths;
        this.speed = speed;
        this.excess = excess;
        this.latitude = latitude;
        this.longitude = longitude;
        this.register = register;
        this.idgroup = idgroup;
    }

    public Integer getIdIncidences() {
        return idIncidences;
    }

    public void setIdIncidences(Integer idIncidences) {
        this.idIncidences = idIncidences;
    }

    public Integer getIdPlates_has_paths() {
        return idPlates_has_paths;
    }

    public void setIdPlates_has_paths(Integer idPlates_has_paths) {
        this.idPlates_has_paths = idPlates_has_paths;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getExcess() {
        return excess;
    }

    public void setExcess(Integer excess) {
        this.excess = excess;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    public Integer getIdgroup() {
        return idgroup;
    }

    public void setIdroup(Integer idgroup) {
        this.idgroup = idgroup;
    }
}
