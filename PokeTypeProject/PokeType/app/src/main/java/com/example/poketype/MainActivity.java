package com.example.poketype;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poketype.PlayerList.Databases.PlayerImageDB;
import com.example.poketype.PlayerList.playerList;
import com.example.poketype.PokemonLists.pokemon_list;

public class MainActivity extends AppCompatActivity {

    Button bt;
    Button bt1;
    Button battle;
    Button listActivity;
    Button tradeList;
    TextView btType,btType2,btType3;
    int firstType, secondType, thirdType;
    Random num = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    public void Init()
    {
        bt = (Button) findViewById(R.id.btn);
        bt1 = (Button) findViewById(R.id.btn1);
        battle = (Button) findViewById(R.id.battle);
        listActivity = (Button) findViewById(R.id.players);
        tradeList = (Button) findViewById(R.id.TradeList);
        btType = (TextView) findViewById(R.id.type);
        btType2 = (TextView) findViewById(R.id.type1);
        btType3 = (TextView) findViewById(R.id.type2);
        ImageView bg = (ImageView) findViewById(R.id.ImageBG);
        bg.setScaleType(ImageView.ScaleType.CENTER);
        BattleButton();
        PlayerButton();
        TradeListActivity();

    }

    //Enable-Disable Trade List button

    private void TradeListOnOff()
    {
        PlayerImageDB db = new PlayerImageDB(getApplicationContext());
        List<String> NameList = db.GetTableNames();
        Object[] namesCount = NameList.toArray();

        if(namesCount.length == 0)
        {
            tradeList.setEnabled(false);
//            Toast.makeText(getApplicationContext(), "No players saved", Toast.LENGTH_SHORT).show();
        }
        else { tradeList.setEnabled(true); }

    }

    public void btClickedVisible(View v)
    {
            bt.setVisibility(View.INVISIBLE);
            bt1.setVisibility(View.VISIBLE);
            battle.setVisibility(View.VISIBLE);
            listActivity.setVisibility(View.VISIBLE);
    }

    public void btClickedInvisible(View v)
    {
            bt.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.INVISIBLE);
            battle.setVisibility(View.INVISIBLE);
            listActivity.setVisibility(View.INVISIBLE);

