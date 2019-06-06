package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyButton;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoAsyncTask;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimerListener, VotoListener {

    TimerTextView timerTextView;
    RarityImageView rarityView;
    MyButton mapButton;
    MyButton gradesButton;
    ProgressBar prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void resetTimer() {

        Date currentTime = Calendar.getInstance().getTime();
        timerTextView.startTimer(currentTime.getTime());

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

        } else {

            startActivity(new Intent(this, BookletActivity.class));
        }

    }

    @Override
    public void onTimerFinished() {

        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);

        resetTimer();
    }

    @Override
    public void onTaskFinished(Voto voto) {

        //Toast.makeText(this, voto.getCapture(), Toast.LENGTH_LONG);

        //Cambio la rarità con il voto che mi è stato passato
        rarityView = findViewById(R.id.rarityImageMain);
        rarityView.changeRarity(voto.getRarity());

        //Imposto i bottoni
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        gradesButton = findViewById(R.id.gradesButton);
        gradesButton.setOnClickListener(this);


        //Nascondo la progress bar
        prog = findViewById(R.id.votoProgressBarMain);
        prog.setVisibility(View.INVISIBLE);

        if (voto.getCapture() == 1) {
            disableVoto();

        } else {

            //Imposto la timer text view
            timerTextView = findViewById(R.id.timerTextViewMain);
            timerTextView.setListener(this);

            resetTimer();

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        setContentView(R.layout.activity_main);

        System.out.println("ciao");
        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);
    }
}
