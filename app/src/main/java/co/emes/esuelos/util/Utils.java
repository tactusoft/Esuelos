package co.emes.esuelos.util;

import com.esri.core.geometry.SpatialReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by csarmiento on 7/04/16.
 */
public class Utils {

    public static final SpatialReference WEB_MERCATOR = SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE);

    private static final String DATABASE_EXTENSION = ".geodatabase";

    private static final String TPK_EXTENSION = ".tpk";

    /**
     * Returns true if a file is a local tiled layer.
     *
     * @param file The file to test.
     * @return true if the file is a local tiled layer.
     */
    public static boolean isLocalTiledLayer(File file) {
        if (file == null || !file.exists() || file.isDirectory())
            return false;
        return file.getName().endsWith(TPK_EXTENSION);

    }

    /**
     * Returns true if a file is a geodatabase.
     *
     * @param file The file to test.
     * @return true if the file is a geodatabase.
     */
    public static boolean isGeodatabase(File file) {

        if (file == null || !file.exists() || file.isDirectory())
            return false;

        return file.getName().endsWith(DATABASE_EXTENSION);
    }

    /**
     * Returns true if a file is a local tiled layer.
     *
     * @param file The file to test.
     * @return true if the file is a local tiled layer.
     */
    public static String getFileName(File file) {
        if (file == null || !file.exists() || file.isDirectory())
            return null;
        return file.getName().substring(file.getName().lastIndexOf("/") + 1);

    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getExtension(String filename) {
        try {
            if (filename == null) {
                return null;
            }
            int extensionPos = filename.lastIndexOf('.');
            int lastUnixPos = filename.lastIndexOf('/');
            int lastWindowsPos = filename.lastIndexOf('\\');
            int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
            int index = lastSeparator > extensionPos ? -1 : extensionPos;
            if (index == -1) {
                return "";
            } else {
                return filename.substring(index + 1);
            }
        }catch(Exception ex){
            return null;
        }
    }

    public static String getEstado(Integer id) {
        String result = null;
        switch (id) {
            case 1:
                result = "ABIERTO";
                break;
            case 2:
                result = "TERMINADO";
                break;
            case 3:
                result = "ANULADO";
                break;
        }
        return result;
    }
}
