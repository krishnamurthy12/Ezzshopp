package com.zikrabyte.organic.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.SearchView;

import com.zikrabyte.organic.fragments.DynamicFragment;
import com.zikrabyte.organic.fragments.GrossaryFragment;
import com.zikrabyte.organic.fragments.VeggiesFragment;


/**
 * Created by Krish on 22-12-2017.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int no_of_pages;
    SearchView.OnQueryTextListener context;
    public MyPagerAdapter(FragmentManager fm, int numofPages) {
        super(fm);
        this.no_of_pages=numofPages;
    }

    public MyPagerAdapter(FragmentManager fm, SearchView.OnQueryTextListener context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        /*DynamicFragment fragment;
        for (int i = 0; i < no_of_pages ; i++) {
            if (i == position) {
                fragment= DynamicFragment.newInstance();
                break;
            }
        }
        return fragment;*/
        switch (position)
        {
            case 0:
//                return new GrossaryFragment();
                return new VeggiesFragment();
            case 1:
//                return new VeggiesFragment();
            return new GrossaryFragment();

          /*  case 2:
                ShakesFragment shakes=new ShakesFragment();
                return shakes;
            case 3:
                ChatsFragment chats=new ChatsFragment();
                return chats;*/

        }
        return null;
    }

    @Override
    public int getCount() {
        return no_of_pages;
    }
}
