package com.academiamoviles.tracklogcopilotommg.ui.fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.academiamoviles.tracklogcopilotommg.R;
import com.academiamoviles.tracklogcopilotommg.polygon.Point;
import com.academiamoviles.tracklogcopilotommg.polygon.Polygon;
import com.academiamoviles.tracklogcopilotommg.ui.UsoApp;
import com.academiamoviles.tracklogcopilotommg.ui.activity.CopilotoActivity;
import com.academiamoviles.tracklogcopilotommg.ws.response.Cerco;
import com.academiamoviles.tracklogcopilotommg.ws.response.Cerco_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Poi;
import com.academiamoviles.tracklogcopilotommg.ws.response.Poi_Response;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta;
import com.academiamoviles.tracklogcopilotommg.ws.response.Ruta_Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Android on 07/02/2017.
 */

public class CopilotoFragment extends BaseFragment implements LocationListener {

    boolean isRed = false;

    private enum AnuncioType {cerco, poi, velocidad, invalido}

    private class Anuncio {
        AnuncioType tipo;
        String mensaje;

        public Anuncio(AnuncioType tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }

        public Anuncio() {
        }
    }

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 1 metro

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1 * 1000; // 1 segundo

    //Mensajes
    private static final String MENSAJE_FUERA_CERCO = "No se encuentra en ningún cerco, regrese a la ruta";
    //private static final String MENSAJE_EXCESO_VELOCIDAD = "Exceso de velocidad en el cerco: %s, baje su velocidad a: %d kilómetros por hora";
    private static final String MENSAJE_EXCESO_VELOCIDAD = "Está excediendo la velocidad, su límite es: %d kilómetros por hora";
    private static final String MENSAJE_INGRESO_CERCO = "Ingresó en el cerco: %s, la velocidad máxima es: %d kilómetros por hora.";
    private static final String MENSAJE_CERCA_POI = "Esta cerca del punto: %s";
    private static final String MENSAJE_X_DICTANCIA_POI = "Esta a 200 metros del punto: %s ";
    private static final String MENSAJE_CERCO_NO_ENCONTRADO = "Cerco no encontrado.";

    private static final long MIN_DISTANCIA_POI = 200;

    //Location Manager
    boolean canGetLocation;
    boolean isNetworkEnabled;
    boolean isGPSEnabled;

    LocationManager locationManager;
    Location locationStart;

    //View
    LinearLayout viewFlash;
    TextView txtVelocidadGPS, txtVelocidadCerco, txtCerco, txtMensaje, txtTitulo, txtUnidadGPS;
    TextToSpeech tts;
    int velocGPS = 0, velocCerco = 0;
    String cercoText = "";

    //Copiloto
    private static final int minPrecision = 50;

    Date dateStart;
    int codRuta;
    Ruta ruta;
    List<Cerco> cercos;
    List<Poi> pois;
    List<Polygon> regions;

    Cerco currentCerco;

    Set<Integer> indexMensajesCercoHablados = new ArraySet<>();
    Set<Poi> indexMensajesPoiHablados = new ArraySet<>();

    List<Anuncio> anuncios;
    Anuncio anuncioInvalido;

    boolean isProcess = false;
    int iOutsideCerco = 0;

    boolean isFlashing = false;
    Date dateDentroCerco = new Date();
    UsoApp objUsoApp;
    long tiempoMinimousoApp = 10 * 60;
    Calendar dateUsoApp = Calendar.getInstance();

    boolean firstFueraCerco = true;


    Date dateDentroPoi = new Date();
    long tiempoMinimoPoi = 5;
    int countAnuncioPoi = 1;
    static int cOrange;
    static int cRed;
    static int cRed2;

    ValueAnimator colorAnimation;
    int iVelocidadCero = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codRuta = getActivity().getIntent().getIntExtra(CopilotoActivity.COD_RUTA, 0);
        getDatos();
        createPolygons();
        initTts();

        cOrange = ContextCompat.getColor(getActivity(), R.color.colorGreen);
        cRed = ContextCompat.getColor(getActivity(), R.color.colorRed);
        cRed2 = ContextCompat.getColor(getActivity(), R.color.colorRed2);

