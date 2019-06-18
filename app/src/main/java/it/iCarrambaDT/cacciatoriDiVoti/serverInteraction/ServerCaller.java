package it.iCarrambaDT.cacciatoriDiVoti.serverInteraction;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import it.iCarrambaDT.cacciatoriDiVoti.entity.MateriaPlus;

public class ServerCaller {

    private static ServerCaller instance = null;

    private static final int SERVER_PORT = 1025;
    private static final String SERVER_IP = "eziodb.ddns.net";
    //TODO rimuovere localhost prima della deploy, verificare dominio
    //private static final String SERVER_IP = "160.80.133.20";

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

        DataInputStream dataRead;
        DataOutputStream dataWrite;

        String votoJson;

        Gson gson = new Gson();
        MateriaPlus materiaPlus;

        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        input = socket.getInputStream();
        output = socket.getOutputStream();

        //faccio sapere al client il tipo di laurea che possiede l'utente
        dataWrite = new DataOutputStream(output);

        dataWrite.writeUTF(laurea);
        dataWrite.flush();

        //assumo che l'invio dei dati avvenga in modo corretto,
        //mi fido del controllo degli errori del livello di collegamento
        //e di quello di TCP
        dataRead = new DataInputStream(input);

        //ricevo il voto (in formato json) dal server
        votoJson = dataRead.readUTF();

        //i dati saranno contenuti in un unica linea
        materiaPlus = gson.fromJson(votoJson, MateriaPlus.class);

        dataRead.close();

        //chiudo la connessione
        socket.close();

        //ok funge
        //System.out.println("SERVER CALLER RECIVED: \n\n\n"+votoJson+"\n\n\n");

        return materiaPlus;
    }
}
