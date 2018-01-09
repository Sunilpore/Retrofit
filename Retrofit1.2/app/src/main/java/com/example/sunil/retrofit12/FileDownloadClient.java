package com.example.sunil.retrofit12;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Sunil on 1/7/2018.
 */

public interface FileDownloadClient {

    String BASE_URL="http://www.gadgetsaint.com/";

    @GET("wp-content/uploads/2016/11/cropped-web_hi_res_512.png")
    Call<ResponseBody> getFileDownloadData();

    @GET
    Call <ResponseBody> getFileDownloadData(@Url String url);

    @Streaming
    @GET
    Call <ResponseBody> getFileDownloadDataStreaming(@Url String url);
}
