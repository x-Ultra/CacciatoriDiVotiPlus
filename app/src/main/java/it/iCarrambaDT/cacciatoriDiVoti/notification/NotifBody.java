package it.iCarrambaDT.cacciatoriDiVoti.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class NotifBody {
    private Context context;
    public void scheduleNotification(long delay, long repeat, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification


        Intent notificationIntent = new Intent(context, NotifListner.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis,repeat, pendingIntent);
    }
}
