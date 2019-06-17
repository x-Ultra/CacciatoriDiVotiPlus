package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import it.iCarrambaDT.cacciatoriDiVoti.R;

public class WaitingServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_server);

        ImageView gifImageView = findViewById(R.id.gifImageView);

        Glide.with(this).asGif().load(R.raw.loading).into(gifImageView);
    }
}
