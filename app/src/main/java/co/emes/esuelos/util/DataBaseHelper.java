package co.emes.esuelos.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.model.Domain;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormComprobacionFoto;
import co.emes.esuelos.model.FormComprobacionHorizonte;
import co.emes.esuelos.model.FormComprobacionHorizonteOpt;
import co.emes.esuelos.model.Research;

/**
 * Created by csarmiento on 11/04/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android"s default system path of your application database.
    private static String DB_PATH =  "/data/data/co.emes.esuelos/databases/";
    private static String DB_NAME = "esuelosdb.sqlite";

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn"t
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does"t exist yet.
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it"d be easy
    // to you to create adapters for your views.

    /**
     * Return values for a single row with the specified id
     * @param id The unique id for the row o fetch
     * @return All column values are stored as properties in the ContentValues object
     */
    public Research getResearch(long id) {
        Research row = new Research();
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("select id, start_date, end_date, status, user, file_path from research where status = ?", new String[] { String.valueOf(id) });
            if (cur.moveToLast()) {
                row.setId(cur.getInt(0));
                row.setStartDate(cur.getString(1));
                row.setEndDate(cur.getString(2));
                row.setStatus(cur.getInt(3));
                row.setUser(cur.getString(4));
                row.setFilePath(cur.getString(5));
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return row;
    }

    public List<Domain> getListDomain(String grupo, boolean selected, boolean showCode) {
        List<Domain> list = new LinkedList<>();
        if(selected) {
            Domain row = new Domain();
            row.setId(null);
            row.setDescripcion("-- Seleccione --");
            list.add(row);
        }
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("select id, codigo, valor, cod_ministerio, descripcion, periodo, orden, grupo " +
                    "from dominio where grupo = ? order by orden", new String[] { grupo.toUpperCase() });
            while (cur.moveToNext()) {
                Domain row = new Domain();
                row.setId(cur.getInt(0));
                row.setCodigo(cur.getString(1));
                row.setValor(cur.getString(2));
                row.setCodMinisterio(cur.getString(3));
                row.setDescripcion(showCode?row.getCodigo():cur.getString(4));
                row.setPeriodo(cur.getString(5));
                row.setOrden(cur.getInt(6));
                row.setGrupo(cur.getString(7));
                list.add(row);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public List<Domain> getListDomain(String grupo) {
        return getListDomain(grupo, true, false);
    }

    public List<Domain> getListDomainShowCode(String grupo) {
        return getListDomain(grupo, true, true);
    }

    public boolean getOpenResearch() {
        boolean result = false;
        Research research = getResearch(1);
        if(research.getStatus()!=null) {
            if (research.getStatus() == 1) {
                Singleton.getInstance().setTpkFile(research.getFilePath());
                result = true;
            }
        }
        return result;
    }

    public Long insertResearch(Research research) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("start_date", research.getStartDate());
            row.put("end_date", research.getEndDate());
            row.put("user", research.getUser());
            row.put("status", research.getStatus());
            row.put("file_path", research.getFilePath());
            id = myDataBase.insert("research", null, row);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public Long insertFormComprobacion(FormComprobacion entity) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("id", entity.getId());
            row.put("nro_observacion", entity.getNroObservacion());
            row.put("reconocedor", entity.getReconocedor());
            row.put("fecha_hora", entity.getFechaHora());
            row.put("longitud", entity.getLongitud());
            row.put("latitud", entity.getLatitud());
            row.put("altitud", entity.getAltitud());
            row.put("nombre_sitio", entity.getNombreSitio());
            row.put("epoca_climatica", entity.getEpocaClimatica());
            row.put("dias_lluvia", entity.getDiasLluvia());
            row.put("pendiente_longitud", entity.getPendienteLongitud());
            row.put("grado_erosion", entity.getGradoErosion());
            row.put("tipo_movimiento", entity.getTipoMovimiento());
            row.put("anegamiento", entity.getAnegamiento());
            row.put("frecuencia", entity.getFrecuencia());
            row.put("duracion", entity.getDuracion());
            row.put("pedregosidad", entity.getPedregosidad());
            row.put("afloramiento", entity.getAfloramiento());
            row.put("fragmento_suelo", entity.getFragmentoSuelo());
            row.put("drenaje_natural", entity.getDrenajeNatural());
            row.put("profundidad_efectiva", entity.getProfundidadEfectiva());
            row.put("epidedones", entity.getEpidedones());
            row.put("endopedones", entity.getEndopedones());
            row.put("estado", entity.getEstado());
            id = myDataBase.insert("form_comprobacion", null, row);
            if(id == -1) {
                myDataBase.update("form_comprobacion", row,
                        "nro_observacion = ?", new String[] { entity.getNroObservacion() });
                id = Long.valueOf(entity.getId());
            } else {
                entity.setId(id.intValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public Long insertFormComprobacionFoto(FormComprobacionFoto entity) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("id", entity.getId());
            row.put("id_form_comprobacion", entity.getIdFormComprobacion());
            row.put("foto", entity.getFoto());
            id = myDataBase.insert("form_comprobacion_foto", null, row);
            if(id == -1) {
                myDataBase.update("form_comprobacion_foto", row,
                        "id = " + entity.getId(), null);
                id = Long.valueOf(entity.getId());
            } else {
                entity.setId(id.intValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public Long insertFormComprobacionHorizonte(FormComprobacionHorizonte entity) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("id", entity.getId());
            row.put("id_form_comprobacion", entity.getIdFormComprobacion());
            row.put("numero_horizonte", entity.getNumeroHorizonte());
            row.put("profundidad", entity.getProfundidad());
            row.put("color_hue", entity.getColorHue());
            row.put("color_value", entity.getColorValue());
            row.put("color_chroma", entity.getColorChroma());
            row.put("color_porcentaje", entity.getColorPorcentaje());
            row.put("tipo_material", entity.getTipoMaterial());
            row.put("clase_textural", entity.getClaseTextural());
            row.put("modificador_textura", entity.getModificadorTextura());
            row.put("clase_organico", entity.getClaseOrganico());
            row.put("clase_composicion", entity.getClaseComposicion());
            row.put("textura_porcentaje", entity.getTexturaPorcentaje());
            row.put("estructura_tipo", entity.getEstructuraTipo());
            row.put("estructura_clase", entity.getEstructuraClase());
            row.put("estructura_grado", entity.getEstructuraGrado());
            row.put("forma_rompe", entity.getFormaRompe());
            row.put("motivo_no_estructura", entity.getMotivoNoEstructura());
            row.put("horizonte_clase", entity.getHorizonteClase());
            row.put("horizonte_caracterisitica", entity.getHorizonteCaracterisitica());
            row.put("textura_otro", entity.getTexturaOtro());
            row.put("estructura_otra", entity.getEstructuraOtra());
            id = myDataBase.insert("form_comprobacion_horizonte", null, row);
            if(id == -1) {
                myDataBase.update("form_comprobacion_horizonte", row, "id = " + entity.getId(), null);
                id = Long.valueOf(entity.getId());
            } else {
                entity.setId(id.intValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public Long insertFormComprobacionHorizonteOptional(FormComprobacionHorizonteOpt entity) {
        return insertFormComprobacionHorizonteOpt("form_comprobacion_opcional", entity);
    }

    public Long insertFormComprobacionHorizonteFlecked(FormComprobacionHorizonteOpt entity) {
        return insertFormComprobacionHorizonteOpt("form_comprobacion_moteado", entity);
    }

    private Long insertFormComprobacionHorizonteOpt(String table, FormComprobacionHorizonteOpt entity) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("id", entity.getId());
            row.put("id_form_compr_horiz", entity.getIdFormComprobacionHorz());
            row.put("numero_horizonte", entity.getNumeroHorizonte());
            row.put("profundidad", entity.getProfundidad());
            row.put("color_hue", entity.getColorHue());
            row.put("color_value", entity.getColorValue());
            row.put("color_chroma", entity.getColorChroma());
            row.put("color_porcentaje", entity.getColorPorcentaje());
            row.put("tipo_material", entity.getTipoMaterial());
            row.put("clase_textural", entity.getClaseTextural());
            row.put("modificador_textura", entity.getModificadorTextura());
            row.put("clase_organico", entity.getClaseOrganico());
            row.put("clase_composicion", entity.getClaseComposicion());
            row.put("textura_porcentaje", entity.getTexturaPorcentaje());
            row.put("estructura_tipo", entity.getEstructuraTipo());
            row.put("estructura_clase", entity.getEstructuraClase());
            row.put("estructura_grado", entity.getEstructuraGrado());
            row.put("forma_rompe", entity.getFormaRompe());
            row.put("motivo_no_estructura", entity.getMotivoNoEstructura());
            row.put("horizonte_clase", entity.getHorizonteClase());
            row.put("horizonte_caracterisitica", entity.getHorizonteCaracterisitica());
            row.put("textura_otro", entity.getTexturaOtro());
            row.put("estructura_otra", entity.getEstructuraOtra());
            id = myDataBase.insert(table, null, row);
            if(id == -1) {
                myDataBase.update(table, row, "id = " + entity.getId(), null);
            } else {
                entity.setId(id.intValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public List<FormComprobacion> getListFormComprobacion() {
        List<FormComprobacion> list = new LinkedList<>();
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id, nro_observacion, reconocedor, fecha_hora, longitud, latitud, altitud,\n" +
                    "nombre_sitio, epoca_climatica, dias_lluvia, pendiente_longitud, grado_erosion, tipo_movimiento, anegamiento,\n" +
                    "frecuencia, duracion, pedregosidad, afloramiento, fragmento_suelo, drenaje_natural, profundidad_efectiva,\n" +
                    "epidedones, endopedones, estado\n" +
                    "FROM form_comprobacion\n" +
                    "ORDER BY id", new String[] {});
            while (cur.moveToNext()) {
                FormComprobacion row = new FormComprobacion();
                row.setId(cur.getInt(0));
                row.setNroObservacion(cur.getString(1));
                row.setReconocedor(cur.getInt(2));
                row.setFechaHora(cur.getString(3));
                row.setLongitud(cur.getDouble(4));
                row.setLatitud(cur.getDouble(5));
                row.setAltitud(cur.getDouble(6));
                row.setNombreSitio(cur.getString(7));
                row.setEpocaClimatica(cur.getInt(8));
                row.setDiasLluvia(cur.getString(9));
                row.setPendienteLongitud(cur.getInt(10));
                row.setGradoErosion(cur.getInt(11));
                row.setTipoMovimiento(cur.getInt(12));
                row.setAnegamiento(cur.getInt(13));
                row.setFrecuencia(cur.getInt(14));
                row.setDuracion(cur.getInt(15));
                row.setPedregosidad(cur.getInt(16));
                row.setAfloramiento(cur.getInt(17));
                row.setFragmentoSuelo(cur.getInt(18));
                row.setDrenajeNatural(cur.getInt(19));
                row.setProfundidadEfectiva(cur.getInt(20));
                row.setEpidedones(cur.getInt(21));
                row.setEndopedones(cur.getInt(21));
                row.setEstado(cur.getInt(23));
                list.add(row);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public FormComprobacionFoto getFormComprobacionFoto(Integer idFormComprobacion) {
        FormComprobacionFoto result = new FormComprobacionFoto();
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id, id_form_comprobacion, foto "+
                    "FROM form_comprobacion_foto WHERE id_form_comprobacion = ?",
                    new String[]{String.valueOf(idFormComprobacion)});
            if(cur.moveToNext()){
                result.setId(cur.getInt(0));
                result.setIdFormComprobacion(cur.getInt(1));
                result.setFoto(cur.getString(2));
            }
        }catch(SQLException e) {
            e.printStackTrace();;
        } finally {
            close();
        }

        return result;
    }

    public List<FormComprobacionHorizonte> getListFormComprobacionHorizonte(Integer idFormComprobacion) {
        List<FormComprobacionHorizonte> list = new LinkedList<>();
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id,\n" +
                    "id_form_comprobacion,\n" +
                    "numero_horizonte,\n" +
                    "profundidad,\n" +
                    "color_hue,\n" +
                    "color_value,\n" +
                    "color_chroma,\n" +
                    "color_porcentaje,\n" +
                    "tipo_material,\n" +
                    "clase_textural,\n" +
                    "modificador_textura,\n" +
                    "clase_organico,\n" +
                    "clase_composicion,\n" +
                    "textura_porcentaje,\n" +
                    "estructura_tipo,\n" +
                    "estructura_clase,\n" +
                    "estructura_grado,\n" +
                    "forma_rompe,\n" +
                    "motivo_no_estructura,\n" +
                    "horizonte_clase,\n" +
                    "horizonte_caracterisitica,\n" +
                    "textura_otro,\n" +
                    "estructura_otra " +
                    "FROM form_comprobacion_horizonte\n" +
                    "WHERE id_form_comprobacion = ?\n" +
                    "ORDER BY numero_horizonte", new String[] {idFormComprobacion.toString()});
            while (cur.moveToNext()) {
                FormComprobacionHorizonte row = new FormComprobacionHorizonte();
                row.setId(cur.getInt(0));
                row.setIdFormComprobacion(cur.getInt(1));
                row.setNumeroHorizonte(cur.getInt(2));
                row.setProfundidad(cur.getInt(3));
                row.setColorHue(cur.getInt(4));
                row.setColorValue(cur.getInt(5));
                row.setColorChroma(cur.getInt(6));
                row.setColorPorcentaje(cur.getInt(7));
                row.setTipoMaterial(cur.getInt(8));
                row.setClaseTextural(cur.getInt(9));
                row.setModificadorTextura(cur.getInt(10));
                row.setClaseOrganico(cur.getInt(11));
                row.setClaseComposicion(cur.getInt(12));
                row.setTexturaPorcentaje(cur.getInt(13));
                row.setEstructuraTipo(cur.getInt(14));
                row.setEstructuraClase(cur.getInt(15));
                row.setEstructuraGrado(cur.getInt(16));
                row.setFormaRompe(cur.getInt(17));
                row.setMotivoNoEstructura(cur.getInt(18));
                row.setHorizonteClase(cur.getInt(19));
                row.setHorizonteCaracterisitica(cur.getInt(20));
                row.setTexturaOtro(cur.getString(21));
                row.setEstructuraOtra(cur.getString(22));
                list.add(row);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public List<FormComprobacionHorizonteOpt> getListFormComprobacionHorizonteOptional(Integer idFormComprobacionHorz) {
        return getListFormComprobacionHorizonteOpt("form_comprobacion_opcional", idFormComprobacionHorz);
    }

    public List<FormComprobacionHorizonteOpt> getListFormComprobacionHorizonteFlecked(Integer idFormComprobacionHorz) {
        return getListFormComprobacionHorizonteOpt("form_comprobacion_moteado", idFormComprobacionHorz);
    }

    private List<FormComprobacionHorizonteOpt> getListFormComprobacionHorizonteOpt(String table, Integer idFormComprobacion) {
        List<FormComprobacionHorizonteOpt> list = new LinkedList<>();
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id,\n" +
                    "id_form_compr_horiz,\n" +
                    "numero_horizonte,\n" +
                    "profundidad,\n" +
                    "color_hue,\n" +
                    "color_value,\n" +
                    "color_chroma,\n" +
                    "color_porcentaje,\n" +
                    "tipo_material,\n" +
                    "clase_textural,\n" +
                    "modificador_textura,\n" +
                    "clase_organico,\n" +
                    "clase_composicion,\n" +
                    "textura_porcentaje,\n" +
                    "estructura_tipo,\n" +
                    "estructura_clase,\n" +
                    "estructura_grado,\n" +
                    "forma_rompe,\n" +
                    "motivo_no_estructura,\n" +
                    "horizonte_clase,\n" +
                    "horizonte_caracterisitica,\n" +
                    "textura_otro,\n" +
                    "estructura_otra " +
                    "FROM " + table + "\n" +
                    "WHERE id_form_compr_horiz = ?\n" +
                    "ORDER BY numero_horizonte", new String[] {idFormComprobacion.toString()});
            while (cur.moveToNext()) {
                FormComprobacionHorizonteOpt row = new FormComprobacionHorizonteOpt();
                row.setId(cur.getInt(0));
                row.setIdFormComprobacionHorz(cur.getInt(1));
                row.setNumeroHorizonte(cur.getInt(2));
                row.setProfundidad(cur.getInt(3));
                row.setColorHue(cur.getInt(4));
                row.setColorValue(cur.getInt(5));
                row.setColorChroma(cur.getInt(6));
                row.setColorPorcentaje(cur.getInt(7));
                row.setTipoMaterial(cur.getInt(8));
                row.setClaseTextural(cur.getInt(9));
                row.setModificadorTextura(cur.getInt(10));
                row.setClaseOrganico(cur.getInt(11));
                row.setClaseComposicion(cur.getInt(12));
                row.setTexturaPorcentaje(cur.getInt(13));
                row.setEstructuraTipo(cur.getInt(14));
                row.setEstructuraClase(cur.getInt(15));
                row.setEstructuraGrado(cur.getInt(16));
                row.setFormaRompe(cur.getInt(17));
                row.setMotivoNoEstructura(cur.getInt(18));
                row.setHorizonteClase(cur.getInt(19));
                row.setHorizonteCaracterisitica(cur.getInt(20));
                row.setTexturaOtro(cur.getString(21));
                row.setEstructuraOtra(cur.getString(22));
                list.add(row);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public String getNroObservacion(String type) {
        String result = null;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT MAX(id)+1 FROM form_comprobacion", new String[] {});
            if (cur.moveToNext()) {
                int maxId = (cur.getInt(0) == 0)?1:cur.getInt(0);
                String date = Utils.dateToString(new Date(), "yyyyMMdd");
                result =  type + "-" + Singleton.getInstance().getAndroidId().toUpperCase()  + "-" +  date + maxId;
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

}
