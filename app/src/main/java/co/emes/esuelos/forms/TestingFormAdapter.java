package co.emes.esuelos.forms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import co.emes.esuelos.R;

/**
 * Created by csarmiento on 11/04/16.
 */
public class TestingFormAdapter<T> extends ArrayAdapter<T> {

    public TestingFormAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con two_line_list_item.xml
            listItemView = inflater.inflate(
                    R.layout.list_skyline_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los text views
        TextView title = (TextView)listItemView.findViewById(R.id.input_depth_1);
        TextView subtitle = (TextView)listItemView.findViewById(R.id.input_color_1);
        TextView subtitle3 = (TextView)listItemView.findViewById(R.id.input_texture_1);

        //Obteniendo instancia de la Tarea en la posición actual
        T item = (T)getItem(position);

        //Dividir la cadena en Nombre y Hora
        String cadenaBruta;
        String subCadenas [];

        cadenaBruta = item.toString();
        subCadenas = cadenaBruta.split("|",2);

        title.setText(subCadenas[0]);
        subtitle.setText(subCadenas[1]);

        //Devolver al ListView la fila creada
        return listItemView;
    }
}
