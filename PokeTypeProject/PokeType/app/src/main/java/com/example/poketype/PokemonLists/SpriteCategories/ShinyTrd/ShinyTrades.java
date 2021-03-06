package com.example.poketype.PokemonLists.SpriteCategories.ShinyTrd;

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

import com.example.poketype.PlayerList.Databases.PlayerShinyDB;
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

public class ShinyTrades extends DialogFragment
{
    private static final String TAG = "ShinyTrades";

    /*DATABASES*/
    PlayerShinyDB plrImageDB;

    //For database population PlayerImage.db
    String DatabaseSaveNames;
    byte[] DatabaseSaveImages;
    private TextView nameText;
    private String name = "";
    /*----------------------------*/

    View view;//used to hide keyboard

    //for Grid population
    String[] gridnames_S = {null};
    ArrayList<Item> gridimages_S = new ArrayList<>();
    ShinypkmnAdapter gridAdapter_S;

    //for image deletion
    Button deleteSelection;
    Button cancelSelection;

    //base calls
    Button spritesButton;
    ImageView pokemonimage;
    GridView shinyPkmnGrid;
    EditText pkmnName;
    ConstraintLayout constraintLayout;

    //Gets Value from SectionsPagerAdapter and places into this constructor
    public ShinyTrades(String text)
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
        View rootView = inflater.inflate(R.layout.shiny_trades, container, false);
        getView(rootView);

        /*---------For Database ---------*/
        plrImageDB = new PlayerShinyDB(getActivity());
        /*---------End Database Code ---------*/


        /*-------------------------------- INIT --------------------------------*/

        //Layout
        constraintLayout = rootView.findViewById(R.id.SpkmnFragment);

        //Button
        spritesButton = rootView.findViewById(R.id.shinysprtBT);
        spritesButton.setEnabled(false);
        deleteSelection = constraintLayout.findViewById(R.id.Sdelete_selectionBT);
        cancelSelection = constraintLayout.findViewById(R.id.Scancel_selection);

        //ImageView
        pokemonimage = rootView.findViewById(R.id.Spokemonimagechoice);

        //EditText
        pkmnName = rootView.findViewById(R.id.SPokemonText);

        //TextView
        nameText = rootView.findViewById(R.id.SnameText);
        nameText.setText(name);

