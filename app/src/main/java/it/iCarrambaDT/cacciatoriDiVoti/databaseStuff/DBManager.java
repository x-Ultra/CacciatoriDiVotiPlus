package it.iCarrambaDT.cacciatoriDiVoti.databaseStuff;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class DBManager {

    private static DBManager instance = null;
    private DBHelper helper;

    private Context context;

    /*
        L'activity chiamante mediamnte l'instanziazione di DBManager avrà
        l possibilità di far partire la popolazione e la creazione, ed esecuzione di query ad-hoc.

        DBManager avrà un istanza di DBHelper.

        Gestisce apertura dei file .csv per l'estrazione dei dati necessari alla creazione
        e al popolamento, quindi vedere se dare al costruttore un InputStream

     */

    private DBManager(Context context){
        this.context = context;
    }

    public Vector<Voto> getVoti(){
        return helper.getObtainedVoti();
    }

    public static DBManager getInstance(Context context) {

        if(instance == null){
            instance = new DBManager(context);
        }

        return instance;
    }

    //chiamato da activities diverse da quella iniziale,
    //l'iniziale va a riempire il campo COL_ATTR
    public static DBManager getInstance() {
        return instance;
    }

    public void createDBorCheck() throws IOException {
        helper = new DBHelper(context);
        helper.onCreate(helper.getWritableDatabase());
    }

    //Non dovrebbe servire più
    /*
    public void populateDBorCheck() throws IOException{

        //se il DB è vuoto, lo popolo
        if(!helper.isEmpty())
            return;

        String line;
        while(true){
            line = br.readLine();
            if(line == null)
                break;
            helper.populate(helper.getWritableDatabase(), line.split(SEPARATOR));
        }
    }
    */

    public void updateVoto(String voto, String materia,String tempoCattura,String rarita){
        helper.updateVoto(voto, materia, tempoCattura,rarita);
    }

    public void insertVoto(String voto, String materia, String tempoCattura, String crediti, String rarita) {
        helper.insertNewVoto(voto,materia,tempoCattura,crediti,rarita);
    }

    public Voto getVoto(String materia){
        return helper.getVoto(materia);
    }


}
