package com.academiamoviles.tracklogcopilototck.ws.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android on 07/02/2017.
 */

public class Cerco {

    @Expose
    @SerializedName("nCodRut")
    public Integer nCodRut;

    @Expose
    @SerializedName("nCodCer")
    public Integer nCodCer;

    @Expose
    @SerializedName("nCodFlo")
    public Integer nCodFlo;

    @Expose
    @SerializedName("cNombre")
    public String cNombre;

    @Expose
    @SerializedName("cDesCer")
    public String cDesCer;

    @Expose
    @SerializedName("cTipo")
    public String cTipo;

    @Expose
    @SerializedName("cClaCer")
    public String cClaCer;

    @Expose
    @SerializedName("cColCer")
    public String cColCer;

    @Expose
    @SerializedName("cPuntoUnoX")
    public Double cPuntoUnoX;

    @Expose
    @SerializedName("cPuntoUnoY")
    public Double cPuntoUnoY;

    @Expose
    @SerializedName("dRadio")
    public Double dRadio;

    @Expose
    @SerializedName("gPolCer")
    public PolCer gPolCer;

    @Expose
    @SerializedName("cEstCer")
    public String cEstCer;

    @Expose
    @SerializedName("nCodUsulng")
    public Integer nCodUsulng;

    @Expose
    @SerializedName("dFecIng")
    public String dFecIng;

    @Expose
    @SerializedName("nCodUsuMod")
    public Integer nCodUsuMod;

    @Expose
    @SerializedName("dFecMod")
    public String dFecMod;

    @Expose
    @SerializedName("codigo")
    public String codigo;

    @Expose
    @SerializedName("nVelCer")
    public int nVelCer;

    @Expose
    @SerializedName("cFlagKpi")
    public String cFlagKpi;


    public static class PolCer {

        @Expose
        @SerializedName("srid")
        public Integer srid;

        @Expose
        @SerializedName("version")
        public Integer version;

        @Expose
        @SerializedName("points")
        public List<Point> points;
    }

    public static class Point {

        @Expose
        @SerializedName("x")
        public Double x;

        @Expose
        @SerializedName("y")
        public Double y;

    }
}
