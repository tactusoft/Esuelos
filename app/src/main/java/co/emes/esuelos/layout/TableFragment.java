package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import co.emes.esuelos.R;
import co.emes.esuelos.forms.FormComprobacionAdapter;
import co.emes.esuelos.model.FormComprobacion;
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

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        List<FormComprobacion> testingList = dataBaseHelper.getListFormComprobacion();

        FormComprobacionAdapter testingAdapter = new FormComprobacionAdapter(getActivity(), getFragmentManager(),
                R.layout.list_skyline_item, testingList);
        listViewTesting.setItemsCanFocus(false);
        listViewTesting.setAdapter(testingAdapter);

        return rootView;
    }

}
