package com.example.logintest.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.logintest.CitySearch;
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
import static android.app.Activity.RESULT_OK;
import static com.example.logintest.allVar.darkBackground;

public class DashboardFragment extends Fragment {
    private TextView city, temp, main, humidity, wind, realFeel, time, id_degree, id_main;
    private ImageView weatherImage;
    private FusedLocationProviderClient client;
    private static int indexfor = 5;
    private static String lat;
    private static String lon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardddddd, container, false);

        ScrollView background = view.findViewById(R.id.dasshhhh);
        id_degree = view.findViewById(R.id.id_degree);
        id_main = view.findViewById(R.id.id_main);
        if (darkBackground)
        {
            background.setBackgroundResource(R.drawable.dark_backgr);
            id_degree.setTextColor(Color.WHITE);
            id_main.setTextColor(Color.WHITE);
        }
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
                    WeatherByCityName("Ho Chi Minh");
                }
            }
        });

        Button idoption = view.findViewById(R.id.id_options);
        idoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
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
                .url("https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + "b7e355ceb16c12ca7846fbc2b724e15f" + "&units=metric")
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

    private void WeatherByLatLon(String lat,String lon){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid="+"b7e355ceb16c12ca7846fbc2b724e15f"+"&units=metric")
                .get().build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response=client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);
                        TextView[] forecast = new TextView[5];
                        TextView[] forecastTemp=new TextView[5];
                        ImageView[] forecastIcons=new ImageView[5];
                        IdAssign(forecast,forecastTemp,forecastIcons);

                        indexfor=5;
                        for (int i=0;i<forecast.length;i++){
                            forecastCal(forecast[i],forecastTemp[i],forecastIcons[i],indexfor,json);
                        }

                        JSONArray list=json.getJSONArray("list");
                        JSONObject objects = list.getJSONObject(0);
                        JSONArray array=objects.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons=object.getString("icon");

                        Date currentDate=new Date();
                        String dateString=currentDate.toString();
                        String[] dateSplit=dateString.split(" ");
                        String date=dateSplit[0]+", "+dateSplit[1] +" "+dateSplit[2];

                        JSONObject Main=objects.getJSONObject("main");
                        double temparature=Main.getDouble("temp");
                        String Temp=Math.round(temparature)+"°C";
                        double Humidity=Main.getDouble("humidity");
                        String hum=Math.round(Humidity)+"%";
                        double FeelsLike=Main.getDouble("feels_like");
                        String feelsValue=Math.round(FeelsLike)+"°";

                        JSONObject Wind=objects.getJSONObject("wind");
                        String windValue=Wind.getString("speed")+" "+"km/h";

                        JSONObject CityObject=json.getJSONObject("city");
                        String City=CityObject.getString("name");

                        setDataText(city,City);
                        setDataText(temp,Temp);
                        setDataText(main,description);
                        setDataImage(weatherImage,icons);
                        setDataText(time,date);
                        setDataText(humidity,hum);
                        setDataText(realFeel,feelsValue);
                        setDataText(wind,windValue);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setDataText(final TextView text, final String value) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setDataImage(final ImageView ImageView, final String value){
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value){
                    case "01d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w01d)); break;
                    case "01n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w01d)); break;
                    case "02d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w02d)); break;
                    case "02n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w02d)); break;
                    case "03d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d)); break;
                    case "03n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d)); break;
                    case "04d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w04d)); break;
                    case "04n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w04d)); break;
                    case "09d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w09d)); break;
                    case "09n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w09d)); break;
                    case "10d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w10d)); break;
                    case "10n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w10d)); break;
                    case "11d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w11d)); break;
                    case "11n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w11d)); break;
                    case "13d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w13d)); break;
                    case "13n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w13d)); break;
                    default:ImageView.setImageDrawable(getResources().getDrawable(R.drawable.w03d));

                }
            }
        });
    }


    private void forecastCal(TextView forecast,TextView forecastTemp,ImageView forecastIcons,int index,JSONObject json) throws JSONException {
        JSONArray list=json.getJSONArray("list");
        for (int i=index; i<list.length(); i++) {
            JSONObject object = list.getJSONObject(i);

            String dt=object.getString("dt_txt"); // dt_text.format=2020-06-26 12:00:00
            String[] a=dt.split(" ");
            if ((i==list.length()-1) && !a[1].equals("12:00:00")){
                String[] dateSplit=a[0].split("-");
                Calendar calendar=new GregorianCalendar(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])-1,Integer.parseInt(dateSplit[2]));
                Date forecastDate=calendar.getTime();
                String dateString=forecastDate.toString();
                String[] forecastDateSplit=dateString.split(" ");
                String date=forecastDateSplit[0]+", "+forecastDateSplit[1] +" "+forecastDateSplit[2];
                setDataText(forecast, date);

                JSONObject Main=object.getJSONObject("main");
                double temparature=Main.getDouble("temp");
                String Temp=Math.round(temparature)+"°";
                setDataText(forecastTemp,Temp);

                JSONArray array=object.getJSONArray("weather");
                JSONObject object1=array.getJSONObject(0);
                String icons=object1.getString("icon");
                setDataImage(forecastIcons,icons);

                return;
            }
            else if (a[1].equals("12:00:00")){

                String[] dateSplit=a[0].split("-");
                Calendar calendar=new GregorianCalendar(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])-1,Integer.parseInt(dateSplit[2]));
                Date forecastDate=calendar.getTime();
                String dateString=forecastDate.toString();
                String[] forecastDateSplit=dateString.split(" ");
                String date=forecastDateSplit[0]+", "+forecastDateSplit[1] +" "+forecastDateSplit[2];
                setDataText(forecast, date);


                JSONObject Main=object.getJSONObject("main");
                double temparature=Main.getDouble("temp");
                String Temp=Math.round(temparature)+"°";
                setDataText(forecastTemp,Temp);

                JSONArray array=object.getJSONArray("weather");
                JSONObject object1=array.getJSONObject(0);
                String icons=object1.getString("icon");
                setDataImage(forecastIcons,icons);


                indexfor=i+1;
                return;
            }
        }
    }

    private void IdAssign(TextView[] forecast, TextView[] forecastTemp, ImageView[] forecastIcons) {
        View rootView = getView();
        if (rootView != null) {
            forecast[0] = rootView.findViewById(R.id.id_forecastDay1);
            forecast[1] = rootView.findViewById(R.id.id_forecastDay2);
            forecast[2] = rootView.findViewById(R.id.id_forecastDay3);
            forecast[3] = rootView.findViewById(R.id.id_forecastDay4);
            forecast[4] = rootView.findViewById(R.id.id_forecastDay5);
            forecastTemp[0] = rootView.findViewById(R.id.id_forecastTemp1);
            forecastTemp[1] = rootView.findViewById(R.id.id_forecastTemp2);
            forecastTemp[2] = rootView.findViewById(R.id.id_forecastTemp3);
            forecastTemp[3] = rootView.findViewById(R.id.id_forecastTemp4);
            forecastTemp[4] = rootView.findViewById(R.id.id_forecastTemp5);
            forecastIcons[0] = rootView.findViewById(R.id.id_forecastIcon1);
            forecastIcons[1] = rootView.findViewById(R.id.id_forecastIcon2);
            forecastIcons[2] = rootView.findViewById(R.id.id_forecastIcon3);
            forecastIcons[3] = rootView.findViewById(R.id.id_forecastIcon4);
            forecastIcons[4] = rootView.findViewById(R.id.id_forecastIcon5);
        }
    }


    public void showPopup(View v) {
        Context context = requireContext(); // Use the Fragment's context
        PopupMenu popup = new PopupMenu(context, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click events
                return DashboardFragment.this.onMenuItemClick(item);
            }
        });
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }



    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.id_currentLocation) {
            WeatherByLatLon(lat, lon);
            return true;
        } else if (itemId == R.id.id_otherCity) {
            Intent intent = new Intent(requireActivity(), CitySearch.class);
            startActivityForResult(intent, 1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String citySearched=data.getStringExtra("result");
                WeatherByCityName(citySearched);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }
}
