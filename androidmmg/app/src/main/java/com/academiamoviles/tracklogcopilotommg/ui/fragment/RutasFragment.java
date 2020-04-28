package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.ui.activity.CopilotoActivity;
import com.academiamoviles.tracklogcopilotommg.ui.activity.LogoutActivity;
import com.academiamoviles.tracklogcopilotommg.ui.activity.UsoAppActivity;
import com.academiamoviles.tracklogcopilotommg.ui.adapter.RutasAdapter;
import com.academiamoviles.tracklogcopilotommg.ui.dialog.BaseDialogFragment;
import com.academiamoviles.tracklogcopilotommg.util.Util;
import com.academiamoviles.tracklogcopilotommg.ws.asynctask.BaseAsyncTask;
import com.academiamoviles.tracklogcopilotommg.ws.asynctask.HttpAsyncTask;
import com.academiamoviles.tracklogcopilotommg.ws.response.Cerco_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Poi_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta_Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by Android on 07/02/2017.
 */

public class RutasFragment extends BaseLoadFragment {

    public static final int REQCOD_DIALOG_MENSAJE = 100;

    RecyclerView listView;
    TextView txtActualizacion;

    HttpAsyncTask taskRutas, taskCercos, taskPois;

    Ruta_Response ruta_response;
    Cerco_Response cerco_response;
    Poi_Response poi_response;

    Ruta ruta_select;

    Calendar calendarRuta;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ruta_response = Ruta_Response.readIS(getActivity());
        cerco_response = Cerco_Response.readIS(getActivity());
        poi_response = Poi_Response.readIS(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = inflateView(R.layout.rutas_fragment, (ViewGroup) getView());
        txtActualizacion = (TextView) view.findViewById(R.id.txtActualización);
        txtActualizacion.setSelected(true);
        listView = (RecyclerView) view.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        //txtRutas = (TextView) view.findViewById(R.id.txtRutas);
        //txtCercos = (TextView) view.findViewById(R.id.txtCercos);
        //txtPois = (TextView) view.findViewById(R.id.txtPois);

        setView(view);

        if (savedInstanceState != null) {
            calendarRuta = (Calendar) savedInstanceState.getSerializable("CALENDAR");
        }

        if (ruta_response == null)
            getRutas();
        else
            updateList();
    }

    TextView txtCercos, txtRutas, txtPois;

    private void mostrarCantidadDatos(int cantRutas, int cantCercos, int cantPois) {
        txtRutas.setText("Cantidad de rutas: " + cantRutas);
        txtCercos.setText("Cantidad de cercos: " + cantCercos);
        txtPois.setText("Cantidad de POIs: " + cantPois);
    }

    /**
     * Consume el ws para obtener las rutas del usuario y las almacena.
     */
    private void getRutas() {
        //getActivity().invalidateOptionsMenu();
        getActivity().supportInvalidateOptionsMenu();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usuarioWeb", myApp.objUser.userWeb);

        taskRutas = new HttpAsyncTask("ruta/usuario", jsonObject.toString());
        taskRutas.setCallback(new BaseAsyncTask.Callback() {
            @Override
            public void onPostExecuteTask(int requestCode, int resultCode, Object result) {
                calendarRuta = Calendar.getInstance();
                if (resultCode == BaseAsyncTask.RESULT_OK) {
                    try {
                        ruta_response = Ruta_Response.fromJson((String) result);
                        String fecha = Util.dateFormat(DateFormat.FULL, DateFormat.MEDIUM, calendarRuta);
                        ruta_response.fecha = fecha;
                        Ruta_Response.saveIS(getActivity(), ruta_response.toJson());
                        getCercos();
                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                    }
                } else {
                    hideProgress();
                    setErrorLoadShown(true);
                }
            }
        });
        taskRutas.execute();
        //ActivityCompat.invalidateOptionsMenu(getActivity());
    }

