package co.emes.esuelos.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.R;

import co.emes.esuelos.fileselector.FileOperation;
import co.emes.esuelos.fileselector.FileSelector;
import co.emes.esuelos.fileselector.OnHandleFileListener;
import co.emes.esuelos.layout.FragmentDatabase;
import co.emes.esuelos.layout.FragmentHome;
import co.emes.esuelos.layout.FragmentMap;
import co.emes.esuelos.adapter.DrawerItemCustomAdapter;
import co.emes.esuelos.layout.FragmentSummary;
import co.emes.esuelos.layout.FragmentSummaryDetail;
import co.emes.esuelos.model.DomainExtend;
import co.emes.esuelos.model.Research;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.TPKHelper;
import co.emes.esuelos.util.Utils;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    Toolbar toolbar;
    CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    AlertDialog.Builder alertDialog;
    AlertDialog.Builder confirmDialog;
    DataBaseHelper dataBaseHelper;
    List<DomainExtend.DataModel> listDataModel;
    int dataBaseType = 1;

    Research research;

    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Singleton.getInstance().setAndroidId(androidId);

        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        listDataModel =  new LinkedList<>();
        try {
            dataBaseHelper = new DataBaseHelper(getApplicationContext());
            dataBaseHelper.createDataBase();
            research = dataBaseHelper.getOpenResearch();
            if(research==null) {
                research = new Research();
                alertDialog.show();
            } else {
                Singleton.getInstance().setResearch(research);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupToolbar();
        setupAlertBuilder();
        setupConfirmBuilder();
        setupDrawerToggle();

        Fragment fragment = new FragmentHome();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setSelection(0);
        setTitle(getResources().getString(R.string.ndi_home));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            for (Fragment frag : fm.getFragments()) {
                if (frag!=null && frag.isVisible()) {
                    if (frag instanceof FragmentMap) {
                        showFragment(new FragmentHome(), R.string.ndi_home);
                        mDrawerList.setItemChecked(0, true);
                        mDrawerList.setSelection(0);
                        return;
                    } else if (frag instanceof FragmentSummary) {
                        showFragment(new FragmentHome(), R.string.ndi_home);
                        mDrawerList.setItemChecked(0, true);
                        mDrawerList.setSelection(0);
                        return;
                    } else if (frag instanceof FragmentDatabase) {
                        showFragment(new FragmentHome(), R.string.ndi_home);
                        mDrawerList.setItemChecked(0, true);
                        mDrawerList.setSelection(0);
                        return;
                    }  else if (frag instanceof FragmentSummaryDetail) {
                        showFragment(new FragmentSummary(), R.string.ndi_summary);
                        mDrawerList.setItemChecked(2, true);
                        mDrawerList.setSelection(2);
                        return;
                    }
                }
            }
            super.onBackPressed();
        }
    }

    public void showFragment(Fragment fragment, int title) {
        getSupportActionBar().setTitle(title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public void selectItem(int position) {
        Fragment fragment = null;
        String title = getResources().getString(R.string.ndi_home);
        switch (position) {
            case 0:
                title = getResources().getString(R.string.ndi_home);
                fragment = new FragmentHome();
                break;
            case 1:
                title = getResources().getString(R.string.ndi_map);
                fragment = new FragmentMap();
                break;
            case 2:
                title = getResources().getString(R.string.ndi_summary);
                fragment = new FragmentSummary();
                break;
            case 3:
                title = getResources().getString(R.string.ndi_tpk);
                fragment = new FragmentDatabase();
                break;
            case 4:
                dataBaseType = 1;
                confirmDialog.setTitle(getResources().getString(R.string.ndi_database));
                confirmDialog.show();
                break;
            case 5:
                dataBaseType = 2;
                confirmDialog.setTitle(getResources().getString(R.string.ndi_load_database));
                confirmDialog.show();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(title);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    void setupDrawerToggle(){
        listDataModel.add(new DomainExtend.DataModel(R.drawable.ic_home, getResources().getString(R.string.ndi_home)));
        listDataModel.add(new DomainExtend.DataModel(R.drawable.ic_layers, getResources().getString(R.string.ndi_map)));
        listDataModel.add(new DomainExtend.DataModel(R.drawable.ic_list, getResources().getString(R.string.ndi_summary)));
        listDataModel.add(new DomainExtend.DataModel(R.drawable.folder, getResources().getString(R.string.ndi_tpk)));
        listDataModel.add(new DomainExtend.DataModel(R.drawable.download, getResources().getString(R.string.ndi_database)));
        listDataModel.add(new DomainExtend.DataModel(R.drawable.ic_open, getResources().getString(R.string.ndi_load_database)));
        DomainExtend.DataModel[] drawerItem = new DomainExtend.DataModel[listDataModel.size()];
        listDataModel.toArray(drawerItem);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    String filePath = data.getData().getPath();
                    File file = new File(filePath);
                    if (!Utils.isLocalTiledLayer(file)) {
                        alertDialog.show();
                    } else {
                        try {
                            TPKHelper tpkHelper = new TPKHelper(getApplicationContext(), file);
                            tpkHelper.createTPK();
                            Research research = new Research();
                            research.setStartDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            research.setEndDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            research.setStatus(1);
                            research.setUser("carlossarmientor@gmail.com");
                            research.setTpkFilePath(tpkHelper.getFilePath());
                            dataBaseHelper.insertResearch(research);
                        }catch(Exception ex){
                            ex.printStackTrace();;
                        }
                    }
                }
                break;
        }
    }

    public void exportDatabse() {
        final String[] mFileFilter = {".sqlite" };
        new FileSelector(MainActivity.this, FileOperation.SAVE, mSaveFileListener, mFileFilter).show();
    }

    public void loadDatabse() {
        final String[] mFileFilter = {".sqlite" };
        new FileSelector(MainActivity.this, FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
    }

    private void setupAlertBuilder(){
        alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Seleccionar Mapa");
        alertDialog.setMessage("Actualmente no existe una cartografía asociada al Estudio de Suelos." +
                " Seleccione Aceptar para realizar la búsqueda de un Mapa (tpk)");
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectItem(3);
                    }
                });
    }

    private void setupConfirmBuilder(){
        confirmDialog = new AlertDialog.Builder(
                MainActivity.this);
        confirmDialog.setCancelable(false);
        confirmDialog.setMessage("Esta Operación eliminará los datos actuales. ¿Está seguro de continuar?");
        confirmDialog.setIcon(android.R.drawable.ic_dialog_info);
        confirmDialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(dataBaseType == 1) {
                            exportDatabse();
                        } else {
                            loadDatabse();
                        }
                    }
                });
        confirmDialog.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
    }

    OnHandleFileListener mSaveFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            try {
                String newFilePath = filePath;
                //File sd = new File(filePath);
                String ext = Utils.getExtension(filePath);
                if(ext == null || ext.isEmpty()) {
                    newFilePath = newFilePath + ".sqlite";
                } else if(!ext.toUpperCase().equals("SQLITE")) {
                    newFilePath = newFilePath.replace(ext, "sqlite");
                }
                File data = Environment.getDataDirectory();
                //if (sd.canWrite()) {
                    String currentDBPath = "//data//" + getPackageName() + "//databases//esuelosdb.sqlite";
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(newFilePath);
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        dataBaseHelper.deleteAll();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.fileCreationOk),
                                Toast.LENGTH_LONG).show();
                    }
                /*} else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.accessDenied),
                            Toast.LENGTH_LONG).show();
                }*/
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            File file = new File(filePath);
            if (!Utils.isDatabase(file)) {
                Toast.makeText(getApplicationContext(), "No ha seleccionado ningún archivo!", Toast.LENGTH_LONG).show();
            } else {
                new LoadFileTask(getApplicationContext(), file).execute();
            }
        }
    };

    class LoadFileTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        File file;

        public LoadFileTask(Context context, File file) {
            progressDialog = new ProgressDialog(MainActivity.this);
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
                dataBaseHelper.createDataBase(this.file);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(Void unused) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Base de Datos cargada exitosamente!", Toast.LENGTH_LONG).show();
        }
    }

}
