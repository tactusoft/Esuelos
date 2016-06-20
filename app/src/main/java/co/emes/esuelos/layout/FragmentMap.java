package co.emes.esuelos.layout;

import android.app.AlertDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.emes.esuelos.R;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.emes.esuelos.main.MainActivity;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormNotaCampo;
import co.emes.esuelos.model.FormTodos;
import co.emes.esuelos.util.BasemapComponent;
import co.emes.esuelos.util.DataBaseHelper;
import co.emes.esuelos.util.Singleton;
import co.emes.esuelos.util.Utils;

/**
 * Created by csarmiento on 11/14/16
 */
public class FragmentMap extends Fragment {

    MapView mMapView = null;
    ArcGISLocalTiledLayer localTiledLayer;

    MenuItem addFormMenuItem;
    MenuItem gpsMenuItem;
    MenuItem identifyMenuItem;

    LocationDisplayManager lDisplayManager;
    BasemapComponent basemapComponent;
    GraphicsLayer graphicsLayerAll;
    GraphicsLayer graphicsLayer;

    List<FeatureLayer> featureLayerList;

    final SpatialReference wm = SpatialReference.create(102100);
    final SpatialReference egs = SpatialReference.create(4326);

    LocationManager manager;
    Point mLocation = null;
    Point tempPoint;
    Boolean localMap = false;

    AlertDialog.Builder alertDialog;
    DataBaseHelper dataBaseHelper;

    public FragmentMap() {
        dataBaseHelper = new DataBaseHelper(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Singleton.getInstance().getResearch()==null) {
            setupAlertBuilder(R.string.map_empty_all);
        } else {
            String tpkFilePath = Singleton.getInstance().getResearch().getTpkFilePath();
            if (tpkFilePath== null || tpkFilePath.isEmpty()) {
                setupAlertBuilder(R.string.map_empty_map);
            } else {
                String geoFilePath = Singleton.getInstance().getResearch().getGeoFilePath();
                if (geoFilePath == null || geoFilePath.isEmpty()) {
                    setupAlertBuilder(R.string.map_empty_geo);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ArcGISRuntime.setClientId("oSPc8ziJY5htmmW2");
        setHasOptionsMenu(true);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        graphicsLayer = new GraphicsLayer();
        mMapView.addLayer(graphicsLayer);

        graphicsLayerAll = new GraphicsLayer();
        mMapView.addLayer(graphicsLayerAll);

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.setLocationListener(new MyLocationListener());
        lDisplayManager.start();
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);

        basemapComponent = new BasemapComponent(mMapView);
        if(Singleton.getInstance().getResearch() != null) {
            localTiledLayer = basemapComponent.loadLocalTileLayer(Singleton.getInstance().getResearch().getTpkFilePath());
            mMapView.setExtent(localTiledLayer.getFullExtent(), 0, false);

            featureLayerList = basemapComponent.loadGeodatabaseLayer(Singleton.getInstance().getResearch().getGeoFilePath());
            for (FeatureLayer featureLayer : featureLayerList) {
                mMapView.addLayer(featureLayer);
            }
        } else {
            localTiledLayer = basemapComponent.loadLocalTileLayer(DataBaseHelper.DB_PATH + DataBaseHelper.TPK_NAME);
            mMapView.setExtent(localTiledLayer.getFullExtent(), 0, false);
            localMap = true;
        }

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                int[] graphicIDs = graphicsLayerAll.getGraphicIDs(x, y, 15, 1);
                graphicsLayerAll.clearSelection();
                graphicsLayerAll.setSelectedGraphics(graphicIDs, true);
                if(graphicIDs.length > 0) {
                    String formType = null;
                    String noObservacion = null;
                    String fecha = null;
                    String coords = null;
                    Graphic graphic = graphicsLayerAll.getGraphic(graphicIDs[0]);
                    Map<String, Object> attr = graphic.getAttributes();
                    Long id = (Long)attr.get("id");
                    String type = (String)attr.get("type");
                    switch (type) {
                        case "N":
                            FormNotaCampo formNotaCampo = dataBaseHelper.getFormNotaCampo(id.intValue());
                            formType = "Nota de Campo";
                            noObservacion = formNotaCampo.getNroObservacion();
                            fecha = formNotaCampo.getFechaHora();
                            coords = formNotaCampo.getLongitud() + " " + formNotaCampo.getLatitud();
                            break;
                        case "D":
                            break;
                        case "C":
                            FormComprobacion formComprobacion = dataBaseHelper.getFormComprobacion(id.intValue());
                            formType = "Comprobación";
                            noObservacion = formComprobacion.getNroObservacion();
                            fecha = formComprobacion.getFechaHora();
                            coords = formComprobacion.getLongitud() + " " + formComprobacion.getLatitud();
                            break;
                    }

                    showMapFormDialog(formType, noObservacion, fecha, coords);
                }
            }
        });

