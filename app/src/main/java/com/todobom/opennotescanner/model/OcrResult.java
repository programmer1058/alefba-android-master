package com.todobom.opennotescanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OcrResult {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("document")
    @Expose
    private Document document;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}