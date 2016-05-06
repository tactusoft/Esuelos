package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 6/05/16.
 */
public class FormComprobacionHorizonte implements Serializable {

    Integer id;
    Integer idFormComprobacion;
    Integer numeroHorizonte;
    Integer profundidad;
    Integer colorHue;
    Integer colorValue;
    Integer colorChroma;
    Integer colorPorcentaje;
    Integer tipoMaterial;
    Integer claseTextural;
    Integer modificadorTextura;
    Integer claseOrganico;
    Integer claseComposicion;
    Integer texturaPorcentaje;
    Integer estructuraTipo;
    Integer estructuraClase;
    Integer estructuraGrado;
    Integer formaRompe;
    Integer motivoNoEstructura;
    Integer horizonteClase;
    Integer horizonteCaracterisitica;
    String texturaOtro;
    String estructuraOtra;

    public FormComprobacionHorizonte() {
    }

    public FormComprobacionHorizonte(Integer id, Integer idFormComprobacion, Integer numeroHorizonte, Integer profundidad,
                                     Integer colorHue, Integer colorValue, Integer colorChroma, Integer colorPorcentaje,
                                     Integer tipoMaterial, Integer claseTextural, Integer modificadorTextura, Integer claseOrganico,
                                     Integer claseComposicion, Integer texturaPorcentaje, Integer estructuraTipo, Integer estructuraClase,
                                     Integer estructuraGrado, Integer formaRompe, Integer motivoNoEstructura, Integer horizonteClase,
                                     Integer horizonteCaracterisitica, String texturaOtro, String estructuraOtra) {
        this.id = id;
        this.idFormComprobacion = idFormComprobacion;
        this.numeroHorizonte = numeroHorizonte;
        this.profundidad = profundidad;
        this.colorHue = colorHue;
        this.colorValue = colorValue;
        this.colorChroma = colorChroma;
        this.colorPorcentaje = colorPorcentaje;
        this.tipoMaterial = tipoMaterial;
        this.claseTextural = claseTextural;
        this.modificadorTextura = modificadorTextura;
        this.claseOrganico = claseOrganico;
        this.claseComposicion = claseComposicion;
        this.texturaPorcentaje = texturaPorcentaje;
        this.estructuraTipo = estructuraTipo;
        this.estructuraClase = estructuraClase;
        this.estructuraGrado = estructuraGrado;
        this.formaRompe = formaRompe;
        this.motivoNoEstructura = motivoNoEstructura;
        this.horizonteClase = horizonteClase;
        this.horizonteCaracterisitica = horizonteCaracterisitica;
        this.texturaOtro = texturaOtro;
        this.estructuraOtra = estructuraOtra;
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

    public Integer getNumeroHorizonte() {
        return numeroHorizonte;
    }

    public void setNumeroHorizonte(Integer numeroHorizonte) {
        this.numeroHorizonte = numeroHorizonte;
    }

    public Integer getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Integer profundidad) {
        this.profundidad = profundidad;
    }

    public Integer getColorHue() {
        return colorHue;
    }

    public void setColorHue(Integer colorHue) {
        this.colorHue = colorHue;
    }

    public Integer getColorValue() {
        return colorValue;
    }

    public void setColorValue(Integer colorValue) {
        this.colorValue = colorValue;
    }

    public Integer getColorChroma() {
        return colorChroma;
    }

    public void setColorChroma(Integer colorChroma) {
        this.colorChroma = colorChroma;
    }

    public Integer getColorPorcentaje() {
        return colorPorcentaje;
    }

    public void setColorPorcentaje(Integer colorPorcentaje) {
        this.colorPorcentaje = colorPorcentaje;
    }

    public Integer getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(Integer tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public Integer getClaseTextural() {
        return claseTextural;
    }

    public void setClaseTextural(Integer claseTextural) {
        this.claseTextural = claseTextural;
    }

    public Integer getModificadorTextura() {
        return modificadorTextura;
    }

    public void setModificadorTextura(Integer modificadorTextura) {
        this.modificadorTextura = modificadorTextura;
    }

    public Integer getClaseOrganico() {
        return claseOrganico;
    }

    public void setClaseOrganico(Integer claseOrganico) {
        this.claseOrganico = claseOrganico;
    }

    public Integer getClaseComposicion() {
        return claseComposicion;
    }

    public void setClaseComposicion(Integer claseComposicion) {
        this.claseComposicion = claseComposicion;
    }

    public Integer getTexturaPorcentaje() {
        return texturaPorcentaje;
    }

    public void setTexturaPorcentaje(Integer texturaPorcentaje) {
        this.texturaPorcentaje = texturaPorcentaje;
    }

    public Integer getEstructuraTipo() {
        return estructuraTipo;
    }

    public void setEstructuraTipo(Integer estructuraTipo) {
        this.estructuraTipo = estructuraTipo;
    }

    public Integer getEstructuraClase() {
        return estructuraClase;
    }

    public void setEstructuraClase(Integer estructuraClase) {
        this.estructuraClase = estructuraClase;
    }

    public Integer getEstructuraGrado() {
        return estructuraGrado;
    }

    public void setEstructuraGrado(Integer estructuraGrado) {
        this.estructuraGrado = estructuraGrado;
    }

    public Integer getFormaRompe() {
        return formaRompe;
    }

    public void setFormaRompe(Integer formaRompe) {
        this.formaRompe = formaRompe;
    }

    public Integer getMotivoNoEstructura() {
        return motivoNoEstructura;
    }

    public void setMotivoNoEstructura(Integer motivoNoEstructura) {
        this.motivoNoEstructura = motivoNoEstructura;
    }

    public Integer getHorizonteClase() {
        return horizonteClase;
    }

    public void setHorizonteClase(Integer horizonteClase) {
        this.horizonteClase = horizonteClase;
    }

    public Integer getHorizonteCaracterisitica() {
        return horizonteCaracterisitica;
    }

    public void setHorizonteCaracterisitica(Integer horizonteCaracterisitica) {
        this.horizonteCaracterisitica = horizonteCaracterisitica;
    }

    public String getTexturaOtro() {
        return texturaOtro;
    }

    public void setTexturaOtro(String texturaOtro) {
        this.texturaOtro = texturaOtro;
    }

    public String getEstructuraOtra() {
        return estructuraOtra;
    }

    public void setEstructuraOtra(String estructuraOtra) {
        this.estructuraOtra = estructuraOtra;
    }
}
