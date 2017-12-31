package com.example.sunil.retrofiteg10;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Sunil on 12/30/2017.
 */

public class MyAdapter extends BaseAdapter{


    private Context mContext;
    private final String[] heroname;

    public MyAdapter(Context mContext, String[] heroname) {

        this.mContext=mContext;
        this.heroname=heroname;
    }


    @Override
    public int getCount() {
        return heroname.length;
    }

    @Override
    public Object getItem(int i) {
        return heroname[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup vg) {

        ViewHolder vh;
        if(view==null){
           vh=new ViewHolder();
           view= LayoutInflater.from(mContext).inflate(R.layout.lay, vg, false);
           vh.name=view.findViewById(R.id.tv_name);
           view.setTag(vh);
        }
        else
        vh= (ViewHolder) view.getTag();

        String current= (String) getItem(i);

        vh.name.setText(current);
        //vh.name.setTag(current);

        return view;
    }

    private class ViewHolder{
        TextView name;
    }

}
