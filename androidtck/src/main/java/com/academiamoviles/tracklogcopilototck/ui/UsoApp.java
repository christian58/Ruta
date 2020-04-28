package com.academiamoviles.tracklogcopilototck.ui;

import android.content.Context;

import com.academiamoviles.tracklogcopilototck.util.InternalStorageUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by administrador on 21/02/2017.
 */

public class UsoApp {

    @Expose
    @SerializedName("fecha")
    public List<String> fecha= new ArrayList<>();


    public List<Calendar> fechaCalendar= new ArrayList<>();

    public void addFecha(Calendar calendar){
        fechaCalendar.add(calendar);
        fecha.add(dateFormat(calendar));
    }

    private String dateFormat(Calendar fecha){


        SimpleDateFormat sdfFormat = new SimpleDateFormat("EEEE dd MMMM hh:mm:ss aa");
        String strDate= sdfFormat.format(fecha.getTime());

        return strDate;
    }



    public String toJson(){

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }

    public static UsoApp fromJson(String json){

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, UsoApp.class);
    }

    private static final String FILE_NAME = "USO_APP";

    public void saveIS(Context context){
        InternalStorageUtils.save(context, FILE_NAME, toJson());
    }

    public static UsoApp readIS(Context context){
        String json = InternalStorageUtils.read(context, FILE_NAME);
        if(!json.equals("")){
            UsoApp usoApp = fromJson(json);
            for (int i = 0; i < usoApp.fecha.size(); i++) {
                usoApp.fechaCalendar.add(i, getCalendar(usoApp.fecha.get(i)));

            }
            return usoApp;
        }
        return  new UsoApp();
    }

    public static boolean deleteIS(Context context){
        return InternalStorageUtils.delete(context, FILE_NAME);
    }

    public static Calendar getCalendar(String fecha){

        SimpleDateFormat format1 = new SimpleDateFormat("EEEE dd MMMM hh:mm:ss aa");
        Date date = new Date();
        Calendar c = Calendar.getInstance();

        try {
            date = format1.parse(fecha);
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.setTime(date);
        return c;
    }

}
