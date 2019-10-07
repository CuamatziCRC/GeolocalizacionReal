package com.example.cuamatzi.geolocalizacionreal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //Marcador y parametros para la ubicacion de uno

    private Marker marcador;
    double latitud = 0.0;
    double longitud = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
    /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }

    //METODO PARA LA UBICACION Y CENTRADO DE LA PANTALLA EN EL MOMENTO QUE NOS LOCALICE
    private void agregarMarcador(double latitud, double longitud) {
        LatLng coordenadas = new LatLng(latitud, longitud);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        ///Se debe modificar el marcador en caso de que apunte a null y propiedades del marcador
        //se mostrara una imagen y titulo
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("MI POSICION ACTUAL")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));

        //animacion de la camara
        mMap.animateCamera(miUbicacion);
    }


    //conocer la ubicacion
    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            agregarMarcador(latitud, longitud);
        }
    }


    ///Hacemos un objeto LocationListener para la ubicacion GPS del dispositivo
    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Aqui se coloca el metodo de actualizacion de ubicacion
            actualizarUbicacion(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    //Aqui obtendrermos geo posicionamiento y con el metodorequestlocationUpdates,con actualizaciones de 15 segundos
    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locListener);
        /*
        * Para que la aplicación muestre la ubicación del usuario hay que cambiar el parámetro LocationManager.GPS_PROVIDER
         * por LocationManager.NETWORK_PROVIDER como se muestra en la siguiente linea de código:
            //En este caso se hace uso del provedor de telefonia
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,loclistener);﻿
        * */
    }
}
