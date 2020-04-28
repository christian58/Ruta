package com.academiamoviles.tracklogcopilototck.ui.fragment;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.academiamoviles.tracklogcopilototck.MyApp;
import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.database.DatabaseHelper;
import com.academiamoviles.tracklogcopilototck.model.Incidences;
import com.academiamoviles.tracklogcopilototck.model.Paths;
import com.academiamoviles.tracklogcopilototck.model.PlatesHasPaths;
import com.academiamoviles.tracklogcopilototck.polygon.Point;
import com.academiamoviles.tracklogcopilototck.polygon.Polygon;
import com.academiamoviles.tracklogcopilototck.ui.Configuration;
import com.academiamoviles.tracklogcopilototck.ui.UsoApp;
import com.academiamoviles.tracklogcopilototck.ui.activity.CopilotoActivity;
import com.academiamoviles.tracklogcopilototck.ws.response.Cerco;
import com.academiamoviles.tracklogcopilototck.ws.response.Cerco_Response;
import com.academiamoviles.tracklogcopilototck.ws.response.Poi;
import com.academiamoviles.tracklogcopilototck.ws.response.Poi_Response;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta;
import com.academiamoviles.tracklogcopilototck.ws.response.Ruta_Response;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Android on 07/02/2017.
 */
//OnCameraIdleListener, OnCameraMoveStartedListener, OnCameraMoveListener, OnCameraMoveCanceledListener
public class CopilotoFragment extends BaseFragment implements LocationListener, OnMapReadyCallback {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    private GoogleMap mapCurrent;
    private Marker auto;

    float auxPosLat=(float)-12.090503367;
    float auxPosLon=(float)-77.020577392;

    float auxPosLatPos=(float)-12.090503367;
    float auxPosLonPos=(float)-77.020577392;
    boolean cambio = false;

    float angle = 0;
    float angleTemp = 0;


    private boolean isCanceled = false;
    private PolylineOptions currPolylineOptions;

    public static final CameraPosition SYDNEY =
            new CameraPosition.Builder().target(new LatLng(-12.090503367, -77.020577392))
                    .zoom(15.5f)
                    .bearing(90) //horizontal
                    .tilt(45)   //vertical
                    .build();

    public CameraPosition POS_Actual;
    //-33.87365, 151.20689

    boolean isRed = false;
    boolean isAmber = false;

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        mapCurrent = googleMap;
//        mapCurrent.getUiSettings().setMyLocationButtonEnabled(false);
//        mapCurrent.setMyLocationEnabled(true);
//       /*
//       //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
//        try {
//            MapsInitializer.initialize(this.getActivity());
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//       */
//
//        // Updates the location and zoom of the MapView
//        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);*/
//        mapCurrent.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));

        mapCurrent = googleMap;
        mapCurrent.setBuildingsEnabled(false);
        mapCurrent.setMyLocationEnabled(true);

        //Log.d("ACTUALLOCATION", mapCurrent.la);

//        mapCurrent.setOnCameraIdleListener(this);
//        mapCurrent.setOnCameraMoveStartedListener(this);
//        mapCurrent.setOnCameraMoveListener(this);
//        mapCurrent.setOnCameraMoveCanceledListener(this);
//
//        // We will provide our own zoom controls.
//        mapCurrent.getUiSettings().setZoomControlsEnabled(false);
//        mapCurrent.getUiSettings().setMyLocationButtonEnabled(true);

        // Show Sydney
        //mapCurrent.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.090503367, -77.020577392), 10));

        //mapCurrent.moveCamera(CameraUpdateFactory.newCameraPosition(SYDNEY));
/********888***************/
//        mapCurrent = googleMap;
        //mapCurrent.setMinZoomPreference(12);

