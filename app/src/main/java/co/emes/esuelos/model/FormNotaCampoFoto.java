package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 4/05/16.
 */
public class FormNotaCampoFoto implements Serializable {

    private Integer id;
    private Integer idFormNotaCampo;
    private String foto;

    public FormNotaCampoFoto() {
    }

    public FormNotaCampoFoto(Integer id, Integer idFormNotaCampo, String foto) {
        this.id = id;
        this.idFormNotaCampo = idFormNotaCampo;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdFormNotaCampo() {
        return idFormNotaCampo;
    }

    public void setIdFormNotaCampo(Integer idFormNotaCampo) {
        this.idFormNotaCampo = idFormNotaCampo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
