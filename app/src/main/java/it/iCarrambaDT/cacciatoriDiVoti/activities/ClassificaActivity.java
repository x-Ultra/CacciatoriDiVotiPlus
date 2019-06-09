package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.PageListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.PageView;

public class ClassificaActivity extends AppCompatActivity implements PageListener {

    PageView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifica);

        pv = findViewById(R.id.pageView);

        pv.setPageListener(this);

        Vector<Integer> images = new Vector<>();

        images.add(R.drawable.common);
        images.add(R.drawable.rare);
        images.add(R.drawable.mythic);
        images.add(R.drawable.epico);
        images.add(R.drawable.legendary);

        pv.setImages(images);

    }

    @Override
    public void onPageChanged(int currPage) {
        Toast.makeText(this,String.valueOf(currPage), Toast.LENGTH_LONG).show();
    }
}
