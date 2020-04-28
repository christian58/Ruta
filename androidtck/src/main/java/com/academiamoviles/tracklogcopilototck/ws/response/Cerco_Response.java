package com.academiamoviles.tracklogcopilototck.ws.response;

import android.content.Context;

import com.academiamoviles.tracklogcopilototck.util.InternalStorageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 07/02/2017.
 */

public class Cerco_Response {

    @Expose
    @SerializedName("ok")
    public boolean ok;

    @Expose
    @SerializedName("data")
    public List<Cerco> listaCercos=new ArrayList<>();


    public String toJson(){

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    public static Cerco_Response fromJson(String json){

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, Cerco_Response.class);
    }

    /**Almacenamiento de los pois en la memoria interna**/

    private static final String FILE_NAME = "Cerco";

    public static void saveIS(Context context, String json){
        InternalStorageUtils.save(context, FILE_NAME, json);
    }

    public static Cerco_Response readIS(Context context){
        String json = InternalStorageUtils.read(context, FILE_NAME);
        if (json.equals(""))
            return null;

        return fromJson(json);
    }

    public static boolean deleteIS(Context context){
        return InternalStorageUtils.delete(context, FILE_NAME);
    }

}
