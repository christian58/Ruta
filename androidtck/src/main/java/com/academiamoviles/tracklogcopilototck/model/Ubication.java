package com.academiamoviles.tracklogcopilototck.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veraj on 5/08/2018.
 */

public class Ubication {
    List<Coordinates> listCoordinates = new ArrayList<>();

    public List<Coordinates> getListCoordinates() {
        return listCoordinates;
    }

    public void setListCoordinates(List<Coordinates> listCoordinates) {
        this.listCoordinates = listCoordinates;
    }
}
