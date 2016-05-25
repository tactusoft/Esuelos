package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 25/05/16.
 */
public class FormTodos implements Serializable {

    String nroObservacion;
    String fechaHora;
    Integer estado;
    String tipo;
    Object formulario;

    public FormTodos() {
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
}
