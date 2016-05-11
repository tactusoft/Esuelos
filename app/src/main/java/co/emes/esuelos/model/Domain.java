package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 20/04/16.
 */
public class Domain implements Serializable {

    private Integer id;
    private String codigo;
    private String valor;
    private String codMinisterio;
    private String descripcion;
    private String periodo;
    private Integer orden;
    private String grupo;

    public Domain() {
    }

    public Domain(Integer id) {
        this.id = id;
    }

    public Domain(Integer id, String codigo, String valor, String codMinisterio, String descripcion,
                  String periodo, Integer orden, String grupo) {
        this.id = id;
        this.codigo = codigo;
        this.valor = valor;
        this.codMinisterio = codMinisterio;
        this.descripcion = descripcion;
        this.periodo = periodo;
        this.orden = orden;
        this.grupo = grupo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodMinisterio() {
        return codMinisterio;
    }

    public void setCodMinisterio(String codMinisterio) {
        this.codMinisterio = codMinisterio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
