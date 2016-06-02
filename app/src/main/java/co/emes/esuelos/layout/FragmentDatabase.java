package co.emes.esuelos.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import co.emes.esuelos.R;
import co.emes.esuelos.main.MainActivity;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 29/05/16.
 */
public class FragmentDatabase extends Fragment {

    TextView txtLoadMap;
    TextView txtLoadGeo;

    public void FragmentDatabase () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);

        txtLoadMap = (TextView)rootView.findViewById(R.id.txt_load_map);
        txtLoadGeo = (TextView)rootView.findViewById(R.id.txt_load_geo);

        if(Singleton.getInstance().getResearch()!=null) {
            String tpkFilePath = Singleton.getInstance().getResearch().getTpkFilePath();
            if(tpkFilePath!=null && !tpkFilePath.isEmpty()) {
                File file = new File(tpkFilePath);
                txtLoadMap.setText(String.format(getResources().getString(R.string.dtb_select_file), Utils.getFileName(file)));
            }
            String geoFilePath = Singleton.getInstance().getResearch().getGeoFilePath();
            if(geoFilePath!=null && !geoFilePath.isEmpty()) {
                File file = new File(geoFilePath);
                txtLoadGeo.setText(String.format(getResources().getString(R.string.dtb_select_file), Utils.getFileName(file)));
            }
        }

        Button btnLoadMap = (Button) rootView.findViewById(R.id.btn_load_map);
        btnLoadMap.setTransformationMethod(null);
        btnLoadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadTPK();
            }
        });

        Button btnLoadGeo = (Button) rootView.findViewById(R.id.btn_load_geo);
        btnLoadGeo.setTransformationMethod(null);
        btnLoadGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadGEO();
            }
        });
        return rootView;
    }
}
