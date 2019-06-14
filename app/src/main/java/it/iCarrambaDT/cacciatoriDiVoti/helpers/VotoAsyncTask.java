package it.iCarrambaDT.cacciatoriDiVoti.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.serverInteraction.ServerCaller;

public class VotoAsyncTask extends AsyncTask<Context, Void, MateriaPlus>{

    private String laurea;
    private VotoListener vl;
    private MateriaPlus materiaPlus;

    //nuovo costruttore, questo e' quello che dovra' essere chiamato
    public VotoAsyncTask(String laurea){
        this.laurea = laurea;
    }

    //usato solo per test
    public VotoAsyncTask(){
        this.laurea = "ing_info";
    }

    public void setListener(VotoListener vl){
        this.vl = vl;
    }

    @Override
    protected MateriaPlus doInBackground(Context... contexts) {


        Context context = contexts[0];
        try {
            materiaPlus = ServerCaller.getInstance().getVotoFromServer(laurea);
        }catch (IOException e){
            String errorMessage = context.getString(R.string.serverError);
            //Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }

        //System.out.println("MATERIA OTTENUTA:\n\n\n"+materiaPlus.toString()+"\n\n\n");
        return materiaPlus;
    }

    @Override
    protected void onPostExecute(MateriaPlus materia) {
        System.out.println("finito");
        vl.onTaskFinished(materia);
        super.onPostExecute(materia);
    }
}
