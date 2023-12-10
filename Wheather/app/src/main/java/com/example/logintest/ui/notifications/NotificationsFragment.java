package com.example.logintest.ui.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.logintest.API.APIWeather;
import com.example.logintest.API.Asset;
import com.example.logintest.API.dataPoint;
import com.example.logintest.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NotificationsFragment extends Fragment {

    String[] attributes = {"temperature","humidity","rainfall","windSpeed"};
    String[] times = {"Hour","Day","Week","Month","Year"};

    ArrayList<Float> dataList = new ArrayList<>();
    ArrayList<String> yourXAxisValues = new ArrayList<>();
    ArrayList<String> ending = new ArrayList<>();
    String end;
    AutoCompleteTextView txtattribute ;
    AutoCompleteTextView txttime ;
    AutoCompleteTextView txtend ;
    ArrayAdapter<String> adapterattributes;
    ArrayAdapter<String> adaptertimes;
    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();
    long totime ;
    long fromtime ;
    String att = "temperature";
    String time = "Day";
    Asset body;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        txtattribute = rootView.findViewById(R.id.attribute);
        txttime = rootView.findViewById(R.id.time);
        txtend = rootView.findViewById(R.id.ending);
        LineChart chart = rootView.findViewById(R.id.chart);
        Button show = rootView.findViewById(R.id.btnshow);
        
        chart.setVisibility(View.GONE);
        txtattribute.setText("temperature");
        adapterattributes = new ArrayAdapter<String>(requireContext(),R.layout.list_item,attributes);
        txtattribute.setAdapter(adapterattributes);

        txttime.setText("Day");
        adaptertimes = new ArrayAdapter<String>(requireContext(),R.layout.list_item,times);
        txttime.setAdapter(adaptertimes);


        txtend.setText(" ");

        txtattribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                att = item;
            }
        });

        txttime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                time = item;
            }
        });



        String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDIyMjQ1MDIsImlhdCI6MTcwMjEzODEwMiwianRpIjoiYmQyZTFiMDItYzFmYi00Y2M2LTllZDgtZDcwNzA2YjVjMTZkIiwiaXNzIjoiaHR0cHM6Ly91aW90Lml4eGMuZGV2L2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0ZTNhNDQ5Ni0yZjE5LTQ4MTMtYmYwMC0wOTQwN2QxZWU4Y2IiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcGVucmVtb3RlIiwic2Vzc2lvbl9zdGF0ZSI6IjNhNWRiYmFhLWEyODgtNDcwNi04Njk1LWVlMmIyZjJhZjYyMCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly91aW90Lml4eGMuZGV2Il0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJkZWZhdWx0LXJvbGVzLW1hc3RlciIsIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJvcGVucmVtb3RlIjp7InJvbGVzIjpbInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6aW5zaWdodHMiLCJyZWFkOmFzc2V0cyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwic2lkIjoiM2E1ZGJiYWEtYTI4OC00NzA2LTg2OTUtZWUyYjJmMmFmNjIwIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiRmlyc3QgTmFtZSBMYXN0IG5hbWUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IkZpcnN0IE5hbWUiLCJmYW1pbHlfbmFtZSI6Ikxhc3QgbmFtZSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.VkJBboCDMmdTGKGllPO_xIulwdwb3GQw02hHuu8aAORG6aFtF1jl0oiKGr1SbdDr6h5t39IOPRz_uzaop5wJsSY47P4gp7PpPuiACzRcjdkyyQKKuPo7g-OUWKM-grqfA1wI9cIZJOv2izxEkN36KLlbfIjiSVc27UvuEvFNI542fN-IC92FeR2iJwgomRU8LAvocwAZetH8IrrKcFsR4-pDfk9DiMBE_sH2KBANXmUGakT2s2XyembdYsW2DLNGBrnoo8kBiobJHOq2Dy5RYtzeOlCBYAepyoXsO-2ASObgDyihbb06Fb55VdPlrUBETr8S1ob7LzAy_8yqkG2iOA";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIWeather apiInterface = retrofit.create(APIWeather.class);
        String aid = "5zI6XqkQVSfdgOrZ1MyWEf";




        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(time == "Hour"){
                    totime = System.currentTimeMillis();
                    fromtime = totime - 3600000;
                    body = new Asset((long)fromtime, (long)totime,"string");

                } else if (time == "Day") {
                    totime = System.currentTimeMillis();
                    fromtime = totime - 86400000 ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if (time == "Week") {
                    totime = System.currentTimeMillis();
                    fromtime = totime - 604800000 ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if ( time=="Month"){
                    totime = System.currentTimeMillis();
                    fromtime = totime - 2678400000L ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if (time =="Year"){
                    totime = System.currentTimeMillis();
                    fromtime = totime - 31536000000L ;
                    body = new Asset((long)fromtime, (long)totime,"string");

                }


                Call<JsonArray> call = apiInterface.getAssetDatapointAttribute(token, aid, att ,body);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        Log.d("API CALL", response.code() + "");
                        JsonArray jsonArray = response.body();
                        if (jsonArray != null) {
                            Gson gson = new Gson();
                            dataList.clear();
                            yourXAxisValues.clear();
                            Type listType = new TypeToken<List<dataPoint>>() {}.getType();
                            List<dataPoint> dataPoints = gson.fromJson(jsonArray, listType);

                            for (dataPoint dataPoint : dataPoints) {
                                long x = dataPoint.getX();
                                float y = (float)dataPoint.getY();
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                                String end = sdf1.format(x);
                                ending.add(end);
                                dataList.add(0, y);

                                if(time == "Hour"){
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int minuteofhour = calendar.get(Calendar.MINUTE);
                                    yourXAxisValues.add(0,Integer.toString(minuteofhour));
                                } else if (time == "Day") {
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH", Locale.getDefault());
                                    String formattedHour = sdf2.format(x);
                                    yourXAxisValues.add(0,formattedHour);
                                } else if(time == "Week"){
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                    String[] days = {"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
                                    String dayName = days[dayOfWeek - 1];
                                    yourXAxisValues.add(0,dayName);
                                } else if (time == "Month") {
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                    yourXAxisValues.add(0,Integer.toString(dayOfMonth));
                                } else if (time == "Year") {
                                    Date date = new Date(x);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    int month = calendar.get(Calendar.MONTH);
                                    yourXAxisValues.add(0,Integer.toString(month));
                                }

                                Log.d("DataPoint", "x: " + x + ", y: " + y);
                            }
                            List<Entry> entries = new ArrayList<>();

                            if(time == "Hour"){
                                int minute = Integer.parseInt(yourXAxisValues.get(0));
                                yourXAxisValues.clear();
                                for (int i = 0; i < 12; i++) {
                                    if(Math.abs(minute-i*5)<3){
                                        entries.add(new Entry(i, dataList.get(0)));
                                    }
                                    else {
                                        entries.add(new Entry(i, -1));
                                    }
                                    yourXAxisValues.add(Integer.toString(i*5));
                                }
                            }
                            else if(time == "Day"){
                                for (int i = 0; i < dataList.size(); i++) {
                                    entries.add(new Entry(i,dataList.get(i)));
                                }
                            }
                            else if (time == "Week") {
                                ArrayList<String> ei = new ArrayList<>(yourXAxisValues);
                                yourXAxisValues.clear();

                                ArrayList<String> week = new ArrayList<>();
                                ArrayList<Float> average = new ArrayList<>();

                                int tmp=0;
                                float sum =0;
                                for (int i = 0;i<ei.size();i++){
                                    sum+=dataList.get(i);
                                    tmp++;
                                    if((i<ei.size()-1 && ei.get(i+1)!=ei.get(i)) || i==ei.size()-1){
                                        week.add(ei.get(i));
                                        if(week.size()>0){
                                            average.add(sum/tmp);
                                        }
                                        tmp=0;
                                        sum =0;
                                    }

                                }
                                for (int i=0;i<week.size();i++){
                                    entries.add(new Entry(i, average.get(i)));
                                    yourXAxisValues.add(week.get(i));
                                }
                            } else if (time =="Month") {
                                ArrayList<String> th = new ArrayList<>(yourXAxisValues);
                                yourXAxisValues.clear();

                                ArrayList<String> month = new ArrayList<>();
                                ArrayList<Float> average = new ArrayList<>();

                                int tmp=0;
                                float sum =0;
                                for (int i = 0;i<th.size();i++){
                                    sum+=dataList.get(i);
                                    tmp++;
                                    if((i<th.size()-1 && th.get(i+1)!=th.get(i)) || i==th.size()-1){
                                        month.add(th.get(i));
                                        if(month.size()>0){
                                            average.add(sum/tmp);
                                        }
                                        tmp=0;
                                        sum =0;
                                    }

                                }
                                for (int i=0;i<month.size();i++){
                                    entries.add(new Entry(i, average.get(i)));
                                    yourXAxisValues.add(month.get(i));
                                }
                            } else if (time == "Year") {
                                ArrayList<String> tw = new ArrayList<>(yourXAxisValues);
                                yourXAxisValues.clear();

                                ArrayList<String> year = new ArrayList<>();
                                ArrayList<Float> average = new ArrayList<>();

                                int tmp=0;
                                float sum =0;
                                for (int i = 0;i<tw.size();i++){
                                    sum+=dataList.get(i);
                                    tmp++;
                                    if((i<tw.size()-1 && tw.get(i+1)!=tw.get(i)) || i==tw.size()-1){
                                        year.add(tw.get(i));
                                        if(year.size()>0){
                                            average.add(sum/tmp);
                                        }
                                        tmp=0;
                                        sum =0;
                                    }

                                }
                                for (int i=0;i<year.size();i++){
                                    entries.add(new Entry(i, average.get(i)));
                                    yourXAxisValues.add(year.get(i));
                                }
                            }

                            txtend.setText(ending.get(0).toString());
                            LineDataSet dataSet = new LineDataSet(entries, att); // add entries to dataset
                            if(time=="Hour"){
                                dataSet.enableDashedLine(0, 1, 0);
                                chart.getAxisLeft().setStartAtZero(true);
                                chart.getXAxis().setLabelCount(entries.size()/2);
                            } else if (time == "Day") {
                                chart.getAxisLeft().setStartAtZero(false);
                                chart.getXAxis().setLabelCount(entries.size()/2);
                                dataSet.setDrawFilled(true);
                            } else if (time == "Week") {
                                chart.getAxisLeft().setStartAtZero(false);

                                dataSet.setDrawFilled(true);
                            } else if (time == "Month") {
                                chart.getAxisLeft().setStartAtZero(false);
                                chart.getXAxis().setLabelCount(entries.size()/2);
                                dataSet.setDrawFilled(true);
                            } else if (time == "Year") {
                                chart.getAxisLeft().setStartAtZero(false);
                                chart.getXAxis().setLabelCount(entries.size()/4);
                                dataSet.setDrawFilled(true);}

                            chart.setVisibility(View.VISIBLE);
                            dataSet.setColor(Color.BLUE);
                            dataSet.setValueTextColor(Color.BLACK);
                            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // if you want the line to be smooth
                            // if you want the area below the line to be filled
                            dataSet.setFillColor(Color.BLUE);
                            dataSet.setDrawCircles(true); // to draw the circles
                            dataSet.setDrawValues(false);
                            dataSet.setCircleColor(Color.BLUE);
                            dataSet.setCircleRadius(5f);

                            LineData lineData = new LineData(dataSet);
                            chart.setData(lineData);
// Styling
                            chart.getDescription().setEnabled(false); // No description at the bottom
                            chart.getAxisLeft().setDrawGridLines(false); // No grid lines
                            chart.getAxisLeft().setDrawAxisLine(false); // No axis line
                            chart.getAxisLeft().setDrawLabels(true); // to show y-axis labels
                            chart.getXAxis().setDrawLabels(true);
                            chart.getXAxis().setDrawGridLines(false); // No grid lines
                            chart.getXAxis().setDrawAxisLine(true); // Only show the x-axis line
                            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); // X-axis at the bottom

                            if(yourXAxisValues.size()==1){
                                chart.getXAxis().setLabelCount(yourXAxisValues.size());
                            }
                            chart.getAxisRight().setEnabled(false); // No right y-axis
                            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(yourXAxisValues){}); // To format the x-axis labels
                            chart.getLegend().setEnabled(true); // No legend

                            chart.invalidate(); // refresh
                        }
                        else {
                            Log.d("API CALL", "Response not successful");
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.d("API CALL", t.getMessage().toString());
                    }
                });
            }

        });

        return rootView;
    }


}