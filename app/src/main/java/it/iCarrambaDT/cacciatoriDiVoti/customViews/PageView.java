package it.iCarrambaDT.cacciatoriDiVoti.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;

public class PageView extends View {

    int PAGE_NBR = 6;
    int vertSpace = 30;
    int horiSpace = 10;
    int vertCurve = 20;
    int horiCurve = 10;
    String backColor = "#787878";
    String foreColor = "#ffffff";
    Paint forePaint;
    Paint backPaint;
    Paint linePaint;

    private int wid;
    private int hei;
    int currPage = 0;
    PageListener pl;
    Vector<Integer> images;

    public PageView(Context context) {
        super(context);
        init();
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Recupero parametri XML
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PageView);

        String pageNbr = arr.getString(R.styleable.PageView_pageNumber);
        if (pageNbr != null) {
            PAGE_NBR = Integer.parseInt(pageNbr);
        }

        String vert = arr.getString(R.styleable.PageView_verticalSpace);
        if (vert != null) {
            vertSpace = Integer.parseInt(vert);
        }

        String hori = arr.getString(R.styleable.PageView_horizontalSpace);
        if (hori != null) {
            horiSpace = Integer.parseInt(hori);
        }

        String cVert = arr.getString(R.styleable.PageView_curveHeight);
        if (vert != null) {
            vertCurve = Integer.parseInt(cVert);
        }

        String cHori = arr.getString(R.styleable.PageView_curveWidth);
        if (hori != null) {
            horiCurve = Integer.parseInt(cHori);
        }

        String temp = arr.getString(R.styleable.PageView_backColor);
        if (temp != null) {
            backColor = temp;
        }

        temp = arr.getString(R.styleable.PageView_foreColor);
        if (temp != null) {
            foreColor = temp;
        }

