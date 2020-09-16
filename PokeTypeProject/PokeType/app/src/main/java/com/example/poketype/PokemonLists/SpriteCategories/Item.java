package com.example.poketype.PokemonLists.SpriteCategories;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class Item
{

    Drawable image;
    //String name;

    public Item(Drawable Pimage)
    {
        //this.name = PokName;
        this.image = Pimage;
    }

    public Drawable getPokImage()
    {
        return image;
    }

}