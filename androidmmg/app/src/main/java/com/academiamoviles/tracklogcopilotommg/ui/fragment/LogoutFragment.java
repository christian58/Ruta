package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.ui.activity.LoginActivity;
import com.academiamoviles.tracklogcopilotommg.util.Constants;
import com.academiamoviles.tracklogcopilotommg.util.SeguridadUtils;
import com.academiamoviles.tracklogcopilotommg.util.UserUtil;
import com.academiamoviles.tracklogcopilotommg.util.Util;
import com.academiamoviles.tracklogcopilotommg.ws.asynctask.BaseAsyncTask;
import com.academiamoviles.tracklogcopilotommg.ws.asynctask.HttpAsyncTask;
import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Android on 07/02/2017.
 */

public class LogoutFragment extends BaseFragment implements View.OnClickListener {

    EditText edtUser, edtPassword;
    HttpAsyncTask taskLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.logout_fragment, container, false);
        edtUser = (EditText) view.findViewById(R.id.edtUser);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                if (Util.connection(getActivity())) {
                    if (validarCampos()) {
                        logout(edtUser.getText().toString().trim(), edtPassword.getText().toString().trim());
                    }
                } else {
                    Util.showToast(getActivity(), Constants.Mensaje.ERROR_CONEXION);
                }
                break;
        }
    }

    private Boolean validarCampos() {

        if (edtUser.getText().toString().trim().equals("")) {
            Util.showToast(getActivity(), "Complete los datos solicitados.");
            return false;
        } else if (edtPassword.getText().toString().trim().equals("")) {
            Util.showToast(getActivity(), "Complete los datos solicitados.");
            return false;
        }

        return true;
    }

    private void logout(String user, String password) {

        try {
            String userShared = SeguridadUtils.desencriptar(UserUtil.getUser(getActivity()));
            String passShared = SeguridadUtils.desencriptar(UserUtil.getPassword(getActivity()));

            if (user.equals(userShared) && password.equals(passShared)) {
                showProgress("Espere...");
                JsonObject jsonObject = new JsonObject();
                //jsonObject.addProperty("idEmpresa", myApp.objUser.idEmpresa);
                jsonObject.addProperty("usuario", user);
                jsonObject.addProperty("password", password);

                HttpAsyncTask taskLogout = new HttpAsyncTask("usuario/logout", jsonObject.toString());
                taskLogout.setCallback(new BaseAsyncTask.Callback() {
                    @Override
                    public void onPostExecuteTask(int requestCode, int resultCode, Object result) {
                        hideProgress();
                        if (resultCode == BaseAsyncTask.RESULT_OK) {
                            try {
                                JSONObject jsonResponse = new JSONObject((String) result);
                                if (jsonResponse.getBoolean("ok")) {
                                    myApp.clearApp();
                                    UserUtil.saveCredentials(getActivity(), "", "");
                                    Intent intent = new Intent(getActivity(), LoginActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    showMensajeDialog("Usuario inválido.");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Util.showToast(getActivity(), Constants.Mensaje.ERROR_SC);
                        }
                    }
                });
                taskLogout.execute();


            } else {
                Util.showToast(getActivity(), "Usuario inválido.");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskLogin != null && taskLogin.getStatus() == AsyncTask.Status.RUNNING) {
            taskLogin.cancel(true);
        }
    }
}