    /**
     * Consume el ws para obtener los cercos de las rutas y las almacena.
     */
    private void getCercos() {

        JsonArray jsonArray = new JsonArray();
        //int ids[] = new int[ruta_response.listaRutas.size()];
        for (int i = 0; i < ruta_response.listaRutas.size(); i++) {
            //ids[i]=ruta_response.listaRutas.get(i).nCodRuta;
            jsonArray.add(ruta_response.listaRutas.get(i).nCodRuta);
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ruta_id", jsonArray);
        jsonObject.addProperty("usuarioWeb", myApp.objUser.userWeb);

        //taskCercos = new HttpAsyncTask("cerco/rutas", jsonObject.toString());
        taskCercos = new HttpAsyncTask("cerco/rutasUsuario", jsonObject.toString());
        taskCercos.setCallback(new BaseAsyncTask.Callback() {
            @Override
            public void onPostExecuteTask(int requestCode, int resultCode, Object result) {
                if (resultCode == BaseAsyncTask.RESULT_OK) {
                    try {
                        cerco_response = Cerco_Response.fromJson((String) result);
                        Cerco_Response.saveIS(getActivity(), (String) result);
                        getPois();
                    } catch (Exception e) {
                        hideProgress();
                        e.printStackTrace();
                        setErrorLoadShown(true);
                    }
                } else {
                    hideProgress();
                    setErrorLoadShown(true);
                }
            }
        });
        taskCercos.execute();
    }

    /**
     * Consume el ws para obtener los POIs y los almacena.
     */
    private void getPois() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("usuarioWeb", myApp.objUser.userWeb);

        taskPois = new HttpAsyncTask("poi", jsonObject.toString());
        taskPois.setCallback(new BaseAsyncTask.Callback() {
            @Override
            public void onPostExecuteTask(int requestCode, int resultCode, Object result) {
                hideProgress();
                if (resultCode == BaseAsyncTask.RESULT_OK) {
                    try {
                        poi_response = Poi_Response.fromJson((String) result);
                        Poi_Response.saveIS(getActivity(), (String) result);
                        //updateList();
                        getPoisPorRuta();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setErrorLoadShown(true);
                    }
                } else {
                    setErrorLoadShown(true);
                }
            }
        });
        taskPois.execute();
    }

