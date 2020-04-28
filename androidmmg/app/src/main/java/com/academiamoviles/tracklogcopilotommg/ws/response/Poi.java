package com.academiamoviles.tracklogcopilotommg.ws.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 07/02/2017.
 */

public class Poi {

    @Expose
    @SerializedName("nCodPoi")
    public Integer nCodPoi;

    @Expose
    @SerializedName("nCodRut")
    public Integer nCodRut;

    @Expose
    @SerializedName("cNomPoi")
    public String cNomPoi;

    @Expose
    @SerializedName("cTipoPoi")
    public String cTipoPoi;

    @Expose
    @SerializedName("cDirPoi")
    public Integer cDirPoi;

    @Expose
    @SerializedName("cDistPoi")
    public String cDistPoi;

    @Expose
    @SerializedName("cProvPoi")
    public Integer cProvPoi;

    @Expose
    @SerializedName("cPaisPoi")
    public String cPaisPoi;

    @Expose
    @SerializedName("cNomViaPoi")
    public String cNomViaPoi;

    @Expose
    @SerializedName("nLatPoi")
    public Double nLatPoi;

    @Expose
    @SerializedName("nLonPoi")
    public Double nLonPoi;

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
    @SerializedName("cEstPoi")
    public String cEstPoi;

    @Expose
    @SerializedName("mensaje")
    public String mensaje;
}
