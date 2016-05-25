package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.forms.FormTodosArrayAdapter;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormNotaCampo;
import co.emes.esuelos.model.FormTodos;
import co.emes.esuelos.util.DataBaseHelper;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class TableFragment extends Fragment {

    ListView listViewTesting;

    public TableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        listViewTesting = (ListView) rootView.findViewById(R.id.list_forms);

        List<FormTodos> formTodosList =  new LinkedList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        List<FormComprobacion> testingList = dataBaseHelper.getListFormComprobacion();
        for(FormComprobacion form:testingList){
            FormTodos formTodos =  new FormTodos();
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Comprobación");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodosList.add(formTodos);
        }

        List<FormNotaCampo> fieldNoteList = dataBaseHelper.getListFormNotaCampo();
        for(FormNotaCampo form:fieldNoteList){
            FormTodos formTodos =  new FormTodos();
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Nota de Campo");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodosList.add(formTodos);
        }

        FormTodosArrayAdapter testingAdapter = new FormTodosArrayAdapter(getActivity(), getFragmentManager(),
                R.layout.list_skyline_item, formTodosList);
        listViewTesting.setItemsCanFocus(false);
        listViewTesting.setAdapter(testingAdapter);

        return rootView;
    }

}
