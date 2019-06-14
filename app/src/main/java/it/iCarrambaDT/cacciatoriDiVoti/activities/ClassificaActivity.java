package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.adapters.ClassificaAdapter;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.PageListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.PageView;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Materia;

public class ClassificaActivity extends AppCompatActivity implements PageListener {

    private DBManager dbManager = DBManager.getInstance(this);
    private RecyclerView rv;
    TextView tvEmpty;
    PageView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Nascondo title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifica);

        //Imposto le immagini della PageView
        pv = findViewById(R.id.pageView);

        pv.setPageListener(this);

        Vector<Integer> images = new Vector<>();

        images.add(R.drawable.common);
        images.add(R.drawable.rare);
        images.add(R.drawable.mythic);
        images.add(R.drawable.epico);
        images.add(R.drawable.legendary);

        pv.setImages(images);

        //Carico i voti
        rv = findViewById(R.id.rvClassifica);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        insertFakeVoti();

        tvEmpty = findViewById(R.id.tvEmptyClassifica);

        dbManager.createDBorCheck();
        Vector<Materia> votiOttenuti = dbManager.getVotiInOrder(1);

        //Se non ce ne sono mostro un messaggio
        if(votiOttenuti.size() != 0)
            tvEmpty.setVisibility(View.GONE);

        //System.out.print("DB SIZE: "+votiOttenuti.size());

        ClassificaAdapter adapter = new ClassificaAdapter(votiOttenuti);
        rv.setAdapter(adapter);

    }

    @Override
    public void onPageChanged(int currPage) {
        Vector<Materia> votiOttenuti = dbManager.getVotiInOrder(currPage+1);

        //Se non ce ne sono mostro un messaggio
        if(votiOttenuti.size() == 0)
            tvEmpty.setVisibility(View.VISIBLE);
        else
            tvEmpty.setVisibility(View.GONE);


        //System.out.print("DB SIZE: "+votiOttenuti.size());

        ClassificaAdapter adapter = new ClassificaAdapter(votiOttenuti);
        rv.setAdapter(adapter);
    }

    private void insertFakeVoti(){

        /*
        Rarità calcolata a mano utilizzando la funzione di rarità
         */

        dbManager.insertVoto("18", "Algebra E Logica","1200", "6","5");
        dbManager.insertVoto("31", "Mobile Programming","921", "6","5"); //:)
        dbManager.insertVoto("30", "Ingegneria Del Software E Progettazione Web","21", "12", "5");
        dbManager.insertVoto("18", "Basi Di Dati","121", "9","5");
        dbManager.insertVoto("24", "Automi E Linguaggi","5021", "6", "5");
        dbManager.insertVoto("28", "aa","3021", "6", "2");
        dbManager.insertVoto("18", "aaaa","1200", "6","1");
        dbManager.insertVoto("31", "aaaaaa","921", "6","4"); //:)
        dbManager.insertVoto("30", "bbb","21", "12", "1");
        dbManager.insertVoto("18", "bbbbbbbbbbbb","121", "9","3");
        dbManager.insertVoto("24", "aaaaaaaaaa","5021", "6", "5");
        dbManager.insertVoto("28", "2342","3021", "6", "5");
    }
}
