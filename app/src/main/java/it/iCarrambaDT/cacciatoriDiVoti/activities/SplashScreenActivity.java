package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBTask;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.FileTask;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import it.iCarrambaDT.cacciatoriDiVoti.notification.NotifListner;

import static it.iCarrambaDT.cacciatoriDiVoti.notification.App.CHANNEL_ID;
import static java.lang.Thread.sleep;
import android.app.Service;

public class SplashScreenActivity extends AppCompatActivity {

    private String databaseCsvFileName = "database_info.csv";
    private String coordsFile = "coord.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_Launcher);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //ottendo tutti i dati per la verifica qui di seguito
        AssetManager assets = getAssets();
        try {
            //verifico se il DB è già stato creato e popolato
            DBTask dbTask = new DBTask(this);
            dbTask.execute();

            //verifico se dispondo di tutti i file (SharedPreferences ecc..)

            //1)file SharedPreferences coordinates da coord.txt
            SharedPreferences sharedCoord = getSharedPreferences("coordinates", MODE_PRIVATE);

            //2)file SharedPreferences lastLogs per memorizzare ultima apertura dell'app
            SharedPreferences lastLogs = getSharedPreferences("lastLogs", MODE_PRIVATE);

            InputStream coordsInputStream = assets.open(this.coordsFile, MODE_PRIVATE);
            FileTask fileTask = new FileTask(this, coordsInputStream);
            fileTask.execute(sharedCoord, lastLogs);

            //System.out.println("sup");

        } catch (IOException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, R.string.dbError, Toast.LENGTH_SHORT).show();
        }

        //qui da caricare chiamata ad altra activity
        ((MyTextView)findViewById(R.id.tvDbProg)).setText("DB check done");
        ((MyTextView)findViewById(R.id.tvFileProg)).setText("FILE check done");

        //dbm.createDBorCheck();
        SharedPreferences shared = getSharedPreferences("userData", MODE_PRIVATE);
        SharedManager manager = new SharedManager(shared);

        String username= manager.getUserDataInfo("username");

        // commentare la roba sotto e levare qui il commento per fissare la EntryPointActivity
        // startActivity( new Intent(this, EntryPointActivity.class));

         if (username.equals("notfound")){
            startActivity( new Intent(this, EntryPointActivity.class));
         }else{


             //dbm.createDBorCheck();
             //Intent i = new Intent(this, WaitingServerActivity.class);
             // Intent i = new Intent(this, BookletActivity.class);
            // Intent i = new Intent(this, MainActivity.class);
             Intent i = new Intent(this,VotoCattActivity.class);
            //Intent i = new Intent(this, BookletActivity.class);
            //Intent i = new Intent(this, ClassificaActivity.class);

            startActivity(i);}
            finish();
    }



}
