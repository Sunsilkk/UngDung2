package com.example.logintest;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private static final double MIN_ZOOM = 18.0;
    private static final double MAX_ZOOM = 21.0;
    private static final double ZOOM_LEVEL = 19.5;
    private static final GeoPoint UIT_LOCATION = new GeoPoint(10.870, 106.80324);
    private static final GeoPoint STATION_1 = new GeoPoint(10.869778736885038, 106.80280655508835);
    private static final GeoPoint STATION_2 = new GeoPoint(10.869778736885038, 106.80345028525176);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        map = rootView.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(MIN_ZOOM);
        map.setMaxZoomLevel(MAX_ZOOM);
        map.getController().setZoom(ZOOM_LEVEL);
        map.setScrollableAreaLimitLatitude(10.875, 10.865, 100);
        map.setScrollableAreaLimitLongitude(106.800, 106.806, 100);
        map.getController().setCenter(UIT_LOCATION);

        Marker station1Marker = new Marker(map, requireContext());
        station1Marker.setPosition(STATION_1);
        station1Marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        station1Marker.setIcon(requireContext().getDrawable(R.drawable.baseline_cloud_24));

        Marker station2Marker = new Marker(map, requireContext());
        station2Marker.setPosition(STATION_2);
        station2Marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        station2Marker.setIcon(requireContext().getDrawable(R.drawable.baseline_cloud_24));

        map.getOverlays().add(station1Marker);
        map.getOverlays().add(station2Marker);

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionsIfNecessary(permissions);

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}