        anuncios = new ArrayList<>();
        anuncioInvalido = new Anuncio(AnuncioType.invalido, MENSAJE_FUERA_CERCO);

        objUsoApp = UsoApp.readIS(getActivity());
        if (objUsoApp == null) {
            objUsoApp = new UsoApp();
        }

    }

    /***
     * Obtiene los datos almacenados en la memoria
     */
    private void getDatos() {

        ruta = new Ruta();
        Ruta_Response ruta_response = Ruta_Response.readIS(getContext());
        for (Ruta item : ruta_response.listaRutas) {
            if (item.nCodRuta == codRuta) {
                ruta = item;
            }
        }

        cercos = new ArrayList<>();
        Cerco_Response cerco_response = Cerco_Response.readIS(getContext());
        for (Cerco item : cerco_response.listaCercos) {
            if (item.nCodRut == codRuta) {
                cercos.add(item);
            }
        }

        pois = new ArrayList<>();
        Poi_Response poi_response = Poi_Response.readIS(getContext());
        //pois = poi_response.listaPois;
        for (Poi item : poi_response.listaPois) {
            if (item.nCodRut!=null && item.nCodRut == codRuta) {
                pois.add(item);
            }
        }

    }

    /**
     * Método que crea el polígono
     */
    private void createPolygons() {
        regions = new ArrayList<>();
        for (Cerco item : cercos) {
            List<Point> coords = new ArrayList<>();

            for (Cerco.Point point : item.gPolCer.points) {
                Point point2D = new Point(point.x, point.y);
                coords.add(point2D);

            }
            Polygon polygon = new Polygon.Builder().addVertex(coords).build();
            regions.add(polygon);
        }
    }

    /**
     * Inicializa el TextToSpeech
     */
    private void initTts() {
        if (tts != null)
            return;

        tts = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(new Locale("es", "PE"));
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.copiloto_fragment, container, false);
        viewFlash = (LinearLayout) view.findViewById(R.id.viewFlash);
        txtMensaje = (TextView) view.findViewById(R.id.txtMensaje);
        txtVelocidadGPS = (TextView) view.findViewById(R.id.txtVelocidadGPS);
        txtVelocidadCerco = (TextView) view.findViewById(R.id.txtVelocidadCerco);
        txtCerco = (TextView) view.findViewById(R.id.txtCerco);
        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        txtUnidadGPS = (TextView) view.findViewById(R.id.txtUnidadGPS);

        /**
         if (cercos.size() == 0)
         txtTitulo.setText("No se encontró ningún cerco.");
         else {
         txtTitulo.setText(String.format("Tiene %d cercos para esta ruta.", cercos.size()));
         }*/

        if (savedInstanceState != null) {
            cercoText = savedInstanceState.getString("cercoText");
            velocCerco = savedInstanceState.getInt("velocCerco", 0);
            velocGPS = savedInstanceState.getInt("velocGPS", 0);


        }
        txtVelocidadCerco.setText(getVelocidad(velocCerco));
        txtVelocidadGPS.setText(getVelocidad(velocGPS));
        txtCerco.setText(cercoText);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iniLocation();
    }

    //VALIDACIONES

    private String getMessageFor(int velocity, Cerco cerco) {
        if (cerco == null) {
            return null;
        }
        int maxVelocity = cerco.nVelCer;
        if (velocity > maxVelocity) {
            return String.format(MENSAJE_EXCESO_VELOCIDAD, maxVelocity);
        }
        return null;
    }

    /**
     * Valida si un punto esta dentro de un polígono.
     *
     * @param point    Punto a validar
     * @param polygons Lista de polígonos
     * @return Retorna de índice del polígono que contiene el punto.
     * Retorna nulo si el punto no está contenido en ningun polígono.
     */
    private int validate(Point point, List<Polygon> polygons) {
        int success = -1;
        int i = 0;
        for (Polygon polygon : polygons) {
            if (polygon.contains(point)) {
                success = i;
                return success;
            }
            i++;

        }
        return success;
    }

    /**
     * Valida si una ubicación está cerca de un POI
     *
     * @param location Ubicación
     * @param pois     Lista de POIs
     * @return Retorna el POI cercano
     */
    private Poi validate(Location location, List<Poi> pois) {
        for (Poi poi : pois) {
            Location locationPoi = new Location("");
            locationPoi.setLongitude(poi.nLonPoi);
            locationPoi.setLatitude(poi.nLatPoi);
            float distance = location.distanceTo(locationPoi);
            if (distance < MIN_DISTANCIA_POI) {
                return poi;
            }
        }
        return null;
    }

    private int getVelocityManual(Location location) {
        float distance = locationStart.distanceTo(location);

        if (distance > minPrecision) {
            Date date = new Date();
            long interval = date.getTime() - dateStart.getTime();
            long timeSec = TimeUnit.MILLISECONDS.toSeconds(interval);
            dateStart = date;
            locationStart = location;
            return (int) ((distance / timeSec) * 3.6);
            //return 50;
        }

        return 0;
    }


    private void animationVelocity() {

        /*
        boolean hasVelocity = false;
        for (Anuncio anuncio : anuncios) {
            if (anuncio.tipo == AnuncioType.velocidad) {
                hasVelocity = true;
            }
        }
        */

        if (isRed) {
            viewFlash.setBackgroundColor(cRed);
            txtVelocidadGPS.setTextColor(cRed);
            txtUnidadGPS.setTextColor(cRed);
        } else {
            viewFlash.setBackgroundColor(cOrange);
            txtVelocidadGPS.setTextColor(cOrange);
            txtUnidadGPS.setTextColor(cOrange);
        }

        /*
        int velocity = 0;
        for (Anuncio anuncio : anuncios) {
            if (anuncio.tipo == AnuncioType.velocidad) {
                velocity++;
            }
        }
        int colorActual = 0;
        Drawable background = viewFlash.getBackground();
        if (background instanceof ColorDrawable)
            colorActual = ((ColorDrawable) background).getColor();

        if (velocity > 0) {
            if (!isFlashing && colorActual == cOrange) {
                isFlashing = true;

                //changeColor(colorActual, cRed);

                colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), cRed2, cRed);
                colorAnimation.setDuration(250); // milliseconds
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        viewFlash.setBackgroundColor((int) animator.getAnimatedValue());
                        txtVelocidadGPS.setTextColor(cRed);
                        txtUnidadGPS.setTextColor(cRed);

                    }

                });
                colorAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        changeToFalseFlashing();
                        animation.setStartDelay(5000);
                        animation.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        changeToFalseFlashing();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                colorAnimation.start();
            }

        } else {
            if (!isFlashing && colorActual != cOrange) {
                changeColor(colorActual, cOrange);
            }
        }
        */
    }

    /*
    private void changeColor(int fromColor, int toColor) {
        if (colorAnimation != null) {
            colorAnimation.cancel();
        }
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                viewFlash.setBackgroundColor((int) animator.getAnimatedValue());
                txtVelocidadGPS.setTextColor((int) animator.getAnimatedValue());
                txtUnidadGPS.setTextColor((int) animator.getAnimatedValue());

            }

        });
        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                changeToFalseFlashing();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        colorAnimation.start();
    }
    */

    /*
    private void newColor(int toColor) {
        int colorActual = 0;
        Drawable background = viewFlash.getBackground();
        if (background instanceof ColorDrawable)
            colorActual = ((ColorDrawable) background).getColor();

        changeColor(colorActual, toColor);

    }
    */

    private void changeToFalseFlashing() {
        isFlashing = false;
    }

    private void reproducir(final Anuncio anuncio) {
        if (anuncio == null) {
            return;
        }
        if (!tts.isSpeaking()) {
            tts.speak(anuncio.mensaje, TextToSpeech.QUEUE_FLUSH, null, "anuncio");
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    if (anuncios.size() > 0) {
                        anuncios.remove(0);
                    }
                }

                @Override
                public void onDone(String utteranceId) {
                    if (anuncios.size() > 0) {
                        reproducir(anuncios.get(0));
                    }
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
        }

    }
    //-----------------------

    //MÉTODOS IMPLEMENTADOS
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("cercoText", cercoText);
        outState.putInt("velocGPS", velocGPS);
        outState.putInt("velocCerco", velocCerco);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        stopGPS();
        super.onDestroy();

        if (tts != null) {
            //tts.stop();
            tts.shutdown();
        }
    }
    //----------------------------------


    //LOCATION MANAGER

    /**
     * Inicializa el Location Manager
     */
    private void iniLocation() {
        if (!GPSProviderEnabled(getActivity())) {
            //this.canGetLocation = false;
            txtMensaje.setVisibility(View.VISIBLE);
        } else {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                txtMensaje.setVisibility(View.GONE);
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                }
            }
        }
    }

    /**
     * Verifica el proveedor de ubicación
     *
     * @param mContext
     * @return True = activo, Falso = inactivo.
     */
    public boolean GPSProviderEnabled(Context mContext) {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            return false;
        }
        return true;
    }

    /**
     * Detiene la actualización de ubicación
     */
    public void stopGPS() {
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isProcess) {

            isProcess = true;
            try {

                //registrando el uso del app
                long tiempoUsoApp = getInterval(dateUsoApp.getTime(), new Date());
                if (tiempoUsoApp > tiempoMinimousoApp) {
                    dateUsoApp = Calendar.getInstance();
                    objUsoApp.addFecha(dateUsoApp);
                    objUsoApp.saveIS(getActivity());

                }

                //Velocidad del GPS
                int velocidadGPS = location.getSpeed() == -1 ? 0 : (int) (location.getSpeed() * 3.6);

                /**
                 // OBTENCIÓN DE LA VELOCIDAD DE FORMA MANUAL

                 if (locationStart == null) {
                 locationStart = location;
                 dateStart = new Date();
                 }
                 velocidadGPS = getVelocityManual(location);
                 */


                //iVelocidadCero - validamos 4 intentos antes de mostrar la velocidad cero.
                if(velocidadGPS!=0 || (velocidadGPS==0 && iVelocidadCero>=4
                )) {
                    String messageVelocity = getMessageFor(velocidadGPS, currentCerco);
                    if (messageVelocity != null) {
                        removeAnuncio(AnuncioType.velocidad);
                        anuncios.add(new Anuncio(AnuncioType.velocidad, messageVelocity));
                        isRed = true;
                        //newColor(cRed);
                    } else {
                        removeAnuncio(AnuncioType.velocidad);
                        isRed = false;
                        //newColor(cOrange);
                    }

                    velocGPS = velocidadGPS;
                    txtVelocidadGPS.setText(getVelocidad(velocGPS));

                    if(velocidadGPS>0)
                        iVelocidadCero=0;

                } else {
                    iVelocidadCero++;
                    isRed = false;
                }

                animationVelocity();

                //validate region
                Point point = new Point(location.getLongitude(), location.getLatitude());
                int index = validate(point, regions);
                if (index != -1) {

                    //set current cerco
                    currentCerco = cercos.get(index);

                    //actualizando la fecha dentro del cerco
                    dateDentroCerco = new Date();

                    //validate
                    if (!indexMensajesCercoHablados.contains(index)) {
                        indexMensajesCercoHablados.add(index);
                        String nombre = currentCerco.cNombre;
                        int velocidad = currentCerco.nVelCer;
                        String mensaje = String.format(MENSAJE_INGRESO_CERCO, nombre, velocidad);

                        removeAnuncio(anuncioInvalido.tipo);
                        removeAnuncio(AnuncioType.cerco);

                        anuncios.add(new Anuncio(AnuncioType.cerco, mensaje));

                        cercoText = "Cerco: " + nombre;
                        txtCerco.setText(cercoText);
                        velocCerco = velocidad;
                        txtVelocidadCerco.setText(getVelocidad(velocCerco));

                        firstFueraCerco = true;
                    }
                    iOutsideCerco = 0;
                } else {
                    if (iOutsideCerco > 3) {
                        long tiempoFueraCerco = getInterval(dateDentroCerco, new Date());
                        int tiempoMinimoFueraCerco = (ruta.tiempoMinimoFueraCerco == null || ruta.tiempoMinimoFueraCerco == 0) ? 10 : ruta.tiempoMinimoFueraCerco;

                        if ((tiempoFueraCerco > tiempoMinimoFueraCerco)) {
                            firstFueraCerco = false;
                            currentCerco = null;

                            //limpiando los cercos ya hablados
                            indexMensajesCercoHablados = new ArraySet<>();

                            removeAnuncio(AnuncioType.invalido);
                            anuncios.add(anuncioInvalido);
                            cercoText = MENSAJE_CERCO_NO_ENCONTRADO;
                            txtCerco.setText(cercoText);
                            velocCerco = 0;
                            txtVelocidadCerco.setText(getVelocidad(velocCerco));
                            dateDentroCerco = new Date();
                        }
                    }

                    iOutsideCerco++;
                }


                //validate POI
                Poi resultPoi = validate(location, pois);
                if (resultPoi != null) {

                    if (countAnuncioPoi == 1) {
                        dateDentroPoi = new Date();
                        if (!indexMensajesPoiHablados.contains(resultPoi)) {
                            indexMensajesPoiHablados.add(resultPoi);
                            String message = String.format(MENSAJE_X_DICTANCIA_POI, resultPoi.cNomPoi);

                            anuncios.add(0, new Anuncio(AnuncioType.poi, message));
                        } else {
                            String message = String.format(MENSAJE_CERCA_POI, resultPoi.cNomPoi);
                            //add to queue message cerco
                            anuncios.add(0, new Anuncio(AnuncioType.poi, message));
                        }

                        countAnuncioPoi++;

                    } else if (countAnuncioPoi == 2) {
                        long tiempoFueraPoi = getInterval(dateDentroPoi, new Date());
                        if (tiempoFueraPoi > tiempoMinimoPoi) {

                            //removeAnuncio(AnuncioType.poi);
                            if (!indexMensajesPoiHablados.contains(resultPoi)) {
                                indexMensajesPoiHablados.add(resultPoi);
                                String message = String.format(MENSAJE_X_DICTANCIA_POI, resultPoi.cNomPoi);

                                anuncios.add(0, new Anuncio(AnuncioType.poi, message));
                            } else {
                                String message = String.format(MENSAJE_CERCA_POI, resultPoi.cNomPoi);
                                //add to queue message cerco
                                anuncios.add(0, new Anuncio(AnuncioType.poi, message));
                            }
                            countAnuncioPoi++;
                        }
                    }


                } else {
                    removeAnuncio(AnuncioType.poi);
                    countAnuncioPoi = 1;
                }

                Log.d("ANUNCIOS", "" + anuncios.size());

                //anuncios.add(0,new Anuncio(AnuncioType.velocidad, "CURVA CERRADA PUICA"));
                if (anuncios.size() > 0)
                    reproducir(anuncios.get(0));


            } catch (Exception e) {
                e.printStackTrace();
            }

            isProcess = false;
        }


    }

    private void removeAnuncio(AnuncioType tipo) {
        for (int i = anuncios.size() - 1; i >= 0; i--) {
            if (anuncios.get(i).tipo == tipo) {
                Log.d("removeAnuncio", "Tipo: " + tipo);
                anuncios.remove(i);
            }
        }
    }

    private long getInterval(Date dateOld, Date dateNew) {
        long interval = dateNew.getTime() - dateOld.getTime();
        return TimeUnit.MILLISECONDS.toSeconds(interval);
    }

    private String getVelocidad(int velocidad) {
        return String.valueOf(velocidad);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationManager", "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LocationManager", "onProviderEnabled");
        iniLocation();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LocationManager", "onProviderDisabled");
        iniLocation();
    }
    //---------------------
}
