package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 18/04/16.
 */
public class Research implements Serializable {

    private Integer id;
    private String startDate;
    private String endDate;
    private Integer status;
    private String user;
    private String tpkFilePath;
    private String geoFilePath;

    public Research() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTpkFilePath() {
        return tpkFilePath;
    }

    public void setTpkFilePath(String tpkFilePath) {
        this.tpkFilePath = tpkFilePath;
    }

    public String getGeoFilePath() {
        return geoFilePath;
    }

    public void setGeoFilePath(String geoFilePath) {
        this.geoFilePath = geoFilePath;
    }
}
