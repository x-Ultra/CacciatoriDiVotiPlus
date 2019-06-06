package it.iCarrambaDT.cacciatoriDiVoti.controller;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBHelper;
import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;

import static java.security.AccessController.getContext;

public class CreateVoto {

    private Random random=new Random();
    private int rarity;
    private Voto voto;
    private String MYMP="coordinates";
    private String LOG_FILE_NAME="lastLogs";
    private SharedPreferences prefs;
    private SharedManager managCoord;
    private SharedManager managLog;
    private DBManager db;



    public CreateVoto(Context context) {
        voto=null;
        rarity=0;

        prefs = context.getSharedPreferences(MYMP, Context.MODE_PRIVATE);
        managCoord=new SharedManager(prefs);

        prefs = context.getSharedPreferences(LOG_FILE_NAME, Context.MODE_PRIVATE);
        managLog=new SharedManager(prefs);

        db=DBManager.getInstance();
    }

    public int setRarity(){
        int rangeRarity=31;
        int index=random.nextInt(rangeRarity);
        if (index>14){
            this.rarity=1;
        }
        else if (index>6){
            this.rarity=2;
        }
        else if (index>2){
            this.rarity=3;
        }
        else if (index>0){
            this.rarity=4;
        }
        else {this.rarity=5;}
        return this.rarity;
    }

     public Voto takeVotoToDb(int rarity){

        this.voto = db.getVotoRarLessEqual(rarity);
         while (true) {

            if (this.voto == null && rarity<5) {        //Se Ezio si occupa del controllo della rarità allora deve occuparsi anche del caso voto==31
                rarity++;
                this.voto=db.getVotoRarLessEqual(rarity);
            }
            else if(2*(Math.max(18, this.voto.getMark())-17)/(3*14.0)+this.voto.getCredits()/(3*18.0)>=0.2*rarity) {
                int newRarity=rarity+1;
                db.updateRarita(Integer.toString(newRarity),this.voto.getSubject());
                this.voto = db.getVotoRarLessEqual(rarity);
                //System.out.println(voto.getSubject() + " " + voto.getMark() + " " + voto.getCredits() + " " + voto.getRarity() + " " + rarity);
            }
            else break;
         }
         return this.voto;
     }

    public Voto modifyVoto(Voto vota){

        int indexRandom;
        int z;
        double beforeRarity=0;
        int markUpdate;
        int startRange;

        this.voto = vota;

        if (this.voto.getMark()==0){
            markUpdate=18;
        }
        else
            markUpdate=this.voto.getMark();

        while (beforeRarity<0.2*(this.voto.getRarity()-1) && markUpdate<31){
            markUpdate++;
            beforeRarity=(2*(markUpdate-17)/(3*14.0)+this.voto.getCredits()/(3*18.0));
        }

        startRange=markUpdate;
        while (beforeRarity<0.2*this.voto.getRarity()&&markUpdate<32){
            markUpdate++;
            beforeRarity=(2*(markUpdate-17)/(3*14.0)+this.voto.getCredits()/(3*18.0));
        }

        markUpdate--;
        indexRandom=markUpdate-startRange;

        if (indexRandom<=0){
            z=0;
        }
        else{
            z=random.nextInt(indexRandom+1);
        }


        this.voto.setMark(z+startRange);
        return this.voto;
    }

    public void setOldMark(Voto voto){
        String ret=voto.getSubject()+"-"+
                Integer.toString(voto.getCredits())+"-"+
                Integer.toString(voto.getMark())+"-" +
                Integer.toString(voto.getRarity())+"-"+
                Integer.toString(voto.getCapture())+"-"+
                Double.toString(voto.getLat())+"-" +
                Double.toString(voto.getLng());
        System.out.println(ret + " " + voto.getLat() + " " + voto.getLng() ) ;
        managLog.setLastVoto(ret);
                return ;
    }

    public Voto getOldMark(){
        String[] ret= managLog.getLastVoto();
        Voto mark=new Voto(ret[0],
                Integer.parseInt(ret[1]),
                Integer.parseInt(ret[2]),
                Integer.parseInt(ret[3]));
        mark.setCapture(Integer.parseInt(ret[4]));
        mark.setLat(Double.parseDouble(ret[5]));
        mark.setLng(Double.parseDouble(ret[6]));
        mark.setPos(new LatLng(mark.getLat(),mark.getLng()));
        return mark;
    }

    public Voto setNewPos(Voto voto){
        LatLng pos;
        String[] arrayPos;

        int posIndex=random.nextInt(5)+1;
        String index="p"+Integer.toString(posIndex);

        arrayPos=managCoord.getCoords(index);
        System.out.println(arrayPos[0] + " " + arrayPos[1]);
        voto.setLat(Double.parseDouble(arrayPos[0]));
        voto.setLng(Double.parseDouble(arrayPos[1]));
        pos=new LatLng(Double.parseDouble(arrayPos[0]),Double.parseDouble(arrayPos[1]));
        voto.setPos(pos);
        return voto;
    }

    public void setVotoToDB(Voto voto){
        db.updateVoto(Integer.toString(voto.getMark()),voto.getSubject());
        db.updateRarita(Integer.toString(voto.getRarity()),voto.getSubject());
        return;
    }

}
