package com.academiamoviles.tracklogcopilotommg.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Android on 07/02/2017.
 */
public class Progress_Dialogfragment extends BaseDialogFragment {

    public static final int REQCOD_DIALOG_PROGRESS = 1;

    String mensaje;

    public static Progress_Dialogfragment newInstance(String mensaje) {
        Bundle args = new Bundle();
        args.putString("mensaje",mensaje);
        Progress_Dialogfragment fragment = new Progress_Dialogfragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Progress_Dialogfragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mensaje = getArguments().getString("mensaje");
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(mensaje);
        setCancelable(false);
        dialog.setIndeterminate(true);

        return dialog;
    }

}
