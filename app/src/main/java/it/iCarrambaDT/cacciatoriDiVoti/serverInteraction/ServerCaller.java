package it.iCarrambaDT.cacciatoriDiVoti.serverInteraction;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;

public class ServerCaller {

    private static ServerCaller instance = null;

    private static final int SERVER_PORT = 1025;
    private static final String SERVER_IP = "eziodb.ddns.net";

    private ServerCaller(){
    }

    public static ServerCaller getInstance() {
        if(instance == null){
            instance = new ServerCaller();
        }

        return instance;
    }

    //I VALORI DI LAUREA CHE IL SERVER RICONOSCE SONO:
    // eco
    // med
    // ing_info
    public MateriaPlus getVotoFromServer(String laurea) throws IOException {

        InputStream input;
        OutputStream output;

        BufferedWriter buffWrite;
        BufferedReader buffRead;

        String votoJson;

        Gson gson = new Gson();
        MateriaPlus materiaPlus;

        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        input = socket.getInputStream();
        output = socket.getOutputStream();

        //faccio sapere al client il tipo di laurea che possiede l'utente
        buffWrite = new BufferedWriter(new OutputStreamWriter(output));

        buffWrite.write(laurea);
        //assumo che l'invio dei dati avvenga in modo corretto,
        //mi fido del controllo degli errori del livello di collegamento
        //e di quello di TCP

        buffRead = new BufferedReader(new InputStreamReader(input));
        //ricevo il voto (in formato json) dal server
        votoJson = buffRead.readLine();
        //i dati saranno contenuti in un unica linea

        materiaPlus = gson.fromJson(votoJson, MateriaPlus.class);

        input.close();
        output.close();

        //chiudo la connessione
        socket.close();

        return materiaPlus;
    }
}
