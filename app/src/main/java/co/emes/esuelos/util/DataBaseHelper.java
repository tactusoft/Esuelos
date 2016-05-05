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
import java.util.LinkedList;
import java.util.List;

import co.emes.esuelos.model.Domain;
import co.emes.esuelos.model.FormComprobacion;
import co.emes.esuelos.model.FormComprobacionFoto;
import co.emes.esuelos.model.Research;

/**
 * Created by csarmiento on 11/04/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android"s default system path of your application database.
    private static String DB_PATH = "/data/data/co.emes.esuelos/databases/";
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
        if(dbExist){
            //do nothing - database already exist
        }else{
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
        return checkDB != null ? true : false;
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
            if (cur.moveToNext()) {
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

    public List<Domain> getListDomain(String grupo, boolean selected) {
        List<Domain> list = new LinkedList<Domain>();
        if(selected) {
            Domain row = new Domain();
            row.setId(null);
            row.setDescripcion("-- Seleccione --");
            list.add(row);
        }
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("select id, codigo, valor, cod_ministerio, descripcion, periodo, orden, grupo " +
                    "from domain where grupo = ? order by orden", new String[] { grupo.toUpperCase() });
            while (cur.moveToNext()) {
                Domain row = new Domain();
                row.setId(cur.getInt(0));
                row.setCodigo(cur.getString(1));
                row.setValor(cur.getString(2));
                row.setCodMinisterio(cur.getString(3));
                row.setDescripcion(cur.getString(4));
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
        return getListDomain(grupo, true);
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
            id = myDataBase.insert("form_comprobacion", null, row);
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }
}
