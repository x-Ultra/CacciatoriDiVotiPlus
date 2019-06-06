package it.iCarrambaDT.cacciatoriDiVoti.controller;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;


import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class MainController {
    private int rarity;
    private CreateVoto voto;
    private TimeControl dates;
    private String prova;
    private Voto mark;
    private String ciao = "ciao";
    private String ciao2 = "nammerda";
    private int tor=4;

    public MainController(Context context) {
        voto=null;
        dates=new TimeControl(context);
        voto= new CreateVoto(context);
        mark=null;
    }
    public Voto returnVoto(){

        //Controllo se non è passata l'ora e nel caso prendo il vecchio voto
        if (dates.compareDate()){
            mark=voto.getOldMark();
            return mark;
        }

        int rarity;

        mark = new Voto();

        //System.out.println(mark.getLat() + " " + mark.getLng());

        //qui setto la nuova data nelle sharedPreferences
        dates.setOldDate(dates.getActualDate());

        //Qui calcolo la rarità
        rarity=voto.setRarity();
        mark=voto.takeVotoToDb(rarity);

        //Se ricevo null dalla funzione di prima vuol dire che ha finito tutti gli esami e ritorno null a Pasquale
        if(mark==null){
            return null;
        }

        //Qui mi calcolo il nuovo voto
        mark=voto.modifyVoto(mark);

        //setto la posizione
        mark=voto.setNewPos(mark);

        //setto il nuovo voto trovato nelle sharedPreferences
        voto.setOldMark(mark);
        return mark;

    }

    public void catchedMark(Voto voto){

        voto.setCapture(1);

        this.voto.setOldMark(voto);
        if (2*(voto.getMark()+1-17)/(3*14.0)+voto.getCredits()/(3*18.0)>=0.2*voto.getRarity()){
            voto.setRarity(voto.getRarity()+1);
        }
        this.voto.setVotoToDB(voto);
    }

}

