package com.example.neror.trabalho_4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.neror.trabalho_4.Interface.AsyncResponse;
import com.example.neror.trabalho_4.Service.InfracaoService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by neror on 27/11/2017.
 */

public abstract class MapsBase extends FragmentActivity implements OnMapReadyCallback,AsyncResponse {

    private FloatingActionButton mFloatingActionButton;
    private GoogleMap mMap;
    private Random mRandom = new Random(1984);

    public  GoogleMap getMap(){
        return mMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFloatingActionButton = findViewById(R.id.floatButton);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewInfracaoActivity.class);
                startActivityForResult(intent, 0);
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            mMap.setMyLocationEnabled(true);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
            return;
        }

        mMap.setMyLocationEnabled(true);

        InfracaoService.AsynTaskGetInfracao data = new InfracaoService.AsynTaskGetInfracao();
        data.delegate = this;
        data.execute();
    }

    @SuppressWarnings("MissingPermission")
    protected LatLng getLocation(boolean pUserRandom) {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(pUserRandom)
                return new LatLng(getRandon(location.getLatitude(),location.getLatitude()+0.02896),getRandon(location.getLongitude(),location.getLongitude()+0.02896));
            return new LatLng(location.getLatitude(),location.getLongitude());

        }catch (Exception pEx){
            Log.e("Erro","Ocorreu um erro ao tentar obter a localização.",pEx);
            Toast.makeText(this,"Ocorreu um erro ao tentar obter a localização.",Toast.LENGTH_LONG);
        }
        return null;
    }

    private double getRandon(double pMin, double pMax){
        return mRandom.nextDouble() * (pMax-pMin)+ pMin;
    }
}
