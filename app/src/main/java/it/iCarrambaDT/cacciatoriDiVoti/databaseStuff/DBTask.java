package it.iCarrambaDT.cacciatoriDiVoti.databaseStuff;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;

import static java.lang.Thread.sleep;

/*
    Classe usata per creare e popolare il DB
    partendo dal file .csv (InputStream riferente)
 */
//TODO Controllare se qui si può levare InputStream

public class DBTask extends AsyncTask<Void, String, String> {

    private WeakReference<Activity> reference;

    public DBTask(Activity activity){
        this.reference = new WeakReference<>(activity);
    }

    /*
        Crea il DB, se necessario, informa dello stato sulla creazione
        Popola il DB se necessatio, informa dello stato sulla creazione
     */

    @Override
    protected String doInBackground(Void... voids) {

        DBManager dbManager = DBManager.getInstance(reference.get());

        //verifico/creo DB
        publishProgress(reference.get().getString(R.string.dbCreation));
        dbManager.createDBorCheck();
        publishProgress(reference.get().getString(R.string.dbCreated));

        /*
        //niente più popolamento
        //verifico/popolo DB
        publishProgress(reference.get().getString(R.string.dbPopulation));
        try {
            dbManager.populateDBorCheck();
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(reference.get().getString(R.string.csvReadError));
            try {
                //se ho un errore in lettura invito l'utente a riavviare
                wait(1000000000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        publishProgress(reference.get().getString(R.string.dbPopulated));

        */
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        ((MyTextView)(reference.get().findViewById(R.id.tvDbProg))).setText(values[0]);
    }
}
