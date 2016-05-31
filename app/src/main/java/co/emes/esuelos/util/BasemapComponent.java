package co.emes.esuelos.util;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BasemapComponent {

    public enum DataType {
        GEODATABASE,
        LOCAL_TILED_LAYER,
        TILED_SERVICE_LAYER
    }

    private static final Envelope sWorldExtent = new Envelope(-100000000, -100000000, 100000000, 100000000);

    private final GraphicsLayer mDefaultGraphicsLayer = new GraphicsLayer(Utils.WEB_MERCATOR, sWorldExtent);

    private final MapView mMapView;

    private final List<Layer> mActiveLayers = new ArrayList<Layer>();

    /**
     * Constructs a new basemap component.
     *
     * @param mapView The MapView to bind layers to.
     */
    public BasemapComponent(MapView mapView) {
        mMapView = mapView;
        mMapView.setMaxExtent(sWorldExtent);
    }

    /**
     * Loads the default online service (World Street Map) as the active
     * basemap layer. This will remove any other active basemap layers.
     */
    public void loadOnlineLayer(String url) {
        removeActiveLayers();
        ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer(url);
        mMapView.addLayer(tiledLayer);
        mActiveLayers.add(tiledLayer);
    }

    /**
     * Load a "blank" layer (all gray tiles) with a spatial reference of Web Mercator and
     * a full extent of the world.
     */
    public void loadDefaultCanvas(boolean removeLayer) {
        if(removeLayer) {
            removeActiveLayers();
        }
        mMapView.addLayer(mDefaultGraphicsLayer);
        mActiveLayers.add(mDefaultGraphicsLayer);
    }

    /**
     * Load a local tiled layer.
     *
     * @param path The path to the local tiled layer.
     */
    public ArcGISLocalTiledLayer loadLocalTileLayer(String path, boolean removeLayer) {
        if(removeLayer) {
            removeActiveLayers();
        }
        ArcGISLocalTiledLayer baseMap = new ArcGISLocalTiledLayer(path);
        mActiveLayers.add(baseMap);
        mMapView.addLayer(baseMap, mActiveLayers.size());


        return baseMap;
    }

    /**
     * Load a local tiled layer.
     *
     * @param path The path to the local tiled layer.
     */
    public ArcGISLocalTiledLayer loadLocalTileLayer(String path) {
        ArcGISLocalTiledLayer baseMap = new ArcGISLocalTiledLayer(path);
        mMapView.addLayer(baseMap, 0);
        mActiveLayers.add(baseMap);
        return baseMap;
    }

    /**
     * Loads a geodatabase to be used as a basemap.
     *
     * @param filePath The absolute path to the runtime geodatabase.
     */
    public List<FeatureLayer> loadGeodatabaseLayer(String filePath) {
        List<FeatureLayer> result = new LinkedList<>();
        try {
            Geodatabase gdb = new Geodatabase(filePath);
            for (GeodatabaseFeatureTable gdbFeatureTable : gdb.getGeodatabaseTables()) {
                if (gdbFeatureTable.hasGeometry())
                    result.add(new FeatureLayer(gdbFeatureTable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Removes any active basemap layers. This method can
     * be safely called even if there are no active layers.
     */
    public void removeActiveLayers() {
        for (Layer layer : mActiveLayers)
            mMapView.removeLayer(layer);

        mActiveLayers.clear();
    }

    /**
     * Returns the active (top most) basemap layer.
     *
     * @return The topmost basemap layer.
     */
    public Layer getActiveLayer() {
        return mActiveLayers.isEmpty() ? null : mActiveLayers.get(0);
    }
}