            btType.setVisibility(View.GONE);
            btType2.setVisibility(View.GONE);
            btType3.setVisibility(View.GONE);
    }

    //View random types on press
    @SuppressLint("ClickableViewAccessibility")
    public void BattleButton()
    {

        battle.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    battle.setTextColor(Color.parseColor("#02E1FD"));

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    battle.setTextColor(Color.parseColor("#FFFFFF"));
                    btType.setVisibility(View.VISIBLE);
                    btType2.setVisibility(View.VISIBLE);
                    btType3.setVisibility(View.VISIBLE);

                    //apply random number for TextView appearance, text
                    firstType = num.nextInt(18);
                    secondType = num.nextInt(18);
                    thirdType = num.nextInt(18);


                    if(firstType == secondType || secondType == thirdType || thirdType == firstType){
                        firstType = num.nextInt(18);
                        secondType = num.nextInt(18);
                        thirdType = num.nextInt(18);
                    }


                    // Prevent TextViews to hold the same random number
                    if(firstType != secondType && secondType != thirdType && thirdType != firstType)
                    {

                        //randomize text and background that will be applied to TextViews -> btType,btType2,btType3 based on the RAND
                        switch (firstType) {

                            case 0:
                                btType.setText("NORMAL");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.normal);
                                break;

                            case 1:
                                btType.setText("FIGHTING");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.fighting);
                                break;

                            case 2:
                                btType.setText("FLYING");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.flying);
                                break;

                            case 3:
                                btType.setText("POISON");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.poison);
                                break;

                            case 4:
                                btType.setText("GROUND");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.ground);
                                break;

                            case 5:
                                btType.setText("ROCK");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.rock);
                                break;

                            case 6:
                                btType.setText("BUG");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.bug);
                                break;

                            case 7:
                                btType.setText("GHOST");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.ghost);
                                break;

                            case 8:
                                btType.setText("STEEL");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.steel);
                                break;

                            case 9:
                                btType.setText("FIRE");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.fire);
                                break;

                            case 10:
                                btType.setText("WATER");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.water);
                                break;

                            case 11:
                                btType.setText("GRASS");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.grass);
                                break;

                            case 12:
                                btType.setText("ELECTRIC");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.electric);
                                break;

                            case 13:
                                btType.setText("PSYCHIC");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.psychic);
                                break;

                            case 14:
                                btType.setText("ICE");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.ice);
                                break;

                            case 15:
                                btType.setText("DRAGON");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.dragon);
                                break;

                            case 16:
                                btType.setText("DARK");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.dark);
                                break;

                            case 17:
                                btType.setText("FAIRY");
                                btType.setTextColor(Color.WHITE);
                                btType.setBackgroundResource(R.drawable.fairy);
                                break;
                        }//ends Switch firstType

                        switch (secondType) {

                            case 0:
                                btType2.setText("NORMAL");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.normal);
                                break;

                            case 1:
                                btType2.setText("FIGHTING");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.fighting);
                                break;

                            case 2:
                                btType2.setText("FLYING");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.flying);
                                break;

                            case 3:
                                btType2.setText("POISON");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.poison);
                                break;

                            case 4:
                                btType2.setText("GROUND");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.ground);
                                break;

                            case 5:
                                btType2.setText("ROCK");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.rock);
                                break;

                            case 6:
                                btType2.setText("BUG");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.bug);
                                break;

                            case 7:
                                btType2.setText("GHOST");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.ghost);
                                break;

                            case 8:
                                btType2.setText("STEEL");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.steel);
                                break;

                            case 9:
                                btType2.setText("FIRE");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.fire);
                                break;

                            case 10:
                                btType2.setText("WATER");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.water);
                                break;

                            case 11:
                                btType2.setText("GRASS");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.grass);
                                break;

                            case 12:
                                btType2.setText("ELECTRIC");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.electric);
                                break;

                            case 13:
                                btType2.setText("PSYCHIC");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.psychic);
                                break;

                            case 14:
                                btType2.setText("ICE");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.ice);
                                break;

                            case 15:
                                btType2.setText("DRAGON");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.dragon);
                                break;

                            case 16:
                                btType2.setText("DARK");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.dark);
                                break;

                            case 17:
                                btType2.setText("FAIRY");
                                btType2.setTextColor(Color.WHITE);
                                btType2.setBackgroundResource(R.drawable.fairy);
                                break;
                        }//ends Switch secondType

                        switch (thirdType) {

                            case 0:
                                btType3.setText("NORMAL");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.normal);
                                break;

                            case 1:
                                btType3.setText("FIGHTING");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.fighting);
                                break;

                            case 2:
                                btType3.setText("FLYING");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.flying);
                                break;

                            case 3:
                                btType3.setText("POISON");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.poison);
                                break;

                            case 4:
                                btType3.setText("GROUND");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.ground);
                                break;

                            case 5:
                                btType3.setText("ROCK");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.rock);
                                break;

                            case 6:
                                btType3.setText("BUG");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.bug);
                                break;

                            case 7:
                                btType3.setText("GHOST");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.ghost);
                                break;

                            case 8:
                                btType3.setText("STEEL");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.steel);
                                break;

                            case 9:
                                btType3.setText("FIRE");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.fire);
                                break;

                            case 10:
                                btType3.setText("WATER");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.water);
                                break;

                            case 11:
                                btType3.setText("GRASS");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.grass);
                                break;

                            case 12:
                                btType3.setText("ELECTRIC");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.electric);
                                break;

                            case 13:
                                btType3.setText("PSYCHIC");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.psychic);
                                break;

                            case 14:
                                btType3.setText("ICE");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.ice);
                                break;

                            case 15:
                                btType3.setText("DRAGON");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.dragon);
                                break;

                            case 16:
                                btType3.setText("DARK");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.dark);
                                break;

                            case 17:
                                btType3.setText("FAIRY");
                                btType3.setTextColor(Color.WHITE);
                                btType3.setBackgroundResource(R.drawable.fairy);
                                break;
                        }//ends Switch thirdType
                    }//ends IF
                }
                return true;
            }
        });
    }

    //change Activity -> playerList.class
    @SuppressLint("ClickableViewAccessibility")
    public void PlayerButton()
    {
        listActivity.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    listActivity.setTextColor(Color.parseColor("#02E1FD"));

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    listActivity.setTextColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(getApplicationContext(), playerList.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }

    //Change Activity -> pokemon_list.class
    @SuppressLint("ClickableViewAccessibility")
    public void TradeListActivity()
    {
        tradeList.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    tradeList.setTextColor(Color.parseColor("#02E1FD"));

                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    tradeList.setTextColor(Color.parseColor("#FFFFFF"));
                    PlayerImageDB db = new PlayerImageDB(getApplicationContext());
                    List<String> NameList = db.GetTableNames();
                    Object[] namesCount = NameList.toArray();

                    if(namesCount.length == 0)
                    {

                         tradeList.setTextColor(Color.parseColor("#FFFFFF"));
                         Toast.makeText(getApplicationContext(), "No players saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                            Intent intent = new Intent(getApplicationContext(), pokemon_list.class);
                            startActivity(intent);
                        }


                }
                return true;
            }
        });



    }//ends PkmnListActivity


    public void systemExit(View v)
    {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);

        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });


        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }//ends systemExit

}//End main