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
    private String[] COL_ATTRS = null;
    private static final int DB_VERSION = 1;


    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public DBHelper(Context context, String[] COL_ATTRS){
        super(context, DB_NAME, null, DB_VERSION);
        this.COL_ATTRS = COL_ATTRS;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;

        query = "CREATE TABLE "+ TABLE_NAME +" ("+
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ";
        for(String attr: COL_ATTRS){
            query += attr + " TEXT, ";
        }
        //deleting  last ','
        query = query.substring(0, query.length()-2);
        query += ");";

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

    public void updateVoto(String voto, String materia){
        String query;
        query = "UPDATE "+TABLE_NAME+" SET Voto = '"+voto+"' WHERE Materia = '"+materia+"';";
        this.getWritableDatabase().execSQL(query);
    }

    public void updateRarita(String rarita, String materia){
        String query;
        query = "UPDATE "+TABLE_NAME+" SET Rarita = '"+rarita+"' WHERE Materia = '"+materia+"';";
        this.getWritableDatabase().execSQL(query);
    }

    public Vector<Voto> getObtainedVoti(){
        String query;
        query = "SELECT * FROM "+TABLE_NAME+" WHERE Voto <> 0;";

        Cursor cursor = this.getWritableDatabase().rawQuery(query, null);

        if (!(cursor.moveToFirst())){
            return new Vector<>();
        }

        Vector<Voto> vectVoto = new Vector<>();
        do {

            vectVoto.add( new Voto(cursor.getString(cursor.getColumnIndex("Materia")),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Crediti"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Voto"))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita")))));

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
                Integer.parseInt(cursor.getString(cursor.getColumnIndex("Rarita"))));

        cursor.close();
        return voto;
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //to fill later
    }
}
