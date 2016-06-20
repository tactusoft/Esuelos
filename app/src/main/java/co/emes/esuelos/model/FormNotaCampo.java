package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 4/05/16.
 */
public class FormNotaCampo implements Serializable {

    Integer id;
    String nroObservacion;
    Integer reconocedor;
    String fechaHora;
    Double longitud;
    Double latitud;
    Double altitud;
    String nombreSitio;
    Integer epocaClimatica;
    String diasLluvia;
    Integer gradiente;
    Integer pendienteLongitud;
    Integer pendienteForma;
    Integer claseErosion;
    Integer tipoErosion;
    Integer gradoErosion;
    Integer claseMovimiento;
    Integer tipoMovimiento;
    Integer frecuenciaMovimiento;
    Integer anegamiento;
    Integer frecuencia;
    Integer duracion;
    Integer pedregosidad;
    Integer afloramiento;
    String vegetacionNatural;
    Integer grupoUso;
    Integer subgrupoUso;
    String nombreCultivo;
    String observaciones;
    Integer estado;
    String paisaje;
    String simbolo;

    public FormNotaCampo() {
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

    public Integer getReconocedor() {
        return reconocedor;
    }

    public void setReconocedor(Integer reconocedor) {
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

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
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

    public Integer getGradiente() {
        return gradiente;
    }

    public void setGradiente(Integer gradiente) {
        this.gradiente = gradiente;
    }

    public Integer getPendienteLongitud() {
        return pendienteLongitud;
    }

    public void setPendienteLongitud(Integer pendienteLongitud) {
        this.pendienteLongitud = pendienteLongitud;
    }

    public Integer getPendienteForma() {
        return pendienteForma;
    }

    public void setPendienteForma(Integer pendienteForma) {
        this.pendienteForma = pendienteForma;
    }

    public Integer getClaseErosion() {
        return claseErosion;
    }

    public void setClaseErosion(Integer claseErosion) {
        this.claseErosion = claseErosion;
    }

    public Integer getTipoErosion() {
        return tipoErosion;
    }

    public void setTipoErosion(Integer tipoErosion) {
        this.tipoErosion = tipoErosion;
    }

    public Integer getGradoErosion() {
        return gradoErosion;
    }

    public void setGradoErosion(Integer gradoErosion) {
        this.gradoErosion = gradoErosion;
    }

    public Integer getClaseMovimiento() {
        return claseMovimiento;
    }

    public void setClaseMovimiento(Integer claseMovimiento) {
        this.claseMovimiento = claseMovimiento;
    }

    public Integer getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(Integer tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getFrecuenciaMovimiento() {
        return frecuenciaMovimiento;
    }

    public void setFrecuenciaMovimiento(Integer frecuenciaMovimiento) {
        this.frecuenciaMovimiento = frecuenciaMovimiento;
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

    public String getVegetacionNatural() {
        return vegetacionNatural;
    }

    public void setVegetacionNatural(String vegetacionNatural) {
        this.vegetacionNatural = vegetacionNatural;
    }

    public Integer getGrupoUso() {
        return grupoUso;
    }

    public void setGrupoUso(Integer grupoUso) {
        this.grupoUso = grupoUso;
    }

    public Integer getSubgrupoUso() {
        return subgrupoUso;
    }

    public void setSubgrupoUso(Integer subgrupoUso) {
        this.subgrupoUso = subgrupoUso;
    }

    public String getNombreCultivo() {
        return nombreCultivo;
    }

    public void setNombreCultivo(String nombreCultivo) {
        this.nombreCultivo = nombreCultivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getPaisaje() {
        return paisaje;
    }

    public void setPaisaje(String paisaje) {
        this.paisaje = paisaje;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}
