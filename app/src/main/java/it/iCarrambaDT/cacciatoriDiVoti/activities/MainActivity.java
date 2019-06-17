package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.text.ParseException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.GifImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyButton;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoAsyncTask;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoListener;
import it.iCarrambaDT.cacciatoriDiVoti.notification.NotifListner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimerListener, VotoListener {

    TimerTextView timerTextView;
    RarityImageView rarityView;
    MyButton mapButton;
    MyButton gradesButton;
    MyButton standingsButton;
    GifImageView gifView;
    private String degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedManager sm = new SharedManager(getSharedPreferences("laurea", MODE_PRIVATE));
        degree = sm.getLaurea();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

            String transitionName = "rarity";

            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, rarityView, transitionName);
            startActivity(new Intent(this, MapActivity.class), transitionActivityOptions.toBundle());

        } else if (v.getId() == R.id.gradesButton) {

            startActivity(new Intent(this, BookletActivity.class));

        } else if (v.getId() == R.id.classificaButton) {

            startActivity(new Intent(this, ClassificaActivity.class));

        } else if (v.getId() == R.id.reconnectButton) {

            this.onResume();
        }

    }

    @Override
    public void onTimerFinished() {

        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);

    }

    @Override
    public void onTaskFinished(MateriaPlus materiaPlus) {
        long delay = 0;
        gifView = findViewById(R.id.gifImageViewMain);
        gifView.setVisibility(View.GONE);

        if (materiaPlus == null) {

            Toast.makeText(this,R.string.cantConnectStr,Toast.LENGTH_LONG).show();

            gradesButton = findViewById(R.id.gradesButton);
            gradesButton.setOnClickListener(this);

            standingsButton = findViewById(R.id.classificaButton);
            standingsButton.setOnClickListener(this);

            MyTextView remText = findViewById(R.id.tempoTextView);
            remText.setText(R.string.cantConnectStr);

            timerTextView = findViewById(R.id.timerTextViewMain);
            timerTextView.setVisibility(View.INVISIBLE);

            MyImageView recButt = findViewById(R.id.reconnectButton);
            recButt.setOnClickListener(this);

            return;
        }


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
                delay = materiaPlus.getTimerInMillis();
                scheduleNotification(getBaseContext().getApplicationContext(),delay, materiaPlus.getTimeToLiveMinutes()*60*1000,  NotifListner.NOTIFICATION_ID);
                System.out.println(materiaPlus.getTimeToLiveMinutes());
                System.out.println(materiaPlus.getTimerInMillis());

            } catch (ParseException e) {
                //Errore nella ricezione del pacchetto try again

                VotoAsyncTask vat = new VotoAsyncTask(degree);
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

        //Nascondo il bottone di riconnessione
        MyImageView recButt = findViewById(R.id.reconnectButton);
        recButt.setVisibility(View.INVISIBLE);




    }

    @Override
    protected void onResume() {

        super.onResume();

        setContentView(R.layout.activity_main);

        gifView = findViewById(R.id.gifImageViewMain);
        gifView.setGifImageResource(R.drawable.loading);

        //System.out.println("ciao");
        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask(degree);
        vat.setListener(this);

        vat.execute(this);
    }

    public void scheduleNotification(Context context, long delay, long repeat, int notificationId) {

        Intent notificationIntent = new Intent(context, NotifListner.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        System.out.println(repeat);
        System.out.println(delay);



        long futureInMillis = SystemClock.elapsedRealtime() + delay + 5000;
        System.out.println(futureInMillis);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, repeat, pendingIntent);

    }

}
