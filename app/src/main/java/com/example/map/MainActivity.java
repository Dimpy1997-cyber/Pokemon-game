package com.example.map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        CheckUserPermissions();
        LoadPockemon();

    }

    // code by hussein
    //access to permsions
    void CheckUserPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        runListener();// init the contact list

    }

    //get access to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    runListener();
                    // init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText(this, "access denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void runListener() {
        locationListener myloc = new locationListener();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 10, myloc);
        myThread myth = new myThread();
        myth.start();
    }

    Location oldloc;

    class myThread extends Thread {
        myThread() {
            oldloc = new Location("Start");
            oldloc.setLatitude(0);
            oldloc.setLongitude(0);
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if(oldloc.distanceTo(locationListener.location)==0) {
                        continue;
                    }
                    oldloc=locationListener.location;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            map.clear();
                            LatLng sydney = new LatLng(locationListener.location.getLatitude(),
                                    locationListener.location.getLongitude());
                            map.addMarker(new MarkerOptions().position(sydney)
                                    .title("Me")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

                            for (int i = 0; i < ListPockemons.size(); i++) {
                                Pockemon pockemon = ListPockemons.get(i);

                                if(pockemon.isCatch==false) {
                                    LatLng locpockemon = new LatLng(pockemon.location.getLatitude(), pockemon.location.getLongitude());
                                    map.addMarker(new MarkerOptions().position(locpockemon)
                                            .title(pockemon.name)
                                            .snippet(pockemon.des + ",power:" + pockemon.power)
                                            .icon(BitmapDescriptorFactory.fromResource(pockemon.Image)));
                                }
                                // catch the pockemon
                                if(locationListener.location.distanceTo(pockemon.location)<2){
                                    MyPower=MyPower+pockemon.power;
                                    Toast.makeText(MainActivity.this,"Catch pockemon, new power is"+ MyPower,
                                            Toast.LENGTH_LONG).show();
                                    pockemon.isCatch=true;
                                    ListPockemons.set(i,pockemon);

                                }

                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        double MyPower=0;
        // to add list of pockemon from its class
        ArrayList<Pockemon> ListPockemons = new ArrayList<>();
         public void LoadPockemon() {
            ListPockemons.add((new Pockemon(R.drawable.bulbasaur, "bulbasur",
                    "lives in friycat", 90.5, 31.6382675, 74.8057818)));
            ListPockemons.add((new Pockemon(R.drawable.charmander, "charmander",
                    "lives in usa", 55, 31.6377925, 74.823892)));
            ListPockemons.add((new Pockemon(R.drawable.squirtle, "squirtle",
                    "lives in japan", 33.5, 31.6326694, 74.805656)));
        }
    }
