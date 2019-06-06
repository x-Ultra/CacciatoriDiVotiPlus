package it.iCarrambaDT.cacciatoriDiVoti.helpers;

import android.content.Context;
import android.os.AsyncTask;

import it.iCarrambaDT.cacciatoriDiVoti.controller.MainController;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class VotoAsyncTask extends AsyncTask<Context, Void, Voto>{

    private VotoListener vl;

    public void setListener(VotoListener vl){
        this.vl = vl;
    }

    @Override
    protected Voto doInBackground(Context... contexts) {
        //get voto
        MainController mc = new MainController(contexts[0]);
        System.out.println("operazione");
        Voto v = mc.returnVoto();
        System.out.println("operazione finita");
        return v;
    }

    @Override
    protected void onPostExecute(Voto voto) {
        System.out.println("finito");
        vl.onTaskFinished(voto);
        super.onPostExecute(voto);
    }
}
