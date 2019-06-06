package it.iCarrambaDT.cacciatoriDiVoti.entity;


import com.google.android.gms.maps.model.LatLng;

public class Voto {
    private String subject;
    private int credits;
    private int mark;
    private int rarity;
    private int capture;
    private LatLng pos;
    private Double lat;
    private Double lng;

    public LatLng getPos() {
        return pos;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Voto(String s, int c, int m, int r){
        subject=s;
        credits=c;
        mark=m;
        rarity=r;
        lat=0.0;
        lng=0.0;
        pos=new LatLng(lat,lng);
        capture=0;


    }

    public  Voto() {

    }
    public int getCapture() {
        return capture;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setCapture(int capture) {
        this.capture = capture;
    }
    public void setPos(LatLng pos) {
        this.pos = pos;
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

}
