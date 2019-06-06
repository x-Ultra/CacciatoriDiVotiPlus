package it.iCarrambaDT.cacciatoriDiVoti.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import it.iCarrambaDT.cacciatoriDiVoti.R;

public class RarityImageView extends android.support.v7.widget.AppCompatImageView {

    Context myContext;

    public RarityImageView(Context context) {
        super(context);
        myContext = context;
    }

    public RarityImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        myContext = context;
    }

    public RarityImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myContext = context;
    }

    public void changeRarity(int rarity) {

        switch (rarity) {

            case 1:
                //Cambiare immagine a comune
                this.setImageDrawable(getResources().getDrawable(R.drawable.common,myContext.getTheme()));
                break;

            case 2:
                //Cambiare immagine a rara
                this.setImageDrawable(getResources().getDrawable(R.drawable.rare,myContext.getTheme()));
                break;

            case 3:
                //Cambiare immagine a mitica
                this.setImageDrawable(getResources().getDrawable(R.drawable.mythic,myContext.getTheme()));
                break;

            case 4:
                //Cambiare immagine a epica
                this.setImageDrawable(getResources().getDrawable(R.drawable.epico,myContext.getTheme()));
                break;

            case 5:
                //Cambiare immagine a leggendaria
                this.setImageDrawable(getResources().getDrawable(R.drawable.legendary,myContext.getTheme()));
                break;
        }
    }
}
