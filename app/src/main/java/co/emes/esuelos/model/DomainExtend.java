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

    /**
     * Created by anupamchugh on 10/12/15.
     */
    public static class DataModel {

        public int icon;
        public String name;

        // Constructor.
        public DataModel(int icon, String name) {

            this.icon = icon;
            this.name = name;
        }
    }
}
