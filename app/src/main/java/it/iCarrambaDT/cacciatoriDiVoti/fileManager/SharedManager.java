package it.iCarrambaDT.cacciatoriDiVoti.fileManager;

import android.content.SharedPreferences;

public class SharedManager {

    private SharedPreferences shared;
    private String userDataKey = "useraname";
    private String laureaKey = "laurea";
    private String lastVotoKey = "lastVoto";
    private String LATLON_SEPARATOR = "-";
    private String VOTO_SEPARATOR  = "-";

    public static final String nomeUtente = "nomeUtente";

    public SharedManager(SharedPreferences shared){
        this.shared = shared;
    }

    public String getLaurea(){
        return shared.getString(laureaKey, "null");
    }

    public void setLaurea(String value){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(laureaKey, value);
        editor.apply();
    }

    public void setLastVoto(String value){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(lastVotoKey, value);
        editor.apply();
    }

    public String getUserDataInfo(String value){
        return shared.getString(value, "notfound");
    }

    public void setUserDataInfo(String value){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(userDataKey, value);
        editor.apply();

    }
    public String[] getCoords(String key){
        return shared.getString(key, "notfound").split(LATLON_SEPARATOR);
    }


    public String[] getLastVoto(){
        return shared.getString(lastVotoKey, "null").split(VOTO_SEPARATOR);
    }

}
