package com.example.sunil.retrofiteg10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static String Mytag="myTag";
    MyAdapter adapter;
    ListView listView;
    Context mContext;
    ArrayList<Hero> heros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView) findViewById(R.id.listview_lay);

        mContext=this;

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

            Api api=retrofit.create(Api.class);

        Call <ArrayList<Hero>> call=api.getHeros();
//        Toast.makeText(MainActivity.this,"=api.getHeros(): "+call,Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<ArrayList<Hero>>() {
            @Override
            public void onResponse( Call<ArrayList<Hero>> call, Response<ArrayList<Hero>> response) {

                heros = response.body();
                Toast.makeText(MainActivity.this,"response.body(): "+response.body(),Toast.LENGTH_SHORT).show();

                if (heros != null) {

               /* String[] heroname = new String[heros.size()];
                String[] realName = new String[heros.size()];


                    arrayList=new ArrayList<>();
                for (int i = 0; i < heros.size(); i++) {

                    heroname[i] = heros.get(i).getName();
                    realName[i] = heros.get(i).getRealname();
                    Hero hero=new Hero();

                    hero.setName(heros.get(i).getName());
                    hero.setRealname(heros.get(i).getRealname());
                    arrayList.add(hero);
                }*/

                    /*arrayList=new ArrayList<>();
                    arrayList.addAll(heros);*/
                adapter=new MyAdapter(mContext,heros);
                listView.setAdapter(adapter);
                //listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,heroname));

               /* for (Hero h : heros) {
                    Log.d("myTag", h.getLogin());
                    *//*Log.d("myTag", h.getName());
                    Log.d("myTag", h.getRealname());
                    Log.d("myTag", h.getImageurl());*//*
                }*/
            }
                else
                    Toast.makeText(MainActivity.this,"String array is null ",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ArrayList<Hero>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failed,try again",Toast.LENGTH_SHORT).show();
            }
        }
    );

    }


}
