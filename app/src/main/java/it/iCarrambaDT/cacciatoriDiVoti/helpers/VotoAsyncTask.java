package it.iCarrambaDT.cacciatoriDiVoti.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.controller.MainController;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
import it.iCarrambaDT.cacciatoriDiVoti.serverInteraction.ServerCaller;

//TODO non so che ci fai con il post execute, lascio Voto, e' da modificare ?
public class VotoAsyncTask extends AsyncTask<Context, Void, Voto>{

    private String laurea;
    private VotoListener vl;

    //nuovo costruttore, questo e' quello che dovra' essere chiamato
    public void VotoAsyncTask(String laurea){
        this.laurea = laurea;
    }

    public void setListener(VotoListener vl){
        this.vl = vl;
    }

    @Override
    protected MateriaPlus doInBackground(Context... contexts) {

        Context context = contexts[0];

        MateriaPlus materiaPlus = null;

        System.out.println("operazione");
        try {
            materiaPlus = ServerCaller.getInstance().getVotoFromServer(laurea);
        }catch (IOException e){
            String errorMessage = context.getString(R.string.serverError);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }

        System.out.println("operazione finita");
        return materiaPlus;
    }

    @Override
    protected void onPostExecute(Voto voto) {
        System.out.println("finito");
        vl.onTaskFinished(voto);
        super.onPostExecute(voto);
    }
}
