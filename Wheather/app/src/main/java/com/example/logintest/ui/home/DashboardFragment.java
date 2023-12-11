package com.example.logintest.ui.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.logintest.API.APIInterface;
import com.example.logintest.API.ApIClient;
import com.example.logintest.Model.token;
import com.example.logintest.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView day = rootView.findViewById(R.id.day);
        TextView location = rootView.findViewById(R.id.location);
        TextView Temper = rootView.findViewById(R.id.temp);
        TextView Feels = rootView.findViewById(R.id.feels);
        TextView descr = rootView.findViewById(R.id.descr);
        TextView Sunrise =rootView.findViewById(R.id.sunrise);
        TextView Sunset = rootView.findViewById(R.id.sunset);
        TextView humid = rootView.findViewById(R.id.humid);
        TextView hi = rootView.findViewById(R.id.hi);
        ImageView descrImg = rootView.findViewById(R.id.descrImg);

        APIInterface apiInterface;

        apiInterface = ApIClient.getClient().create(APIInterface.class);
//        hi.setText(name);
        Call<token> call = apiInterface.getAsset("4EqQeQ0L4YNWNNTzvTOqjy");
        call.enqueue(new Callback<token>() {
            @Override
            public void onResponse(Call<token> call, Response<token> response) {
                token asset = response.body();

                String dt = asset.attributes.data.value.dt;
                try {
                    long time = Long.parseLong(dt) * 1000L;
                    Date date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                    String _day = sdf.format(date);
                    day.setText(_day);
                } catch (NumberFormatException e) {
                }
                String sunrise = asset.attributes.data.value.sys.sunrise;
                try {
                    long time = Long.parseLong(sunrise) * 1000L;
                    Date timerise = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String formattedTimerise = sdf.format(timerise);
                    Sunrise.setText("Sunrise \n "+formattedTimerise);
                } catch (NumberFormatException e) {
                }
                String sunset = asset.attributes.data.value.sys.sunset;
                try {
                    long time = Long.parseLong(sunset) * 1000L;
                    Date timeset = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String formattedTimeset = sdf.format(timeset);
                    Sunset.setText("Sunset \n"+formattedTimeset);
                } catch (NumberFormatException e)
                {
                }
                String temp = asset.attributes.data.value.main.temp;
                try {
                    float floatValue = Float.parseFloat(temp);
                    long inttemp = Math.round(floatValue);
                    Temper.setText(String.valueOf(inttemp)+"\u00B0C");
                } catch (NumberFormatException e) {
                }
                String feels = asset.attributes.data.value.main.feels_like;
                try {
                    float floatValue = Float.parseFloat(feels);
                    long intfeels = Math.round(floatValue);
                    Feels.setText("Feels Like: "+String.valueOf(intfeels) + "\u00B0C");
                } catch (NumberFormatException e) {
                }
                String humidity = asset.attributes.data.value.main.humidity;
                humid.setText("Humidity \n"+ humidity + "%");
                String Location = asset.attributes.data.value.name;
                location.setText(Location);
                String desc = asset.attributes.data.value.weather[0].description;
                descr.setText(desc);
                String main = asset.attributes.data.value.weather[0].main;
                switch (main) {
                    case "Clouds":
                        descrImg.setImageResource(R.drawable.cloud);
                        break;
                    case "Clear":
                        descrImg.setImageResource(R.drawable.clear);
                        break;
                    case "Rain":
                        descrImg.setImageResource(R.drawable.rain);
                        break;
                    case "Sunny":
                        descrImg.setImageResource(R.drawable.sun);
                        break;
                }
            }

            @Override
            public void onFailure(Call<token> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
            }
        });
        return rootView;
    }
}