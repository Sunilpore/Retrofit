package com.example.sunil.retrofiteg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    TextView id,login;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id= (TextView) findViewById(R.id.tv_id);
        login= (TextView) findViewById(R.id.tv_login);
        listView= (ListView) findViewById(R.id.listview_lay);

        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);

        Call<UserModel> call=apiInterface.getUserData();


        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel=response.body();
                id.setText(""+userModel.getId());
                login.setText(userModel.getLogin().toString());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
