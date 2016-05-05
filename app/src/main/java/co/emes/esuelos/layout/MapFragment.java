package co.emes.esuelos.layout;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import java.io.File;

import co.emes.esuelos.util.BasemapComponent;
import co.emes.esuelos.util.NetworkUtil;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class MapFragment extends Fragment {

    MapView mMapView = null;
    ArcGISLocalTiledLayer localTiledLayer;

    /*
    // The basemap switching menu items.
    private MenuItem mStreetsMenuItem = null;
    private MenuItem mTopoMenuItem = null;
    private MenuItem mGrayMenuItem = null;
    private MenuItem mOceansMenuItem = null;
    private MenuItem mSatelliteMenuItem = null;

    // Create MapOptions for each type of basemap.
    private final MapOptions mTopoBasemap = new MapOptions(MapOptions.MapType.TOPO);
    private final MapOptions mStreetsBasemap = new MapOptions(MapOptions.MapType.STREETS);
    private final MapOptions mGrayBasemap = new MapOptions(MapOptions.MapType.GRAY);
    private final MapOptions mOceansBasemap = new MapOptions(MapOptions.MapType.OCEANS);
    private final MapOptions mSatelliteBasemap = new MapOptions(MapOptions.MapType.SATELLITE);*/

    private ImageView imgAddMap;
    private ImageView imgAddForm;
    private ImageView imgClearMap;

    private LocationDisplayManager lDisplayManager;
    private BasemapComponent basemapComponent;
    private GraphicsLayer graphicsLayer;

    // Spatial references used for projecting points
    final SpatialReference wm = SpatialReference.create(102100);
    final SpatialReference egs = SpatialReference.create(4326);

    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int DIALOG_FRAGMENT = 1;

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

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.INITIALIZED) {
                    // When the map is initialized, start the LocationDisplayManager.
                    lDisplayManager.setLocationListener(mLocationListener);
                    lDisplayManager.start();
                }
            }
        });
        mMapView.setAllowRotationByPinch(true);

        /**
         * On long pressing the map view, route from our current location to the
         * pressed location.
         *
         */
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            private static final long serialVersionUID = 1L;
            public boolean onLongPress(final float x, final float y) {
                final Point loc = mMapView.toMapPoint(x, y);
                //Point point = (Point) GeometryEngine.project(loc, wm, egs);
                graphicsLayer.removeAll();
                graphicsLayer.addGraphic(new Graphic(loc,new  SimpleMarkerSymbol(Color.GREEN, 25, SimpleMarkerSymbol.STYLE.CROSS)));
                Singleton.getInstance().setX(x);
                Singleton.getInstance().setY(y);
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

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu,inflater);

        // Get the basemap switching menu items.
        mStreetsMenuItem = menu.getItem(0);
        mTopoMenuItem = menu.getItem(1);
        mGrayMenuItem = menu.getItem(2);
        mOceansMenuItem = menu.getItem(3);
        mSatelliteMenuItem = menu.getItem(4);

        // Also set the topo basemap menu item to be checked, as this is the default.
        mTopoMenuItem.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item selection.
        switch (item.getItemId()) {
            case R.id.World_Street_Map:
                mMapView.setMapOptions(mStreetsBasemap);
                mStreetsMenuItem.setChecked(true);
                return true;
            case R.id.World_Topo:
                mMapView.setMapOptions(mTopoBasemap);
                mTopoMenuItem.setChecked(true);
                return true;
            case R.id.Gray:
                mMapView.setMapOptions(mGrayBasemap);
                mGrayMenuItem.setChecked(true);
                return true;
            case R.id.Ocean_Basemap:
                mMapView.setMapOptions(mOceansBasemap);
                mOceansMenuItem.setChecked(true);
                return true;
            case R.id.Satellite_Basemap:
                mMapView.setMapOptions(mSatelliteBasemap);
                mSatelliteMenuItem.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    final LocationListener mLocationListener; {
        mLocationListener = new LocationListener() {
            boolean locationChanged = false;

            @Override
            public void onLocationChanged(Location location) {
                if (!locationChanged) {
                    // For first fix location, convert to map spatial reference.
                    Point currentPt = new Point(location.getLongitude(), location.getLatitude());
                    if (NetworkUtil.getConnectivityStatus(getActivity()) == 0) {
                        mMapView.zoomTo(currentPt, 60.0f);
                    } else {
                        Point currentMapPt = (Point) GeometryEngine.project(currentPt,
                                egs, mMapView.getSpatialReference());

                        // Use a suitable accuracy value for the typical app usage, if no accuracy
                        // value is available.
                        float accuracy = 100;
                        if (location.hasAccuracy()) {
                            accuracy = location.getAccuracy();
                        }

                        // Convert the accuracy to units of the map, and apply a suitable zoom
                        // factor for the app.
                        Unit mapUnits = mMapView.getSpatialReference().getUnit();
                        double zoomToWidth = 500 * Unit.convertUnits(accuracy,
                                Unit.create(LinearUnit.Code.METER), mapUnits);
                        Envelope zoomExtent = new Envelope(currentMapPt, zoomToWidth, zoomToWidth);

                        // Make sure that the initial zoom is done WITHOUT animation, or it may
                        // interfere with autopan.
                        mMapView.setExtent(zoomExtent, 0, false);
                    }

                    // Dont run this again.
                    locationChanged = true;

                    // Now start the navigation mode.
                    lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.NAVIGATION);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
    }

    void initializeLayerOnStatusChangedListener(String filePath) {
        localTiledLayer = new ArcGISLocalTiledLayer(filePath);
        mMapView.addLayer(localTiledLayer, 0);

        /*
         * Use OnStatusChangedListener to listen to
         * events such as change of status. This event allows developers to
         * check if layer is indeed initialized and ready for use, and take
         * appropriate action.
         */
        OnStatusChangedListener onStatusChangedListener = new OnStatusChangedListener() {
            private static final long serialVersionUID = 1L;
            /*
             * This callback method will be invoked when status of layer changes
             */
            public void onStatusChanged(Object arg0, STATUS status) {
                /*
                 * Check if layer's new status = INITIALIZED. If it is,
                 * set the extent.
                 */
                if (status.equals(OnStatusChangedListener.STATUS.INITIALIZED)) {
                    //mMapView.setExtent(localTiledLayer.getExtent());
                    lDisplayManager.setLocationListener(mLocationListener);
                    lDisplayManager.start();
                }
            }
        };
        localTiledLayer.setOnStatusChangedListener(onStatusChangedListener);
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

    private void addFormAction() {

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
                        dialog.dismiss();
                    }
                });

        builderSingle.setPositiveButton(
                getResources().getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                new FragmentFieldNote().show(getFragmentManager(),
                                        FragmentFieldNote.TAG);
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

}
