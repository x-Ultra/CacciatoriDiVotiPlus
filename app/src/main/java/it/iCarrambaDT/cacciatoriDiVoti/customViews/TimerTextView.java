package it.iCarrambaDT.cacciatoriDiVoti.customViews;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

public class TimerTextView extends android.support.v7.widget.AppCompatTextView {

    //TimerTextView creata per implementare un countdown timer per API < 24
    //Questa view conta solo intervalli inferiori ad un ora

    private TimerListener tl;
    private CountDownTimer cd;

    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Imposto un listener da chiamare alla fine del timer
    public void setListener(TimerListener tl) {
        this.tl = tl;
    }

    //Time in milliseconds
    public void startTimer(long time) {

        //Imposto il timer, mi interessano solo i millisecondi che mancano alla prossima ora
        cd = new CountDownTimer(time, 10) {

            public void onTick(long millisUntilFinished) {
                setTime(millisUntilFinished);
            }

            public void onFinish() {
                tl.onTimerFinished();
            }

        }.start();



    }

    //Time in milliseconds
    public void setTime(long time) {
        long onlyMillis = time%1000;
        long onlySec = ((time - onlyMillis)/1000)%60;
        long onlyMin = ((time - onlyMillis - onlySec*1000)/60000);
        String millis;
        String seconds;
        String minutes;

        if (onlyMillis < 100)
            millis = ":0" + onlyMillis/10;
        else
            millis = ":" + onlyMillis/10;

        if (onlySec < 10)
            seconds = ":0" + onlySec;
        else
            seconds = ":" + onlySec;

        if (onlyMin < 10)
            this.setText("0" + onlyMin + seconds + millis);
        else
            this.setText(onlyMin + seconds + millis);
    }



    public void stopTimer() {
        if (cd != null)
            cd.cancel();
    }

}
