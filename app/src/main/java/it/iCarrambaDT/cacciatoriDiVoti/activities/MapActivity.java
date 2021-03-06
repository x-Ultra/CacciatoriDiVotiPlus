package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Materia;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.LocationController;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.LocationUser;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoAsyncTask;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TimerListener, VotoListener, LocationUser {

    private MapView mapView;
    private GoogleMap map;
    private RarityImageView rarityView;
    private TimerTextView timer;
    private static final int PERMISSIONS_REQUEST_RESULT = 1;
    private boolean mapReady = false;
    private boolean votoReady = false;
    private boolean alCapt = false;
    private MateriaPlus materiaPlus;
    private int rarityId;
    private Marker currMark;
    private Circle currCirc;
    private LocationController lc;
    private String degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Nascondi la barra superiore
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Imposto la mappa
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //Imposto la classe per ottenere la posizione
        lc = new LocationController();

        //Controllo i permessi, li chiedo se non mi sono stati concessi
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_RESULT);

        } else {
            mapSetUp();
        }

    }

    //Funzione chiamata qunado arriva una nuova posizione accettabile
    @Override
    public void locationArrived(Location location) {

        //Converto la posizione del materiaPlus
        LatLng posizioneVoto = currMark.getPosition();
        Location posVoto = new Location("");
        posVoto.setLatitude(posizioneVoto.latitude);
        posVoto.setLongitude(posizioneVoto.longitude);


        //Misuro la discanza, se ?? minore di 30 metri catturo il materiaPlus
        if (posVoto.distanceTo(location) <= 30 && !alCapt) {
            alCapt = true;
            votoCatturato();
        }
    }

    private void votoCatturato() {

        timer.stopTimer();

        //Passo il oto all'activity successiva
        //System.out.println(materiaPlus.getSubject() + " " + materiaPlus.getMark());

        //Calcolo il tempo di cattura
        String minLeft = timer.getText().subSequence(0,2).toString();
        String secLeft = timer.getText().subSequence(3,5).toString();

        int csLeft = Integer.parseInt(minLeft)*6000 + Integer.parseInt(secLeft)*100;

        String tempoCattura = Materia.catturaToString(materiaPlus.getTimeToLiveMinutes()*6000 - csLeft);

        System.out.println(materiaPlus.getTimeToLiveMinutes()+ "-" + minLeft +"-"+secLeft+"-"+tempoCattura+ "-" + csLeft);

        Intent i = new Intent(this,VotoCattActivity.class);
        Bundle votoBundle = new Bundle();
        votoBundle.putString("Materia", materiaPlus.getSubject());
        votoBundle.putInt("Crediti", materiaPlus.getCredits());
        votoBundle.putInt("Voto", materiaPlus.getMark());
        votoBundle.putInt("Rarita", materiaPlus.getRarity());
        votoBundle.putString("TempoCattura", tempoCattura);

        i.putExtra("Bundle", votoBundle);


        //Salvo i voto nel DB
        DBManager dbm = DBManager.getInstance(this);
        dbm.createDBorCheck();

        dbm.insertVoto(String.valueOf(materiaPlus.getMark()),materiaPlus.getSubject(),String.valueOf(materiaPlus.getTimeToLiveMinutes()*6000 - csLeft), String.valueOf(materiaPlus.getCredits()),String.valueOf(materiaPlus.getRarity()));


        //Salvo il voto nelle shared preferences
        SharedManager sm = new SharedManager(getSharedPreferences("lastLogs", MODE_PRIVATE));

        sm.setLastVoto(materiaPlus.getSubject() + "-" + materiaPlus.getEmissionTime());

        String transitionName = "rarity";

        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MapActivity.this, rarityView, transitionName);
        startActivity(i, transitionActivityOptions.toBundle());

        finish();
    }

    private void disableVoto() {

        MyTextView textView = findViewById(R.id.myTextView);
        textView.setVisibility(View.INVISIBLE);

        timer = findViewById(R.id.timerTextViewMap);
        timer.stopTimer();
        timer.setText(getString(R.string.votoCatturatoStr));
        timer.setTextSize(20);
    }

    //Chiedo di attivare la mappa
    private void mapSetUp() {
        mapView.getMapAsync(this);
    }

    //Set up activity
    //Quando ?? pronta sia la mappa che il materiaPlus imposto tutta la activity
    private void setUpActivity() {


        if (materiaPlus == null) {

            Toast.makeText(this,R.string.cantConnectStr,Toast.LENGTH_LONG).show();

            return;
        }

        //Elimino il veccchio cerchio
        removeCircle();

        //Imposto le view
        rarityView = findViewById(R.id.rarityImageViewMap);
        rarityView.changeRarity(materiaPlus.getRarity());

        timer = findViewById(R.id.timerTextViewMap);
        timer.setListener(this);

        //Rendo invisibile la progress bar
        ProgressBar prog = findViewById(R.id.progressBar2);
        prog.setVisibility(View.INVISIBLE);

        //Controllo se il voto ?? gi?? stato catturato
        SharedManager sm = new SharedManager(getSharedPreferences("lastLogs", MODE_PRIVATE));

        String[] materiaString = sm.getLastVoto();

        if (materiaString[0].equals(materiaPlus.getSubject()) && materiaString[1].equals(materiaPlus.getEmissionTime()))
            disableVoto();
        else {


            //Per testing eliminare commento
            //createCircle(41.8525221, 12.62088576);
            createCircle(materiaPlus.getLat(), materiaPlus.getLng());

            //Sposto la telecamera sul cerchio
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(materiaPlus.getLat(), materiaPlus.getLng()), 100);
            System.out.println(materiaPlus.getLat() +"-"+ materiaPlus.getLng());
            map.animateCamera(cameraUpdate);

            //Inizio a controllare la posizione
            lc.startListening(this, this);

            try {
                resetTimer(materiaPlus.getTimerInMillis());
            } catch (ParseException e) {
                //Errore nella ricezione del pacchetto try again

                disableVoto();
                VotoAsyncTask vat = new VotoAsyncTask(degree);
                vat.setListener(this);

                vat.execute(this);
            }
        }

    }

    //Listener per il timer
    @Override
    public void onTimerFinished() {

        lc.stopListening();

        //Chiedo (rarit??) del materiaPlus al control
        VotoAsyncTask vat = new VotoAsyncTask(degree);
        vat.setListener(this);

        vat.execute(this);

    }

    //Imposto la mappa qunado ?? pronta
    @SuppressLint("MissingPermission")
    @Override
    public synchronized void onMapReady(GoogleMap googleMap) {

        System.out.println("map ready");
        mapReady = true;

        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        mapView.onResume();

        if (votoReady)
            setUpActivity();
    }

    //Listener per l'asyncTask del materiaPlus
    @Override
    public synchronized void onTaskFinished(MateriaPlus materia) {

        this.materiaPlus = materia;

        votoReady = true;
        if (mapReady)
            setUpActivity();

    }


    //Funzione che cre un cerchio con un marker al centro,
    private void createCircle(double lat, double lon) {


        switch (materiaPlus.getRarity()) {

            case 1:
                //Cambiare immagine a comune
                rarityId = R.drawable.common;
                break;

            case 2:
                //Cambiare immagine a rara
                rarityId = R.drawable.rare;
                break;

            case 3:
                //Cambiare immagine a mitica
                rarityId = R.drawable.mythic;
                break;

            case 4:
                //Cambiare immagine a epica
                rarityId = R.drawable.epico;
                break;

            case 5:
                //Cambiare immagine a leggendaria
                rarityId = R.drawable.legendary;
                break;
        }

        //Ridimensioniamo imamgine
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),rarityId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);

        //Creo il marker
        MarkerOptions mo = new MarkerOptions();
        mo.position(new LatLng(lat,lon));
        mo.icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));
        currMark = map.addMarker(mo);

        //Creo il cerchio
        CircleOptions co = new CircleOptions();

        co.center(new LatLng(lat,lon));
        co.radius(30);

        //Colore del bordo
        co.strokeColor(Color.BLACK);

        //Colore del riempimneto
        co.fillColor(0x30ff0000);

        //Larghezza del bordo
        co.strokeWidth(2);

        currCirc = map.addCircle(co);

    }

    //Funzione che rimuove cerchio e marker
    private void removeCircle() {
        if (currCirc != null && currMark != null) {
            currMark.remove();
            currCirc.remove();
        }
    }

    //Reset del timer
    private void resetTimer(long timeLeft) {
        timer.startTimer(timeLeft);
    }

    //Prendo il risultato della richiesta dei permessi
    //Se si, apri la mappa
    //Se no, torna lla precedente activity
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_RESULT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapSetUp();
                } else {
                    startActivity(new Intent(this, WaitingServerActivity.class));
                    finish();
                }
            }
        }
    }

    //Modifico le funzioni del lifetime per la mappa
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        SharedManager sm = new SharedManager(getSharedPreferences("userData", MODE_PRIVATE));
        degree = sm.getLaurea();

        //Chiedo (rarit??) del materiaPlus al control
        VotoAsyncTask vat = new VotoAsyncTask(degree);
        vat.setListener(this);

        vat.execute(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



}
