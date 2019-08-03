package com.zikrabyte.organic.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.beanclasses.ImageItemViewpager;

import java.util.List;

/**
 * Created by Krish on 26-02-2018.
 */

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    List<ImageItemViewpager> list_for_viewpager;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<ImageItemViewpager> list_for_viewpager) {
        this.context = context;
        this.list_for_viewpager=list_for_viewpager;
        this.inflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_for_viewpager.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= this.inflater.inflate(R.layout.pager_list_for_viewpager,container,false);
        ImageView imageView=(ImageView)view.findViewById(R.id.vI_pager_logo);
        TextView vT_plv_heading= (TextView) view.findViewById(R.id.vT_pager_heading);
        TextView vT_plv_subheading= (TextView) view.findViewById(R.id.vT_pager_subheading);
        TextView vT_plv_desc= (TextView) view.findViewById(R.id.vT_pager_description);

        imageView.setImageResource(this.list_for_viewpager.get(position).getImageId());
        vT_plv_heading.setText(this.list_for_viewpager.get(position).getHeading());
        vT_plv_subheading.setText(this.list_for_viewpager.get(position).getSubheading());
        vT_plv_desc.setText(this.list_for_viewpager.get(position).getDescription());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);

    }
}
