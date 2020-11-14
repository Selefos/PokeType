package com.example.poketype.PlayerList.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for storing images to dynamicly created tables
 * originating from Player names in DatabaseHandler Activity
 *
 * Created by Andreas Giannakakis on 7/19/2020.
 *
 *
 */

public class PlayerImageDB extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "PlayerImage.db";
    public static String TABLE_PLAYER = "Player_Name";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "POKEMONNAME";
    public static final String COL_3 = "IMAGE_R";


    public PlayerImageDB(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        onCreate(db);
    }

    public void addTable(String PlayerName)
    {
        SQLiteDatabase db = getWritableDatabase();
        String createTablePlayer = "CREATE TABLE " + PlayerName + " (ID INTEGER PRIMARY KEY , " +
                "POKEMONNAME TEXT, IMAGE_R BLOB)";

        db.execSQL(createTablePlayer);
        db.close();
    }

    public void updateTable(String PlayerName, String PlayerNameUpdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateTablePlayer = "ALTER TABLE " + PlayerName + " RENAME TO " + PlayerNameUpdate;

        db.execSQL(updateTablePlayer);
        db.close();
    }

    public void deleteTable(String PlayerName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + PlayerName);
    }


    public List<String> GetTableNames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectTables = "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata'";
        Cursor cursor = db.rawQuery(selectTables, null);

        List<String> result = new ArrayList<String>();

        if(cursor.moveToFirst())
        {
            do{ result.add(cursor.getString(0));}while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return result;
    }

    public boolean insertStringImageToTable(String playerName, String name, byte[] image_r)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues playerValues = new ContentValues();

        playerValues.put(COL_2, name);
        playerValues.put(COL_3, image_r);

        long result_pkmName = db.insert(playerName, null, playerValues);

        db.close();
        if(result_pkmName == -1) {return false;} else {return true;}

    }

    public List<String> getNamesForGrid(String TableName)
    {
        /*------------------- SELECT ALL QUERIES -------------------*/
        List<String> list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TableName;
        Cursor cursor = db.rawQuery(selectQuery, null);

        /*-------- Loops through all rows and adds to list ---------*/
        if(cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1)); // Fetches Pokemon Name to cursor
            }while (cursor.moveToNext());
        }//ends IF

        /*-------------------- Close Connection --------------------*/
        cursor.close();
        db.close();

        return list;
    }

    public Cursor getImageforGrid(String TableName, String imageName)
    {

        TABLE_PLAYER = TableName;
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TableName;
        String[] getData = {PlayerImageDB.COL_3};
        String select = PlayerImageDB.COL_2 + " LIKE ?";
        String[] selection = {imageName};
        Cursor cursor = db.query(PlayerImageDB.TABLE_PLAYER, getData, select, selection, null, null, null);

        return cursor;
    }

    public Integer deleteImagesFromGrid(String TableName, String name)
    {
        TABLE_PLAYER = TableName;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PLAYER, "POKEMONNAME = ?", new String[]{name});

    }

}