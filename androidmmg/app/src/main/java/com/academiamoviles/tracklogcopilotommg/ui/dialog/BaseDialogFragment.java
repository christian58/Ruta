package com.academiamoviles.tracklogcopilotommg.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import com.academiamoviles.tracklogcopilotommg.MyApp;

public class BaseDialogFragment extends AppCompatDialogFragment {

    public static final int RESULT_POSITIVE = 2;
    public static final int RESULT_NEUTRAL = 0;
    public static final int RESULT_NEGATIVE = 1;
    public static final int RESULT_CANCEL = -1;

    protected DialogListener listener;

    protected MyApp myApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApp) getActivity().getApplication();
        try {
            listener = (DialogListener) getTargetFragment();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getStringResource(int resId){
        return getActivity().getResources().getString(resId);
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }

    public interface DialogListener {

        void onClickDialogButton(Dialog dialog, int requestCode, int resultCode, Object item);
    }


}
