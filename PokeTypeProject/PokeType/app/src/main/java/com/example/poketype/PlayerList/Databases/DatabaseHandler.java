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
 * Database creation for player names and stats
 * @author Giannakakis Andreas
 * date:
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "PlayerList.db";
    public static final String TABLE_PLAYER = "Player_Name";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PLAYER";
    public static final String COL_3 = "WINS";
    public static final String COL_4 = "LOSSES";
    public static final String COL_5 = "DAYS_TO_BEST";
    public static final String COL_6 = "PLAYER_TEAM";
    public static final String TABLE_TEAM = "Team_Name";
    public static final String COL_7 = "ID2";
    public static final String COL_8 = "TEAM";


    public DatabaseHandler(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTablePlayer = " CREATE TABLE " + TABLE_PLAYER + " (ID INTEGER PRIMARY KEY, " +
                "PLAYER TEXT , WINS INTEGER, LOSSES INTEGER, DAYS_TO_BEST INTEGER, PLAYER_TEAM)";
        db.execSQL(createTablePlayer);


        //created for spinner population
        String createTableTeam = " CREATE TABLE " + TABLE_TEAM + " (ID2 INTEGER PRIMARY KEY, " +
                "TEAM TEXT)";
        db.execSQL(createTableTeam);


        db.execSQL("INSERT INTO " + TABLE_TEAM + "(TEAM) VALUES ('Instinct')");
        db.execSQL("INSERT INTO " + TABLE_TEAM + "(TEAM) VALUES ('Mystic')");
        db.execSQL("INSERT INTO " + TABLE_TEAM + "(TEAM) VALUES ('Valor')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        onCreate(db);
    }


    public boolean insertData(String name, String wins, String losses, String days, String team)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //insert data on TABLE_PLAYER
        ContentValues playerValues = new ContentValues();
        playerValues.put(COL_2, name);
        playerValues.put(COL_3, wins);
        playerValues.put(COL_4, losses);
        playerValues.put(COL_5, days);
        playerValues.put(COL_6, team);
        long result_player = db.insert(TABLE_PLAYER, null, playerValues);


        db.close();
        if(result_player == -1) {return false;} else {return true;}

    }//ends insertData


    public boolean updateData(String id, String name, String wins, String losses, String days, String team)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues playerValues = new ContentValues();
        playerValues.put(COL_1, id);
        playerValues.put(COL_2, name);
        playerValues.put(COL_3, wins);
        playerValues.put(COL_4, losses);
        playerValues.put(COL_5, days);
        playerValues.put(COL_6, team);
        db.update(TABLE_PLAYER, playerValues, "ID = ?", new String[] {id});
        return true;
    }//ends updateData


    public Integer deleteData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PLAYER, "PLAYER = ?", new String[]{name});
    }//ends deleteData


    public Cursor loadData(String plr_name, SQLiteDatabase db)
    {
        //Load data from TABLE_PLAYER
        String[] getData = {DatabaseHandler.COL_3, DatabaseHandler.COL_4, DatabaseHandler.COL_5, DatabaseHandler.COL_6};
        String select = DatabaseHandler.COL_2 + " LIKE ?";
        String[] selection = {plr_name};
        Cursor cursor = db.query(DatabaseHandler.TABLE_PLAYER, getData, select, selection, null, null, null);
        return cursor;
    }// ends loadData


    public Cursor positionSetTeam(String team, SQLiteDatabase db)
    {
        String[] getID = {DatabaseHandler.COL_6};
        String select = DatabaseHandler.COL_2 + " LIKE ?";
        String[] selection = {team};
        Cursor cursor = db.query(DatabaseHandler.TABLE_PLAYER, getID, select, selection,null,null,null);
        return cursor;
    }//ends positionSetITeam


    public Cursor positionSetID(String id, SQLiteDatabase db)
    {
        String[] getID = {DatabaseHandler.COL_1};
        String select = DatabaseHandler.COL_2 + " LIKE ?";
        String[] selection = {id};
        Cursor cursor = db.query(DatabaseHandler.TABLE_PLAYER, getID, select, selection,null,null,null);
        return cursor;
    }//ends positionSetITeam


    public List<String> populateSpinner()
    {
        /*------------------- SELECT ALL QUERIES -------------------*/
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        /*-------- Loops through all rows and adds to list ---------*/
        if(cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1)); // Fetches Player Name to cursor
            }while (cursor.moveToNext());
        }//ends IF

        /*-------------------- Close Connection --------------------*/
        cursor.close();
        db.close();

        return list;
    } //ends populateSpinner


    public List<String> populateTeam()
    {
        /*------------------- SELECT ALL QUERIES -------------------*/
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_TEAM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        /*-------- Loops through all rows and adds to list ---------*/
        if(cursor.moveToFirst())
        {
            do
            {
                list.add(cursor.getString(1)); // Fetches Team Name to cursor
            }while (cursor.moveToNext());
        }//ends IF

        /*-------------------- Close Connection --------------------*/
        cursor.close();
        db.close();

        return list;
    } //ends populateSpinner


    public List<String> getColumnNames()
    {
        //used to check for duplicate Names in COL_2

        List<String> colNames = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYER;
        Cursor crs = db.rawQuery(selectQuery, null);

        if(crs.moveToFirst())
        {
            do
            {
                colNames.add(crs.getString(1));
            }while (crs.moveToNext());
        }//ends IF

        crs.close();
        db.close();

        return colNames;
    }

}