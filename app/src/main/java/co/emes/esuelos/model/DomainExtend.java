package co.emes.esuelos.model;

/**
 * Created by csarmiento on 13/05/16.
 */
public class DomainExtend extends Domain {

    public DomainExtend() {
        super();
    }

    @Override
    public String toString(){
        return this.getCodigo();
    }
}
