package com.example.poketype.PokemonLists.SpriteCategories.LuckyTrd;

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

public class L_Grid_View extends LinearLayout
{
    private ImageView mImageView;

    public L_Grid_View(@NonNull Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.luckypkmn_item, this);
        mImageView = (ImageView) getRootView().findViewById(R.id.luckyPkmnsprites);
    }

    public void display_L(Drawable img, boolean isSelected)
    {
        mImageView.setImageDrawable(img);
        display_L(isSelected);
    }

    public void display_L(boolean isSelected)
    {
        mImageView.setBackgroundResource(isSelected ? R.drawable.selectedgriditem : R.drawable.gridbacjgroundlucky);
    }

}
