package co.emes.esuelos.model;

import java.io.Serializable;
import java.util.Date;

import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 18/04/16.
 */
public class Research implements Serializable {

    private Integer id;
    private String startDate;
    private String endDate;
    private Integer status;
    private String user;
    private String filePath;

    public Research() {
    }

    public Research(Integer id, String startDate, String endDate, Integer status, String user, String filePath) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.user = user;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
