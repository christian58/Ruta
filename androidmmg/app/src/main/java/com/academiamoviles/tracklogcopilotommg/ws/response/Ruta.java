package com.academiamoviles.tracklogcopilotommg.ws.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07/02/2017.
 */

public class Ruta{

    @Expose
    @SerializedName("nCodRuta")
    public Integer nCodRuta;

    @Expose
    @SerializedName("cOriRuta")
    public String cOriRuta;

    @Expose
    @SerializedName("cDesRuta")
    public String cDesRuta;

    @Expose
    @SerializedName("nCodUsuIng")
    public Integer nCodUsuIng;

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
    @SerializedName("cDescripRuta")
    public String cDescripRuta;

    @Expose
    @SerializedName("nTiemRuta")
    public String nTiemRuta;

    @Expose
    @SerializedName("cPerRuta")
    public String cPerRuta;

    @Expose
    @SerializedName("cLleRuta")
    public String cLleRuta;

    @Expose
    @SerializedName("tiempoMinimoFueraCerco")
    public Integer tiempoMinimoFueraCerco;

}
