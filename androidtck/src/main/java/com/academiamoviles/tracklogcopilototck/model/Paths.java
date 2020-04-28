package com.academiamoviles.tracklogcopilototck.model;

/**
 * Created by veraj on 2/08/2018.
 */

public class Paths {

    Integer idPaths;
    Integer nCodRuta;

    public Paths(Integer nCodRuta) {
        this.nCodRuta = nCodRuta;
    }

    public Integer getIdPaths() {
        return idPaths;
    }

    public void setIdPaths(Integer idPaths) {
        this.idPaths = idPaths;
    }

    public Integer getnCodRuta() {
        return nCodRuta;
    }

    public void setnCodRuta(Integer nCodRuta) {
        this.nCodRuta = nCodRuta;
    }
}
