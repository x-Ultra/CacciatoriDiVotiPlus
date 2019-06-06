package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
import it.iCarrambaDT.cacciatoriDiVoti.adapters.BookletAdapters;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBHelper;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;

public class BookletActivity extends AppCompatActivity {

    private DBManager dbManager = DBManager.getInstance();
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklet);

        MyTextView tvEmptyBooklet = findViewById(R.id.tvEmptyBooklet);

        rv = findViewById(R.id.rvBooklet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        //Levare esto commmento per vedere qualche voto nel libretto
        insertFakeVoti();

        Vector<Voto> votiOttenuti = dbManager.getVoti();
        if(votiOttenuti.size() != 0)
            tvEmptyBooklet.setVisibility(View.GONE);

        //System.out.print("DB SIZE: "+votiOttenuti.size());

        BookletAdapters adapter = new BookletAdapters(votiOttenuti);
        rv.setAdapter(adapter);
    }

    private void insertFakeVoti(){

        /*
        Rarità calcolata a mano utilizzando la funzione di rarità
         */

        dbManager.updateVoto("18", "Algebra E Logica");
        dbManager.updateVoto("31", "Mobile Programming"); //:)
        dbManager.updateVoto("30", "Ingegneria Del Software E Progettazione Wab");
        dbManager.updateVoto("18", "Basi Di Dati");
        dbManager.updateVoto("24", "Automi E Linguaggi");
        dbManager.updateRarita("1","Algebra E Logica");
        dbManager.updateRarita("4","Mobile Programming"); //:)
        dbManager.updateRarita("5","Ingegneria Del Software E Progettazione Wab");
        dbManager.updateRarita("2","Basi Di Dati");
        dbManager.updateRarita("3","Automi E Linguaggi");
    }
}
