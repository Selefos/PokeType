package com.example.poketype.PokemonLists.SpriteCategories.RegularTrd;

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

public class R_Grid_View extends LinearLayout
{
    private ImageView mImageView;

    public R_Grid_View(@NonNull Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.regularpkmn_item, this);
        mImageView = (ImageView) getRootView().findViewById(R.id.regularPkmnsprites);
    }

    public void display(Drawable img, boolean isSelected)
    {
        mImageView.setImageDrawable(img);
        display(isSelected);
    }

    public void display(boolean isSelected)
    {
        mImageView.setBackgroundResource(isSelected ? R.drawable.selectedgriditem : R.drawable.gridbackgroundregular);
    }

}
