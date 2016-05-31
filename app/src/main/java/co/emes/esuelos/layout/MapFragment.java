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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import co.emes.esuelos.R;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.emes.esuelos.util.BasemapComponent;
import co.emes.esuelos.util.Singleton;

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

        basemapComponent = new BasemapComponent(mMapView);
        localTiledLayer = basemapComponent.loadLocalTileLayer(Singleton.getInstance().getResearch().getTpkFilePath());
        mMapView.setExtent(localTiledLayer.getFullExtent(), 0, false);

        final List<FeatureLayer> featureLayerList =
                basemapComponent.loadGeodatabaseLayer(Singleton.getInstance().getResearch().getGeoFilePath());
        for(FeatureLayer featureLayer:featureLayerList){
            mMapView.addLayer(featureLayer);
        }

        mMapView.setOnLongPressListener(new OnLongPressListener() {
            private static final long serialVersionUID = 1L;
            public boolean onLongPress(final float x, final float y) {
                final Point loc = mMapView.toMapPoint(x, y);
                tempPoint = (Point) GeometryEngine.project(loc, wm, egs);
                graphicsLayer.removeAll();
                graphicsLayer.addGraphic(new Graphic(loc,new  SimpleMarkerSymbol(Color.GREEN, 25, SimpleMarkerSymbol.STYLE.CROSS)));
                showListView();
                searchSuelos(featureLayerList, loc);
                return true;
            }
        });

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

    public void searchSuelos(List<FeatureLayer> featureLayerList, Point point){
        QueryParameters queryParameters = new QueryParameters();
        // optional
        //q.setWhere("PROD_GAS='Yes'");
        queryParameters.setReturnGeometry(true);
        queryParameters.setInSpatialReference(mMapView.getSpatialReference());
        queryParameters.setGeometry(point);
        queryParameters.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        featureLayerList.get(4).selectFeatures(queryParameters, FeatureLayer.SelectionMode.NEW, callback);
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

        AlertDialog dialog = builderSingle.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Window view = ((AlertDialog)dialog).getWindow();
                //view.setBackgroundDrawableResource(R.color.principal_background);
                //view.setTitleColor(getActivity().getResources().getColor(R.color.principal_font));

                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getActivity().getResources().getColor(R.color.second_font));
                positiveButton.setTransformationMethod(null);
                positiveButton.invalidate();

                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getActivity().getResources().getColor(R.color.second_font));
                negativeButton.setTransformationMethod(null);
                negativeButton.invalidate();
            }
        });

        dialog.show();
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

    CallbackListener<FeatureResult> callback = new CallbackListener<FeatureResult>() {
        public void onCallback(FeatureResult fSet) {
            Iterator itr = fSet.iterator();
            while(itr.hasNext()) {
                Object element = itr.next();
                if (element instanceof Feature) {
                    Feature feature = (Feature) element;
                    Map<String, Object> map = feature.getAttributes();
                    String paisaje = (String) map.get("Paisaje");
                    Singleton.getInstance().setPaisaje(paisaje);
                    String simbolo = (String) map.get("Simbolo");
                    Singleton.getInstance().setSimbolo(simbolo);
                    break;
                }
            }
        }

        public void onError(Throwable arg0) {

        }
    };

}
