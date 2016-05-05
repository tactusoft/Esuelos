package co.emes.esuelos.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csarmiento on 11/04/16.
 */
public class CustomDataSource {

    public static List FORMS = new ArrayList<CustomForm>();

    static{
        FORMS.add(new CustomForm(1,"Nota de Campo","Nota de Campo"));
        FORMS.add(new CustomForm(2,"Detallada","Detallada"));
        FORMS.add(new CustomForm(3,"Comprobación","Comprobación"));
    }
}
