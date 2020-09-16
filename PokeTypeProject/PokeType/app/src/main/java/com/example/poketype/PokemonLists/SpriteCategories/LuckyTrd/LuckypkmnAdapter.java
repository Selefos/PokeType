package com.example.poketype.PokemonLists.SpriteCategories.LuckyTrd;

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

public class LuckypkmnAdapter extends ArrayAdapter<Item>
{

    private  String[] names;
    ArrayList<Item> gridimages_L = new ArrayList();
    public List selectedPositions_L;
    View view;


    public LuckypkmnAdapter(Context context, int itemView, ArrayList<Item> objects)
    {
        super(context, itemView, objects);
        gridimages_L = objects;
        selectedPositions_L = new ArrayList<>();
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

            /*view = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.regularpkmn_item, null);*/

        L_Grid_View customView = (convertView == null) ? new L_Grid_View(getContext()) : (L_Grid_View) convertView;
        customView.display_L(gridimages_L.get(position).getPokImage(), selectedPositions_L.contains(position));

        //ImageView imageView = (ImageView) view.findViewById(R.id.regularPkmnsprites);
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //imageView.setBackground(gridimages.get(position).getPokImage());

        //TextView textView = (TextView) view.findViewById(R.id.PkmnNames);
        //textView.setText(names[position]);

        return customView;
    }
}