        mapCurrent.setIndoorEnabled(true);
        UiSettings uiSettings = mapCurrent.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

//        mapCurrent.setMinZoomPreference(12);
//        mapCurrent.setMaxZoomPreference(17);

//        POS_Actual = new CameraPosition.Builder()
//                .target(new LatLng(-12.090503367, -77.020577392))      // Sets the center of the map to Mountain View
//                .zoom(19)                   // Sets the zoom
//                .bearing(45)                // Sets the orientation of the camera to east
//                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
//                .build();                   // Creates a CameraPosition from the builder
//        mapCurrent.animateCamera(CameraUpdateFactory.newCameraPosition(POS_Actual),2000,null);


//        POS_Actual =
//                new CameraPosition.Builder().target(new LatLng(-12.090503367, -77.020577392))
//                        .zoom(10f)
//                        .bearing(0) //horizontal
//                        .tilt(45)   //vertical
//                        .build();

        LatLng posIni = new LatLng(-12.090503367, -77.020577392);


        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.vehiculo);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(posIni);
//        mapCurrent.clear();
        markerOptions.title("Maker_Auto");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).anchor(0.5f,0.5f);
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.vehiculo));
        markerOptions.getPosition();
        auto = mapCurrent.addMarker(markerOptions);


        //mapCurrent.moveCamera(CameraUpdateFactory.newCameraPosition(POS_Actual));

//        POS_Actual = new CameraPosition.Builder()
//                .target(new LatLng(-12.090503367, -77.020577392))      // Sets the center of the map to Mountain View
//                .zoom(19)                   // Sets the zoom
//                .bearing(45)                // Sets the orientation of the camera to east
//                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
//                .build();

        //mapCurrent.animateCamera(CameraUpdateFactory.newCameraPosition(POS_Actual),2000,null);


        //mapCurrent.moveCamera(CameraUpdateFactory.newCameraPosition(SYDNEY));

