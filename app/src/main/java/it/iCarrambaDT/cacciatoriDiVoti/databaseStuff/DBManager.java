package it.iCarrambaDT.cacciatoriDiVoti.databaseStuff;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class DBManager {

    private String SEPARATOR = ",";
    private static DBManager instance = null;
    private DBHelper helper;

    private BufferedReader br;
    private Context context;

    /*
        L'activity chiamante mediamnte l'instanziazione di DBManager avrà
        l possibilità di far partire la popolazione e la creazione, ed esecuzione di query ad-hoc.

        DBManager avrà un istanza di DBHelper.

        Gestisce apertura dei file .csv per l'estrazione dei dati necessari alla creazione
        e al popolamento, quindi vedere se dare al costruttore un InputStream

     */

    private DBManager(Context context, InputStream is){
        this.br = new BufferedReader(new InputStreamReader(is));
        this.context = context;
    }

    public Vector<Voto> getVoti(){
        return helper.getObtainedVoti();
    }

    public static DBManager getInstance(Context context, InputStream is) {

        if(instance == null){
            instance = new DBManager(context, is);
        }

        return instance;
    }

    //chiamato da activities diverse da quella iniziale,
    //l'iniziale va a riempire il campo COL_ATTR
    public static DBManager getInstance() {
        return instance;
    }

    public void createDBorCheck() throws IOException {
        String COL_ATTRS;
        COL_ATTRS = br.readLine();
        helper = new DBHelper(context, COL_ATTRS.split(SEPARATOR));
        helper.onCreate(helper.getWritableDatabase());
    }

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

    public void updateVoto(String voto, String materia){
        helper.updateVoto(voto, materia);
    }

    public void updateRarita(String rarita, String materia){
        helper.updateRarita(rarita, materia);
    }

    public Voto getVoto(String materia){
        return helper.getVoto(materia);
    }


    public Voto getVotoRarLessEqual(int num){
        return helper.getVotoRarLessEqual(num);
    }

}
