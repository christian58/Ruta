package com.academiamoviles.tracklogcopilototck.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.model.Paths;
import com.academiamoviles.tracklogcopilototck.model.Plates;
import com.academiamoviles.tracklogcopilototck.model.UserPlate;
import com.academiamoviles.tracklogcopilototck.model.Users;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;
import com.academiamoviles.tracklogcopilototck.ui.activity.CopilotoActivity;
import com.academiamoviles.tracklogcopilototck.ui.activity.LogActivity;
import com.academiamoviles.tracklogcopilototck.ui.activity.LogoutActivity;
import com.academiamoviles.tracklogcopilototck.ui.activity.UsoAppActivity;
import com.academiamoviles.tracklogcopilototck.ui.adapter.RutasAdapter;
import com.academiamoviles.tracklogcopilototck.ui.dialog.BaseDialogFragment;
import com.academiamoviles.tracklogcopilototck.util.Constants;
import com.academiamoviles.tracklogcopilototck.util.Util;
import com.academiamoviles.tracklogcopilototck.ws.asynctask.BaseAsyncTask;
import com.academiamoviles.tracklogcopilototck.ws.asynctask.HttpAsyncTask;
import com.academiamoviles.tracklogcopilototck.ws.response.Cerco_Response;
import com.academiamoviles.tracklogcopilototck.ws.response.Poi_Response;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta_Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    DatabaseHelper mDBHelper;
    Users users;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDBHelper = new DatabaseHelper(getContext());
        users = mDBHelper.getUserByUsername(myApp.objUser.userWeb);
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

        setView(view);

        if (savedInstanceState != null) {
            calendarRuta = (Calendar) savedInstanceState.getSerializable("CALENDAR");
        }

        if (ruta_response == null)
            getRutas();
        else
            updateList();
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
                //hideProgress();
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
                                    poi_response.listaPois.get(j).nCodRutas.add(object.getInt("nCodRut"));
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

        List<Paths> listPaths = new ArrayList<>();
        for(int i=0; i<ruta_response.listaRutas.size(); i++){
            Paths paths = new Paths(ruta_response.listaRutas.get(i).nCodRuta);
            listPaths.add(paths);
        }	

        mDBHelper.createPaths(listPaths);
        setContentShown(true);
    }

    @Override
    public void onClickDialogButton(Dialog dialog, int requestCode, int resultCode, Object object) {
        super.onClickDialogButton(dialog, requestCode, resultCode, object);
        if (requestCode == REQCOD_DIALOG_MENSAJE && resultCode == BaseDialogFragment.RESULT_POSITIVE) {
			final List<Plates> listPlates = mDBHelper
					.getListPlatesByUserID(users.getIdUsers());
			String plates[] = new String[listPlates.size() + 1];
			plates[0] = "Registrar nueva placa";
			for (int i = 0; i < listPlates.size(); i++) {
				plates[i + 1] = listPlates.get(i).getSerie();
			}
			new AlertDialog.Builder(getContext())
					.setTitle("Seleccione una placa para iniciar ruta")
					.setItems(plates, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int pos) {
							if (pos == 0) {
								openNewPlate();
								return;
							}
							Plates p = listPlates.get(pos - 1);
							startCopilotoWithPlate(p);
						}
					}).setNegativeButton("Cancelar", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).create().show();
        }
    }
    private void openNewPlate() {
		LinearLayout ll = new LinearLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		int dp = (int) (16 * Resources.getSystem().getDisplayMetrics().density);
		ll.setPadding(dp, 0, dp, 0);
		ll.setLayoutParams(params);
		ll.setOrientation(LinearLayout.VERTICAL);
		final EditText et = new EditText(getContext());
		et.setLines(1);
		et.setInputType(EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		ll.addView(et, params);
		new AlertDialog.Builder(getContext()).setTitle("Ingrese nueva placa")
				.setView(ll).setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String serie = et.getText().toString();
						Plates newPlate = new Plates(serie, users.getIdUsers());
						mDBHelper.createPlate(newPlate);
						Plates plate = mDBHelper.getPlateBySerie(serie);
						startCopilotoWithPlate(plate);
					}
				}).create().show();
    }
    private void startCopilotoWithPlate(Plates plate) {
    	Configuration.userPlate = new UserPlate(users, plate);
        Intent intent = new Intent(getActivity(), CopilotoActivity.class);
        intent.putExtra(CopilotoActivity.COD_RUTA, ruta_select.nCodRuta);
        getActivity().startActivity(intent);
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
            case R.id.action_driver_name:
            	updateDriverName();
                return true;
            case R.id.action_log:
                startActivity(new Intent(getActivity(), LogActivity.class));
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
    
	public void updateDriverName() {
		final SharedPreferences prefs = getActivity().getSharedPreferences(Constants.USER_PREFS_NAME, 0);
		final String savedName = prefs.getString(Constants.DRIVER_NAME_KEY, null);
		LinearLayout ll = new LinearLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int dp = (int) (16 * Resources.getSystem().getDisplayMetrics().density);
		ll.setPadding(dp, 0, dp, 0);
		ll.setLayoutParams(params);
		ll.setOrientation(LinearLayout.VERTICAL);
		final EditText et = new EditText(getContext());
		et.setLines(1);
		if (savedName != null)
			et.setText("" + savedName);
		ll.addView(et, params);
		new AlertDialog.Builder(getContext()).setTitle("Nombre de conductor (reportes)").setView(ll)
				.setPositiveButton("OK", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String name = et.getText().toString();
						Editor edit = prefs.edit();
						edit.putString(Constants.DRIVER_NAME_KEY, name);
						edit.commit();
						Toast.makeText(getContext(), "Nombre guardado", Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("Cancelar", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).create().show();
	}
}
