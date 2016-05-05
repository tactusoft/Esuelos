package co.emes.esuelos.forms;

import java.io.Serializable;

/**
 * Created by csarmiento on 11/04/16.
 */
public class CustomForm implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private String status;
    private Float x;
    private Float y;

    public CustomForm() {

    }

    public CustomForm(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CustomForm(Integer id, String name, String description, String status, Float x, Float y) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString(){
        return name + ";" + description;
    }
}