//        mapCurrent.moveCamera(CameraUpdateFactory.newLatLng(posIni));



        if(ActivityCompat.checkSelfPermission(getActivity() ,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        //map.setMyLocationEnabled(true);

    }

//    @Override
//    public void onCameraIdle() {
//        if (currPolylineOptions != null) {
//            addCameraTargetToPath();
//            mapCurrent.addPolyline(currPolylineOptions);
//        }
//        currPolylineOptions = null;
//        isCanceled = false;  // Set to *not* clear the map when dragging starts again.
//        //Log.i(TAG, "onCameraIdle");
//
//    }
//
//    @Override
//    public void onCameraMoveStarted(int reason) {
//        if (!isCanceled) {
//            mapCurrent.clear();
//        }
//
//        String reasonText = "UNKNOWN_REASON";
//        currPolylineOptions = new PolylineOptions().width(5);
//        switch (reason) {
//            case OnCameraMoveStartedListener.REASON_GESTURE:
//                currPolylineOptions.color(Color.BLUE);
//                reasonText = "GESTURE";
//                break;
//            case OnCameraMoveStartedListener.REASON_API_ANIMATION:
//                currPolylineOptions.color(Color.RED);
//                reasonText = "API_ANIMATION";
//                break;
//            case OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION:
//                currPolylineOptions.color(Color.GREEN);
//                reasonText = "DEVELOPER_ANIMATION";
//                break;
//        }
//        //Log.i(TAG, "onCameraMoveStarted(" + reasonText + ")");
//        addCameraTargetToPath();
//
//    }
//
//    @Override
//    public void onCameraMove() {
//        // When the camera is moving, add its target to the current path we'll draw on the map.
//        if (currPolylineOptions != null) {
//            addCameraTargetToPath();
//        }
//        //Log.i(TAG, "onCameraMove");
//    }
//
//    @Override
//    public void onCameraMoveCanceled() {
//        // When the camera stops moving, add its target to the current path, and draw it on the map.
//        if (currPolylineOptions != null) {
//            addCameraTargetToPath();
//            mapCurrent.addPolyline(currPolylineOptions);
//        }
//        isCanceled = true;  // Set to clear the map when dragging starts again.
//        currPolylineOptions = null;
//        //Log.i(TAG, "onCameraMoveCancelled");
//
//    }

    private void addCameraTargetToPath() {
        LatLng target = mapCurrent.getCameraPosition().target;
        currPolylineOptions.add(target);
    }

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
    private static final String MENSAJE_X_DICTANCIA_POI = "Está a %s metros del punto %s ";
    private static final String MENSAJE_CERCO_NO_ENCONTRADO = "Cerco no encontrado.";
    private static final String MENSAJE_CERCA_VELOCIDAD_LIMITE = "Está cerca de la velocidad límite de: %d kilómetros por hora";

    private static final long MIN_DISTANCIA_POI = 200;
    private static final int RANGO_CERCA_VELOCIDAD_LIMITE = 2;

    //Location Manager
    boolean canGetLocation;
    boolean isNetworkEnabled;
    boolean isGPSEnabled;

    LocationManager locationManager;
    Location locationStart;

    Location auxLocation;

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
    static int cAmber;

    long countLocation =0;
    long countLocationBefore =0;
    DatabaseHelper mDBHelper;
    long idGroup=0;
    PlatesHasPaths platesHasPathsCreated;
    int idPlatePaths;

    ValueAnimator colorAnimation;
    int iVelocidadCero = 0;

    MyApp myApp;
	List<Poi> poisIntermedio = new ArrayList<>(Arrays.asList(
			new Poi(12, "IMATA CARGADO", -15.83026, -71.0834),
            new Poi(12, "IMATA VACIO", -15.836506, -71.089061),
			new Poi(12, "SAN JOSE", -16.578987, -71.838162),
			new Poi(596, "IMATA CARGADO", -15.83371, -71.086386),
            new Poi(596, "IMATA VACIO", -15.836506, -71.089061),
			new Poi(596, "SAN CAMILO", -16.686681, -71.916318),
			new Poi(905, "CONDOROMA", -15.296815, -71.137322),
            new Poi(905, "CONGUYA", -14.019947, -71.975583),
			new Poi(869, "SAN CAMILO", -16.686681, -71.916318),
			new Poi(724, "IMATA CARGADO", -15.836506, -71.089061),
            new Poi(724, "IMATA VACIO", -15.83026, -71.0834)));
    int countPoiIntermedio = 1;
    MediaPlayer mp;


    List<Poi> poisAux = new ArrayList<>(Arrays.asList(
            new Poi(12, "POI Curva paucarpata 1", -16.420091, -71.495574),
            new Poi(12, "POI Normal paucarpata 2", -16.421484, -71.496358),
            new Poi(12, "POI modulo paucarpata", -16.422814, -71.512971),
            new Poi(596, "POI rico pollo", -16.424074, -71.515808),
            new Poi(596, "POI King Broaster", -16.420899, -71.508245),
            new Poi(596, "POI Curva Salaverry", -16.433814, -71.537962)



            ));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codRuta = getActivity().getIntent().getIntExtra(CopilotoActivity.COD_RUTA, 0);


        mDBHelper = new DatabaseHelper(getContext());

        Paths paths = mDBHelper.getPathsByCodRuta(codRuta);

        PlatesHasPaths platesHasPaths = new PlatesHasPaths(Configuration.userPlate.getPlates().getIdPlates(),paths.getIdPaths(),new Date());
        idPlatePaths = mDBHelper.createPlatePaths(platesHasPaths);
        Configuration.IDPLATES_HAS_PATHS=idPlatePaths;
        Configuration.COD_RUTA = codRuta;



        getDatos();
        createPolygons();
        initTts();

        cOrange = ContextCompat.getColor(getActivity(), R.color.colorGreen);
        cRed = ContextCompat.getColor(getActivity(), R.color.colorRed);
        cRed2 = ContextCompat.getColor(getActivity(), R.color.colorRed2);
        cAmber = ContextCompat.getColor(getActivity(), R.color.colorAmber);

        anuncios = new ArrayList<>();
        anuncioInvalido = new Anuncio(AnuncioType.invalido, MENSAJE_FUERA_CERCO);

        objUsoApp = UsoApp.readIS(getActivity());
        if (objUsoApp == null) {
            objUsoApp = new UsoApp();
        }


        myApp = (MyApp) getActivity().getApplication();
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
            if (item.nCodRutas!=null && item.nCodRutas.indexOf(codRuta) != -1) {
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

        mMapView = (MapView) view.findViewById((R.id.user_list_map));

        Bundle mapViewBundle = null;

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

            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);


        }
        txtVelocidadCerco.setText(getVelocidad(velocCerco));
        txtVelocidadGPS.setText(getVelocidad(velocGPS));
        txtCerco.setText(cercoText);

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

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
    int prevMaxSpeedWarned = 0;
    private void getCautionMessageFor(int velocity, Cerco cerco) {
		isAmber = false;
        if (cerco == null) {
        	prevMaxSpeedWarned = 0;
            return;
        }
        int maxVelocity = cerco.nVelCer;
        if (velocity <= maxVelocity && velocity >= maxVelocity - RANGO_CERCA_VELOCIDAD_LIMITE) {
    		isAmber = true;
        	if (prevMaxSpeedWarned != 0 && prevMaxSpeedWarned == maxVelocity)
        		return;
        	prevMaxSpeedWarned = maxVelocity;
            String message = String.format(MENSAJE_CERCA_VELOCIDAD_LIMITE, maxVelocity);
            anuncios.add(new Anuncio(AnuncioType.velocidad, message));
            return;
        }
        prevMaxSpeedWarned = 0;
        return;
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

            Log.d("CnombrePoi",String.valueOf(poi.cNomPoi));
            Log.d("cPais",String.valueOf(poi.cPaisPoi));
            Log.d("cTipo",String.valueOf(poi.cTipoPoi));
            Log.d("cNombVia",String.valueOf(poi.cNomViaPoi));
            Log.d("cMensaje",String.valueOf(poi.mensaje));
            Log.d("cDisPoi",String.valueOf(poi.nDisPoi));

            if (poi.nDisPoi != null) {
            	if (distance < poi.nDisPoi) {
            		Log.d("CopilotoFragment", "[Custom] Poi detectado a " + distance);
            		return poi;
            	}
        	} else if (distance < MIN_DISTANCIA_POI) {
        		Log.d("CopilotoFragment", "[Default] Poi detectado a " + distance);
                return poi;
            }
        }
        return null;
    }
    
    private Poi validateIntermedio(Location location, List<Poi> pois) {
        String codUsu = myApp.objUser.userId;
        for (Poi poi : pois) {
            Location locationPoi = new Location("");
            locationPoi.setLongitude(poi.nLonPoi);
            locationPoi.setLatitude(poi.nLatPoi);
            float distance = location.distanceTo(locationPoi);
            if (distance < 100 && poi.nCodUsu.equals(new Integer(codUsu))) {
                return poi;
            }
        }
        return null;
    }
    
    private void playSound(final int resId) {
    	new Thread(new Runnable() {
			@Override
			public void run() {
		    	if (mp != null && mp.isPlaying()) {
		    		mp.stop();
		    		mp.release();
		    	}
		    	mp = MediaPlayer.create(getActivity(), resId);
		    	mp.start();
		    	while (mp.isPlaying()) {}
		    	mp.release();
		    	mp = null;
			}
		}).start();
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
        } else if (isAmber) {
            viewFlash.setBackgroundColor(cAmber);
            txtVelocidadGPS.setTextColor(cAmber);
            txtUnidadGPS.setTextColor(cAmber);
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

        //MapView
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        stopGPS();
        super.onDestroy();

        if (tts != null) {
            //tts.stop();
            tts.shutdown();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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

    public float inclinationAngle(float x1, float y1, float x2, float y2){
        if(x2-x1 != 0){
            float m = (y2-y1)/(x2-x1);
            float angle_rad = (float) Math.atan(m);
            float angle2 = (float) (angle_rad*180/3.1415926);
            float distancia = (float) Math.sqrt(Math.pow(x2-x1,2)+ Math.pow(y2-y1,2));
            //0.00003
            if(distancia > 0.00003){
                return angle2;
            }
//        Log.d("X1", String.valueOf(x1));
//        Log.d("Y1", String.valueOf(y1));
//        Log.d("X2", String.valueOf(x2));
//        Log.d("Y2", String.valueOf(y2));
//        Log.d("RAD", String.valueOf(angle_rad));


            return  angle;

        }
        else return angle;

    }

    public void addMarker(Poi poiMarker){
        LatLng latLngI = new LatLng(poiMarker.nLatPoi, poiMarker.nLonPoi);
        //mapCurrent.addMarker(new MarkerOptions().position(latLngI).title(poiMarker.cNomViaPoi));

        String s = poiMarker.cNomPoi;
        String typeMarker;
        BitmapDrawable bitmapdraw; //= (BitmapDrawable)getResources().getDrawable(R.drawable.default1);
        //CASE
        if (poiMarker.cNomPoi.contains("Curva")) {
            // it contains world
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.curvas);
        }
        else{
            bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.default1);
        }

        Log.d("NombreMARKER1", String.valueOf(poiMarker.cNomPoi));
        int height = 100;
        int width = 100;
        //LatLng posIni = new LatLng(-12.090503367, -77.020577392);
        //BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.curvas);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngI);
//        mapCurrent.clear();
        markerOptions.title(poiMarker.cNomPoi);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).anchor(0.5f,1f);
