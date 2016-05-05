package co.emes.esuelos.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by csarmiento on 11/04/16.
 */
public class TPKHelper {

    //The Android's default system path of your application database.
    private static String TPK_PATH = "/data/data/co.emes.esuelos/databases/";
    private String tpkName;

    private final Context myContext;
    private String filePath;
    private File file;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public TPKHelper(Context context, File file) {
        this.myContext = context;
        this.file = file;
        this.tpkName = Utils.getFileName(this.file);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createTPK() throws IOException {
        boolean dbExist = checkTPK();
        if(!dbExist){
            try {
                copyTPK();
            } catch (IOException e) {
                throw new Error("Error copying tpk");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkTPK(){
        String filePath = TPK_PATH + this.tpkName;
        File file = new File(filePath);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void copyTPK() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = new FileInputStream(file);
        // Path to the just created empty db
        String outFileName = TPK_PATH + this.tpkName;
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

    public String getFilePath() {
        filePath = TPK_PATH + tpkName;
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