        //Grid
        shinyPkmnGrid = rootView.findViewById(R.id.gridShiny);

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
        onClick();
        DeleteFromGrid();
        hideKeyboard();
    }

    public void getView(View rootView)
    {
        view = rootView;
    }

    public void onClick()
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

    @SuppressLint("ClickableViewAccessibility")
    public void hideKeyboard()
    {
        shinyPkmnGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
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

        if(!nameText.getText().toString().equals(""))
        {
            //pass names from PlayerShinyDB.db Table to Grid
            plrImageDB = new PlayerShinyDB(getActivity());

            //Get the names that respond to each image from the database
            List<String> pkmnNames = null;
            pkmnNames = plrImageDB.getNamesForGrid(nameText.getText().toString());

            //Pass the names to array for comparison and automated image selection through cursor
            Object[] imageNames = pkmnNames.toArray();
            gridnames_S = pkmnNames.toArray(new String[0]);
            gridimages_S = new ArrayList<>();

            byte[] imagesGr = null;

            for(int i = 0; i < imageNames.length; i++)
            {
                String name = imageNames[i].toString();
                Cursor cursor = plrImageDB.getImageforGrid(nameText.getText().toString(), name);

                if (cursor.moveToFirst())
                {
                    imagesGr = cursor.getBlob(0);
                    Drawable imageR = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(imagesGr, 0, imagesGr.length));
                    gridimages_S.add(new Item(imageR));
                }

            }// ends for
        }//ends if

        gridAdapter_S = new ShinypkmnAdapter(getContext(), R.layout.shinypkmn_item, gridimages_S);
        shinyPkmnGrid.setAdapter(gridAdapter_S);

    }

    public void DeleteFromGrid()
    {
        shinyPkmnGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id)
            {

                //hides keyboard
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                IBinder binder = view.getWindowToken();
                imm.hideSoftInputFromWindow(binder,0);
                pkmnName.clearFocus();

                TransitionManager.beginDelayedTransition(constraintLayout);
                deleteSelection.setEnabled(true);
                deleteSelection.setVisibility(View.VISIBLE);
                cancelSelection.setEnabled(true);
                cancelSelection.setVisibility(View.VISIBLE);


                shinyPkmnGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                int selectedIndex = gridAdapter_S.selectedPositions_S.indexOf(position);

                //check if the Item position is selected and apply new background
                if(selectedIndex > -1)
                {
                    gridAdapter_S.selectedPositions_S.remove(selectedIndex);
                    ((S_Grid_View)view).display_S(false);
                    ((S_Grid_View)view).display_S(false);

                }else
                {
                    gridAdapter_S.selectedPositions_S.add(position);
                    ((S_Grid_View)view).display_S(true);
                }

                //removes buttons if no items are selected after initial selection
                if(gridAdapter_S.selectedPositions_S.size() == 0)
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

                        //pass names from PlayerShinyDB.db to string array
                        plrImageDB = new PlayerShinyDB(getActivity());
                        List<String> pkmnNames = null;
                        pkmnNames = plrImageDB.getNamesForGrid(nameText.getText().toString());
                        Object[] pokeNames = pkmnNames.toArray();

                        //store the selected indexes values to array
                        Object[] positionMultitude = gridAdapter_S.selectedPositions_S.toArray();

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
                                    Integer deleteRows = plrImageDB.deleteImagesFromGrid(nameText.getText().toString(), name);
                                }

                            }
                            //remove selected positions to refresh Grid View
                            gridimages_S.remove(nameIndex);
                            //Toast.makeText(getContext(), "Index Positions: \n" + name + nameIndex, Toast.LENGTH_SHORT).show();
                        }

                        //refresh grid view after deletion
                        gridAdapter_S = new ShinypkmnAdapter(getContext(), R.layout.shinypkmn_item, gridimages_S);
                        shinyPkmnGrid.setAdapter(gridAdapter_S);

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
                        Object[] positionMultitude = gridAdapter_S.selectedPositions_S.toArray();

                        //refresh gridview
                        shinyPkmnGrid.setAdapter(gridAdapter_S);

                        for(int i = 0; i < positionMultitude.length; i++)
                        {
                            int selectedIndex = gridAdapter_S.selectedPositions_S.indexOf(positionMultitude[i]);

                            if(selectedIndex > -1)
                            {
                                gridAdapter_S.selectedPositions_S.remove(selectedIndex);
                                ((S_Grid_View)view).display_S(false);
                            }
                            gridAdapter_S.selectedPositions_S.clear();
                            //Toast.makeText(getContext(), "Index Positions: \n" + positionMultitude[i].toString(), Toast.LENGTH_SHORT).show();
                        }

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


        //pass images to Drawable array
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;

        String[] images = assetManager.list("PokemonShiny");
        final Drawable[] drawables = new Drawable[images.length];

        try
        {
            for (int i = 0; i < images.length; i++)
            {
                inputStream = getResources().getAssets().open("PokemonShiny/" + images[i]);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                drawables[i] = drawable;
            }
            inputStream.close();

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

                //pass names from PlayerShinyDB.db Table to Grid
                PlayerShinyDB dbs = new PlayerShinyDB(getActivity());

                //Get the names that respond to each image from the database
                List<String> pkmnNames = new ArrayList<String>();
                pkmnNames = dbs.getNamesForGrid(nameText.getText().toString());

                //Pass the names to array for comparison and automated image selection through cursor
                Object[] imageNames = pkmnNames.toArray();

                for(int i = 0; i < finalTxtNames.length; i++)
                {

                    if(getName.equalsIgnoreCase(finalTxtNames[i]))
                    {

                        pokemonimage.setBackground(drawables[i]);

                        //convert Drawble to byte[]
                        Bitmap bitmap = ((BitmapDrawable)drawables[i]).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);

                        //prepares values for insertion to Database
                        DatabaseSaveNames = finalTxtNames[i];
                        DatabaseSaveImages = stream.toByteArray();
                        spritesButton.setEnabled(true);

                        //Toast.makeText(getContext(),"Name: \n" + DatabaseSaveNames ,Toast.LENGTH_SHORT).show();
                    }
                    if(getName.equals(""))
                    {
                        spritesButton.setEnabled(false);
                        pokemonimage.setBackgroundResource(R.drawable.questionmark);
                    }
                }//ends for

                for (Object imageName : imageNames)
                {
                    if (getName.equalsIgnoreCase(imageName.toString()) && !getName.equals(""))
                    {
                        Toast.makeText(getContext(), "Duplicate Entry \n" + imageName.toString(), Toast.LENGTH_SHORT).show();
                        spritesButton.setEnabled(false);
                        pokemonimage.setBackgroundResource(R.drawable.questionmark);
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
}
