package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 25/05/16.
 */
public class FormTodos implements Serializable {

    Long id;
    String nroObservacion;
    String fechaHora;
    Integer estado;
    String tipo;
    Object formulario;
    Double longitud;
    Double latitud;

    public FormTodos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroObservacion() {
        return nroObservacion;
    }

    public void setNroObservacion(String nroObservacion) {
        this.nroObservacion = nroObservacion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Object getFormulario() {
        return formulario;
    }

    public void setFormulario(Object formulario) {
        this.formulario = formulario;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
}
