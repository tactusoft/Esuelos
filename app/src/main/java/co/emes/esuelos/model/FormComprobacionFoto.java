package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 4/05/16.
 */
public class FormComprobacionFoto implements Serializable {

    private Integer id;
    private Integer idFormComprobacion;
    private byte[] foto;

    public FormComprobacionFoto() {
    }

    public FormComprobacionFoto(Integer id, Integer idFormComprobacion, byte[] foto) {
        this.id = id;
        this.idFormComprobacion = idFormComprobacion;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFormComprobacion() {
        return idFormComprobacion;
    }

    public void setIdFormComprobacion(Integer idFormComprobacion) {
        this.idFormComprobacion = idFormComprobacion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
