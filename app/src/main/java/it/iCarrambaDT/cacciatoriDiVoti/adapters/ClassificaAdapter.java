package it.iCarrambaDT.cacciatoriDiVoti.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.RarityImageView;
import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class ClassificaAdapter extends RecyclerView.Adapter<ClassificaAdapter.MyViewHolder>  {

    private Vector<Voto> voti;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyTextView cfu;
        public MyTextView materia;
        public MyTextView voto;
        public MyTextView tempoCattura;

        public MyViewHolder(View v) {
            super(v);
            voto = v.findViewById(R.id.tvVotoCL);
            materia = v.findViewById(R.id.tvMateriaCL);
            cfu = v.findViewById(R.id.tvPostoCL);
            tempoCattura = v.findViewById(R.id.tvTempoCL);
        }
    }

    public ClassificaAdapter(Vector<Voto> voti) {
        this.voti = voti;
    }

    @Override
    public ClassificaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.classifica_item, viewGroup, false);

        return new ClassificaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassificaAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.materia.setText(voti.get(i).getSubject());
        myViewHolder.voto.setText(Integer.toString(voti.get(i).getMark()));
        myViewHolder.cfu.setText(Integer.toString(i+1) + ".");
        myViewHolder.tempoCattura.setText(Voto.catturaToString(voti.get(i).getTempoCattura()));

    }

    @Override
    public int getItemCount() {
        return voti.size();
    }

}