//        markerOptions
        //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.vehiculo));
        markerOptions.getPosition();
        mapCurrent.addMarker(markerOptions).showInfoWindow();


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
                        isAmber = false;
                        //newColor(cRed);
                    } else {
                        removeAnuncio(AnuncioType.velocidad);
                        isRed = false;
                        getCautionMessageFor(velocidadGPS, currentCerco);
                        //newColor(cOrange);
                    }

                    velocGPS = velocidadGPS;
                    txtVelocidadGPS.setText(getVelocidad(velocGPS));

                    if(velocidadGPS>0)
                        iVelocidadCero=0;

                } else {
                    iVelocidadCero++;
                    isRed = false;
                    isAmber = false;
                }

                Log.d("Latitud LOCATION_",String.valueOf(location.getLatitude()));
                Log.d("Longitud LOCATION_",String.valueOf(location.getLongitude()));
               // Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.get));
//                Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.));
//                Log.d("Speed LOCATION_",String.valueOf(location.));

//                auxLocation = location;
//                auxLocation.setLatitude(auxPosLat);
//                auxLocation.setLongitude(auxPosLon);


                animationVelocity();

                //validate region
                Point point = new Point(location.getLongitude(), location.getLatitude());
                //Point point = new Point(auxLocation.getLongitude(), auxLocation.getLatitude());

                //ubicacion actual
                //auto.setPosition(new LatLng( auxPosLat, auxPosLon));
                //auto.setPosition(new LatLng( -12.091003367, -77.023577392));
                auto.setPosition(new LatLng(point.y, point.x));

