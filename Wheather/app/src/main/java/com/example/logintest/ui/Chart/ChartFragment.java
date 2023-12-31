package com.example.logintest.ui.Chart;

import static com.example.logintest.allVar.darkBackground;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import com.example.logintest.API.APIWeather;
import com.example.logintest.API.ApIClient;
import com.example.logintest.API.Asset;
import com.example.logintest.API.dataPoint;
import com.example.logintest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParsePosition;
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


public class ChartFragment extends Fragment {

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
    String selectedDateTime;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        ConstraintLayout background = rootView.findViewById(R.id.chartFrag);
        if (darkBackground)
        {
            background.setBackgroundResource(R.drawable.chart_light_background);
        }
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
        txtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePickerDialog();

            }
        });
        // Create an instance of the APIInterface
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://uiot.ixxc.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIWeather apiInterface = retrofit.create(APIWeather.class);
        String aid = "5zI6XqkQVSfdgOrZ1MyWEf";

        String atoken = "Bearer "+ ApIClient.getToken();


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtend.getText().toString().equals(" ")){
                    totime = System.currentTimeMillis();
                }else {
                    txtend.setText(txtend.getText().toString());
                    String dateTimeString = txtend.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    ParsePosition pp1 = new ParsePosition(0);
                    Date date = dateFormat.parse(dateTimeString,pp1);
                    long timestamp = date.getTime();
                    totime = timestamp;
                    Log.d("date2", String.valueOf(timestamp));
                }

                if(time == "Hour"){
                    fromtime = totime - 3600000;
                    body = new Asset((long)fromtime, (long)totime,"string");

                } else if (time == "Day") {
                    fromtime = totime - 86400000 ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if (time == "Week") {
                    fromtime = totime - 604800000 ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if ( time=="Month"){
                    fromtime = totime - 2678400000L ;
                    body = new Asset((long)fromtime, (long)totime,"string");
                } else if (time =="Year"){
                    fromtime = totime - 31536000000L ;
                    body = new Asset((long)fromtime, (long)totime,"string");

                }

                Call<JsonArray> call = apiInterface.getAssetDatapointAttribute(atoken, aid, att ,body);


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
                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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

                            try {
                                txtend.setText(ending.get(0).toString());
                            }
                            catch(Exception e) {
                                dataList.add((float)-1);
                                yourXAxisValues.add(String.valueOf(0));

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
                                chart.getXAxis().setLabelCount(entries.size());
                                dataSet.setDrawFilled(true);}

                            if(yourXAxisValues.size()==1){
                                chart.getXAxis().setLabelCount(yourXAxisValues.size());
                                chart.getAxisLeft().setStartAtZero(true);
                            }

                            chart.setVisibility(View.VISIBLE);


                            dataSet.setDrawCircles(true);
                            dataSet.setDrawValues(false);
                            dataSet.setColor(Color.YELLOW);
                            dataSet.setValueTextColor(Color.BLACK);
                            dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                            dataSet.setFillColor(Color.YELLOW);
                            dataSet.setCircleColor(Color.YELLOW);
                            dataSet.setCircleRadius(5f);


                            chart.setBackgroundColor(Color.TRANSPARENT);
                            chart.setGridBackgroundColor(Color.GRAY);
                            chart.setBorderColor(Color.DKGRAY);
                            chart.setBorderWidth(2f);
                            chart.getDescription().setEnabled(false);

                            XAxis xAxis = chart.getXAxis();
                            xAxis.setTextColor(Color.BLACK);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(false);

                            YAxis yAxis = chart.getAxisLeft();
                            yAxis.setTextColor(Color.BLACK);
                            yAxis.setDrawGridLines(true);
                            yAxis.setGridColor(Color.GRAY);


                            chart.animateX(1000);

                            chart.invalidate();

                            LineData lineData = new LineData(dataSet);
                            chart.setData(lineData);
                            chart.getDescription().setEnabled(false);
                            chart.getAxisLeft().setDrawGridLines(false);
                            chart.getAxisLeft().setDrawAxisLine(false);
                            chart.getAxisLeft().setDrawLabels(true);
                            chart.getXAxis().setDrawLabels(true);
                            chart.getXAxis().setDrawGridLines(false);
                            chart.getXAxis().setDrawAxisLine(true);
                            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);


                            chart.getAxisRight().setEnabled(false); // No right y-axis
                            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(yourXAxisValues){});
                            chart.getLegend().setEnabled(true); // No legend

                            chart.invalidate(); // refresh
                            ending.clear();

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
    private void showDateTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý ngày tháng năm được chọn
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        // TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Xử lý giờ phút được chọn
                                        String time = hourOfDay + ":" + minute;

                                        // Kết hợp ngày và giờ để có ngày giờ đầy đủ
                                        selectedDateTime = date + " " + time;

                                        // Hiển thị lên EditText
                                        txtend.setText(selectedDateTime);
                                        Log.d("date",selectedDateTime);
                                    }
                                }, hour, minute, true); // Đặt true để hiển thị đồng hồ 24 giờ

                        // Hiển thị hộp thoại chọn giờ
                        timePickerDialog.show();
                    }
                }, year, month, day);

        // Hiển thị hộp thoại chọn ngày
        datePickerDialog.show();
    }



}