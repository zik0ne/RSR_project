package androidstudioapp.android.com.rsr;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {



    LocationStateChangeBroadcastReceiver locationStateChangeBroadcastReceiver = new LocationStateChangeBroadcastReceiver();
    InternalLocationChangeReceiver internalLocationChangeReceiver = new InternalLocationChangeReceiver();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final float DEFAULT_ZOOM = 16f;
    private boolean isReceiverRegistered = false;
    private LocationManager locationManager;
    private BroadcastReceiver receiver = null;
    private GoogleMap mMap;
    private Button button1;
    private Button button2;




    class InternalLocationChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //this method will be called on gps disabled
            openGpsWindow();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_maps_tablet);
        }
        else
        {
            setContentView(R.layout.activity_maps);
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        //Reveal Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        //set Toolbar tittle
        this.getSupportActionBar().setTitle("");


        //Move from one activity to another by clicking button Bel RSR nu
        button1 = findViewById(R.id.button_BelRSRnu);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton();
            }
        });

        //Return to page 1 by clicking "< RSRPechhulp" button
        button2 = findViewById(R.id.back_button_RSRPecchulp);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Gets the device's location permissions in order to find user's location
        getLocationPermission();

        //Checks if Wifi is enabled or disabled
        wifiNdata();

        //Checks if gps is enabled or disabled
        GPS();



    }



    protected void onResume() {
        super.onResume();
        updateLocationUI();
        //Checking location State
        CheckLocationState();

        //Checks the registration of the receiver
        if (!isReceiverRegistered) {
            isReceiverRegistered = true;
            registerReceiver(receiver, new IntentFilter("android.net.wifi.STATE_CHANGE"));// IntentFilter to wifi state change is "android.net.wifi.STATE_CHANGE"
            GPS();
            return;
        }

        UnRegisterGpsReceiver();
        updateLocationUI();
    }

    protected void onPause() {
        super.onPause();

        if (isReceiverRegistered) {
            isReceiverRegistered = false;
            unregisterReceiver(receiver);

            UnRegisterGpsReceiver();

            return;
        }

        GPS();
        updateLocationUI();

    }

    //Gets the device's location permissions
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLocationPermissionsGranted = true;
            initMap();
            updateLocationUI();

        } else {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;

        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mLocationPermissionsGranted = true; //user gave location permissions

                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        openGpsWindow();
                        UnRegisterGpsReceiver();
                    }
                    initMap(); //initialize the map
                    updateLocationUI(); //updates the device's location

                }else{
                    //returns to main activity
                    mainActivity();
                    finish();
                }
                GPS();
                UnRegisterGpsReceiver();

            }
        }
        initMap(); //initialize the map
        updateLocationUI(); //updates the device's location

    }

    //Initializing map
    private void initMap(){

        //initializing map

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //map is ready

        mMap = googleMap;


        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

                    != PackageManager.PERMISSION_GRANTED) {

                return;

            }
            //mMap.setMyLocationEnabled(true);
        }
        else{
            updateLocationUI();
        }
    }

    //Getting the device's current location
    private void getDeviceLocation() {

        // getting the device's current location

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if (mLocationPermissionsGranted) {

            final Task location = mFusedLocationProviderClient.getLastLocation();


            location.addOnCompleteListener(new OnCompleteListener() {

                @Override

                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful() && task.getResult() != null) {

                        //onComplete: found location

                        Location currentLocation = (Location) task.getResult();

                        double latitude = currentLocation.getLatitude();

                        double longitude = currentLocation.getLongitude();


                        //Finding user's location
                        LatLng myCoordinates = new LatLng(latitude, longitude);
                        moveCamera(myCoordinates, DEFAULT_ZOOM);


                        //Opens an info window above user to display location
                        InfoWindowAdapter infoWindowAdapter = new InfoWindowAdapter((getApplicationContext()));
                        mMap.setInfoWindowAdapter(infoWindowAdapter);

                        //Adding an icon marker to display the user's location and the info window from above
                        MarkerOptions marker = new MarkerOptions();
                        mMap.addMarker(marker.position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_mini))).showInfoWindow();


                    }
                    else {
                        //unable to get current location
                        updateLocationUI();
                    }

                }

            });

        }else{
            updateLocationUI();
        }

    }

    private void moveCamera(LatLng latLng, float zoom) {

        //moveCamera: moving the camera to: lat: + latLng.latitude +  lng:  + latLng.longitude

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }

    //Updates the user's location
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        try {
            if (mLocationPermissionsGranted){
                mMap.clear();
                getDeviceLocation();
            } else {
                getLocationPermission();
            }
        } catch (SecurityException e) {
        }
    }

    //Checks the state of the gps: enabled/disabled
    public void GPS(){
        try {
            registerReceiver(locationStateChangeBroadcastReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LocationStateChangeBroadcastReceiver.GPS_CHANGE_ACTION);
            registerReceiver(internalLocationChangeReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Checks the state of Wifi : enabled/disabled
    public void wifiNdata(){
        receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
                NetworkInfo data = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
                if (!wifi.isConnected() && !data.isConnected() ){
                    openWifiWindow();
                }
                else{
                    if((wifi.isConnected() || data.isConnected()) || (!wifi.isConnected() && data.isConnected())){
                        updateLocationUI();
                    }
                }
            }
        };

    }

    //Pops up the window in order to make a phone call
    public void startButton() {
        Intent intent = new Intent(this, WindowCall.class);
        startActivity(intent);

    }

    //Pops up a window in order to open GPS
    public void openGpsWindow() {
        Intent intent = new Intent(this, EnableGpsWindow.class);
        startActivity(intent);
    }

    //Pops up a window in order to open WIFI
    public void openWifiWindow() {
        Intent intent = new Intent(this, EnableWifiWindow.class);
        startActivity(intent);
    }

    //Goes to MainActivity
    public void mainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Checks location State
    private void CheckLocationState() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            updateLocationUI();
        }else{
            openGpsWindow();
        }
    }

    //Unregisters gps receiver
    public void UnRegisterGpsReceiver(){
        unregisterReceiver(locationStateChangeBroadcastReceiver);
        unregisterReceiver(internalLocationChangeReceiver);
    }

}


