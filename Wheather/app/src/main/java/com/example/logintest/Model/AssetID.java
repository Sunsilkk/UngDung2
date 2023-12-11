package com.example.logintest.Model;
import com.google.gson.annotations.SerializedName;

public class AssetID {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("attributes")
    public Attributes attributes;
    public class Attributes {
        @SerializedName("rainfall")
        public Rainfall rainfall;
        public class Rainfall{
            @SerializedName("value")
            public String value;
        }
        @SerializedName("temperature")
        public Temp temp;
        public class Temp{
            @SerializedName("value")
            public String value;
        }
        @SerializedName("humidity")
        public Hum hum;
        public class Hum{
            @SerializedName("value")
            public String value;
        }
        @SerializedName("place")
        public Place place;
        public class Place{
            @SerializedName("value")
            public String value;
        }


    }
}
