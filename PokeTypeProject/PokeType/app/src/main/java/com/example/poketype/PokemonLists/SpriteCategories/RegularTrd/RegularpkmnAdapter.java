package com.example.poketype.PokemonLists.SpriteCategories.RegularTrd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.poketype.PokemonLists.SpriteCategories.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Giannakakis on 7/19/2020.
 */

public class RegularpkmnAdapter extends ArrayAdapter<Item>
{

    //private  String[] names;
    ArrayList<Item> gridimages = new ArrayList();
    public List selectedPositions;


    public RegularpkmnAdapter(Context context, int itemView, ArrayList<Item> objects)
    {
        super(context, itemView, objects);
        gridimages = objects;
        selectedPositions = new ArrayList<>();
    }


    @Override
    public int getCount()
    {
       return  super.getCount();
    }


    @Override
    public long getItemId(int position)
    {
        //return 0;
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
            R_Grid_View customView = (convertView == null) ? new R_Grid_View(getContext()) : (R_Grid_View) convertView;
            customView.display(gridimages.get(position).getPokImage(), selectedPositions.contains(position));
        return customView;
    }
}

