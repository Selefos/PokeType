package com.example.poketype.PokemonLists;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.poketype.AboutInfo;
import com.example.poketype.PlayerList.Databases.PlayerImageDB;

import com.example.poketype.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.poketype.PokemonLists.main.SectionsPagerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class pokemon_list extends AppCompatActivity
{

    /*DATABASES*/
    private Spinner PlayerForGrids;
    /*----------------------------*/

    //for selecting name in Fragments
    String value;
    ViewPager viewPager;
    FloatingActionButton info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), value);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Keyboard hover over interface
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        FunctionsCall();

    }


    public void FunctionsCall()
    {
        Init();
        SetTableNamesToSpinner();
        PassSpinnerName();
        infoActivity();
    }//ends FuncionsCall


    public void Init()
    {
        PlayerForGrids = (Spinner) findViewById(R.id.PlayerNamesForGrids);
        info = (FloatingActionButton) findViewById(R.id.info);
    }//ends Init


    private void SetTableNamesToSpinner()
    {
        PlayerImageDB db = new PlayerImageDB(getApplicationContext());
        List<String> NameList = db.GetTableNames();//spinner elements

        //Set adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner2_look, NameList);

        //Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.dropdown_spinner2);

        //Attaching data to spinner
        PlayerForGrids.setAdapter(adapter);
    }


    public void PassSpinnerName()
    {
        PlayerForGrids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Sets spinner selected name to string from PlayerForGrids dropdown menu and passes values
                value = parent.getItemAtPosition(position).toString();//get spinner text position
                ((SectionsPagerAdapter)viewPager.getAdapter()).setValue(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                return;
            }

        });
    }


    public void infoActivity()
    {

        info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), AboutInfo.class);
                startActivity(intent);
            }
        });
    }

}