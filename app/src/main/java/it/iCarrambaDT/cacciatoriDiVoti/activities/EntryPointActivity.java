package it.iCarrambaDT.cacciatoriDiVoti.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.iCarrambaDT.cacciatoriDiVoti.R;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyButton;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyEditText;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MySpinner;
import it.iCarrambaDT.cacciatoriDiVoti.customViews.MyTextView;
import it.iCarrambaDT.cacciatoriDiVoti.fileManager.SharedManager;
import static java.security.AccessController.getContext;


public class EntryPointActivity extends AppCompatActivity  implements View.OnClickListener{

    private TextView tVSpinner;
    private MyTextView welcome;
    private MyTextView textWelcome;
    private MyEditText nomeStudente;
    private MyButton completeButton;
    private MySpinner spinner;
    private List<String> plantsList;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ImageView logoUni;
    private String[] plants;
    private String laurea;
    private String userName;
    private SharedPreferences prefs;
    private SharedManager managLaurea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);

        //opacizzo l'immagine
        logoUni = (ImageView) findViewById(R.id.imageView2);
        logoUni.setImageAlpha(127);

        //dichiaro le varie View
        welcome = (MyTextView) findViewById(R.id.welcomeTitle);
        textWelcome = (MyTextView) findViewById(R.id.welcomeText);
        nomeStudente = (MyEditText) findViewById(R.id.username);
        completeButton = (MyButton) findViewById(R.id.signedComplete);
        completeButton.setOnClickListener(this);
        spinner = (MySpinner) findViewById(R.id.lauree);

        // Riuscire a mettere questo dentro a un Xml per tradurre
        plants = new String[]{
                "Choose your Degree...",
                "Ingegneria informatica",
                "Economia",
                "Medicina",
        };

        plantsList = new ArrayList<>(Arrays.asList(plants));

        // Initializzzo an ArrayAdapter con le lauree
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0 )
                {
                    // Il primo Item lo disabilito per farlo hint
                    return false;
                }
                else
                {

                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0 ){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    tVSpinner = findViewById(R.id.tVSpinner);

                    laurea = selectedItemText;
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    tVSpinner.setTextColor(Color.BLACK);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (laurea != null && nomeStudente.getText().length()>0){
            Toast.makeText
                    (getApplicationContext(), "Vado da Ezio " + nomeStudente.getText() + laurea, Toast.LENGTH_SHORT)
                    .show();

            //Salvo nelle Shared il nome...
            SharedPreferences shared = getSharedPreferences("userData", MODE_PRIVATE);
            SharedManager manager = new SharedManager(shared);

            manager.setUserDataInfo(nomeStudente.getText().toString());

            //...e la laurea

            SharedPreferences prefs = getSharedPreferences("laurea", MODE_PRIVATE);
            SharedManager managerLaurea = new SharedManager(prefs);

            managerLaurea.setLaurea(laurea);

            startActivity(new Intent(this, MainActivity.class));

        }



    }
}