        arr.recycle();
        init();
    }

    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //Recupero parametri XML
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PageView);

        String pageNbr = arr.getString(R.styleable.PageView_pageNumber);
        if (pageNbr != null) {
            PAGE_NBR = Integer.parseInt(pageNbr);
        }

        String vert = arr.getString(R.styleable.PageView_verticalSpace);
        if (vert != null) {
            vertSpace = Integer.parseInt(vert);
        }

        String hori = arr.getString(R.styleable.PageView_horizontalSpace);
        if (hori != null) {
            horiSpace = Integer.parseInt(hori);
        }
        String cVert = arr.getString(R.styleable.PageView_curveHeight);
        if (vert != null) {
            vertCurve = Integer.parseInt(cVert);
        }

        String cHori = arr.getString(R.styleable.PageView_curveWidth);
        if (hori != null) {
            horiCurve = Integer.parseInt(cHori);
        }

        String temp = arr.getString(R.styleable.PageView_backColor);

        if (temp != null) {
            backColor = temp;
        }

        temp = arr.getString(R.styleable.PageView_foreColor);
        if (temp != null) {
            foreColor = temp;
        }
        arr.recycle();
        init();
    }

    //Dichiaro i colori che userò
    private void init() {

        forePaint = new Paint();
        backPaint = new Paint();
        linePaint = new Paint();

        System.out.println(foreColor);
        forePaint.setStyle(Paint.Style.FILL);
        forePaint.setColor(Color.parseColor(foreColor));
        forePaint.setAntiAlias(true);


        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(Color.parseColor(backColor));
        backPaint.setAntiAlias(true);

        linePaint.setColor(Color.BLACK);
        //linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);

    }


    //recupero la dimensione della View
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.wid = w;
        this.hei = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //Disegno la View
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0,wid,hei,linePaint);
        //Ricavo la grandezza del rettangolo
        float rectSize = (float) wid / PAGE_NBR;

        //Toast.makeText(getContext(), String.valueOf(currPage), Toast.LENGTH_SHORT).show();
        //Disegno tutti i rettangolo più scuri
        for (int i = 0; i < PAGE_NBR; i++) {

            if (i != currPage) {

                canvas.drawArc(i*rectSize,0,i*rectSize+horiCurve,vertCurve*2,-180,90,true,backPaint);
                canvas.drawArc((i+1)*rectSize-horiCurve,0,(i+1)*rectSize,vertCurve*2,0,-90,true,backPaint);
                canvas.drawRect(i*rectSize+(float)horiCurve/2-1, 0, 1+(i + 1) * rectSize-(float)horiCurve/2, hei, backPaint);
                canvas.drawRect(i * rectSize, vertCurve, (i + 1) * rectSize, hei, backPaint);

                //Disegno le immagini se mi sono state passate, le scurisco
                if (images != null) {
                    Drawable d = getResources().getDrawable(images.get(i), getContext().getTheme());
                    Drawable wrappedDrawable = DrawableCompat.wrap(d);
                    DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.DARKEN);
                    DrawableCompat.setTint(wrappedDrawable, Color.parseColor(backColor));
                    d.setBounds(Math.round(i * rectSize + horiSpace), vertSpace, Math.round((i + 1) * rectSize - horiSpace), hei - vertSpace);
                    d.draw(canvas);
                    //DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#4fff51"));
                }

                //Disegno delle linee per separare i rettangoli
                if (i != 0)
                    canvas.drawLine(i * rectSize, 0, i * rectSize, hei, linePaint);
                if (i != PAGE_NBR - 1)
                    canvas.drawLine((i + 1) * rectSize, 0, (i + 1) * rectSize, hei, linePaint);
            }
        }

        //Disegno il rettangolo bianco
        canvas.drawArc(currPage*rectSize,0,currPage*rectSize+horiCurve,vertCurve*2,-180,90,true,forePaint);
        canvas.drawArc((currPage+1)*rectSize-horiCurve,0,(currPage+1)*rectSize,vertCurve*2,0,-90,true,forePaint);
        canvas.drawRect(currPage*rectSize+(float)horiCurve/2, 0, (currPage + 1) * rectSize-(float)horiCurve/2, hei, forePaint);
        canvas.drawRect(currPage * rectSize, vertCurve, (currPage + 1) * rectSize, hei, forePaint);

        //Disegno l'immagine nel rettangolo se mi è stata passata
        if (images != null) {
            Drawable d = getResources().getDrawable(images.get(currPage), getContext().getTheme());
            d.setBounds(Math.round(currPage * rectSize + horiSpace), vertSpace, Math.round((currPage + 1) * rectSize - horiSpace), hei - vertSpace);
            d.draw(canvas);
        }



    }

    //Funzione per passare le immagini da inserire
    public void setImages(Vector<Integer> newImages) {
        this.images = newImages;
    }

    //funzione per passare il metodo da chiamare quando viene cliccato un rettangolo
    public void setPageListener(PageListener pl) {
        this.pl = pl;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            //se l'utente clicca un rettangolo diverso dall'attuale chiamo la funzione e cambio la View
            case MotionEvent.ACTION_DOWN:
                float rectSize = (float)wid/PAGE_NBR;
                int prev = currPage;
                currPage = (int)(event.getX()/rectSize);
                if (currPage != prev) {
                    invalidate();
                    if (pl != null)
                        pl.onPageChanged(currPage);
                }
                break;
        }
        return true;
    }

    public int getPAGE_NBR() {
        return PAGE_NBR;
    }

    public void setPAGE_NBR(int PAGE_NBR) {
        this.PAGE_NBR = PAGE_NBR;
    }

    public int getVertSpace() {
        return vertSpace;
    }

    public void setVertSpace(int vertSpace) {
        this.vertSpace = vertSpace;
    }

    public int getHoriSpace() {
        return horiSpace;
    }

    public void setHoriSpace(int horiSpace) {
        this.horiSpace = horiSpace;
    }

    public int getVertCurve() {
        return vertCurve;
    }

    public void setVertCurve(int vertCurve) {
        this.vertCurve = vertCurve;
    }

    public int getHoriCurve() {
        return horiCurve;
    }

    public void setHoriCurve(int horiCurve) {
        this.horiCurve = horiCurve;
    }
}
