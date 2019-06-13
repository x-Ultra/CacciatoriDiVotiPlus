package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.GifImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyButton;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoAsyncTask;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimerListener, VotoListener {

    TimerTextView timerTextView;
    RarityImageView rarityView;
    MyButton mapButton;
    MyButton gradesButton;
    MyButton standingsButton;
    GifImageView gifView;
    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //startActivity(new Intent(this, WaitingServerActivity.class));

        startActivity(new Intent(this, BookletActivity.class));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Usato per testare classifica
        //startActivity(new Intent(this, ClassificaActivity.class));

    }

    private void resetTimer(long millis) {

        timerTextView.startTimer(millis);

    }

    private void disableVoto() {

        MyTextView textView = findViewById(R.id.tempoTextView);
        textView.setText(getString(R.string.votoCatturatoStr));
        textView.setTextSize(20);

        timerTextView = findViewById(R.id.timerTextViewMain);

        timerTextView.stopTimer();
        timerTextView.setVisibility(View.INVISIBLE);
    }


    //Listeners per bottoni, TimerTextView e asyncTask del voto
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.mapButton) {

            startActivity(new Intent(this,MapActivity.class));

        } else if (v.getId() == R.id.gradesButton) {

            startActivity(new Intent(this, BookletActivity.class));

        } else if (v.getId() == R.id.classificaButton) {

            startActivity(new Intent(this, ClassificaActivity.class));
        }

    }

    @Override
    public void onTimerFinished() {

        System.out.println("ciao");

        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);

    }

    @Override
    public void onTaskFinished(MateriaPlus materiaPlus) {


        if (materiaPlus == null) {

            Toast.makeText(this,R.string.cantConnectStr,Toast.LENGTH_LONG).show();

            gradesButton = findViewById(R.id.gradesButton);
            gradesButton.setOnClickListener(this);

            standingsButton = findViewById(R.id.classificaButton);
            standingsButton.setOnClickListener(this);

            return;
        }

        ConstraintLayout constraintLayout = findViewById(R.id.parent);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.tempoTextView,ConstraintSet.TOP,R.id.rarityImageMain,ConstraintSet.BOTTOM,8);
        constraintSet.applyTo(constraintLayout);

        //Controllo se il voto è già stato catturato
        SharedManager sm = new SharedManager(getSharedPreferences("lastLogs", MODE_PRIVATE));


        String[] materiaString = sm.getLastVoto();


        if (materiaString[0].equals(materiaPlus.getSubject()) && materiaString[1].equals(materiaPlus.getEmissionTime()))
            disableVoto();
        else {

            //Imposto la timer text view
            timerTextView = findViewById(R.id.timerTextViewMain);
            timerTextView.setListener(this);

            try {
                resetTimer(materiaPlus.getTimerInMillis());
            } catch (ParseException e) {
                //Errore nella ricezione del pacchetto try again

                VotoAsyncTask vat = new VotoAsyncTask();
                vat.setListener(this);

                vat.execute(this);
                return;
            }

        }
        //Toast.makeText(this, voto.getCapture(), Toast.LENGTH_LONG);

        //Cambio la rarità con il voto che mi è stato passato
        rarityView = findViewById(R.id.rarityImageMain);
        rarityView.changeRarity(materiaPlus.getRarity());

        //Imposto i bottoni
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        gradesButton = findViewById(R.id.gradesButton);
        gradesButton.setOnClickListener(this);


        standingsButton = findViewById(R.id.classificaButton);
        standingsButton.setOnClickListener(this);

        //Nascondo la progress bar
        prog = findViewById(R.id.votoProgressBarMain);
        prog.setVisibility(View.INVISIBLE);



    }

    @Override
    protected void onResume() {

        super.onResume();

        setContentView(R.layout.activity_main);

        gifView = findViewById(R.id.gifImageViewMain);
        gifView.setGifImageResource(R.drawable.loading);

        //System.out.println("ciao");
        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);
    }
}
