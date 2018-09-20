package android.bg.ro.boardgame;

import android.bg.ro.boardgame.services.GoogleMapsService;
import android.bg.ro.boardgame.services.TaskGoogleMaps;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.bg.ro.boardgame.services.GoogleMapsService.getLocationFromString;

public class SelectAddressMapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskGoogleMaps{

    private GoogleMap mMap;
    private TaskGoogleMaps taskGoogleMaps;
    private String location;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button searchButton = (Button) findViewById(R.id.buttonSearch);
        final EditText address = (EditText) findViewById(R.id.text);
        taskGoogleMaps = this;
        location = "";

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = address.getText().toString();
                List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
                params.add(new Pair<>("location",location));
                new GoogleMapsService(SelectAddressMapsActivity.this,taskGoogleMaps,params).execute("");
            }
        });

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!location.equals("")){
                    onBackPressed();
                }
                else {
                    Toast.makeText(SelectAddressMapsActivity.this, "Please select a valid address.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Bucharest and move the camera
        LatLng bucharest = new LatLng(44.426767,26.102538);
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( bucharest, 15.0f) );
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("location",location);
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        intent.putExtras(bundle);
        setResult(50, intent);
        super.onBackPressed();
    }

    @Override
    public void getLocation(LatLng latLng) {
        if(latLng == null){
            Toast.makeText(SelectAddressMapsActivity.this, "No result found. Try to be more specific.",
                    Toast.LENGTH_LONG).show();
            location = "";
        }
        else {
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            latitude = latLng.latitude ;
            longitude = latLng.longitude;
        }
    }

}
