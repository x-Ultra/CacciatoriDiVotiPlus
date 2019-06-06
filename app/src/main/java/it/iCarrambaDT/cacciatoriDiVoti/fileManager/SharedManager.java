package it.iCarrambaDT.cacciatoriDiVoti.fileManager;

import android.content.SharedPreferences;

public class SharedManager {

    private SharedPreferences shared;
    private String lastLogKey = "lastLog";
    private String lastVotoKey = "lastVoto";
    private String LATLON_SEPARATOR = "-";
    private String VOTO_SEPARATOR  = "-";

    public SharedManager(SharedPreferences shared){
        this.shared = shared;
    }

    public String getLastLog(){
        return shared.getString(lastLogKey, "null");
    }

    public void setLastLog(String value){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(lastLogKey, value);
        editor.apply();
    }

    public void setLastVoto(String value){
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(lastVotoKey, value);
        editor.apply();
    }

    public String[] getCoords(String key){
        return shared.getString(key, "notfound").split(LATLON_SEPARATOR);
    }


    public String[] getLastVoto(){
        return shared.getString(lastVotoKey, "null").split(VOTO_SEPARATOR);
    }

}
