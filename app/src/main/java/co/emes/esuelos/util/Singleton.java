package co.emes.esuelos.util;

import co.emes.esuelos.model.Research;

/**
 * Created by csarmiento on 11/04/16.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    private String androidId;
    private Double x;
    private Double y;
    private Research research;
    private String paisaje;
    private String simbolo;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Research getResearch() {
        return research;
    }

    public void setResearch(Research research) {
        this.research = research;
    }

    public String getPaisaje() {
        return paisaje;
    }

    public void setPaisaje(String paisaje) {
        this.paisaje = paisaje;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}
