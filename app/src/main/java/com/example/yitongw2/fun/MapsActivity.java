package com.example.yitongw2.fun;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PlaceSelectionListener,
        GoogleMap.InfoWindowAdapter, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener
{

    private GoogleMap mMap;
    private Marker marker = null;
    private Place place = null;
    public static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the PlaceAutocompleteFragment and set up onSelectedListener and onError.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView mBtmView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBtmView.setOnNavigationItemSelectedListener(this);
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

        // Add a marker in Sydney and move the camera
        LatLng uci = new LatLng(33.6404952, -117.84429619999);
        // set to custom info window (specified in info_window.xml)
        mMap.setInfoWindowAdapter(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uci, 14.0f));
    }


    /******************************* start PlaceSelectionListener methods ************************/
    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        // Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName() + place.getLatLng());
        LatLng place_selected = place.getLatLng();

        if (marker != null)
            marker.remove();

        this.place = place;
        marker = mMap.addMarker(new MarkerOptions().position(place_selected).title(place.getName().toString()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place_selected, 14.0f));
    }

    @Override
    public void onError(Status status) {
        // Handle the error.
        Log.i(TAG, "An error occurred: " + status);
    }

    /******************************* end PlaceSelectionListener methods ************************/


    /************************ start GoogleMap.InfoWindowAdapter methods ************************/
    @Override
    public View getInfoWindow(Marker m) {
        View v = getLayoutInflater().inflate(R.layout.info_window, null);

        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(place.getName());

        TextView address = (TextView) v.findViewById(R.id.address);
        address.setText(place.getAddress());

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /************************ end GoogleMap.InfoWindowAdapter methods ************************/


    /************************ start GoogleMap.OnMarkerClickListener methods ***********************/
    @Override
    public boolean onMarkerClick (final Marker marker)
    {
        if (place != null)
            marker.showInfoWindow();
        return true;
    }
    /************************ end GoogleMap.OnMarkerClickListener methods ***********************/


    /************************ start GoogleMap.OnInfoWindowClickListener methods *******************/
    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
    }
    /************************ end GoogleMap.OnInfoWindowClickListener methods *******************/


    /*************** start BottomNavigationView.OnNavigationItemSelectedListener methods **********/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_explore:
                // replace ExploreActivity with your Activity class
                // below are transition: once user clicks on explore tab, it will kick off a new
                // Intent that will start ExploreActivity
                //
                Intent explore = new Intent(this, ExploreActivity.class);
                this.startActivity(explore);
                break;
            case R.id.menu_msg:
                // replace MsgActivity with your Activity class
                // below are transition: once user clicks on explore tab, it will kick off a new
                // Intent that will start MsgActivity
                //
                // Intent msg = new Intent(MapsActivity.this, MsgActivity.class);
                //startActivity(explore);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    /*************** end BottomNavigationView.OnNavigationItemSelectedListener methods ************/
}