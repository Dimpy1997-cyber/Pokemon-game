package com.example.map;

import android.location.Location;

public class Pockemon {
    public int Image;
    public String name;
    public String des;
    public double power;
    public boolean isCatch;
    public Location location;

    Pockemon(int Image,String name,String des, double power,double lat, double lag){
        this.Image=Image;
        this.name=name;
        this.des=des;
        this.power=power;
        this.isCatch=false;
        location=new Location(name);
        location.setLongitude(lag);
        location.setLatitude(lat);
    }
}
