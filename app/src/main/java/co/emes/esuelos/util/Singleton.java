package co.emes.esuelos.util;

/**
 * Created by csarmiento on 11/04/16.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private Float x;
    private Float y;
    private String tpkFile;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public String getTpkFile() {
        return tpkFile;
    }

    public void setTpkFile(String tpkFile) {
        this.tpkFile = tpkFile;
    }
}
