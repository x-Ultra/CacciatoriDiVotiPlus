package it.iCarrambaDT.cacciatoriDiVoti.entity;


import com.google.android.gms.maps.model.LatLng;

public class Materia {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;

    private Double lat;
    private Double lng;
    private int tempoCattura; //in cs

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }


    public Materia(String subject, int credits, int mark, int rarity, int tempoCattura){
        this.subject = subject;
        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
        this.tempoCattura = tempoCattura;
    }

    public Materia(String subject, int credits, int mark, int rarity){
        this.subject = subject;
        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
    }

    public int getTempoCattura() {
        return tempoCattura;
    }

    public Materia() {

    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    public void setMark(int mark){
        this.mark=mark;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getSubject() {
        return subject;
    }

    public int getCredits() {
        return credits;
    }

    public int getMark() {
        return mark;
    }

    public int getRarity() {
        return rarity;
    }

    //trasforma hh:cs in cs
    public static int catturaToInt(String tempo) {

        String[] splittedTempo = tempo.split(":");
        return Integer.parseInt(splittedTempo[0])*100 + Integer.parseInt(splittedTempo[1]);
    }

    //trasforma cs in hh:cs
    public static String catturaToString(int tempo) {

        int min = tempo/6000;
        String minS;
        if (min < 10)
            minS = "0" + String.valueOf(min);
        else
            minS = String.valueOf(min);

        if ((tempo-min*6000)/100 <10)
            return minS + ":0" + String.valueOf((tempo-min*6000)/100);
        else
            return minS + ":" + String.valueOf((tempo-min*6000)/100);
    }

}
