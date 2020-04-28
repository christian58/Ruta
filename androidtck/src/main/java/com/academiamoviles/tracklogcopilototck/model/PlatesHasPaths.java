package com.academiamoviles.tracklogcopilototck.model;

import java.util.Date;

/**
 * Created by veraj on 2/08/2018.
 */

public class PlatesHasPaths {

    Integer idPlates_has_paths;
    Integer idPlates;
    Integer idPaths;
    Date date;

    public PlatesHasPaths(Integer idPlates, Integer idPaths, Date date) {
        this.idPlates = idPlates;
        this.idPaths = idPaths;
        this.date = date;
    }

    public Integer getIdPlates_has_paths() {
        return idPlates_has_paths;
    }

    public void setIdPlates_has_paths(Integer idPlates_has_paths) {
        this.idPlates_has_paths = idPlates_has_paths;
    }

    public Integer getIdPlates() {
        return idPlates;
    }

    public void setIdPlates(Integer idPlates) {
        this.idPlates = idPlates;
    }

    public Integer getIdPaths() {
        return idPaths;
    }

    public void setIdPaths(Integer idPaths) {
        this.idPaths = idPaths;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
