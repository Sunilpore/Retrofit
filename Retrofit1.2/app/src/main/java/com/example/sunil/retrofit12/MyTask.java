package com.example.sunil.retrofit12;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Sunil on 1/7/2018.
 */

public class MyTask extends AsyncTask<Void, Void, Boolean> {

    Context mContext;
    String url;

    public MyTask(Context mContext, String url) {
        this.mContext=mContext;
        this.url=url;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl(FileDownloadClient.BASE_URL);

        Retrofit retrofit=builder.build();

        FileDownloadClient filedownloadclient=retrofit.create(FileDownloadClient.class);

        Call<ResponseBody> call=filedownloadclient.getFileDownloadData(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                boolean isSuccessful=writeData(response.body());

                Toast.makeText(mContext,"Success:"+isSuccessful,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext,"Fail",Toast.LENGTH_SHORT).show();
            }
        });

        return null;
    }


    private boolean writeData(ResponseBody body) {

        try {
            File iconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Sample Icon.jpg");

            InputStream inputStream = null;
            OutputStream outputStream = null;
            Toast.makeText(mContext,"1st try catch..",Toast.LENGTH_SHORT).show();

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

                outputStream.flush();
                return true;

            } catch (IOException e) {
                Log.d("myTag", "Exception:"+e);
                Toast.makeText(mContext,"2nd try catch Exception:",Toast.LENGTH_SHORT).show();
                return false;
            } finally {

                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();
            }

        }catch(IOException e){
            Toast.makeText(mContext,"1st try catch Exception",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
