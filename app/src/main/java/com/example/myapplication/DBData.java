package com.example.myapplication;

import java.util.ArrayList;

// whole point of this class is to add data into the database

public class DBData {
    ArrayList<Object> routeData = new ArrayList<>();

    public ArrayList<Object> routeInit(){
        routeData.add("62B"); routeData.add(1); routeData.add("Pit-os");
        routeData.add("62B"); routeData.add(2); routeData.add("Bacayan");
        routeData.add("62B"); routeData.add(3); routeData.add("Talamban");
        routeData.add("62B"); routeData.add(4); routeData.add("Grand Mall");
        routeData.add("62B"); routeData.add(5); routeData.add("USC Talamban");
        routeData.add("62B"); routeData.add(6); routeData.add("Country Mall");
        routeData.add("62B"); routeData.add(7); routeData.add("Ayala Center Cebu");
        routeData.add("62B"); routeData.add(8); routeData.add("Capitol");
        routeData.add("12L"); routeData.add(1); routeData.add("Labangon");
        routeData.add("12L"); routeData.add(2); routeData.add("CCNSHS");
        routeData.add("12L"); routeData.add(3); routeData.add("Gaisano Tisa");
        routeData.add("12L"); routeData.add(4); routeData.add("VSMMC");
        routeData.add("12L"); routeData.add(5); routeData.add("Fuente Osmeña");
        routeData.add("12L"); routeData.add(6); routeData.add("Mango");
        routeData.add("12L"); routeData.add(7); routeData.add("Ayala Center Cebu");
        routeData.add("12L"); routeData.add(8); routeData.add("Capitol");
        routeData.add("12L"); routeData.add(9); routeData.add("Banawa");
        return routeData;
    }
}
