package com.academiamoviles.tracklogcopilotommg.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Android on 08/02/2017.
 */

public class InternalStorageUtils {

    /****
     * Almacena datos en la memoria interna del dispositivo.
     * @param context contexto.
     * @param fileName Nombre del archivo donde se almacenarán los datos.
     * @param text Texto a almacenar.
     */
    public static  void save(Context context,String fileName, String text){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****
     * Lee los datos almacenados en la memoria interna del dispositivo.
     * @param context contexto.
     * @param fileName Nombre del archivo donde se almacenan los datos.
     */
    public static String read(Context context, String fileName){
        File file = new File(context.getFilesDir(),fileName);
        if(!file.exists()){
            return "";
        }
        StringBuilder total = new StringBuilder();
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return total.toString();
    }
    /****
     * Borra los datos almacenados en la memoria interna del dispositivo.
     * @param context contexto.
     * @param fileName Nombre del archivo donde se almacenan los datos.
     */
    public static boolean delete(Context context, String fileName){
        boolean delete=false;
        try {
            delete= context.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return delete;
    }
}