    /**
     * Consume el ws para obtener los POIs por ruta y los almacena.
     */
    private void getPoisPorRuta() {
        JsonArray array = new JsonArray();

        for (int i = 0; i < ruta_response.listaRutas.size(); i++) {
            array.add(ruta_response.listaRutas.get(i).nCodRuta);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ruta_id", array);

        taskPois = new HttpAsyncTask("poi/rutas", jsonObject.toString());
        taskPois.setCallback(new BaseAsyncTask.Callback() {
            @Override
            public void onPostExecuteTask(int requestCode, int resultCode, Object result) {
                hideProgress();
                if (resultCode == BaseAsyncTask.RESULT_OK) {
                    try {
                        JSONObject object1 = new JSONObject((String) result);
                        JSONArray jsonArray = new JSONArray(object1.getString("data"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            for(int j=0; j<poi_response.listaPois.size();j++){
                                if(object.getInt("nCodPoi") == poi_response.listaPois.get(j).nCodPoi){
                                    poi_response.listaPois.get(j).nCodRut = object.getInt("nCodRut");
                                }
                            }
                        }
                        Poi_Response.saveIS(getActivity(), poi_response.toJson());
                        updateList();

                    } catch (Exception e) {
                        e.printStackTrace();
                        setErrorLoadShown(true);
                    }
                } else {
                    setErrorLoadShown(true);
                }
            }
        });
        taskPois.execute();
    }

    /**
     * Actualiza el listado de rutas.
     */
    private void updateList() {

        //mostrarCantidadDatos(ruta_response.listaRutas.size(), cerco_response.listaCercos.size(), poi_response.listaPois.size());
        //getActivity().invalidateOptionsMenu();
        getActivity().supportInvalidateOptionsMenu();
        ActivityCompat.invalidateOptionsMenu(getActivity());

        if (calendarRuta != null) {
            String fecha = Util.dateFormat(DateFormat.FULL, DateFormat.MEDIUM, calendarRuta);
            //String fecha = Util.dateFormat(calendarRuta, "EEEE, dd MMMM, HH:mm:ss");
            txtActualizacion.setText("Última actualización: " + fecha);
        } else {
            if (ruta_response.fecha == null) {
                ruta_response.fecha = Util.dateFormat(DateFormat.FULL, DateFormat.MEDIUM, Calendar.getInstance());
            }

            txtActualizacion.setText("Última actualización: " + ruta_response.fecha);
        }

        RutasAdapter adapter = new RutasAdapter(ruta_response.listaRutas);
        adapter.setOnClickListener(new RutasAdapter.OnItemClickListener<Ruta>() {
            @Override
            public void onItemClick(View v, int position, Ruta item) {
                ruta_select = item;
                if (!Util.GPSProviderEnabled(getActivity())) {
                    Util.showSettingsAlert(getActivity());
                } else if (!Util.checkPermissionLocation(getActivity())) {
                    Util.requestPermissionLocation(getActivity());
                } else {
                    showMensajeDialog("Mensaje", "Esta a punto de iniciar una ruta, ¿desea continuar?", REQCOD_DIALOG_MENSAJE, 2);
                }
            }
        });
        listView.setAdapter(adapter);
        setContentShown(true);
    }

    @Override
    public void onClickDialogButton(Dialog dialog, int requestCode, int resultCode, Object object) {
        super.onClickDialogButton(dialog, requestCode, resultCode, object);
        if (requestCode == REQCOD_DIALOG_MENSAJE && resultCode == BaseDialogFragment.RESULT_POSITIVE) {
            Intent intent = new Intent(getActivity(), CopilotoActivity.class);
            intent.putExtra(CopilotoActivity.COD_RUTA, ruta_select.nCodRuta);
            getActivity().startActivity(intent);
        }
    }

    boolean showDialogMensaje = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Util.REQCOD_PERMISSION_LOCATION) {
            showDialogMensaje = true;
        }
    }

    @Override
    public void reload() {
        getRutas();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if ((taskRutas != null && taskRutas.getStatus() == AsyncTask.Status.RUNNING) ||
                (taskCercos != null && taskCercos.getStatus() == AsyncTask.Status.RUNNING) ||
                (taskPois != null && taskPois.getStatus() == AsyncTask.Status.RUNNING)) {
            menu.findItem(R.id.action_update).setVisible(false);
        } else {
            menu.findItem(R.id.action_update).setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                showProgress("Actualizando");
                getRutas();
                return true;
            case R.id.action_uso:
                Intent i = new Intent(getActivity(), UsoAppActivity.class);
                startActivity(i);
                return true;
            case R.id.action_logout:
                Intent intent = new Intent(getActivity(), LogoutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (calendarRuta != null)
            outState.putSerializable("CALENDAR", calendarRuta);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (showDialogMensaje) {
            if (Util.checkPermissionLocation(getActivity())) {
                showMensajeDialog("Mensaje", "Está a punto de iniciar una ruta, ¿desea continuar?", REQCOD_DIALOG_MENSAJE, 2);
            } else {
                showMensajeDialog("Conceda el permiso de ubicación para continuar");
            }
            showDialogMensaje = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskRutas != null && taskRutas.getStatus() == AsyncTask.Status.RUNNING) {
            taskRutas.cancel(true);
        }

        if (taskCercos != null && taskCercos.getStatus() == AsyncTask.Status.RUNNING) {
            taskCercos.cancel(true);
        }

        if (taskPois != null && taskPois.getStatus() == AsyncTask.Status.RUNNING) {
            taskPois.cancel(true);
        }
    }
}
