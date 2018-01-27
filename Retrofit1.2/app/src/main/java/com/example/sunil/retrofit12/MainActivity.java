package com.example.sunil.retrofit12;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String MyTag="myTag";
    private static final int MY_PERMISSION_REQUEST =100 ;

    Context mContext;
    EditText setUrl;
    Button download;
    private ProgressDialog progress;
    Handler handle,handle1;
    LoopThread loopThread;

    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
        }

        //final String url="https://wallup.net/wp-content/uploads/2017/03/15/123978-sunset.jpg";
        //final String url="http://mojmalnews.com/wp-content/uploads/2017/08/desktop-nature-wallpaper-high-resolution-10-backgrounds-lamborghini-hd-full-screen-widescreen-veneno-plus-on-wallpaper-high-quality-desktop-nature-images-of.jpg";

        mContext=this;
        download= (Button) findViewById(R.id.start_downloading);
        setUrl= (EditText) findViewById(R.id.tv_url);

        loopThread=new LoopThread();
        loopThread.start();
//        handle=new Handler(getApplicationContext().getMainLooper());
//        handle1=new Handler(getApplicationContext().getMainLooper());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Used for user dynamically download file just by copy and paste
                if(setUrl.equals("")){
                    Toast.makeText(mContext, "URL set by USER", Toast.LENGTH_SHORT).show();
                    url=setUrl.getText().toString();
                    Log.d("myTag", "URL inside if:"+url);
                }
                else
                    //url="http://mirrors.standaloneinstaller.com/video-sample/jellyfish-25-mbps-hd-hevc.mkv";
                    url="http://www.hdnicewallpapers.com/Walls/Big/Nature%20and%20Landscape/Road_Between_Nature_View.jpg";
                    //url="https://wallup.net/wp-content/uploads/2017/03/15/123978-sunset.jpg";

                Log.d("myTag", "URL else:"+url);
                downloadfile(url);

                //This can be run when we define Aysnc Class separately i.e. from outside
//                MyTask myTask=new MyTask(mContext,url);
//                myTask.execute();

                dialogmodeProgress();
            }
        });

    }


    /*private void downloadfile(String url) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                //Here write all downloadfile() rest code

                return null;
            }
        }.execute();
    }*/


    private void downloadfile(String url){

        Retrofit.Builder builder=new Retrofit.Builder()
                .baseUrl(FileDownloadClient.BASE_URL);

        Retrofit retrofit=builder.build();

        FileDownloadClient filedownloadclient=retrofit.create(FileDownloadClient.class);

//        Call <ResponseBody> call=filedownloadclient.getFileDownloadData();
//        Call<ResponseBody> call=filedownloadclient.getFileDownloadData(url);
        Call <ResponseBody> call=filedownloadclient.getFileDownloadDataStreaming(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                Log.d("myTag", "responde:"+response.body());
                if(response.isSuccessful()){

                    loopThread.handle.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean isSuccessful=writeData(response.body());
                            Toast.makeText(MainActivity.this,"Success:"+isSuccessful,Toast.LENGTH_SHORT).show();
                        }
                    });

                    //boolean isSuccessful=writeData(response.body());
                    //Toast.makeText(MainActivity.this,"Success:"+isSuccessful,Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this,"Unsuccess",Toast.LENGTH_SHORT).show();
                //progressbar(response.body().contentLength());


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeData(ResponseBody body) {

        try {
            String fileName = URLUtil.guessFileName(url, null,
                    MimeTypeMap.getFileExtensionFromUrl(url));

            File iconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName/*"Sample Icon.jpg"*/);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            Toast.makeText(MainActivity.this,"1st try catch..",Toast.LENGTH_SHORT).show();

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                Log.d("myTag", "fileSize:"+fileSize);
                long fileSizeDownload = 0;

                inputStream = body.byteStream();
                //progressbar(/*fileSize*/);
                outputStream = new FileOutputStream(iconFile);


                int i = 0,oldperc=0,perc;
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                while ((i = inputStream.read(fileReader)) != -1) {

                    outputStream.write(fileReader, 0, i);

                    fileSizeDownload += i;

                    perc=(int) (fileSizeDownload * 100 / fileSize);
                    if(oldperc!=perc){
                        oldperc=perc;

                        String var1=decimalFormat.format((float)fileSizeDownload/1000000);
                        String var2=decimalFormat.format((float)fileSize/1000000);


                        if(progress.getProgress()<progress.getMax()){
                            progress.incrementProgressBy(1);
                        }

                        /*else
                            progress.dismiss();*/

                        Log.d("myTag", "file download:"+var1+"/"+var2+"mb\t"+perc+"%");
                        //Log.d("myTag", "file download:" + fileSizeDownload + " of " + fileSize+" "+perc);
                    }
                }

                progress.dismiss();
                outputStream.flush();
                return true;

            } catch (IOException e) {
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


    //Shown progress bar in Dialog box
    private void dialogmodeProgress(){

        progress = new ProgressDialog(MainActivity.this);
        progress.setMax(100);
        //Log.d("myTag", "setmax:"+progQty);
        progress.setMessage("Wait for a while...");
        progress.setTitle("Downloading");
        progress.setProgress(0);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();
    }



    private void progressbar(int prostatus){

        progress = new ProgressDialog(MainActivity.this);
        progress.setMax(100);
        //Log.d("myTag", "setmax:"+progQty);
        progress.setMessage("Wait for a while...");
        progress.setTitle("Downloading");
        progress.setProgress(prostatus);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (progress.getProgress() <= progress.getMax()) {
                        Thread.sleep(200);
                        handle.sendMessage(handle.obtainMessage());

                        if(progress.getProgress() == progress.getMax())
                            progress.dismiss();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        handle = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                progress.incrementProgressBy(1);
            }

        };
    }


    public static class LoopThread extends Thread{

        private static final String TAG=LoopThread.class.getSimpleName();

        Handler handle;



        @Override
        public void run() {
            super.run();
            Looper.prepare();
            handle=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.i(TAG,"Thread id when message is posted: "+ Thread.currentThread().getId()+", Count : "+msg.obj);

                }
            };

            Looper.loop();
        }
    }


}