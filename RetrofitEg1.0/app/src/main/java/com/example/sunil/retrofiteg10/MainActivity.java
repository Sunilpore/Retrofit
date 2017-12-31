package com.example.sunil.retrofiteg10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        Call <List<Hero>> call=api.getHeros();
//        Toast.makeText(MainActivity.this,"=api.getHeros(): "+call,Toast.LENGTH_SHORT).show();

        call.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse( Call<List<Hero>> call, Response<List<Hero>> response) {

                List<Hero> heros = response.body();
                Toast.makeText(MainActivity.this,"response.body(): "+heros,Toast.LENGTH_SHORT).show();

                if (heros != null) {

                String[] heroname = new String[heros.size()];

                for (int i = 0; i < heros.size(); i++) {
                    heroname[i] = heros.get(i).getName();
                }

                adapter=new MyAdapter(mContext,heroname);
                listView.setAdapter(adapter);
                //listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,heroname));

                for (Hero h : heros) {
                    Log.d("myTag", h.getLogin());
                    /*Log.d("myTag", h.getName());
                    Log.d("myTag", h.getRealname());
                    Log.d("myTag", h.getImageurl());*/
                }
            }
                else
                    Toast.makeText(MainActivity.this,"String array is null ",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failed,try again",Toast.LENGTH_SHORT).show();
            }
        }
    );

    }


}
