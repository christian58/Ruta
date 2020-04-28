package com.academiamoviles.tracklogcopilotommg.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by Android on 07/02/2017.
 */
public class Mensaje_Dialogfragment extends BaseDialogFragment {

    public static final int REQCOD_DIALOG_MENSAJE = 2;

    String titulo, mensaje, botonPositivo, botonNegativo;
    int nroBotones;

    /**Muestra la ventana de diálogo con el boton aceptar y cancelar.
     * @param titulo Título de la ventana de diálogo
     * @param mensaje Mensaje de la ventana de diálogo
     * Por defecto muestra dos botones (Aceptar y cancelar)*/
    public static Mensaje_Dialogfragment newInstance(String titulo, String mensaje) {
        return newInstance(titulo, mensaje, 2);
    }

    /**Muestra la ventana de diálogo con el número de botones asignados.
     * @param titulo Título de la ventana de diálogo
     * @param mensaje Mensaje de la ventana de diálogo
     * @param nroBotones Número de botones a mostrar en la ventana de diálogo. El mayor número a ingresar es 2.
     *                   1: Muestra el botón Aceptar.
     *                   2:  Muestra el botón Aceptar y Cancelar.
     *                   Más: No muestra botones.*/

    public static Mensaje_Dialogfragment newInstance(String titulo, String mensaje, int nroBotones) {
        Bundle args = new Bundle();
        args.putString("tituloLista",titulo);
        args.putString("mensaje",mensaje);
        args.putInt("nroBotones",nroBotones);
        args.putString("botonPositivo","ACEPTAR");
        args.putString("botonNegativo","CANCELAR");
        Mensaje_Dialogfragment fragment = new Mensaje_Dialogfragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**Muestra la ventana de diálogo solo con el boton positivo
     * @param titulo Título de la ventana de diálogo
     * @param mensaje Mensaje de la ventana de diálogo
     * @param botonPositivo Nombre del botón positivo.*/
    public static Mensaje_Dialogfragment newInstance(String titulo, String mensaje, String botonPositivo) {
        return newInstance(titulo, mensaje, botonPositivo,"");
    }

    /**Muestra la ventana de diálogo solo con el boton positivo y negativo
     * @param titulo Título de la ventana de diálogo
     * @param mensaje Mensaje de la ventana de diálogo
     * @param botonPositivo Nombre del botón positivo
     * @param botonNegativo Nombre del botón negativo.*/
    public static Mensaje_Dialogfragment newInstance(String titulo, String mensaje, String botonPositivo, String botonNegativo) {
        Bundle args = new Bundle();
        args.putString("tituloLista",titulo);
        args.putString("mensaje",mensaje);
        args.putInt("nroBotones",2);
        args.putString("botonPositivo",botonPositivo);
        args.putString("botonNegativo",botonNegativo);
        Mensaje_Dialogfragment fragment = new Mensaje_Dialogfragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Mensaje_Dialogfragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titulo = getArguments().getString("tituloLista");
        mensaje = getArguments().getString("mensaje");
        nroBotones = getArguments().getInt("nroBotones");
        botonPositivo = getArguments().getString("botonPositivo");
        botonNegativo = getArguments().getString("botonNegativo");
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(titulo);
        dialog.setMessage(mensaje);
        setCancelable(false);
        dialog.setPositiveButton(botonPositivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener!=null)
                    listener.onClickDialogButton(getDialog(),getTargetRequestCode(), RESULT_POSITIVE, null);
                //dismiss();
            }
        });
        if(nroBotones==2) {
            dialog.setNegativeButton(botonNegativo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(listener!=null)
                        listener.onClickDialogButton(getDialog(), getTargetRequestCode(), RESULT_NEGATIVE, null);
                    //dismiss();
                }
            });
        }

        return dialog.create();
    }

}
