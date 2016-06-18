package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.forms.FormTodosArrayAdapter;
import co.emes.esuelos.main.MainActivity;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormNotaCampo;
import co.emes.esuelos.model.FormTodos;
import co.emes.esuelos.util.DataBaseHelper;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FragmentSummaryDetail extends Fragment {

    ListView listViewTesting;
    String fecha;

    public static FragmentSummaryDetail newInstance(String fecha) {
        FragmentSummaryDetail f = new FragmentSummaryDetail();
        f.setFecha(fecha);
        return f;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        listViewTesting = (ListView) rootView.findViewById(R.id.list_forms);

        MainActivity mainActivity = ((MainActivity)getActivity());
        ActionBar actionBar = mainActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(getResources().getString(R.string.ndi_summary_par), fecha));
        }

        List<FormTodos> formTodosList =  new LinkedList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        List<FormComprobacion> testingList = dataBaseHelper.getListFormComprobacion(this.fecha);
        for(FormComprobacion form:testingList){
            FormTodos formTodos =  new FormTodos();
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Comprobación");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodos.setLongitud(form.getLongitud());
            formTodos.setLatitud(form.getLatitud());
            formTodosList.add(formTodos);
        }

        List<FormNotaCampo> fieldNoteList = dataBaseHelper.getListFormNotaCampo(this.fecha);
        for(FormNotaCampo form:fieldNoteList){
            FormTodos formTodos =  new FormTodos();
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Nota de Campo");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodos.setLongitud(form.getLongitud());
            formTodos.setLatitud(form.getLatitud());
            formTodosList.add(formTodos);
        }

        FormTodosArrayAdapter testingAdapter = new FormTodosArrayAdapter(getActivity(), getFragmentManager(),
                R.layout.list_skyline_item, formTodosList);
        listViewTesting.setAdapter(testingAdapter);

        return rootView;
    }

}
