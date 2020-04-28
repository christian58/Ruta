package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.academiamoviles.tracklogcopilotommg.MyApp;
import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.ui.activity.LoginActivity;
import com.academiamoviles.tracklogcopilotommg.ui.dialog.BaseDialogFragment;
import com.academiamoviles.tracklogcopilotommg.ui.dialog.Mensaje_Dialogfragment;
import com.academiamoviles.tracklogcopilotommg.ui.dialog.Progress_Dialogfragment;
import com.academiamoviles.tracklogcopilotommg.ws.asynctask.HttpAsyncTask;

public class BaseFragment extends Fragment implements BaseDialogFragment.DialogListener{

    /** Standard HttpAsynctask result: operation canceled. */
    public static final int RESULT_CANCELED    = HttpAsyncTask.RESULT_CANCELED;
    /** Standard HttpAsynctask result: operation succeeded. */
    public static final int RESULT_OK           = HttpAsyncTask.RESULT_OK;
    /** Start HttpAsynctask result: operation error. */
    public static final int RESULT_ERROR   = HttpAsyncTask.RESULT_ERROR;

    public static final int ERROR_DATOS   = HttpAsyncTask.ERROR_DATOS;

    public MyApp myApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApp) getActivity().getApplication();
    }


    public void updateWinztaApp(){
        myApp = (MyApp) getActivity().getApplication();
    }


    public String getStringResource(int id){
        return getActivity().getResources().getString(id);
    }

    public void abrirLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }

    //-------------------------------------------
    // DialogFragment
    //-------------------------------------------

    @Override
    public void onClickDialogButton(Dialog dialog, int requestCode, int resultCode, Object object) {
        if(requestCode== Mensaje_Dialogfragment.REQCOD_DIALOG_MENSAJE){
            dialog.dismiss();
        }
    }

    public void showMensajeDialog(String mensaje){
        showMensajeDialog(getStringResource(R.string.app_name), mensaje, Mensaje_Dialogfragment.REQCOD_DIALOG_MENSAJE);
    }

    public void showMensajeDialog(String titulo, String mensaje, int requestCode){
        FragmentManager fm = getFragmentManager();
        Mensaje_Dialogfragment dialog = (Mensaje_Dialogfragment) fm.findFragmentByTag("mensaje_dialog");
        if(dialog==null) {
            dialog = Mensaje_Dialogfragment.newInstance(titulo, mensaje,1);
            dialog.setTargetFragment(this, requestCode);
            dialog.show(fm, "mensaje_dialog");
        }
    }

    public void showMensajeDialog(String titulo, String mensaje, int requestCode, int numBotones){
        FragmentManager fm = getFragmentManager();
        Mensaje_Dialogfragment dialog = (Mensaje_Dialogfragment) fm.findFragmentByTag("mensaje_dialog");
        if(dialog==null) {
            dialog = Mensaje_Dialogfragment.newInstance(titulo, mensaje,numBotones);
            dialog.setTargetFragment(this, requestCode);
            dialog.show(fm, "mensaje_dialog");
        }
    }

    public void showProgress(String mensaje){
        FragmentManager fm = getFragmentManager();
        Progress_Dialogfragment dialog = (Progress_Dialogfragment) fm.findFragmentByTag("progress_dialog");

        if(dialog==null) {
            dialog =Progress_Dialogfragment.newInstance(mensaje);
            dialog.setTargetFragment(this, Progress_Dialogfragment.REQCOD_DIALOG_PROGRESS);
            dialog.show(fm, "progress_dialog");
        }
    }

    public void hideProgress(){
        FragmentManager fm = getFragmentManager();
        Progress_Dialogfragment dialog = (Progress_Dialogfragment) fm.findFragmentByTag("progress_dialog");
        if(dialog!=null) {
            dialog.dismiss();
        }
    }


    //-------------------------------------------
    // Métodos implementados
    //-------------------------------------------
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity) {
            try {
            } catch (ClassCastException e) {
                throw new ClassCastException(
                        "Activity must implement Callback.");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWinztaApp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}