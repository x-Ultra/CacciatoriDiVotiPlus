package it.iCarrambaDT.cacciatoriDiVoti.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;

public class NotifBody {
   private SharedPreferences prefs;
   private SharedManager manag;


   public String takeUserName(Context context){
       prefs = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
       SharedManager manager = new SharedManager(prefs);

       String username= manager.getUserDataInfo("username");
       return username;
   }
}
