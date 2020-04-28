package com.academiamoviles.tracklogcopilotommg.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Toast;

import com.academiamoviles.tracklogcopilotommg.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Android on 07/02/2017.
 */
public class Util {

    public static void setUrl(Context context) {
       Constants.URL = context.getResources().getString(R.string.base_url);
    }

    /***
     * Verifica la conección a internet
     * */
    public static Boolean connection(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;

    }

    public static void showToast(Context context, String mensaje) {
        Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String dateFormat(int styleDate, int styleTime, Calendar calendar) {

        DateFormat df = DateFormat.getDateTimeInstance(styleDate, styleTime,  new Locale("es", "PE"));
        return df.format(calendar.getTime());
    }

    /***
     * Verifica  si el proveedor de GPS está habilitado.
     * */
    public static boolean GPSProviderEnabled(Context mContext) {

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            return false;
        }
        return true;
    }

    /***
     * Método que verifica el permiso para detectar la ubicación.
     * */
    public static boolean checkPermissionLocation(Context mContext){
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public static final int REQCOD_PERMISSION_LOCATION=200;
    /***
     * Método que muestra el diálogo que solicita el permiso de ubicación.
     * */
    public static void requestPermissionLocation(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQCOD_PERMISSION_LOCATION);
    }

    public static void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Configuración de GPS");

        alertDialog.setMessage("GPS no habilitado. ¿Desea ir al menú de configuración?");

        alertDialog.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Método que obtiene Información del paquete de la aplicación.
     * @param mContext Contexto de la aplicación
     * **/
    public static PackageInfo getPackageInfo(Context mContext) {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener Info: " + e);
        }
    }

}
