package it.iCarrambaDT.cacciatoriDiVoti.notification;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.activities.MainActivity;
import it.iCarrambaDT.cacciatoriDiVoti.activities.MapActivity;
import it.iCarrambaDT.cacciatoriDiVoti.activities.SplashScreenActivity;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.TimerTextView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoAsyncTask;
import it.iCarrambaDT.cacciatoriDiVoti.helpers.VotoListener;
import it.iCarrambaDT.cacciatoriDiVoti.serverInteraction.ServerCaller;

import static android.content.Context.MODE_PRIVATE;
import static it.iCarrambaDT.cacciatoriDiVoti.notification.App.CHANNEL_ID;

public class NotifListner extends BroadcastReceiver implements VotoListener {
    public static String LIFETIME;
    private MateriaPlus materia;
    public static String DEGREE = "med";
    public static int NOTIFICATION_ID = 1234;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        //Prendo la laurea dalle shared
        SharedPreferences shared = context.getSharedPreferences("userData", MODE_PRIVATE);
        SharedManager manager = new SharedManager(shared);
        String degree = manager.getLaurea();

        //contatto il server
        VotoAsyncTask vat = new VotoAsyncTask(degree);
        vat.setListener(this);

        vat.execute(context);


    }
    //necessaria per adattare le immagini alle dimensioni e al tipo dell'icona grande
    public Bitmap resizeFunction (Context context, int rarityId){
        Bitmap contactPic = BitmapFactory.decodeResource(context.getResources(),rarityId);

        Resources res = context.getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        contactPic = Bitmap.createScaledBitmap(contactPic, width, height, false);
        return contactPic;
    }





    @Override
    //Qui eseguo le azioni in maniera asincrona
    public void onTaskFinished(MateriaPlus materia) {
        this.materia = materia;

        //creo la PendingIntent
        Intent intentStart = new Intent(context, SplashScreenActivity.class);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intentStart, PendingIntent.FLAG_CANCEL_CURRENT);

        //Prendo il nome dell'utente
        SharedPreferences shared = context.getSharedPreferences("userData", MODE_PRIVATE);
        SharedManager manager = new SharedManager(shared);

        String username= manager.getUserDataInfo("username");


        if(materia == null){
            //Esco e non visualizzo nessuna notifica
            return ;
        }

        int rarityId = R.drawable.common;

        //Scelgo l'mmagine della largeIcon
        switch (materia.getRarity()) {

            case 1:
                //Cambiare immagine a comune
                rarityId = R.drawable.commonnot;
                break;

            case 2:
                //Cambiare immagine a rara
                rarityId = R.drawable.rarenot2;
                break;

            case 3:
                //Cambiare immagine a mitica
                rarityId = R.drawable.mythicnot;
                break;

            case 4:
                //Cambiare immagine a epica
                rarityId = R.drawable.epicnot;
                break;

            case 5:
                //Cambiare immagine a leggendaria
                rarityId = R.drawable.legendarynot;
                break;
        }

        //costruisco la notifica
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setLargeIcon(resizeFunction (context,rarityId))
                .setSmallIcon(R.drawable.logonot)
                .setColor(Color.GREEN)
                .setContentTitle(context.getString(R.string.notificationTitle))
                .setContentText(username + " "+context.getString(R.string.notificationText))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(activity);

        //Permette di vedere la notifica anche dal lock screen
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}

