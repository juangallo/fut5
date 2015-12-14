package com.ort.num172159_180968.fut5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Fields fields;
    private List<Marker> markers;
    private DatabaseHelper db;
    private List<Field> fieldList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHelper(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fieldList = db.getAllFields();
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        markers = new ArrayList<>();
    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            System.out.println(fieldList.size());
            if (fieldList.isEmpty()) {
                setUp();
                callWebService();
            }
            addMarkers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMarkers() {
        fieldList = db.getAllFields();
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.soccerfieldmarker);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bm, 40, 40, true);
        Boolean getAddress = true;
        String addressString = "";
        for (Field f: fieldList) {
            /*if (getAddress)
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(f.getFieldLat(), f.getFieldLon(), 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        if (address.getThoroughfare() != null) {
                            sb.append(address.getThoroughfare());
                            if (address.getSubThoroughfare() != null) {
                                sb.append(" " + address.getSubThoroughfare());
                            }
                        }
                    }
                    addressString = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    getAddress = false;
                }*/
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(f.getFieldLat(), f.getFieldLon())).title(f.getFieldName()).snippet(addressString).icon(BitmapDescriptorFactory.fromBitmap(newBitmap)));
            markers.add(marker);

        }

        centerMap();
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
                for (FieldsResult field : result) {
                    Field dbField = new Field(field.getFieldId(), field.getFieldName(), field.getFieldLat(), field.getFieldLon());
                    System.out.println(dbField.getFieldName());
                    long field1 = db.createField(dbField);
                    System.out.println(field1);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }
}
