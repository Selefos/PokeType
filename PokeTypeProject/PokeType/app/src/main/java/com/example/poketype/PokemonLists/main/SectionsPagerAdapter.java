package com.example.poketype.PokemonLists.main;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.poketype.PokemonLists.SpriteCategories.LuckyTrd.LuckyTrades;
import com.example.poketype.R;
import com.example.poketype.PokemonLists.SpriteCategories.RegularTrd.RegularTrades;
import com.example.poketype.PokemonLists.SpriteCategories.ShinyTrd.ShinyTrades;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.*/

public class SectionsPagerAdapter extends FragmentStatePagerAdapter
{

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.Regular_Trade, R.string.Shiny_Trade, R.string.Lucky_Trade};
    private final Context mContext;
    private String Value;


    public SectionsPagerAdapter(Context context, FragmentManager fm, String value)
    {
        super(fm);
        mContext = context;
        Value = value;
    }


    @Override
    public Fragment getItem(@NonNull int position)
    {

        Fragment fragment = null;
        switch (position)
        {
            //case 0: fragment = new BlankFragment.newInstance(); break; //needs blank fragment class

            case 0: fragment = new RegularTrades(Value);   break;
            case 1: fragment = new ShinyTrades(Value);     break;
            case 2: fragment = new LuckyTrades(Value);     break;

            default: break;
        }

        return fragment;
    }


    @Override
    public int getItemPosition(Object obj)
    {
        return POSITION_NONE;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);

    }


    @Override
    public int getCount()
    {
        // Show 3 total pages.
        return 3;
    }

    public void setValue(String v){
        this.Value = v;
        notifyDataSetChanged();
    }


}
