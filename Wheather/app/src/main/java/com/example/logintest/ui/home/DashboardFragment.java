package com.example.logintest.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.logintest.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DashboardFragment extends Fragment {
    private TextView city, temp, main, humidity, wind, realFeel, time;
    private ImageView weatherImage;
    private FusedLocationProviderClient client;
    private static int indexfor = 5;
    private static String lat;
    private static String lon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentdashboard, container, false);

        city = view.findViewById(R.id.id_city);
        temp = view.findViewById(R.id.id_degree);
        main = view.findViewById(R.id.id_main);
        humidity = view.findViewById(R.id.id_humidity);
        wind = view.findViewById(R.id.id_wind);
        realFeel = view.findViewById(R.id.id_realfeel);
        weatherImage = view.findViewById(R.id.id_weatherImage);
        time = view.findViewById(R.id.id_time);
        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
            }
        }

        client.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = Math.round(location.getLatitude() * 100.0) / 100.0;
                    lat = String.valueOf(latitude);

                    double longitude = Math.round(location.getLongitude() * 100.0) / 100.0;
                    lon = String.valueOf(longitude);

                    WeatherByLatLon(lat, lon);
                } else {
                    WeatherByCityName("London");
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        client.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    double latitude = Math.round(location.getLatitude() * 100.0) / 100.0;
                                    lat = String.valueOf(latitude);

                                    double longitude = Math.round(location.getLongitude() * 100.0) / 100.0;
                                    lon = String.valueOf(longitude);

                                    WeatherByLatLon(lat, lon);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void WeatherByCityName(String city) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + "a89a6e08937dc3686c3da1c1bc341285" + "&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    // Handle failure
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data = response.body().string();
                    try {
                        JSONObject json = new JSONObject(data);
                        JSONObject city = json.getJSONObject("city");
                        JSONObject coord = city.getJSONObject("coord");
                        String lat = coord.getString("lat");
                        String lon = coord.getString("lon");
                        WeatherByLatLon(lat, lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WeatherByLatLon(String lat, String lon) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + "a89a6e08937dc3686c3da1c1bc341285" + "&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    // Handle failure
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data = response.body().string();
                    try {
                        JSONObject json = new JSONObject(data);
                        // Parse the weather data and update the UI
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}