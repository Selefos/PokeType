package com.example.poketype.PokemonLists.SpriteCategories.LuckyTrd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.poketype.PlayerList.Databases.PlayerLuckyDB;
import com.example.poketype.PokemonLists.SpriteCategories.Item;
import com.example.poketype.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class LuckyTrades extends DialogFragment
{

    private static final String TAG = "LuckyTrades";

    /*DATABASES*/

    PlayerLuckyDB plrImageDB;

    //For database population PlayerImage.db
    String DatabaseSaveNames;
    byte[] DatabaseSaveImages;
    byte[] DatabaseSaveImagesShiny;
    private TextView nameText_L;
    private String name = "";
    /*----------------------------*/

    View view;//used to hide keyboard

    //for Grid population
    String[] gridnames_L = {null};
    ArrayList<Item> gridimages_L = new ArrayList<>();
    LuckypkmnAdapter gridAdapter_L;

    //for image deletion
    Button deleteSelection;
    Button cancelSelection;

    //base calls
    Button spritesButton;
    Button spritesButtonShiny;
    ImageView pokemonimage;
    ImageView pokemonimageShiny;
    GridView luckyPkmnGrid;
    EditText pkmnName;
    ConstraintLayout constraintLayout;

    //Gets Value from SectionsPagerAdapter and places into this constructor
    public LuckyTrades(String text)
    {
        this.name = text;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.lucky_trades, container, false);
        getView(rootView);

        /*---------For Database ---------*/
        plrImageDB = new PlayerLuckyDB(getActivity());
        /*---------End Database Code ---------*/


        /*-------------------------------- INIT --------------------------------*/

        //Layout
        constraintLayout = rootView.findViewById(R.id.LpkmnFragment);

        //Button
        spritesButton = rootView.findViewById(R.id.luckysprtBT);
        spritesButton.setEnabled(false);
        spritesButtonShiny = rootView.findViewById(R.id.luckysprtBT2);
        spritesButtonShiny.setEnabled(false);
        deleteSelection = constraintLayout.findViewById(R.id.Ldelete_selectionBT);
        cancelSelection = constraintLayout.findViewById(R.id.Lcancel_selection);

        //ImageView
        pokemonimage = rootView.findViewById(R.id.Lpokemonimagechoice);
        pokemonimageShiny = rootView.findViewById(R.id.Lpokemonimagechoice2);

        //EditText
        pkmnName = rootView.findViewById(R.id.LPokemonText);

        //TextView
        nameText_L = rootView.findViewById(R.id.LnameText);
        nameText_L.setText(name);

        //Grid
        luckyPkmnGrid = rootView.findViewById(R.id.gridLucky);

        //sets Grid elements
        try
        {
            FunctionsCall();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        /*------------------------------ END INIT ------------------------------*/

        return rootView;
    }

    public void FunctionsCall() throws IOException
    {
        pkmnNamesSearch();
        AddToGrid();
        DeleteFromGrid();
        spritesButtonClick();
        shinySpritesButtonClick();
        hideKeyboard();
    }

    public void getView(View rootView)
    {
        view = rootView;
    }

    public void spritesButtonClick()
    {
        spritesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveImagestoGrid();
                AddToGrid();

                pkmnName.setText("");
                spritesButton.setEnabled(false);
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = view.getWindowToken();
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                pkmnName.clearFocus();

            }
        });
    }

    public void shinySpritesButtonClick()
    {
        spritesButtonShiny.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                SaveImagestoGridShiny();
                AddToGrid();

                pkmnName.setText("");
                spritesButtonShiny.setEnabled(false);
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = view.getWindowToken();
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                pkmnName.clearFocus();

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void hideKeyboard()
    {
        luckyPkmnGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = view.getWindowToken();
                imm.hideSoftInputFromWindow(binder,0);
                pkmnName.clearFocus();
            }
        });
    }

    public void AddToGrid()
    {

        if(!nameText_L.getText().toString().equals(""))
        {
            //pass names from PlayerLuckyDB.db Table to Grid
            plrImageDB = new PlayerLuckyDB(getActivity());

            //Get the names that respond to each image from the database
            List<String> pkmnNames = null;
            pkmnNames = plrImageDB.getNamesForGrid(nameText_L.getText().toString());

            //Pass the names to array for comparison and automated image selection through cursor
            Object[] imageNames = pkmnNames.toArray();
            gridnames_L = pkmnNames.toArray(new String[0]);
            gridimages_L = new ArrayList<>();

            byte[] imagesGr = null;

            for(int i = 0; i < imageNames.length; i++)
            {
                String name = imageNames[i].toString();
                Cursor cursor = plrImageDB.getImageforGrid(nameText_L.getText().toString(), name);

                if (cursor.moveToFirst())
                {
                    imagesGr = cursor.getBlob(0);
                    Drawable imageR = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(imagesGr, 0, imagesGr.length));
                    gridimages_L.add(new Item(imageR));
                }

            }// ends for
        }//ends if

        gridAdapter_L = new LuckypkmnAdapter(getContext(), R.layout.luckypkmn_item, gridimages_L);
        luckyPkmnGrid.setAdapter(gridAdapter_L);

    }

    public void DeleteFromGrid()
    {
        luckyPkmnGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id)
            {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = view.getWindowToken();
                imm.hideSoftInputFromWindow(binder,0);
                pkmnName.clearFocus();

                TransitionManager.beginDelayedTransition(constraintLayout);
                deleteSelection.setEnabled(true);
                deleteSelection.setVisibility(View.VISIBLE);
                cancelSelection.setEnabled(true);
                cancelSelection.setVisibility(View.VISIBLE);


                luckyPkmnGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                int selectedIndex = gridAdapter_L.selectedPositions_L.indexOf(position);

                //check if the Item position is selected and apply new background
                if(selectedIndex > -1)
                {
                    gridAdapter_L.selectedPositions_L.remove(selectedIndex);
                    ((L_Grid_View)view).display_L(false);

                }else
                {
                    gridAdapter_L.selectedPositions_L.add(position);
                    ((L_Grid_View)view).display_L(true);
                }

                //removes buttons if no items are selected after initial selection
                if(gridAdapter_L.selectedPositions_L.size() == 0)
                {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    deleteSelection.setVisibility(View.INVISIBLE);
                    cancelSelection.setVisibility(View.INVISIBLE);
                }

                //--------{Delete Button
                deleteSelection.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        //pass names from PlayerLucky.db to string array
                        plrImageDB = new PlayerLuckyDB(getActivity());
                        List<String> pkmnNames = null;
                        pkmnNames = plrImageDB.getNamesForGrid(nameText_L.getText().toString());
                        Object[] pokeNames = pkmnNames.toArray();

                        //store the selected indexes values to array
                        Object[] positionMultitude = gridAdapter_L.selectedPositions_L.toArray();

                        //sort positions selected, from highest to lowest in order for deletion from right to left in grid positions
                        Arrays.sort(positionMultitude, Collections.reverseOrder());

                        //sets initializer for selected positions in order to remove the view
                        int nameIndex = 0;

                        for(int i = 0; i < positionMultitude.length; i++)
                        {

                            //set name according to selected positions
                            nameIndex = (Integer) positionMultitude[i];
                            String name = pokeNames[nameIndex].toString();

                            //check for names in all grid positions and delete if true
                            for(int j = 0; j < pkmnNames.size(); j++)
                            {
                                //Toast.makeText(getContext(), "Index Positions: \n" + pokeNames[nameIndex].toString()+ nameIndex, Toast.LENGTH_SHORT).show();
                                if(name.equalsIgnoreCase(pokeNames[nameIndex].toString()))
                                {
                                    Integer deleteRows = plrImageDB.deleteImagesFromGrid(nameText_L.getText().toString(), name);
                                }

                            }
                            //remove selected positions to refresh Grid View
                            gridimages_L.remove(nameIndex);
                            //Toast.makeText(getContext(), "Index Positions: \n" + name + nameIndex, Toast.LENGTH_SHORT).show();
                        }

                        //refresh grid view after deletion
                        gridAdapter_L = new LuckypkmnAdapter(getContext(), R.layout.luckypkmn_item, gridimages_L);
                        luckyPkmnGrid.setAdapter(gridAdapter_L);

                        TransitionManager.beginDelayedTransition(constraintLayout);
                        deleteSelection.setVisibility(View.INVISIBLE);
                        cancelSelection.setVisibility(View.INVISIBLE);
                        //Toast.makeText(getContext(), "Index Positions: \n" + gridAdapter.selectedPositions, Toast.LENGTH_SHORT).show();
                    }
                });


                //--------{Cancel Button
                cancelSelection.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        //store the selected indexes values to array
                        Object[] positionMultitude = gridAdapter_L.selectedPositions_L.toArray();

                        //refresh gridview
                        luckyPkmnGrid.setAdapter(gridAdapter_L);

                        for(int i = 0; i < positionMultitude.length; i++)
                        {
                            int selectedIndex = gridAdapter_L.selectedPositions_L.indexOf(positionMultitude[i]);

                            if(selectedIndex > -1)
                            {
                                gridAdapter_L.selectedPositions_L.remove(selectedIndex);
                                ((L_Grid_View)view).display_L(false);
                            }

                            //Toast.makeText(getContext(), "Index Positions: \n" + positionMultitude[i].toString(), Toast.LENGTH_SHORT).show();
                        }
                        gridAdapter_L.selectedPositions_L.clear();
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        deleteSelection.setVisibility(View.INVISIBLE);
                        cancelSelection.setVisibility(View.INVISIBLE);
                        //Toast.makeText(getContext(), "Index Positions: \n" + gridAdapter.selectedPositions, Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
    }

    public void pkmnNamesSearch() throws IOException
    {

        //pass images to Drawable array shiny sprites
        AssetManager assetManager2 = getResources().getAssets();
        InputStream inputStream2 = null;

        String[] images2 = assetManager2.list("PokemonShiny");
        assert images2 != null;
        final Drawable[] drawables2 = new Drawable[images2.length];


        //Insert PokemonNames.txt to string array for search on EditText
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        ArrayList lineCnt = new ArrayList();

        try
        {
            //get the number of lines in the .txt
            InputStream inputString = getResources().getAssets().open("PokemonNames.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputString));

            if(!bufferedReader.ready())
            {throw new IOException();}

            while((line = bufferedReader.readLine()) != null)
            {
                lineCnt.add(line);

            }
            bufferedReader.close();

        } catch (IOException e) {e.printStackTrace();}
        final String[] finalTxtNames = (String[]) lineCnt.toArray(new String[lineCnt.size()]);


        //pass images to Drawable array normal sprites
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        String[] images = assetManager.list("PokemonSprites");
        assert images != null;
        final Drawable[] drawables = new Drawable[images.length];

        try
        {
            for (int i = 0; i < images.length; i++)
            {
                //normal sprites
                inputStream = getResources().getAssets().open("PokemonSprites/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;

                //shiny sprites
                inputStream2 = getResources().getAssets().open("PokemonShiny/" + images2[i]);
                Drawable drawable2 = Drawable.createFromStream(inputStream2, null);
                drawables2[i] = drawable2;
            }
            assert inputStream != null;
            inputStream.close();
            inputStream2.close();

        }catch (IOException e){e.printStackTrace();}


        //for database population

        pkmnName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                //set images in ImageView according to text name
                String getName = pkmnName.getText().toString();

                //pass names from PlayerLuckyDB.db Table to Grid
                PlayerLuckyDB dbl = new PlayerLuckyDB(getActivity());

                //Get the names that respond to each image from the database
                List<String> pkmnNames = new ArrayList<String>();
                pkmnNames = dbl.getNamesForGrid(nameText_L.getText().toString());

                //Pass the names to array for comparison and automated image selection through cursor
                Object[] imageNames = pkmnNames.toArray();

                            for(int j = 0; j < finalTxtNames.length; j++)
                            {

                                if (getName.equalsIgnoreCase(finalTxtNames[j]))
                                {

                                    //set the image co-responding to the name
                                    //images and names are detected in linear order within the files in assets folder
                                    pokemonimage.setBackground(drawables[j]);
                                    pokemonimageShiny.setBackground(drawables2[j]);

                                    //convert Drawble to byte[] normal sprites
                                    Bitmap bitmap = ((BitmapDrawable) drawables[j]).getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);

                                    //convert Drawble to byte[] shiny sprites
                                    Bitmap bitmap2 = ((BitmapDrawable) drawables2[j]).getBitmap();
                                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                                    bitmap2.compress(Bitmap.CompressFormat.PNG, 90, stream2);

                                    //prepares values for insertion to Database
                                    DatabaseSaveNames = finalTxtNames[j];
                                    DatabaseSaveImages = stream.toByteArray();
                                    DatabaseSaveImagesShiny = stream2.toByteArray();
                                    spritesButton.setEnabled(true);
                                    spritesButtonShiny.setEnabled(true);

                                    //Toast.makeText(getContext(),"Name: \n" + DatabaseSaveNames ,Toast.LENGTH_SHORT).show();
                                }
                                if (getName.equals(""))
                                {
                                    spritesButton.setEnabled(false);
                                    spritesButtonShiny.setEnabled(false);
                                    pokemonimage.setBackgroundResource(R.drawable.questionmark);
                                    pokemonimageShiny.setBackgroundResource(R.drawable.questionmark);
                                }
                            }//ends for

                for (Object imageName : imageNames)
                {
                    if (getName.equalsIgnoreCase(imageName.toString()) && !getName.equals(""))
                    {
                        Toast.makeText(getContext(), "Duplicate Entry \n" + imageName.toString(), Toast.LENGTH_SHORT).show();
                        spritesButton.setEnabled(false);
                        spritesButtonShiny.setEnabled(false);
                        pokemonimage.setBackgroundResource(R.drawable.questionmark);
                        pokemonimageShiny.setBackgroundResource(R.drawable.questionmark);
                    }
                }

            }//ends afterTextChanged
        });

    }

    public void SaveImagestoGrid()
    {
        //save selection on button click
        plrImageDB.insertStringImageToTable(name, DatabaseSaveNames, DatabaseSaveImages);
    }

    public void SaveImagestoGridShiny()
    {
        //save selection on button click
        plrImageDB.insertStringImageToTable(name, DatabaseSaveNames, DatabaseSaveImagesShiny);
    }
}
