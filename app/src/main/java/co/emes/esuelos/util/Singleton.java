package co.emes.esuelos.util;

/**
 * Created by csarmiento on 11/04/16.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    private String androidId;
    private Double x;
    private Double y;
    private String tpkFile;

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

    public String getTpkFile() {
        return tpkFile;
    }

    public void setTpkFile(String tpkFile) {
        this.tpkFile = tpkFile;
    }
}
