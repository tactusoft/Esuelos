package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import co.emes.esuelos.R;

import co.emes.esuelos.forms.CustomDataSource;
import co.emes.esuelos.forms.CustomForm;
import co.emes.esuelos.forms.CustomFormArrayAdapter;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FormsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;

    private Button btnOK;
    private Integer index;

    public FormsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form, container, false);

        /*btnOK = (Button) rootView.findViewById(R.id.btnServicesOK);
        btnOK.setOnClickListener(okDataListener());
        btnOK.setEnabled(false);*/

        listView = (ListView) rootView.findViewById(R.id.formList);
        adapter = new CustomFormArrayAdapter<CustomForm>(getActivity(), CustomDataSource.FORMS);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setSelected(true);
                index = position;
                //btnOK.setEnabled(true);
            }
        });
        return rootView;
    }

    public View.OnClickListener okDataListener() {
        return new View.OnClickListener() {
            public void onClick(View v) {

            }
        };
    }

}
