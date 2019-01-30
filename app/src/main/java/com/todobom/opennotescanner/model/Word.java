package com.todobom.opennotescanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {

    @SerializedName("box")
    @Expose
    private String box;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("probability")
    @Expose
    private Double probability;


    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

}