package com.ort.num172159_180968.fut5;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    HttpClient client;
    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        client = HttpClientBuilder.create().build();
        new Read().execute();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-34.8076549, -56.1802311), 11));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public JSONArray getFields() throws IOException, JSONException{
        //StringBuilder urlBuilder = new StringBuilder(URL);
        System.out.println((String)getResources().getString(R.string.backend_ip));
        System.out.println("http://" + (String)getResources().getString(R.string.backend_ip) + ":8080/Fut5-war/webservice/getFields");
        String url = "http://" + (String)getResources().getString(R.string.backend_ip) + ":8080/Fut5-war/webservice/getFields";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        int status = response.getStatusLine().getStatusCode();
        System.out.println(status);
        if (status == 200){
            HttpEntity entity = response.getEntity();
            String data = EntityUtils.toString(entity);
            JSONArray fields = new JSONArray(data);
            //int i = 0;
            //while(i < fields.length()) {
            //JSONObject field = fields.getJSONObject(0);
            return fields;
            //}
        } else {
            Toast.makeText(MapsActivity.this, "ERROR", Toast.LENGTH_SHORT);
            return null;
        }
    }

    public class Read extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                int i = 0;
                JSONArray fields = getFields();
                String res = "";
                while(i < fields.length()) {
                    JSONObject field = fields.getJSONObject(i);
                    res += field.getString("fieldName") + "$" + field.getString("fieldLat") + "$" + field.getString("fieldLon");
                    if (i != fields.length()-1) {
                        res += "&";
                    }
                    i++;
                }

                return res;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                StringTokenizer tokensField = new StringTokenizer(s, "&");
                while (tokensField.hasMoreTokens()) {
                    StringTokenizer tokens = new StringTokenizer(tokensField.nextToken(), "$");
                    String first = tokens.nextToken();// this will contain "Fruit"
                    String second = tokens.nextToken();
                    String third = tokens.nextToken();
                    double lat = Double.parseDouble(second);
                    double lon = Double.parseDouble(third);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(first));
                }
            }
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        /*mMap.addMarker(new MarkerOptions().position(new LatLng(-34.8736473, -56.1775762)).title("0 Stress"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.832757,-56.005812)).title("Aerosur Futbol 5"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.884708,-56.187215)).title("Aguada Futbol 5"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.859656,-56.189328)).title("Albatros"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.874005,-56.200594)).title("Bella Vista"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.854418,-56.224229)).title("Belvedere"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(-34.824903,-56.224283)).title("Boomerang"));*/

    }
}
