package com.academiamoviles.tracklogcopilototck.model;

/**
 * Created by veraj on 2/08/2018.
 */

public class Plates {

    Integer idPlates;
    String serie;
    Integer idUsers;

    public Plates(String serie, Integer idUsers) {
        this.serie = serie;
        this.idUsers = idUsers;
    }

    public Integer getIdPlates() {
        return idPlates;
    }

    public void setIdPlates(Integer idPlates) {
        this.idPlates = idPlates;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(Integer idUsers) {
        this.idUsers = idUsers;
    }
}
