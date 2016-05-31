package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.emes.esuelos.R;
import co.emes.esuelos.main.MainActivity;

/**
 * Created by csarmiento on 29/05/16.
 */
public class FragmentDatabase extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);
        Button btnLoadMap = (Button) rootView.findViewById(R.id.btn_load_map);
        btnLoadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadTPK();
            }
        });

        Button btnLoadGeo = (Button) rootView.findViewById(R.id.btn_load_geo);
        btnLoadGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadGEO();
            }
        });
        return rootView;
    }
}
