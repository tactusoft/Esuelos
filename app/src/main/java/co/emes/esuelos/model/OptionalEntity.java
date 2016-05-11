package co.emes.esuelos.model;

import android.view.View;

import java.io.Serializable;
import java.util.List;

/**
 * Created by csarmiento on 11/05/16.
 */
public class OptionalEntity implements Serializable {

    Integer index;
    List<View> viewListOptional;
    List<View> viewListFlecked;

    public OptionalEntity() {
    }

    public OptionalEntity(Integer index, List<View> viewListOptional, List<View> viewListFlecked) {
        this.index = index;
        this.viewListOptional = viewListOptional;
        this.viewListFlecked = viewListFlecked;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<View> getViewListOptional() {
        return viewListOptional;
    }

    public void setViewListOptional(List<View> viewListOptional) {
        this.viewListOptional = viewListOptional;
    }

    public List<View> getViewListFlecked() {
        return viewListFlecked;
    }

    public void setViewListFlecked(List<View> viewListFlecked) {
        this.viewListFlecked = viewListFlecked;
    }
}
