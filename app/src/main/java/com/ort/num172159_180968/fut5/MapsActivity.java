package com.ort.num172159_180968.fut5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Fields;
import com.ort.num172159_180968.fut5.controller.api.FieldsFactory;
import com.ort.num172159_180968.fut5.model.beans.FieldsResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Fields fields;
    private List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34.8076549, -56.1802311), 11));
        markers = new ArrayList<>();
    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            setUp();
            callWebService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            }
        }
    }

    protected void setUp() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        FieldsFactory controllerFactory = new FieldsFactory(magnetClient);
        fields = controllerFactory.obtainInstance();
    }

    private void callWebService(){
        Call<List<FieldsResult>> callObject = fields.getFields(null);
        if (!callObject.equals(null)) {
            try {
                List<FieldsResult> result = callObject.get();
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.soccerfieldmarker);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bm, 40, 40, true);
                for (FieldsResult field : result) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(field.getFieldLat(), field.getFieldLon())).title(field.getFieldName()).icon(BitmapDescriptorFactory.fromBitmap(newBitmap)));
                    markers.add(marker);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        centerMap();
        //int distance =
    }

    public void centerMap(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(Marker m : markers){
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }
}