//                mapCurrent.moveCamera(CameraUpdateFactory.newLatLng(auto.getPosition()));
//                POS_Actual =
//                        new CameraPosition.Builder().target(new LatLng(auto.getPosition().latitude, auto.getPosition().longitude))
//                                .zoom(19f)//modificar de forma dinamica
//                                .bearing(0) //horizontal
//                                .tilt(45)   //vertical
//                                .build();
//                mapCurrent.moveCamera(CameraUpdateFactory.newCameraPosition(POS_Actual));






//                //auxPos+=0.001;
//                auxPosLat-=0.000002;
//                auxPosLon-=0.00002;

//                if(cambio){
//                auxPosLat-=0.00002;
//                auxPosLon-=0.0000;
//
//                }
//                else{
//                    auxPosLat-=0.000002;
//                auxPosLon-=0.00002;
//                }

                //angle = inclinationAngle(auxPosLat,auxPosLon,auxPosLatPos,auxPosLonPos);
                angle = inclinationAngle((float) location.getLatitude(), (float) location.getLongitude(),auxPosLatPos,auxPosLonPos);
                //if(Math.abs(angle - angleTemp) > 2) angle
                Log.d("ANGLE", String.valueOf(angle));
//                auxPosLatPos = auxPosLat;
//                auxPosLonPos = auxPosLon;
                auxPosLatPos = (float) location.getLatitude();
                auxPosLonPos = (float) location.getLongitude();


                POS_Actual = new CameraPosition.Builder()
                        .target(new LatLng(auto.getPosition().latitude, auto.getPosition().longitude))      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom 17
                        .bearing((int)angle + 180)                // Sets the orientation of the camera to east
                        .tilt(60)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mapCurrent.animateCamera(CameraUpdateFactory.newCameraPosition(POS_Actual),2000,null);

                //mapCurrent.setPosition(new LatLng(a_lat, a_lon));
                //Log.d("MENSAJE","MENSAJEEE");
                //Log.d("POINT", String.valueOf(location.getLatitude()));
                //Log.d("POINT", String.valueOf(point.y));

                int index = validate(point, regions);
                if (index != -1) {

                    //set current cerco
                    currentCerco = cercos.get(index);

                    //actualizando la fecha dentro del cerco
                    dateDentroCerco = new Date();

                    //validate
                    if (!indexMensajesCercoHablados.contains(index)) {
                    	indexMensajesCercoHablados.clear();
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


                verifyIncidences(velocGPS,  velocCerco, location.getLatitude(), location.getLongitude());
                countLocation++;

                //validate POI
                Poi resultPoi = validate(location, pois);  //original
                //Poi resultPoi = validate(auxLocation, pois);

                //Poi resultPoi = validate(location, poisAux); //mis marcadores

                if (resultPoi != null) {

                    if (countAnuncioPoi == 1) {
                        dateDentroPoi = new Date();
                        if (!indexMensajesPoiHablados.contains(resultPoi)) {

                            //crear el marker
                            addMarker(resultPoi);
                            //cambio = true;

//                            LatLng latLngI = new LatLng(resultPoi.nLatPoi, resultPoi.nLonPoi);
//                            mapCurrent.addMarker(new MarkerOptions().position(latLngI).title(resultPoi.cNomViaPoi));


                            indexMensajesPoiHablados.add(resultPoi);
                            long distance = resultPoi.nDisPoi != null ? resultPoi.nDisPoi : MIN_DISTANCIA_POI;
                            String message = String.format(MENSAJE_X_DICTANCIA_POI, distance, resultPoi.cNomPoi);

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
                                long distance = resultPoi.nDisPoi != null ? resultPoi.nDisPoi : MIN_DISTANCIA_POI;
                                String message = String.format(MENSAJE_X_DICTANCIA_POI, distance, resultPoi.cNomPoi);

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

                Poi poi = validateIntermedio(location, poisIntermedio);
                if (poi != null) {
                	if (countPoiIntermedio == 1) {
                		playSound(R.raw.ruta_medio);
                		countPoiIntermedio++;
                	}            
                } else {
                	countPoiIntermedio = 1;
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

    public void verifyIncidences(int velocGPS, int velocCerco, double latitude, double longitude){
        if(velocGPS >velocCerco && velocCerco!=0){
               // System.out.println("REGISTRA EXCESO>>> VelocCerco: "+ velocCerco+" velocGPS: "+ velocGPS+" latitude: "+latitude+" longitude: "+longitude);
                if(countLocation-1 == countLocationBefore){

                    if(countLocation-1==0){
                        idGroup++;
                        Incidences  incidences = new Incidences(idPlatePaths, velocCerco, velocGPS, latitude, longitude, new Date(), (int)idGroup );
                        mDBHelper.createIncidence(incidences);
                    }else{
                        Incidences  incidences = new Incidences(idPlatePaths, velocCerco, velocGPS, latitude, longitude, new Date(), (int)idGroup );
                        mDBHelper.createIncidence(incidences);
                    }

                }else{
                    idGroup++;
                    Incidences  incidences = new Incidences(idPlatePaths, velocCerco, velocGPS, latitude, longitude, new Date(), (int)idGroup );
                    mDBHelper.createIncidence(incidences);
                }
               // System.out.println("REGISTRA EXCESSO>> GROUP:" + idGroup);
                countLocationBefore= countLocation;
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
