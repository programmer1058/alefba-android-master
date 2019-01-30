package com.todobom.opennotescanner.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.todobom.opennotescanner.model.OcrResult;

@Entity(tableName = "ocr_table")
public class Ocr {


    @PrimaryKey
    @NonNull
    @ColumnInfo(name="uri")
    private String imageUri;

    @ColumnInfo(name="content")
    private String contentJson;

    Ocr(@NonNull String imageUri, String contentJson){
        this.imageUri = imageUri;
        this.contentJson = contentJson;
    }

    public Ocr(@NonNull String uri, OcrResult result){
        imageUri = uri;
        setContentJson(result);
    }

    @NonNull
    public String getImageUri(){
        return imageUri;
    }

    public String getContentJson(){
        return contentJson;
    }

    public OcrResult getOcrResult(){
        Gson gson = new Gson();
        return gson.fromJson(contentJson, OcrResult.class);
    }

    public void setContentJson(String json){
        contentJson = json;
    }

    private void setContentJson(OcrResult result){
        Gson gson = new Gson();
        contentJson = gson.toJson(result);
    }
}
