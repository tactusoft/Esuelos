package co.emes.esuelos.main;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import co.emes.esuelos.layout.HomeFragment;
import co.emes.esuelos.layout.MapFragment;
import co.emes.esuelos.layout.DataModel;
import co.emes.esuelos.layout.DrawerItemCustomAdapter;
import co.emes.esuelos.layout.FormsFragment;
import co.emes.esuelos.layout.TableFragment;
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
    DataBaseHelper dataBaseHelper;
    List<DataModel> listDataModel;

    private static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        listDataModel =  new LinkedList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        try {
            dataBaseHelper = new DataBaseHelper(getApplicationContext());
            dataBaseHelper.createDataBase();
            if(!dataBaseHelper.getOpenResearch()){
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
                                loadTPK();
                            }
                        });
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listDataModel.add(new DataModel(R.drawable.home, getResources().getString(R.string.ndi_home)));
        listDataModel.add(new DataModel(R.drawable.layers, getResources().getString(R.string.ndi_map)));
        listDataModel.add(new DataModel(R.drawable.forms, getResources().getString(R.string.ndi_forms)));
        listDataModel.add(new DataModel(R.drawable.list, getResources().getString(R.string.ndi_summary)));
        listDataModel.add(new DataModel(R.drawable.folder, getResources().getString(R.string.ndi_tpk)));
        listDataModel.add(new DataModel(R.drawable.download, getResources().getString(R.string.ndi_database)));
        DataModel[] drawerItem = new DataModel[listDataModel.size()];
        listDataModel.toArray(drawerItem);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        mDrawerList.setItemChecked(0, true);
        mDrawerList.setSelection(0);
        setTitle(getResources().getString(R.string.ndi_home));
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        String title = getResources().getString(R.string.ndi_home);
        switch (position) {
            case 0:
                title = getResources().getString(R.string.ndi_home);
                fragment = new HomeFragment();
                break;
            case 1:
                title = getResources().getString(R.string.ndi_map);
                fragment = new MapFragment();
                break;
            case 2:
                title = getResources().getString(R.string.ndi_forms);
                fragment = new FormsFragment();
                break;
            case 3:
                title = getResources().getString(R.string.ndi_summary);
                fragment = new TableFragment();
                break;
            case 4:
                loadTPK();
                break;
            case 5:
                exportDatabse();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
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
                            Singleton.getInstance().setTpkFile(tpkHelper.getFilePath());
                            Research research = new Research();
                            research.setStartDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            research.setEndDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            research.setStatus(1);
                            research.setUser("carlossarmientor@gmail.com");
                            research.setFilePath(tpkHelper.getFilePath());
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

    private void loadTPK() {
        final String[] mFileFilter = { ".tpk" };
        new FileSelector(MainActivity.this, FileOperation.LOAD, mLoadFileListener, mFileFilter).show();
    }

    OnHandleFileListener mLoadFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            File file = new File(filePath);
            if (!Utils.isLocalTiledLayer(file)) {
                alertDialog.show();
            } else {
                try {
                    TPKHelper tpkHelper = new TPKHelper(getApplicationContext(), file);
                    tpkHelper.createTPK();
                    Singleton.getInstance().setTpkFile(tpkHelper.getFilePath());
                    Research research = new Research();
                    research.setStartDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    research.setEndDate(Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    research.setStatus(1);
                    research.setUser("carlossarmientor@gmail.com");
                    research.setFilePath(tpkHelper.getFilePath());
                    dataBaseHelper.insertResearch(research);
                    Toast.makeText(getApplicationContext(), "Cartografía cargada exitosamente!", Toast.LENGTH_LONG).show();
                }catch(Exception ex){
                    ex.printStackTrace();;
                }
            }
        }
    };

    OnHandleFileListener mSaveFileListener = new OnHandleFileListener() {
        @Override
        public void handleFile(final String filePath) {
            try {
                String newFilePath = filePath;
                File sd = new File(filePath);
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

}
