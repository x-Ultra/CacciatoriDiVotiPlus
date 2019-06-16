package it.iCarrambaDT.cacciatoriDiVoti.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.activities.MainActivity;
import it.iCarrambaDT.cacciatoriDiVoti.activities.SplashScreenActivity;

import static it.iCarrambaDT.cacciatoriDiVoti.notification.App.CHANNEL_ID;

public class NotifListner extends BroadcastReceiver {

    public static int NOTIFICATION_ID = 1234;
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentStart = new Intent(context, SplashScreenActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intentStart, PendingIntent.FLAG_CANCEL_CURRENT);

        //contatto il server


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.icarramba_logo)
                .setContentTitle("Prova")
                .setContentText("Provone")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(activity);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
