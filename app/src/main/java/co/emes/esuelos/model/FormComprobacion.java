package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 4/05/16.
 */
public class FormComprobacion implements Serializable {

    Integer id;
    String nroObservacion;
    String reconocedor;
    String fechaHora;
    Double longitud;
    Double latitud;
    Double altitud;
    Integer epocaClimatica;
    String diasLluvia;
    Integer pendienteLongitud;
    Integer gradoErosion;
    Integer tipoMovimiento;
    Integer anegamiento;
    Integer frecuencia;
    Integer duracion;
    Integer pedregosidad;
    Integer afloramiento;
    Integer fragmentoSuelo;
    Integer drenajeNatural;
    Integer profundidadEfectiva;
    Integer epidedones;
    Integer endopedones;
    Integer estado;

    public FormComprobacion() {
    }

    public FormComprobacion(Integer id, String nroObservacion, String reconocedor, String fechaHora, Double longitud,
                            Double latitud, Double altitud, Integer epocaClimatica, String diasLluvia, Integer pendienteLongitud,
                            Integer gradoErosion, Integer tipoMovimiento, Integer anegamiento, Integer frecuencia,
                            Integer duracion, Integer pedregosidad, Integer afloramiento, Integer fragmentoSuelo,
                            Integer drenajeNatural, Integer profundidadEfectiva, Integer epidedones, Integer endopedones,
                            Integer estado) {
        this.id = id;
        this.nroObservacion = nroObservacion;
        this.reconocedor = reconocedor;
        this.fechaHora = fechaHora;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.epocaClimatica = epocaClimatica;
        this.diasLluvia = diasLluvia;
        this.pendienteLongitud = pendienteLongitud;
        this.gradoErosion = gradoErosion;
        this.tipoMovimiento = tipoMovimiento;
        this.anegamiento = anegamiento;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.pedregosidad = pedregosidad;
        this.afloramiento = afloramiento;
        this.fragmentoSuelo = fragmentoSuelo;
        this.drenajeNatural = drenajeNatural;
        this.profundidadEfectiva = profundidadEfectiva;
        this.epidedones = epidedones;
        this.endopedones = endopedones;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroObservacion() {
        return nroObservacion;
    }

    public void setNroObservacion(String nroObservacion) {
        this.nroObservacion = nroObservacion;
    }

    public String getReconocedor() {
        return reconocedor;
    }

    public void setReconocedor(String reconocedor) {
        this.reconocedor = reconocedor;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
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

    public Double getAltitud() {
        return altitud;
    }

    public void setAltitud(Double altitud) {
        this.altitud = altitud;
    }

    public Integer getEpocaClimatica() {
        return epocaClimatica;
    }

    public void setEpocaClimatica(Integer epocaClimatica) {
        this.epocaClimatica = epocaClimatica;
    }

    public String getDiasLluvia() {
        return diasLluvia;
    }

    public void setDiasLluvia(String diasLluvia) {
        this.diasLluvia = diasLluvia;
    }

    public Integer getPendienteLongitud() {
        return pendienteLongitud;
    }

    public void setPendienteLongitud(Integer pendienteLongitud) {
        this.pendienteLongitud = pendienteLongitud;
    }

    public Integer getGradoErosion() {
        return gradoErosion;
    }

    public void setGradoErosion(Integer gradoErosion) {
        this.gradoErosion = gradoErosion;
    }

    public Integer getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(Integer tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getAnegamiento() {
        return anegamiento;
    }

    public void setAnegamiento(Integer anegamiento) {
        this.anegamiento = anegamiento;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getPedregosidad() {
        return pedregosidad;
    }

    public void setPedregosidad(Integer pedregosidad) {
        this.pedregosidad = pedregosidad;
    }

    public Integer getAfloramiento() {
        return afloramiento;
    }

    public void setAfloramiento(Integer afloramiento) {
        this.afloramiento = afloramiento;
    }

    public Integer getFragmentoSuelo() {
        return fragmentoSuelo;
    }

    public void setFragmentoSuelo(Integer fragmentoSuelo) {
        this.fragmentoSuelo = fragmentoSuelo;
    }

    public Integer getDrenajeNatural() {
        return drenajeNatural;
    }

    public void setDrenajeNatural(Integer drenajeNatural) {
        this.drenajeNatural = drenajeNatural;
    }

    public Integer getProfundidadEfectiva() {
        return profundidadEfectiva;
    }

    public void setProfundidadEfectiva(Integer profundidadEfectiva) {
        this.profundidadEfectiva = profundidadEfectiva;
    }

    public Integer getEpidedones() {
        return epidedones;
    }

    public void setEpidedones(Integer epidedones) {
        this.epidedones = epidedones;
    }

    public Integer getEndopedones() {
        return endopedones;
    }

    public void setEndopedones(Integer endopedones) {
        this.endopedones = endopedones;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
