package co.emes.esuelos.layout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import co.emes.esuelos.R;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.io.File;

import co.emes.esuelos.util.BasemapComponent;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class MapFragment extends Fragment {

    MapView mMapView = null;
    ArcGISLocalTiledLayer localTiledLayer;

    MenuItem addFormMenuItem;
    MenuItem gpsMenuItem;
    MenuItem identifyMenuItem;

    ImageView imgAddMap;
    ImageView imgAddForm;
    ImageView imgClearMap;

    LocationDisplayManager lDisplayManager;
    BasemapComponent basemapComponent;
    GraphicsLayer graphicsLayer;

    // Spatial references used for projecting points
    final SpatialReference wm = SpatialReference.create(102100);
    final SpatialReference egs = SpatialReference.create(4326);

    private static final int PICKFILE_RESULT_CODE = 1;

    LocationManager manager;
    Point mLocation = null;
    Point tempPoint;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ArcGISRuntime.setClientId("oSPc8ziJY5htmmW2");
        setHasOptionsMenu(true);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.setLocationListener(new MyLocationListener());
        lDisplayManager.start();
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);

        mMapView.setOnLongPressListener(new OnLongPressListener() {
            private static final long serialVersionUID = 1L;
            public boolean onLongPress(final float x, final float y) {
                final Point loc = mMapView.toMapPoint(x, y);
                tempPoint = (Point) GeometryEngine.project(loc, wm, egs);
                graphicsLayer.removeAll();
                graphicsLayer.addGraphic(new Graphic(loc,new  SimpleMarkerSymbol(Color.GREEN, 25, SimpleMarkerSymbol.STYLE.CROSS)));
                showListView();
                return true;
            }
        });

        basemapComponent = new BasemapComponent(mMapView);
        localTiledLayer = basemapComponent.loadLocalTileLayer(Singleton.getInstance().getTpkFile());
        mMapView.setExtent(localTiledLayer.getFullExtent(), 0, false);

        imgAddMap = (ImageView) rootView.findViewById(R.id.imgAddMap);
        imgAddMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addMapAction();
            }
        });

        imgAddForm = (ImageView) rootView.findViewById(R.id.imgAddForm);
        imgAddForm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFormAction();
            }
        });

        imgClearMap = (ImageView) rootView.findViewById(R.id.imgClearMap);
        imgClearMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearMapAction();
            }
        });

        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu,inflater);
        gpsMenuItem = menu.getItem(0);
        addFormMenuItem = menu.getItem(1);
        identifyMenuItem = menu.getItem(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection.
        switch (item.getItemId()) {
            case R.id.item_gps:
                gpsAction();
                return true;
            case R.id.item_add_form:
                addFormAction();
                return true;
            case R.id.item_identify:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addMapAction() {
        try{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/");
            startActivityForResult(intent,PICKFILE_RESULT_CODE);
        }
        catch(ActivityNotFoundException exp){
            Toast.makeText(getActivity(), "No File (Manager / Explorer)etc Found In Your Device", Toast.LENGTH_SHORT).show();
        }
    }

    private void gpsAction() {
        clearMapAction();
        Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
        mMapView.zoomToResolution(p, 20.0);
        Singleton.getInstance().setX(mLocation.getX());
        Singleton.getInstance().setY(mLocation.getY());
    }

    private void addFormAction() {
        showListView();
    }

    private void clearMapAction() {
        graphicsLayer.removeAll();
    }

    private void showListView() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setIcon(R.drawable.logo);
        builderSingle.setTitle("Formularios Disponibles");
        builderSingle.setCancelable(false);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Nota de Campo");
        arrayAdapter.add("Detallada");
        arrayAdapter.add("Comprobación");

        builderSingle.setNegativeButton(
                getResources().getString(R.string.btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearMapAction();
                        dialog.dismiss();
                    }
                });

        builderSingle.setPositiveButton(
                getResources().getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Singleton.getInstance().setX(tempPoint.getX());
                        Singleton.getInstance().setY(tempPoint.getY());
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                new FragmentTesting().show(getFragmentManager(),
                                        FragmentTesting.TAG);
                                break;
                        }
                    }
                });

        builderSingle.setSingleChoiceItems(arrayAdapter, 0, null);
        builderSingle.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    String filePath = data.getData().getPath();
                    File file = new File(filePath);
                    if (Utils.isLocalTiledLayer(file)) {
                        localTiledLayer = basemapComponent.loadLocalTileLayer(filePath, true);
                        mMapView.setExtent(localTiledLayer.getFullExtent(), 0, false);
                    } else if (Utils.isGeodatabase(file)) {
                        basemapComponent.loadGeodatabaseLayer(filePath, true, true);
                    }
                }
                break;
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.msg_gps))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private class MyLocationListener implements LocationListener {

        public MyLocationListener() {
            super();
        }

        /**
         * If location changes, update our current location. If being found for
         * the first time, zoom to our current position with a resolution of 20
         */
        public void onLocationChanged(Location loc) {
            if (loc == null)
                return;
            boolean zoomToMe = (mLocation == null);
            mLocation = new Point(loc.getLongitude(), loc.getLatitude());
            if (zoomToMe) {
                tempPoint = mLocation;
                Singleton.getInstance().setX(mLocation.getX());
                Singleton.getInstance().setY(mLocation.getY());
                Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
                mMapView.zoomToResolution(p, 20.0);
            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getActivity(), "GPS Deshabilitado",
                    Toast.LENGTH_SHORT).show();
            buildAlertMessageNoGps();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getActivity(), "GPS Habilitado",
                    Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

}
