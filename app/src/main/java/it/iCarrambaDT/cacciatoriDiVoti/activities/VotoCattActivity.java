package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.controller.MainController;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class VotoCattActivity extends AppCompatActivity implements View.OnClickListener {

    TextView esameView;
    TextView creditiView;
    TextView votoView;
    RarityImageView raritaView;
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Nascondi la barra superiore
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voto_catt);

        Intent i = getIntent();

        Bundle votoBundle = i.getBundleExtra("Voto");

        //Imposto i campi
        esameView = findViewById(R.id.corsoTW);
        esameView.setText(votoBundle.getString("Materia"));

        creditiView = findViewById(R.id.creditiTW);
        creditiView.setText(Integer.toString(votoBundle.getInt("Crediti")));

        votoView = findViewById(R.id.votoTW);
        votoView.setText(Integer.toString(votoBundle.getInt("Voto")));

        raritaView = findViewById(R.id.rarityImageView);
        raritaView.changeRarity(votoBundle.getInt("Rarita"));

        homeBtn = findViewById(R.id.myButton);
        homeBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
