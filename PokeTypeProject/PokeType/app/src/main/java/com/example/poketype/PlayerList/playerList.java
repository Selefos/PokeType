package com.example.poketype.PlayerList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.Touch;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poketype.PlayerList.Databases.DatabaseHandler;
import com.example.poketype.PlayerList.Databases.PlayerImageDB;
import com.example.poketype.PlayerList.Databases.PlayerLuckyDB;
import com.example.poketype.PlayerList.Databases.PlayerShinyDB;
import com.example.poketype.PokemonLists.pokemon_list;
import com.example.poketype.R;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main activity for player list entry
 * author Giannakakis Andreas
 * date:
 */

public class playerList extends AppCompatActivity
{
    /*DATABASES*/
    DatabaseHandler myDB;
    SQLiteDatabase sqlDB;
    PlayerImageDB plrImageDB;
    PlayerShinyDB plrShinyDB;
    PlayerLuckyDB plrLuckyDB;
    /*----------------------*/

    Button editvalue;
    Button editvalue2;
    Button editsave;
    Button editdelete;
    Button listGrid;

    TextView background;
    EditText editwin, editloss, editdays, editname, editteam, SearchRow, SearchRow2;
    String searchRow;
    Spinner spinnerPlayername;
    Spinner spinnerTeam;
    Switch switchEdit;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);

        FunctionsCall();

    }//ends onCreate


    public void FunctionsCall()
    {
        init();
        hideKeyboardUnfocus();
        onSave();
        spinnerPlayer();
        PopulateSpinner();
        PopulateTEAM();
        TeamSpinner();
        onDelete();
        editValuesSwitch();
        GridActivity();
    }//ends FunctionsCall


    public void init()
    {
        myDB = new DatabaseHandler(this);
        plrImageDB = new PlayerImageDB(this);
        plrShinyDB = new PlayerShinyDB(this);
        plrLuckyDB = new PlayerLuckyDB(this);

        editsave = (Button) findViewById(R.id.save);
        editdelete = (Button) findViewById(R.id.delete);
        listGrid = (Button) findViewById(R.id.pkmnlist);

        mLinearLayout = (LinearLayout) findViewById(R.id.LinearLayout);
        background = (TextView) findViewById(R.id.background);

        editname = (EditText) findViewById(R.id.PlayerText);
        editwin = (EditText) findViewById(R.id.editwins);
        editloss = (EditText) findViewById(R.id.editlosses);
        editdays = (EditText) findViewById(R.id.editdays);
        editteam = (EditText) findViewById(R.id.teamlist);
        SearchRow = (EditText) findViewById(R.id.SearchRow);
        SearchRow2 = (EditText) findViewById(R.id.SearchRow2);

        spinnerPlayername = (Spinner) findViewById(R.id.spinnerPlayer);
        spinnerTeam = (Spinner) findViewById(R.id.spinnerTeam);
        switchEdit = (Switch) findViewById(R.id.edit_values);

    }//ends Init


    @SuppressLint("ClickableViewAccessibility")
    public void hideKeyboardUnfocus()
    {
        mLinearLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                //unfocus edittexts
                clearEditTextFocus();

                return false;
            }

        });
    }


    public void clearEditTextFocus()
    {
        editname.clearFocus();
        editwin.clearFocus();
        editloss.clearFocus();
        editdays.clearFocus();
    }


    public void editValuesSwitch()
    {
        switchEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked == true)
                {
                    editwin.setEnabled(true);
                    editloss.setEnabled(true);
                    editdays.setEnabled(true);
                    editwin.requestFocus();
                }

                if(isChecked == false)
                {
                    editwin.setEnabled(false);
                    editloss.setEnabled(false);
                    editdays.setEnabled(false);
                }
            }
        });
    }


    public void InsertData()
    {
        boolean isInserted = myDB.insertData(
                editname.getText().toString().trim(),
                editwin.getText().toString().trim(),
                editloss.getText().toString().trim(),
                editdays.getText().toString().trim(),
                editteam.getText().toString().trim()
        );

        if (isInserted) { Toast.makeText(playerList.this, "Data Inserted", Toast.LENGTH_SHORT).show();}
        else { Toast.makeText(playerList.this, "Data Congestion", Toast.LENGTH_SHORT).show();}

        PopulateSpinner();
    }//ends InsertData


    public void UpdateData()
    {
        //needs ID to recognise column
        boolean isUpdated = myDB.updateData(
                SearchRow.getText().toString().trim(),
                editname.getText().toString().trim(),
                editwin.getText().toString().trim(),
                editloss.getText().toString().trim(),
                editdays.getText().toString().trim(),
                editteam.getText().toString().trim()
        );

        if (isUpdated) { Toast.makeText(playerList.this, "Data Updated", Toast.LENGTH_SHORT).show();}
        else { Toast.makeText(playerList.this, "Update Failed", Toast.LENGTH_SHORT).show();}

        PopulateSpinner();
    }//ends UpdateData


    @SuppressLint("ClickableViewAccessibility")
    public void onSave()
    {

        editsave.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    editsave.setTextColor(Color.parseColor("#02E1FD"));


                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    /**
                     * For Database Insertion, Update
                     *
                     * compareNameString gets string from EditText field
                     * compareCursorString gets string from -Cursor- originated from LoadData function
                     * compareDuplicateInDB gets string from Object[] bellow
                     *
                     * InsertData method is called when ( name on EditText ) != (name on -Cursor-) && not null
                     * Update method is called when ( name on EditText ) == ( name on -Cursor- ) && not null
                     * while the -Cursor- and the EditText align
                     *
                     * In order to avoid crash on duplicate entry while the -Cursor- and the EditText don't align
                     * both strings of these entities have to be compared with the stored string of Object[]
                     * If the string of the Object[] == EditText, the value is stored to compareDuplicateInDB
                     *
                     * If -Cursor- and EditText strings don't align and -Cursor- position != to Object[index]
                     * ( essentially if -Cursor-[position] != Array[position] )
                     * Then prevent name duplication in Database
                     *
                     */


                    //For comparison editname string with string in cursor as fetched in LoadData function
                    String compareEditTextString = editname.getText().toString();
                    String compareCursorString = searchRow;


                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    List<String> compare = new ArrayList<String>();
                    compare = db.getColumnNames();


                    //transfers List<String> values to String Array Object[]
                    Object[] stringArray = compare.toArray();
                    String compareDuplicateInDB = null;


                    //search Text input for symbols or spaces. If true prevent insertion to database
                    Pattern findSymbol = Pattern.compile("[!@#$%&`'\"\\\\()/.,_+=\\s|<>?{}\\[\\]~-]");
                    Matcher inspectChar = findSymbol.matcher(compareEditTextString);
                    boolean checkForSymbol = inspectChar.find();


                    if(compareEditTextString.isEmpty())
                    {
                        //checks for empty text and prevents crash
                        if (editname.getText().toString().isEmpty())
                        {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Toast.makeText(getApplicationContext(), "No Entry To Save", Toast.LENGTH_SHORT).show();
                        }
                    }


                    if(!compareEditTextString.isEmpty())
                    {

                        //compares string from EditText to Object[] -> (string array)
                        for (int i = 0; i < stringArray.length; i++)
                        {
                            String ignoreCase = stringArray[i].toString();
                            if (compareEditTextString.equalsIgnoreCase(ignoreCase))
                            {
                                compareDuplicateInDB = stringArray[i].toString();
                            }
                        }

                        //Check if name starts with number and prevent insertion
                        char stringPositionOne = 0;
                        for(int i = 0; i <= 10; i++)
                        {
                            char index = (char)(i + '0'); // ASCII code if 0 = 48 this way the char is printable and detectable
                            // eg. for i = 1 + ASCII(48) -> index = ASCII(49) = char '1'
                            if(compareEditTextString.charAt(0) == index)
                            {
                                stringPositionOne = index;
                                Toast.makeText(getApplicationContext(), "Name cannot start with a number", Toast.LENGTH_SHORT).show();
                            }
                        }

                        //compares EditText string with Object[] index string && -Cursor- string with Object[] index string
                        if (compareEditTextString.equalsIgnoreCase(compareDuplicateInDB) && !compareCursorString.equalsIgnoreCase(compareDuplicateInDB)
                                && !editname.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Duplicate Insertion Prevented", Toast.LENGTH_SHORT).show();
                        }


                        //update data if text(x) == text(x) && not null
                        if (compareEditTextString.equalsIgnoreCase(compareCursorString) && !editname.getText().toString().isEmpty() && !checkForSymbol)
                        {
                            UpdateData();
                        }


                        //insert data and create table if text(x) != text
                        if (!compareEditTextString.equalsIgnoreCase(compareCursorString) && !editname.getText().toString().isEmpty()
                                && !compareEditTextString.equalsIgnoreCase(compareDuplicateInDB) && !checkForSymbol
                                && compareEditTextString.charAt(0) != stringPositionOne)
                        {
                            InsertData();
                            createTable();
                        }


                        if (checkForSymbol)
                        {
                            Toast.makeText(getApplicationContext(), "Name cannot include symbols or spaces", Toast.LENGTH_SHORT).show();
                        }

                    }

                    clearEditTextFocus();
                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    editsave.setTextColor(Color.parseColor("#FFFFFF"));
                }

                return false;
            }
        });

    }//ends onSave


    public void DeleteData()
    {
        Integer deleteRows = myDB.deleteData(editname.getText().toString());
        if(deleteRows > 0) { Toast.makeText(playerList.this, "Player Removed", Toast.LENGTH_SHORT).show(); }//ends IF
        else { Toast.makeText(playerList.this, "Deletion Failed", Toast.LENGTH_SHORT).show(); }//ends ELSE
        PopulateSpinner();
    }//ends DeleteData


    @SuppressLint("ClickableViewAccessibility")
    public void onDelete()
    {

        editdelete.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    editdelete.setTextColor(Color.parseColor("#02E1FD"));

                if(event.getAction() == MotionEvent.ACTION_UP)
                {

                    //Compares editname string with string in cursor as fetched in LoadData function
                    String compareNameString = editname.getText().toString();
                    String compareCursorString = searchRow;

                    if(compareNameString.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"No Entry To Delete",Toast.LENGTH_SHORT).show();
                    }


                    if(compareNameString.equalsIgnoreCase(compareCursorString))
                    {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(playerList.this);
                        builder.setMessage("Remove Player?");
                        builder.setCancelable(true);

                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int i)
                            {
                                DeleteData();
                                dropTable();


                                //if all player entries are deleted from database
                                if(spinnerPlayername.getCount() == 0)
                                {
                                    editname.setText("");
                                    recreate();
                                }

                            }
                        });


                        builder.setPositiveButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    editdelete.setTextColor(Color.parseColor("#FFFFFF"));
                }
                return false;
            }
        });

    }//ends onDelete


    public void LoadData()
    {
        searchRow = editname.getText().toString();
        myDB = new DatabaseHandler(getApplicationContext());
        sqlDB = myDB.getReadableDatabase();

        Cursor cursor = myDB.loadData(searchRow, sqlDB);

        if(cursor.moveToFirst())
        {
            String win = cursor.getString(0);
            String loss = cursor.getString(1);
            String day = cursor.getString(2);
            String team = cursor.getString(3);

            editwin.setText(win);
            editloss.setText(loss);
            editdays.setText(day);
            SearchRow2.setText(team);
        }
    }//ends LoadData


    public void PositionSetTeam()
    {
        searchRow = editname.getText().toString(); //also stores the editext to string for string comparisons in other functions
        myDB = new DatabaseHandler(getApplicationContext());
        sqlDB = myDB.getReadableDatabase();
        Cursor cursor = myDB.positionSetTeam(searchRow, sqlDB);

        if(cursor.moveToFirst())
        {
            String team = cursor.getString(0);
            SearchRow2.setText(team);
        }
    }//ends PositionSetTeam


    public void PositionSetID()
    {
        //used for the update method
        searchRow = editname.getText().toString();
        myDB = new DatabaseHandler(getApplicationContext());
        sqlDB = myDB.getReadableDatabase();
        Cursor cursor = myDB.positionSetID(searchRow, sqlDB);

        if(cursor.moveToFirst())
        {
            String id = cursor.getString(0);
            SearchRow.setText(id);
        }
    }//ends PositionSetID


    private void PopulateSpinner()
    {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> list = db.populateSpinner(); // spinner drop down elements

        //Setting Adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_look, list);

        //Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.dropdown_spinner);

        //Attaching data to spinner
        spinnerPlayername.setAdapter(adapter);
    }//ends populateSpinner


    private void PopulateTEAM()
    {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<String> list = db.populateTeam(); // spinner drop down elements

        //Setting Adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_look, list);

        //Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.dropdown_spinner);

        //Attaching data to spinner
        spinnerTeam.setAdapter(adapter);

    }//ends populateSpinner


    @SuppressLint("ClickableViewAccessibility")
    public void GridActivity()
    {

        listGrid.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    listGrid.setTextColor(Color.parseColor("#02E1FD"));

                if(event.getAction() == MotionEvent.ACTION_UP)
                {

                    listGrid.setTextColor(Color.parseColor("#FFFFFF"));
                    PlayerImageDB db = new PlayerImageDB(getApplicationContext());
                    List<String> NameList = db.GetTableNames();
                    Object[] namesCount = NameList.toArray();

                    if(namesCount.length == 0)
                    {

                        listGrid.setTextColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(getApplicationContext(), "No players saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), pokemon_list.class);
                        startActivity(intent);
                    }

                }

                return false;
            }
        });


    }//ends GridActivity


    public void spinnerPlayer()
    {

        spinnerPlayername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected (AdapterView < ? > parent, View view,int position, long id)
            {
                //Sets spinner selected name to EditText from spinnerPlayerName dropdown menu
                String pName = parent.getItemAtPosition(position).toString();//get spinner text position
                editname.setText(pName);

                LoadData();
                PopulateTEAM();
                PositionSetTeam();
                PositionSetID();


                //changes the background
                String instinct = "Instinct";
                String mystic = "Mystic";
                String valor = "Valor";

                if (SearchRow2.getText().toString().equals(instinct))
                {
                    background.setBackgroundResource(R.drawable.instinct);
                }
                if (SearchRow2.getText().toString().equals(mystic))
                {

                    background.setBackgroundResource(R.drawable.mystic);
                }
                if (SearchRow2.getText().toString().equals(valor))
                {

                    background.setBackgroundResource(R.drawable.valor);
                }

                //sets spinner position according to Team name that is assigned in the database
                spinnerTeam.setSelection(spinnerTeamPosition(spinnerTeam, SearchRow2.getText().toString()));

            }


            public void onNothingSelected (AdapterView < ? > parent)
            {
                return;
            }

        });
    }


    public void TeamSpinner()
    {
        spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            //Set background according to respective team, fetches data from spinnerTeam

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String tName = parent.getItemAtPosition(position).toString();//get spinner text position
                editteam.setText(tName);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {return;}

        });
    }//ends TeamSpinner


    private int spinnerTeamPosition(@NonNull Spinner spinner, String teamName)
    {
        //sets spinner position according to Team name that is assigned in the database

        int index = 0;

        for(int i = 0; i< spinner.getCount(); i++)
        {
            if(spinner.getItemAtPosition(i).equals(teamName)) index = i;
        }

        return index;
    }


    /*------- Dynamic Database for Name Tables PlayerImageDB.db / PlayerShinyDB.db / PlayerLuckyDB.db -------*/

    public void createTable()
    {
        plrImageDB.addTable(editname.getText().toString().trim());
        plrShinyDB.addTableS(editname.getText().toString().trim());
        plrLuckyDB.addTableL(editname.getText().toString().trim());
    }

    public void dropTable()
    {
        plrImageDB.deleteTable(editname.getText().toString().trim());
        plrShinyDB.deleteTableS(editname.getText().toString().trim());
        plrLuckyDB.deleteTableL(editname.getText().toString().trim());
    }


    /*-------------------------------------------------------------------------------------------------------*/
}