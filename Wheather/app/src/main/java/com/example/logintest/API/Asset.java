package com.example.logintest.API;

import com.google.gson.annotations.SerializedName;

public class Asset {
    @SerializedName("fromTimestamp")
    private long fromTimestamp;
    @SerializedName("toTimestamp")
    private long toTimestamp;
    @SerializedName("type")
    private String type;

    public Asset(long fromTimestamp, long toTimestamp, String type){

        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
        this.type = type;
    }
}
