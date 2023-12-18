package com.example.logintest.ui.Map;

import static com.example.logintest.allVar.darkBackground;
import static com.example.logintest.allVar.globalAssetid;

import android.Manifest;

import com.example.logintest.API.APIInterface;
import com.example.logintest.API.ApIClient;
import com.example.logintest.Model.AssetID;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.logintest.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private LinearLayout layout;
    TextView textView;
    TextView text2;
    CardView dialogMap;
    private long ZoomSpeed = 500;
    private double MinZoom = 18.0;
    private double MaxZoom = 21.0;
    private double ZoomLevel = 19.5;
    private GeoPoint UITLocation = new GeoPoint(10.870, 106.80324);
    String Station1STR = "Default Weather";
    String Station2STR = "Light";
    private GeoPoint Station1 = new GeoPoint(10.869778736885038, 106.80280655508835);
    private GeoPoint Station2 = new GeoPoint(10.869905172970164, 106.80345028525176);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        textView = view.findViewById(R.id.textMap);
        textView.setVisibility(View.INVISIBLE);
        text2 = view.findViewById(R.id.header_title);
        dialogMap = view.findViewById(R.id.dialog_map);

        ConstraintLayout background = view.findViewById(R.id.mapFrag);
        if (darkBackground)
        {
            background.setBackgroundResource(R.drawable.dark_backgr);
            textView.setBackgroundResource(R.drawable.dark_backgr);
            text2.setBackgroundResource(R.drawable.dark_backgr);
        }
        Context ctx = requireContext();

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = view.findViewById(R.id.map);

        map.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (map.getVisibility() == View.VISIBLE){
                    dialogMap.setVisibility(View.INVISIBLE);
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));

        Marker Station1Marker = new Marker(map);
        Station1Marker.setPosition(Station1);
        Station1Marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.temsensor);
        int iconWidth = 40; // Specify the desired width of the icon in pixels
        int iconHeight = 40; // Specify the desired height of the icon in pixels
        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) icon).getBitmap(), iconWidth, iconHeight, false);
        Drawable resizedIcon = new BitmapDrawable(getResources(), bitmap);
        Station1Marker.setIcon(resizedIcon);


        Marker Station2Marker = new Marker(map);
        Station2Marker.setPosition(Station2);
        Station2Marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        Drawable icon1 = ContextCompat.getDrawable(requireContext(), R.drawable.light);
        int iconWidth1 = 40; // Specify the desired width of the icon in pixels
        int iconHeight1 = 40; // Specify the desired height of the icon in pixels
        Bitmap bitmap1 = Bitmap.createScaledBitmap(((BitmapDrawable) icon1).getBitmap(), iconWidth1, iconHeight1, false);
        Drawable resizedIcon1 = new BitmapDrawable(getResources(), bitmap1);
        Station2Marker.setIcon(resizedIcon1);


        Station1Marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                INFO(Station1STR);
                return true;
            }
        });
        Station2Marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                INFO(Station2STR);
                return true;
            }
        });
        map.addOnFirstLayoutListener(new MapView.OnFirstLayoutListener() {
            @Override
            public void onFirstLayout(View v, int left, int top, int right, int bottom) {
                map.setTileSource(TileSourceFactory.MAPNIK);
                map.setMultiTouchControls(true);
                map.setMinZoomLevel(MinZoom);
                map.setMaxZoomLevel(MaxZoom);
                map.getController().setZoom(ZoomLevel);
                map.setScrollableAreaLimitLatitude(10.875, 10.865, 100);
                map.setScrollableAreaLimitLongitude(106.800, 106.806, 100);
                map.getController().setCenter(UITLocation);
                map.getOverlays().add(Station1Marker);
                map.getOverlays().add(Station2Marker);
                map.invalidate();
            }
        });

        String[] Permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};

        requestPermissionsIfNecessary(Permission);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    public void INFO(String STR){
        APIInterface apiInterface;
        apiInterface = ApIClient.getClient().create(APIInterface.class);
        Call<AssetID[]> call = apiInterface.getAssetID();
        call.enqueue(new Callback<AssetID[]>() {
            @Override
            public void onResponse(Call<AssetID[]> call, Response<AssetID[]> response) {
                Log.d("API CALL", response.code() + "");
                Log.d("API CALL", response.toString());
                AssetID assetID[] = response.body();
                AssetID id = null;
                if (assetID != null){
                    for (int strID = 0; strID < assetID.length; strID++) {
                        if (assetID[strID].name.equals(STR)) {
                            Log.d("MAP ID", "onResponse: "+assetID[strID].id);
                            id=assetID[strID];
                            globalAssetid = id;
                            break;
                        }

                    }}
                Log.d("MAP ID", "onResponse: "+id.id);
                if (id.attributes.place != null) {
                    textView.setText(" ");
                    dialogMap.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    text2.setText("Weather");
                    textView.append("\nPlace: " + globalAssetid.attributes.place.value);
                    textView.append("\nManufacturer: " + globalAssetid.attributes.manufacturer.value);
                    textView.append("\nTemperature: " + globalAssetid.attributes.temp.value);
                    textView.append("\n:Wind speed " + globalAssetid.attributes.windSpeed.value);
                    textView.append("\nHumidity: " + globalAssetid.attributes.hum.value);
                    textView.append("\nRainfall: " + globalAssetid.attributes.rainfall.value);
                }
//                    Log.d("View", "onResponse: "+id.attributes.place.value+" "+id.attributes.temp.value+" "+id.attributes.rainfall.value+" "+id.attributes.hum.value);
//                    textView.setText("Place: "+id.attributes.place.value+"\nTemperature: "+id.attributes.temp.value+"\nRainfall: "+id.attributes.rainfall.value+"\nHumidity "+id.attributes.hum.value);
                else {
                    if (globalAssetid.attributes.bright != null) {
                        textView.setText(" ");
                        text2.setText("Brightness");
                        dialogMap.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.append("\nBrightness: " + globalAssetid.attributes.bright.value);
                        textView.append("\nColourTemperature: " + globalAssetid.attributes.colourTemperature.value);
                        if (globalAssetid.attributes.colourRGB.value != null)
                            textView.append("\nBrightness: " + globalAssetid.attributes.colourRGB.value);
                        textView.append("\nEmail: " + globalAssetid.attributes.email.value);
                        textView.append("\nOn off: " + globalAssetid.attributes.onOff.value);
                    }
                }

            }
            @Override
            public void onFailure(Call<AssetID[]> call, Throwable t) {
            }
        });}


    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}