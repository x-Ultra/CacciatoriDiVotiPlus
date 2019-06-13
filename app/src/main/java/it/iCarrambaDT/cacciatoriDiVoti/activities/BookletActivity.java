package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Materia;
import it.iCarrambaDT.cacciatoriDiVoti.adapters.BookletAdapters;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;

public class BookletActivity extends AppCompatActivity {

    private DBManager dbManager = DBManager.getInstance(this);
    private RecyclerView rv;

    private MyTextView tvNome;
    private MyTextView tvCrediti;
    private MyTextView tvEsami;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int crediti = 0, esami = 0;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklet);

        MyTextView tvEmptyBooklet = findViewById(R.id.tvEmptyBooklet);

        tvNome = findViewById(R.id.tvStudentsName);
        tvCrediti = findViewById(R.id.tvCreditsEarned);
        tvEsami = findViewById(R.id.tvPassedExams);

        rv = findViewById(R.id.rvBooklet);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        //Levare esto commmento per vedere qualche voto nel libretto
        insertFakeVoti();

        Vector<Materia> votiOttenuti = dbManager.getVoti();
        esami = votiOttenuti.size();

        tvEsami.setText(Integer.toString(esami));

        for(Materia materia: votiOttenuti)
            crediti += materia.getCredits();

        tvCrediti.setText(Integer.toString(crediti));


        SharedPreferences shared = getSharedPreferences("userData", MODE_PRIVATE);
        SharedManager manager = new SharedManager(shared);

        tvNome.setText(manager.getUserDataInfo(SharedManager.nomeUtente));


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

        dbManager.insertVoto("18", "Algebra E Logica","1221", "6","4");
        dbManager.insertVoto("31", "Mobile Programming","1221", "6","3"); //:)
        dbManager.insertVoto("30", "Ingegneria Del Software E Progettazione Web","1221", "12", "2");
        dbManager.insertVoto("18", "Basi Di Dati","1221", "9","5");
        dbManager.insertVoto("24", "Automi E Linguaggi","1221", "6", "1");
        dbManager.insertVoto("28", "Automi E Linguaggi","1221", "6", "5");
    }
}
