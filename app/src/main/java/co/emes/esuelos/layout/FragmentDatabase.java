package co.emes.esuelos.layout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import co.emes.esuelos.R;
import co.emes.esuelos.fileselector.FileOperation;
import co.emes.esuelos.fileselector.FileSelector;
import co.emes.esuelos.fileselector.OnHandleFileListener;
import co.emes.esuelos.model.Research;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.TPKHelper;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 29/05/16.
 */
public class FragmentDatabase extends Fragment {

    TextView txtLoadMap;
    TextView txtLoadGeo;

    DataBaseHelper dataBaseHelper;
    Research research;

    public FragmentDatabase () {
        dataBaseHelper = new DataBaseHelper(getActivity());
        research = Singleton.getInstance().getResearch();
        if (research == null) {
            research =  new Research();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);

        txtLoadMap = (TextView)rootView.findViewById(R.id.txt_load_map);
        txtLoadGeo = (TextView)rootView.findViewById(R.id.txt_load_geo);

        refreshInputs();

        Button btnLoadMap = (Button) rootView.findViewById(R.id.btn_load_map);
        btnLoadMap.setTransformationMethod(null);
        btnLoadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTPK();
            }
        });

        Button btnLoadGeo = (Button) rootView.findViewById(R.id.btn_load_geo);
        btnLoadGeo.setTransformationMethod(null);
        btnLoadGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGEO();
            }
        });
        return rootView;
    }

    private void refreshInputs() {
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
    }

    public void loadTPK() {
        final String[] mFileFilter = { ".tpk" };
        new FileSelector(getActivity(), FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
    }

    public void loadGEO() {
        final String[] mFileFilter = { ".geodatabase" };
        new FileSelector(getActivity(), FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
    }

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            File file = new File(filePath);
            if (!Utils.isLocalTiledLayer(file) && !Utils.isGeodatabase(file) ) {
                Toast.makeText(getActivity(), "No ha seleccionado ningún archivo!", Toast.LENGTH_LONG).show();
            } else {
                new LoadFileTask(getActivity(), file).execute();
            }
        }
    };

    class LoadFileTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        File file;

        public LoadFileTask(Context context, File file) {
            progressDialog = new ProgressDialog(context);
            this.file = file;
        }

        public void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getResources().getString(R.string.msg_loading));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                boolean isTPK = false;
                if (Utils.isLocalTiledLayer(file)) {
                    isTPK = true;
                }
                TPKHelper tpkHelper = new TPKHelper(getActivity(), file);
                tpkHelper.createTPK();
                research.setStartDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                research.setEndDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                research.setStatus(1);
                research.setUser("carlossarmientor@gmail.com");
                if(isTPK) {
                    research.setTpkFilePath(tpkHelper.getFilePath());
                } else {
                    research.setGeoFilePath(tpkHelper.getFilePath());
                }
                dataBaseHelper.insertResearch(research);
                Singleton.getInstance().setResearch(research);

            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void unused) {
            progressDialog.dismiss();
            refreshInputs();
            Toast.makeText(getActivity(), "Archivo cargado exitosamente!", Toast.LENGTH_LONG).show();
        }
    }
}
