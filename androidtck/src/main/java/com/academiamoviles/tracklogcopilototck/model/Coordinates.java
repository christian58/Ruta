package com.academiamoviles.tracklogcopilototck.model;

import java.io.Serializable;

/**
 * Created by veraj on 5/08/2018.
 */

public class Coordinates implements Serializable {
    Double latitude;
    Double longitude;

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
