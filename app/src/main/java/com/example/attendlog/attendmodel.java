package com.example.attendlog;

import java.util.ArrayList;

public class attendmodel {
    ArrayList<String> pino;
    String date,time,noon;
    int absents_count,presents_count;

    public ArrayList<String> getPino() {
        return pino;
    }

    public void setPino(ArrayList<String> pino) {
        this.pino = pino;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoon() {
        return noon;
    }

    public void setNoon(String noon) {
        this.noon = noon;
    }


    public int getAbsents_count() {
        return absents_count;
    }

    public void setAbsents_count(int absents_count) {
        this.absents_count = absents_count;
    }

    public int getPresents_count() {
        return presents_count;
    }

    public void setPresents_count(int presents_count) {
        this.presents_count = presents_count;
    }

    public attendmodel(ArrayList<String> pino, String date, String time, String noon, int absents_count, int presents_count) {
        this.pino = pino;
        this.date = date;
        this.time = time;
        this.noon = noon;
        this.absents_count = absents_count;
        this.presents_count = presents_count;
    }

    public attendmodel() {
    }
}
