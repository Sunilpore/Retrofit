package com.example.sunil.retrofiteg10;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sunil on 12/30/2017.
 */

public class MyAdapter extends BaseAdapter{


    private Context mContext;
    //private final String[] heroname;
    private ArrayList <Hero> mArrayList;

    public MyAdapter(Context mContext, ArrayList<Hero> arrayList/*String[] heroname*/) {

        this.mContext=mContext;
        this.mArrayList=arrayList;
        //this.heroname=heroname;

    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrayList.get(i);
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
           vh.realname=view.findViewById(R.id.tv_realname);
           view.setTag(vh);
        }
        else
        vh= (ViewHolder) view.getTag();

        Hero current= (Hero) getItem(i);

        vh.name.setText(current.getName());
        vh.realname.setText(current.getRealname());
        //vh.name.setTag(current);

        return view;
    }

    private class ViewHolder{
        TextView name,realname;
    }

}
