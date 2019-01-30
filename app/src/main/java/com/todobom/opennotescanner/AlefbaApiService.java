package com.todobom.opennotescanner;

import com.todobom.opennotescanner.model.OcrResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Farbod on 11/10/2016.
 */
public interface AlefbaApiService {
    @Multipart
    @POST("read_image/")
    Call<OcrResult> readImage(@Part MultipartBody.Part request);


}