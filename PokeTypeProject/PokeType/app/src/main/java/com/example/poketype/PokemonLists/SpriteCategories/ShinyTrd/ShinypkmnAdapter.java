package com.example.poketype.PokemonLists.SpriteCategories.ShinyTrd;

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

public class ShinypkmnAdapter extends ArrayAdapter<Item>
{

    private  String[] names;
    ArrayList<Item> gridimages_S = new ArrayList();
    public List selectedPositions_S;
    View view;


    public ShinypkmnAdapter(Context context, int itemView, ArrayList<Item> objects)
    {
        super(context, itemView, objects);
        gridimages_S = objects;
        selectedPositions_S = new ArrayList<>();
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

        S_Grid_View customView = (convertView == null) ? new S_Grid_View(getContext()) : (S_Grid_View) convertView;
        customView.display_S(gridimages_S.get(position).getPokImage(), selectedPositions_S.contains(position));

        //ImageView imageView = (ImageView) view.findViewById(R.id.regularPkmnsprites);
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //imageView.setBackground(gridimages.get(position).getPokImage());

        //TextView textView = (TextView) view.findViewById(R.id.PkmnNames);
        //textView.setText(names[position]);

        return customView;
    }
}

