package com.example.poketype.PokemonLists.SpriteCategories;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.poketype.R;


public class ContainerFragment extends Fragment
{


    public ContainerFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_container, container,false);

        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegularTrades()).commit();
        // Inflate the layout for this fragment
        return view;
    }

}
