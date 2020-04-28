package com.academiamoviles.tracklogcopilototck.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.academiamoviles.tracklogcopilototck.R;
import com.academiamoviles.tracklogcopilototck.model.Coordinates;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Coordinates> listCoordinates;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getActionBarToolbar();
        listCoordinates = (List<Coordinates>) getIntent().getSerializableExtra("listCoordinates");

      /*  listCoordinates = new ArrayList<>();
        listCoordinates.add(new Coordinates(-16.407816, -71.521734));
        listCoordinates.add(new Coordinates(-16.408041, -71.522021));
        listCoordinates.add(new Coordinates(-16.408306, -71.522300));
        listCoordinates.add(new Coordinates(-16.408960, -71.523714));
        listCoordinates.add(new Coordinates(-16.409572, -71.523435));
        listCoordinates.add(new Coordinates(-16.412652, -71.520473));
*/
        //System.out.println(this.getApplicationInfo().dataDir);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(status == ConnectionResult.SUCCESS){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }else{
            //System.out.println("NO SE INICIO MAPA");
            /*Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(),10);
            dialog.show();*/
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        float zoomLevel = 16;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        // Add a marker in Sydney and move the camera
        /*    LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("HOLA MUNDO").icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE
        )));



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
*/

        if(listCoordinates.size()<2){
             LatLng coo = new LatLng(listCoordinates.get(0).getLatitude(), listCoordinates.get(0).getLongitude());
             mMap.addMarker(new MarkerOptions().position(coo).title("Mi Unidad").icon(BitmapDescriptorFactory.defaultMarker(
                     BitmapDescriptorFactory.HUE_RED
             )));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coo, zoomLevel));
        }else{
            PolylineOptions options = new PolylineOptions();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            options.color( Color.BLUE );
            options.width( 5 );
            options.visible( true );

            for ( int i=0; i<listCoordinates.size(); i++ )
            {

                options.add( new LatLng( listCoordinates.get(i).getLatitude(), listCoordinates.get(i).getLongitude() ));
                builder.include(new LatLng(listCoordinates.get(i).getLatitude(), listCoordinates.get(i).getLongitude() ));
            }


            mMap.addPolyline( options );
            LatLngBounds bounds = builder.build();
           // System.out.println(bounds);

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.12);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
            CameraUpdate cu =CameraUpdateFactory.newLatLngBounds(bounds, width,height,padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
           // mMap.animateCamera(CameraUpdateFactory.newLa);

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_type_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Toolbar getActionBarToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        return mToolbar;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
