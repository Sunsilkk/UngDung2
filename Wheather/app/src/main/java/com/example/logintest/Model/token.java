package com.example.logintest.Model;

import com.google.gson.annotations.SerializedName;

public class token {
    @SerializedName("access_token")
    public String access_token;
    @SerializedName("expires_in")
    public int expires_in;
    @SerializedName("refresh_expires_in")
    public int refresh_expires_in;
    @SerializedName("refresh_token")
    public String refresh_token;
    @SerializedName("token_type")
    public String token_type;
    @SerializedName("not-before-policy")
    public int not_before_policy;
    @SerializedName("session_state")
    public String session_state;
    @SerializedName("scope")
    public String scope;

    @SerializedName("attributes")
    public Attributes attributes;
    public class Attributes
    {
        @SerializedName("data")
        public Data data;
        public class Data
        {
            @SerializedName("value")
            public Value value;
            public class Value {
                @SerializedName("dt")
                public String dt;
                @SerializedName("sys")
                public Sys sys;

                public class Sys {
                    @SerializedName("sunset")
                    public String sunset;
                    @SerializedName("sunrise")
                    public String sunrise;
                }
                @SerializedName("main")
                public Main main;
                public class Main
                {
                    @SerializedName("temp")
                    public String temp;
                    @SerializedName("humidity")
                    public String humidity;
                    @SerializedName("feels_like")
                    public String feels_like;
                }
                @SerializedName("name")
                public String name;
                @SerializedName("weather")
                public Weather[] weather;
                public class Weather
                {
                    @SerializedName("description")
                    public String description;
                    @SerializedName("main")
                    public String main;
                }
            }
        }
    }
}
