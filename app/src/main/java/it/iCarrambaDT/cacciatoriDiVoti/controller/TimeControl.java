package it.iCarrambaDT.cacciatoriDiVoti.controller;


import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import it.iCarrambaDT.cacciatoriDiVoti.databaseStuff.DBManager;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;


public class TimeControl  {

    private Calendar calendar;
    private  Date pastDate;
    private Date actualDate;
    private DateFormat df;
    private DBManager db=DBManager.getInstance();
    private String MYMP="lastLogs";
    private SharedPreferences prefs;
    private SharedManager manag;


    public TimeControl(Context context){
        prefs = context.getSharedPreferences(MYMP, Context.MODE_PRIVATE);
        manag=new SharedManager(prefs);
        df=new SimpleDateFormat("yyyy-MM-dd HH:mm");

        calendar=Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        actualDate=calendar.getTime();

        if (manag.getLastLog().compareTo("null") == 0){
            calendar.set(1997,7,13,12,0); /**qui va inserita a funzione che prende dalle sharedPref*/
            pastDate=calendar.getTime();
        }
        else{
            String ret=manag.getLastLog();
            pastDate=df.parse(ret,new ParsePosition(0));
        }
        }

    public Date getActualDate(){
        return this.actualDate;
    }

    public boolean compareDate(){

        System.out.println(actualDate + " ------ " + pastDate);


        return actualDate.toString().compareTo(pastDate.toString()) == 0;
    }

    public void setOldDate(Date date) {
        String data = df.format(date);
        manag.setLastLog(data);
        return;

    }
}
