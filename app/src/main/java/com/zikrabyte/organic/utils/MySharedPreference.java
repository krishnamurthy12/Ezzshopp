package com.zikrabyte.organic.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.zikrabyte.organic.beanclasses.AddToCartDummy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KRISH on 4/6/2018.
 */

public class MySharedPreference {

    public static final String PREFS_NAME = "AddToCartDummy_APP";
    public static final String FAVORITES = "AddToCartDummy_Favorite";

    public MySharedPreference() {
        super();
    }

    public void clearPreferences(Context context)
    {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.apply();

    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<AddToCartDummy> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.apply();
    }

    public void saveCart(Context context, ArrayList<AddToCartDummy> AddToCartDummy) {
        List<AddToCartDummy> favorites = getFavorites(context);
       /* if (favorites == null)
            favorites = new ArrayList<AddToCartDummy>();
        favorites.add(AddToCartDummy);*/
        saveFavorites(context, AddToCartDummy);
    }

    public void removeFromCart(Context context, AddToCartDummy AddToCartDummy) {
        ArrayList<AddToCartDummy> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(AddToCartDummy);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<AddToCartDummy> getFavorites(Context context) {
        SharedPreferences settings;
        List<AddToCartDummy> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            AddToCartDummy[] favoriteItems = gson.fromJson(jsonFavorites,
                    AddToCartDummy[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<AddToCartDummy>(favorites);
        } else
            return null;

        return (ArrayList<AddToCartDummy>) favorites;

    }
}
