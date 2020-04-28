package com.academiamoviles.tracklogcopilototck.ui;

import android.content.Context;
import android.util.Log;

import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.model.UserPlate;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by veraj on 2/08/2018.
 */

public class Configuration {

    public static int DIMENSION_DESING_WIDTH= 640;
    public static int DIMENSION_DESING_HEIGHT= 1136;
    public static int WIDTH_PIXEL;
    public static int HEIGHT_PIXEL;
    public static UserPlate userPlate = null;
    public static int IDPLATES_HAS_PATHS;
    public static int COD_RUTA;




    public static int getHeight(int value){

        return HEIGHT_PIXEL*value/DIMENSION_DESING_HEIGHT;
    }

    public static int getWidth(int value){
        return WIDTH_PIXEL*value/DIMENSION_DESING_WIDTH;
    }

    public static void setWidthPixel(int value){
        WIDTH_PIXEL=value;
    }

    public static void setHeigthPixel(int value){
        HEIGHT_PIXEL=value;
    }

    public static boolean copyDatabase(Context context){

        try{
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION+DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length =0;
            while((length = inputStream.read(buff)) >0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("MainActivity","DB Copied");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;

        }

    }

    public static String createFormat(Date date) {

        return (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }

}
