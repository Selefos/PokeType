package com.example.poketype;

import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AboutInfo extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        textInfo();
    }

    public void textInfo()
    {
        TextView aboutinfo = (TextView) findViewById(R.id.about_info);
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        ArrayList lineCnt = new ArrayList();

        try
        {
            //get the number of lines in the .txt
            InputStream inputString = getAssets().open("aboutInfo.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputString));

            if(!bufferedReader.ready())
            {throw new IOException();}

            while((line = bufferedReader.readLine()) != null)
            {
                lineCnt.add(line);

            }
            bufferedReader.close();

        } catch (IOException e)
        {
            e.printStackTrace();
            /*System.out.println(e);
            Toast.makeText(getApplicationContext(),"Index Positions: \n" + e, Toast.LENGTH_LONG).show();*/
        }


        try
        {
            //reads the lines in .txt
            InputStream inputString = getAssets().open("aboutInfo.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputString));
            for (int i = 0; i < lineCnt.size(); i++)
            {
                line = reader.readLine();
                stringBuffer.append(line + "\n");
            }
            reader.close();

            //sets the text to Scroll View
            aboutinfo.setText(stringBuffer.toString());
        }
        catch (IOException e){e.printStackTrace();}
    }
}