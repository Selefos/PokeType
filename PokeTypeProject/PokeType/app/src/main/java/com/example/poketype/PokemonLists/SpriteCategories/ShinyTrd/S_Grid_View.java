package com.example.poketype.PokemonLists.SpriteCategories.ShinyTrd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.poketype.R;

/**
 * Created by Andreas Giannakakis on 7/19/2020.
 */

public class S_Grid_View extends LinearLayout
{
    private ImageView mImageView;

    public S_Grid_View(@NonNull Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.shinypkmn_item, this);
        mImageView = (ImageView) getRootView().findViewById(R.id.shinyPkmnsprites);
    }

    public void display_S(Drawable img, boolean isSelected)
    {
        mImageView.setImageDrawable(img);
        display_S(isSelected);
    }

    public void display_S(boolean isSelected)
    {
        mImageView.setBackgroundResource(isSelected ? R.drawable.selectedgriditem : R.drawable.gridbackgroundshiny);
    }

}
