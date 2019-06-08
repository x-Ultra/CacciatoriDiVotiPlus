package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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

import java.util.Calendar;
import java.util.Date;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.controller.MainController;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerListener;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
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
    private Voto voto;
    private int rarityId;
    private Marker currMark;
    private Circle currCirc;
    private LocationController lc;

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

        //Converto la posizione del voto
        LatLng posizioneVoto = currMark.getPosition();
        Location posVoto = new Location("");
        posVoto.setLatitude(posizioneVoto.latitude);
        posVoto.setLongitude(posizioneVoto.longitude);


        //Misuro la discanza, se è minore di 30 metri catturo il voto
        if (posVoto.distanceTo(location) <= 30 && !alCapt) {
            alCapt = true;
            votoCatturato();
        }
    }

    private void votoCatturato() {



        System.out.println(voto.getSubject() + " " + voto.getMark());
        Intent i = new Intent(this,VotoCattActivity.class);
        Bundle votoBundle = new Bundle();
        votoBundle.putString("Materia", voto.getSubject());
        votoBundle.putInt("Crediti", voto.getCredits());
        votoBundle.putInt("Voto", voto.getMark());
        votoBundle.putInt("Rarita", voto.getRarity());

        i.putExtra("Voto", votoBundle);

        new MainController(this).catchedMark(voto);

        startActivity(i);
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
    //Quando è pronta sia la mappa che il voto imposto tutta la activity
    private void setUpActivity() {

        //Elimino il veccchio cerchio
        removeCircle();

        //Imposto le view
        rarityView = findViewById(R.id.rarityImageViewMap);
        rarityView.changeRarity(voto.getRarity());

        timer = findViewById(R.id.timerTextViewMap);
        timer.setListener(this);

        //Rendo invisibile la progress bar
        ProgressBar prog = findViewById(R.id.progressBar2);
        prog.setVisibility(View.INVISIBLE);


        if (voto.getCapture() == 1)
            disableVoto();
        else {
            //System.out.println(voto.getLat() + " " + voto.getLng());

            //Per testing eliminare commento
            //createCircle(41.8525221, 12.62088576);
            createCircle(voto.getLat(), voto.getLng());

            //Sposto la telecara sul cerchio
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(voto.getPos(), 100);
            map.animateCamera(cameraUpdate);

            //Inizio a controllare la posizione
            lc.startListening(this, this);

            resetTimer();
        }

    }

    //Listener per il timer
    @Override
    public void onTimerFinished() {

        lc.stopListening();

        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
        vat.setListener(this);

        vat.execute(this);

        resetTimer();
    }

    //Imposto la mappa qunado è pronta
    @SuppressLint("MissingPermission")
    @Override
    public synchronized void onMapReady(GoogleMap googleMap) {

        mapReady = true;

        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        mapView.onResume();

        if (votoReady)
            setUpActivity();
    }

    //Listener per l'asyncTask del voto
    @Override
    public synchronized void onTaskFinished(Voto voto) {

        this.voto = voto;

        votoReady = true;
        if (mapReady)
            setUpActivity();

    }


    //Funzione che cre un cerchio con un marker al centro,
    private void createCircle(double lat, double lon) {


        switch (voto.getRarity()) {

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
    private void resetTimer() {
        Date currentTime = Calendar.getInstance().getTime();
        timer.startTimer(currentTime.getTime());
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

        //Chiedo (rarità) del voto al control
        VotoAsyncTask vat = new VotoAsyncTask();
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
