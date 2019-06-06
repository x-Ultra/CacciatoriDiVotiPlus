package it.iCarrambaDT.cacciatoriDiVoti.fileManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;

import static java.lang.Thread.sleep;

public class FileTask extends AsyncTask<SharedPreferences, String, String> {

    private WeakReference<Activity> reference;
    private InputStream isCoords;
    private String COORD_SEPARATOR = ":";
    private String lastLogKey = "lastLog";
    private String lastVotoKey = "lastVoto";

    public FileTask(Activity activity, InputStream isCoords){
        this.reference = new WeakReference<>(activity);
        this.isCoords = isCoords;
    }

    @Override
    protected String doInBackground(SharedPreferences... sharedPreferences) {

        //verifico SharedPreferences coordinates
        publishProgress(reference.get().getString(R.string.coordsGenerateing));
        if(!isEmpty(sharedPreferences[0]))
            publishProgress(reference.get().getString(R.string.coordsGenerated));

        //apro coords.txt e estraggo info
        SharedPreferences.Editor editorCoords = sharedPreferences[0].edit();
        BufferedReader br = new BufferedReader(new InputStreamReader(isCoords));
        String line;
        String[] parsedTemp;
        String pos;
        String lat_lon;
        while(true){
            try {
                line = br.readLine();
                if(line == null) {
                    editorCoords.apply();
                    br.close();
                    break;
                }
                //riga del tipo P1:lat-lon
                parsedTemp = line.split(COORD_SEPARATOR);
                pos = parsedTemp[0];
                lat_lon = parsedTemp[1];

                editorCoords.putString(pos, lat_lon);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(reference.get(), reference.get().getString(R.string.coordsFileError), Toast.LENGTH_LONG).show();
                try {
                    //invito l'utente a riavviare l'App
                    wait(1000000000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
        //generazione coordinate completata
        publishProgress(reference.get().getString(R.string.coordsGenerated));

        //verifico sharedPreferences lastLogs
        publishProgress(reference.get().getString(R.string.sharedGenerating));
        SharedPreferences.Editor editorLastLogs = sharedPreferences[1].edit();


        if(!isEmpty(sharedPreferences[1])){
            publishProgress(reference.get().getString(R.string.sharedGenerated));
            return null;
        }


        //riempio il file dello sharedPreferences
        editorLastLogs.putString(lastLogKey, "null");

        editorLastLogs.putString(lastVotoKey, "null");

        editorLastLogs.apply();

        publishProgress(reference.get().getString(R.string.sharedGenerated));

        return null;
    }

    private boolean isEmpty(SharedPreferences shared){
        return shared.getAll().isEmpty();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        ((MyTextView)reference.get().findViewById(R.id.tvFileProg)).setText(values[0]);
    }
}