        mMapView.setOnLongPressListener(new OnLongPressListener() {
            private static final long serialVersionUID = 1L;
            public boolean onLongPress(final float x, final float y) {
                final Point loc = mMapView.toMapPoint(x, y);
                if(localMap) {
                    tempPoint = loc;
                } else {
                    tempPoint = (Point) GeometryEngine.project(loc, wm, egs);
                }
                graphicsLayer.removeAll();
                graphicsLayer.addGraphic(new Graphic(loc,new  SimpleMarkerSymbol(Color.GREEN, 25, SimpleMarkerSymbol.STYLE.CROSS)));
                showListView(loc);
                if(featureLayerList!=null && !featureLayerList.isEmpty()) {
                    searchSuelos(featureLayerList, loc);
                }
                return true;
            }
        });

        mMapView.setOnTouchListener(new MapOnTouchListener(getActivity(), mMapView){
            @Override
            public boolean onDoubleTap(MotionEvent point) {
                int[] graphicIDs = graphicsLayerAll.getGraphicIDs(point.getX(), point.getY(), 15, 1);
                graphicsLayerAll.clearSelection();
                graphicsLayerAll.setSelectedGraphics(graphicIDs, true);
                if(graphicIDs.length > 0) {
                    Graphic graphic = graphicsLayerAll.getGraphic(graphicIDs[0]);
                    Map<String, Object> attr = graphic.getAttributes();
                    Long id = (Long)attr.get("id");
                    String type = (String)attr.get("type");
                    switch (type) {
                        case "N":
                            showFragmentFieldNote(id.intValue());
                            break;
                        case "D":
                            break;
                        case "C":
                            showFragmentTesting(id.intValue());
                            break;
                    }
                }
                return false;
            }
        });

        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);

        drawPoints();

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

    private void setupAlertBuilder(int message){
        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Seleccionar Mapa");
        alertDialog.setMessage(message);
        alertDialog.setIcon(android.R.drawable.ic_dialog_info);
        alertDialog.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).selectItem(3);
                    }
                });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTransformationMethod(null);
                positiveButton.invalidate();

                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTransformationMethod(null);
                negativeButton.invalidate();
            }
        });

        dialog.show();
    }

    private void gpsAction() {
        clearMapAction();
        Singleton.getInstance().setX(mLocation.getX());
        Singleton.getInstance().setY(mLocation.getY());
        if(!localMap) {
            Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
            mMapView.zoomToResolution(p, 20.0);
        } else {
            mMapView.centerAndZoom(mLocation.getY(), mLocation.getX(), 0.05f);
        }
    }

    public void searchSuelos(List<FeatureLayer> featureLayerList, Point point){
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setReturnGeometry(true);
        queryParameters.setInSpatialReference(mMapView.getSpatialReference());
        queryParameters.setGeometry(point);
        queryParameters.setSpatialRelationship(SpatialRelationship.INTERSECTS);
        featureLayerList.get(4).selectFeatures(queryParameters, FeatureLayer.SelectionMode.NEW, callback);
    }

    private void addFormAction() {
        showListView(null);
    }

    private void clearMapAction() {
        graphicsLayer.removeAll();
    }

    private void showListView(final Point point) {
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
                getResources().getString(R.string.btn_save_form),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Singleton.getInstance().setX(tempPoint.getX());
                        Singleton.getInstance().setY(tempPoint.getY());
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                showFragmentFieldNote();
                                break;
                            case 1:
                                break;
                            case 2:
                                showFragmentTesting();
                                break;
                        }
                    }
                });

        if(point!=null) {
            builderSingle.setNeutralButton(
                    getResources().getString(R.string.btn_save_geomtry),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Singleton.getInstance().setX(tempPoint.getX());
                            Singleton.getInstance().setY(tempPoint.getY());
                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                            String fechaHora = Utils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                            String nroObservacion;
                            Map<String, Object> attributes;
                            Graphic graphic;
                            switch (selectedPosition) {
                                case 0:
                                    nroObservacion = dataBaseHelper.getNroObservacion("N");
                                    FormNotaCampo formNotaCampo = new FormNotaCampo();
                                    formNotaCampo.setLongitud(Singleton.getInstance().getX());
                                    formNotaCampo.setLatitud(Singleton.getInstance().getY());
                                    formNotaCampo.setFechaHora(fechaHora);
                                    formNotaCampo.setAltitud(null);
                                    formNotaCampo.setNroObservacion(nroObservacion);
                                    formNotaCampo.setEstado(1);
                                    Long formNotaCampoId = dataBaseHelper.insertFormNotaCampo(formNotaCampo);
                                    attributes = new HashMap<>();
                                    attributes.put("id",formNotaCampoId);
                                    attributes.put("type","N");
                                    graphic = new Graphic(point, new SimpleMarkerSymbol(Color.RED,25, SimpleMarkerSymbol.STYLE.CIRCLE), attributes);
                                    graphicsLayerAll.addGraphic(graphic);
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    nroObservacion = dataBaseHelper.getNroObservacion("C");
                                    FormComprobacion formNotaComprobacion = new FormComprobacion();
                                    formNotaComprobacion.setLongitud(Singleton.getInstance().getX());
                                    formNotaComprobacion.setLatitud(Singleton.getInstance().getY());
                                    formNotaComprobacion.setFechaHora(fechaHora);
                                    formNotaComprobacion.setAltitud(null);
                                    formNotaComprobacion.setNroObservacion(nroObservacion);
                                    formNotaComprobacion.setEstado(1);
                                    Long formNotaComprobacionId = dataBaseHelper.insertFormComprobacion(formNotaComprobacion);
                                    attributes = new HashMap<>();
                                    attributes.put("id",formNotaComprobacionId);
                                    attributes.put("type","C");
                                    graphic = new Graphic(point, new SimpleMarkerSymbol(Color.RED,25, SimpleMarkerSymbol.STYLE.CIRCLE), attributes);
                                    graphicsLayerAll.addGraphic(graphic);
                                    break;
                            }
                        }
                    });
        }
        builderSingle.setSingleChoiceItems(arrayAdapter, 0, null);

        AlertDialog dialog = builderSingle.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTransformationMethod(null);
                positiveButton.invalidate();

                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTransformationMethod(null);
                negativeButton.invalidate();

                if(point!=null) {
                    Button neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
                    neutralButton.setTransformationMethod(null);
                    neutralButton.invalidate();
                }
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

    public void showFragmentFieldNote() {
        new FragmentFieldNote().show(getFragmentManager(),
                FragmentFieldNote.TAG);
    }

    public void showFragmentTesting() {
        new FragmentTesting().show(getFragmentManager(),
                FragmentTesting.TAG);
    }

    private void showFragmentTesting(Integer id){
        final FormComprobacion formComprobacion = dataBaseHelper.getFormComprobacion(id);
        FragmentTesting fragmentTesting =  FragmentTesting.newInstance(formComprobacion);
        fragmentTesting.show(getFragmentManager(), FragmentTesting.TAG);
    }

    private void showFragmentFieldNote(Integer id){
        final FormNotaCampo formNotaCampo = dataBaseHelper.getFormNotaCampo(id);
        FragmentFieldNote fragmentFieldNote =  FragmentFieldNote.newInstance(formNotaCampo);
        fragmentFieldNote.show(getFragmentManager(), FragmentTesting.TAG);
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
                if(!localMap) {
                    Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
                    mMapView.zoomToResolution(p, 20.0);
                } else {
                    mMapView.centerAndZoom(mLocation.getY(), mLocation.getX(), 0.05f);
                }
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
            for (Object element : fSet) {
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

    private void drawPoints() {
        List<FormTodos> formTodosList =  new LinkedList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        List<FormComprobacion> testingList = dataBaseHelper.getListFormComprobacion();
        for(FormComprobacion form:testingList){
            FormTodos formTodos = new FormTodos();
            formTodos.setId(Long.valueOf(form.getId()));
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Comprobación");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodos.setLongitud(form.getLongitud());
            formTodos.setLatitud(form.getLatitud());
            formTodosList.add(formTodos);
        }

        List<FormNotaCampo> fieldNoteList = dataBaseHelper.getListFormNotaCampo();
        for(FormNotaCampo form:fieldNoteList){
            FormTodos formTodos =  new FormTodos();
            formTodos.setId(Long.valueOf(form.getId()));
            formTodos.setNroObservacion(form.getNroObservacion());
            formTodos.setFechaHora(form.getFechaHora());
            formTodos.setTipo("Nota de Campo");
            formTodos.setEstado(form.getEstado());
            formTodos.setFormulario(form);
            formTodos.setLongitud(form.getLongitud());
            formTodos.setLatitud(form.getLatitud());
            formTodosList.add(formTodos);
        }

        graphicsLayerAll.removeAll();
        Map<String, Object> attributes;
        for(FormTodos form:formTodosList) {
            Point loc = new Point(form.getLongitud().floatValue(), form.getLatitud().floatValue());
            Point newPoint = (Point) GeometryEngine.project(loc, egs, wm);
            attributes = new HashMap<>();
            attributes.put("id",form.getId());
            attributes.put("type",form.getTipo().equals("Comprobación")?"C":"N");
            graphicsLayerAll.addGraphic(new Graphic(newPoint, new SimpleMarkerSymbol(Color.RED,25,
                    SimpleMarkerSymbol.STYLE.CIRCLE), attributes));
        }
    }

    public void showMapFormDialog(String type, String noObservacion, String fecha, String coords) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_map_form, null);
        dialogBuilder.setView(dialogView);

        final TextView inputFormType = (TextView) dialogView.findViewById(R.id.input_form_type);
        final TextView inputNoObservacion = (TextView) dialogView.findViewById(R.id.input_no_observacion);
        final TextView inputFecha = (TextView) dialogView.findViewById(R.id.input_fecha);
        final TextView inputcoords = (TextView) dialogView.findViewById(R.id.input_coords);

        inputFormType.setText(type);
        inputNoObservacion.setText(noObservacion);
        inputFecha.setText(fecha);
        inputcoords.setText(coords);

        dialogBuilder.setTitle(getResources().getString(R.string.app_name));
        dialogBuilder.setPositiveButton(getResources().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTransformationMethod(null);
                positiveButton.invalidate();
            }
        });

        dialog.show();
    }

}
