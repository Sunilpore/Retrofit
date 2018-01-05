package com.example.sunil.retrofiteg11;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String MyTag="myTag";
    private static final int MY_PERMISSION_REQUEST =100 ;

    Button download;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        }

        final String url="https://wallup.net/wp-content/uploads/2017/03/15/123978-sunset.jpg";

        download= (Button) findViewById(R.id.start_downloading);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadfile(url);
            }
        });

    }


    private void downloadfile(String url){

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl(FileDownloadClient.BASE_URL);

        Retrofit retrofit=builder.build();

        FileDownloadClient filedownloadclient=retrofit.create(FileDownloadClient.class);

//        Call <ResponseBody> call=filedownloadclient.getFileDownloadData();
//        Call <ResponseBody> call=filedownloadclient.getFileDownloadData(url);
        Call <ResponseBody> call=filedownloadclient.getFileDownloadDataStreaming(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                /*new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {

                        return null;
                    }
                }.execute();*/

                boolean isSuccessful=writeData(response.body());

                Toast.makeText(MainActivity.this,"Success:"+isSuccessful,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean writeData(ResponseBody body) {

        try {
            File iconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Sample Icon.jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;
            Toast.makeText(MainActivity.this,"1st try catch..",Toast.LENGTH_SHORT).show();

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownload = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(iconFile);

                int i = 0;
                while ((i = inputStream.read(fileReader)) != -1) {
                    outputStream.write(fileReader, 0, i);

                    fileSizeDownload += i;

                    Log.d("myTag", "file download:" + fileSizeDownload + " of " + fileSize);
                }


                /*while (true) {
                    int i=inputStream.read(fileReader);

                    if(i!=-1)
                        break;

                    outputStream.write(fileReader, 0, i);
                    fileSizeDownload += i;
                    Log.d("myTag", "file download:" + fileSizeDownload + " of " + fileSize);
                }*/

                outputStream.flush();
                return true;

            } catch (IOException e) {
                Log.d("myTag", "Exception:"+e);
                Toast.makeText(MainActivity.this,"2nd try catch Exception:",Toast.LENGTH_SHORT).show();
                return false;
            } finally {

                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();
            }

        }catch(IOException e){
            Toast.makeText(MainActivity.this,"1st try catch Exception",Toast.LENGTH_SHORT).show();
            return false;
        }

    }


}
