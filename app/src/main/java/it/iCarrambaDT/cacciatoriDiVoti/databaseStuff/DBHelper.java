package it.iCarrambaDT.cacciatoriDiVoti.databaseStuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.Random;
import java.util.Vector;

import it.iCarrambaDT.cacciatoriDiVoti.entity.Voto;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DataBaseVoti";
    public static final String TABLE_NAME = "Voti";
    static final String ID = "_id";
    //private String[] COL_ATTRS = null;
    private static final int DB_VERSION = 1;


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
/*
    public DBHelper(Context context, String[] COL_ATTRS){
        super(context, DB_NAME, null, DB_VERSION);
        //this.COL_ATTRS = COL_ATTRS;
    }
*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        query = "CREATE TABLE "+ TABLE_NAME +" ("+
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "Materia TEXT, " +
                "Voto TEXT, " +
                "Crediti TEXT, " +
                "Rarita TEXT, " +
                "TempoCattura INTEGER);";

        try {
            db.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(){

        String query = "SELECT * FROM "+TABLE_NAME+";";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() == 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;

    }

    //non dovrebbe servire più
    /*
    public void populate(SQLiteDatabase db, String[] line) {

        ContentValues values = new ContentValues();
        int i = 0;

        for (String element : line) {
            values.put(COL_ATTRS[i], element);
            i++;
        }
        try {
            db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

    //Cambiata leggermente - Aggiunto il tempo di cattura
    public void updateVoto(String voto, String materia, String tempoCattura, String rarita){
        String query;
        query = "UPDATE "+TABLE_NAME+" SET Voto = '"+voto+"', TempoCattura = '"+tempoCattura+"', Rarita = '"+rarita+"' WHERE Materia = '"+materia+"';";
        this.getWritableDatabase().execSQL(query);
    }

    public void insertNewVoto(String voto, String materia, String tempoCattura, String crediti, String rarita) {
        if (getVoto(materia) == null) {
            String query;
            query = "INSERT INTO " + TABLE_NAME + "(Voto,Materia,Rarita,Crediti,TempoCattura) VALUES('" + voto + "','" + materia + "','" + rarita + "','" + crediti + "','" + tempoCattura + "');";
            this.getWritableDatabase().execSQL(query);
        } else {
            updateVoto(voto,materia,tempoCattura,rarita);
        }
    }

    //Non dovrebbe servire più
    /*
    public void updateRarita(String rarita, String materia){
        String query;
        query = "UPDATE "+TABLE_NAME+" SET Rarita = '"+rarita+"' WHERE Materia = '"+materia+"';";
        this.getWritableDatabase().execSQL(query);
    }
    */

    public Vector<Voto> getVotiInOrder(String rarity) {
        String query;
        query = "SELECT * FROM "+TABLE_NAME+" WHERE Rarita='"+rarity+"' ORDER BY TempoCattura LIMIT 10;";

        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);

        if (!(cursor.moveToFirst())){
            return new Vector<>();
        }

        Vector<Voto> vectVoto = new Vector<>();
        do {

            vectVoto.add( new Voto(cursor.getString(cursor.getColumnIndex("Materia")),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Crediti"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Voto"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita"))),
                    cursor.getInt(cursor.getColumnIndex("TempoCattura"))));

        }while(cursor.moveToNext());

        cursor.close();

        return vectVoto;


    }
    public Vector<Voto> getObtainedVoti(){
        String query;
        query = "SELECT * FROM "+TABLE_NAME+";";

        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);

        if (!(cursor.moveToFirst())){
            return new Vector<>();
        }

        Vector<Voto> vectVoto = new Vector<>();
        do {

            vectVoto.add( new Voto(cursor.getString(cursor.getColumnIndex("Materia")),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Crediti"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Voto"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita"))),
                    cursor.getInt(cursor.getColumnIndex("TempoCattura"))));

        }while(cursor.moveToNext());

        cursor.close();

        return vectVoto;
    }

    //data nome materia ritornare classe voto
    public Voto getVoto(String materia){
        String query;
        query = "SELECT * FROM "+TABLE_NAME+" WHERE Materia = '"+materia+"';";

        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0){
            return null;
        }

        Voto voto = new Voto(cursor.getString(cursor.getColumnIndex("Materia")),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Crediti"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Voto"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita"))),
                cursor.getInt(cursor.getColumnIndex("TempoCattura")));
        cursor.close();
        return voto;
    }

    //Non dovrebbe più essere usata
    /*
    //dare voto (classe) con rarità minore o uguale ad uno dato
    public Voto getVotoRarLessEqual(int num){
        String query;
        query = "SELECT * FROM "+TABLE_NAME+" WHERE Rarita <= "+num+" AND Voto <> 31;";
        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0){
            return null;
        }

        cursor.moveToPosition(new Random().nextInt(cursor.getCount()));

        Voto voto = new Voto(cursor.getString(cursor.getColumnIndex("Materia")),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Crediti"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Voto"))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita"))));

        cursor.close();
        return voto;
    }
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to fill later
    }
}
