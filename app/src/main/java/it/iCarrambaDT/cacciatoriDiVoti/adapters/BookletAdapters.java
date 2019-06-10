package it.iCarrambaDT.cacciatoriDiVoti.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;

public class BookletAdapters extends RecyclerView.Adapter<BookletAdapters.MyViewHolder> {

    private Vector<Voto> voti;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public MyTextView cfu;
        public MyTextView materia;
        public MyTextView voto;
        public RarityImageView rarImm;

        public MyViewHolder(View v) {
            super(v);
            voto = v.findViewById(R.id.tvVoto);
            materia = v.findViewById(R.id.tvMateria);
            cfu = v.findViewById(R.id.tvCfu);
            rarImm = v.findViewById(R.id.iwRarity);
        }
    }

    public BookletAdapters(Vector<Voto> voti) {
        this.voti = voti;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.booklet_item, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.materia.setText(voti.get(i).getSubject());
        myViewHolder.voto.setText(Integer.toString(voti.get(i).getMark()));
        myViewHolder.cfu.setText(Integer.toString(voti.get(i).getCredits()) + " CFU");
        myViewHolder.rarImm.changeRarity(voti.get(i).getRarity());

        //Non dovrebbe servire più questa roba sulla rarità
        /*
        double rarita = 2 * (voti.get(i).getMark() - 17) / (3 * 14.0) + voti.get(i).getCredits() / (3 * 18.0);

        if (rarita > 0.2 * (voti.get(i).getRarity()) || rarita < 0.2 * (voti.get(i).getRarity()-1)){
            System.out.println(voti.get(i).getSubject());
            voti.get(i).setRarity(voti.get(i).getRarity() - 1);
        }
        */


    }

    @Override
    public int getItemCount() {
        return voti.size();
    }
}
