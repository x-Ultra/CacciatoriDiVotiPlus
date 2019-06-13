package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.GifImageView;

public class WaitingServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_server);

        GifImageView gifImageView = findViewById(R.id.gifImageView);
        //gifImageView.setGifImageResource(R.drawable.loading);
        Uri a = Uri.parse("https://media.giphy.com/media/13gvXfEVlxQjDO/giphy.gif");
        gifImageView.setGifImageUri(a);

    }
}
