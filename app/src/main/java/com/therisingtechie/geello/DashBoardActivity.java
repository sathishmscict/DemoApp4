package com.therisingtechie.geello;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.therisingtechie.geello.fragments.FragmentHome;
import com.therisingtechie.geello.fragments.FragmentSearch;
import com.therisingtechie.geello.helper.GPSTracker;
import com.therisingtechie.geello.helper.NetConnectivity;
import com.therisingtechie.geello.session.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashBoardActivity extends AppCompatActivity {


    @BindView(R.id.bottomBar)
    BottomBar bottomBar;



    private Context context=this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();
    private String TITLE;
    private TextView tvTitle;
    private Geocoder geocoder;
    private String TAG  = DashBoardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_dash_board);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         tvTitle = (TextView)toolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        geocoder = new Geocoder(this, Locale.getDefault());


        sessionManager  = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();






        if(userDetails.get(SessionManager.KEY_USER_ID).equals("0"))
        {
            Intent intet = new Intent(context , SplashScreenActivity.class);
            //intet =  new Intent(context , LoginActivity.class);
            startActivity(intet);
            finish();

        }


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {


                Fragment fragment = null;

                String title = getString(R.string.app_name);

                if (tabId == R.id.tab_home) {

                    fragment = new FragmentHome();
                    // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                    TITLE = getResources().getString(R.string.app_name);


                } else if (tabId == R.id.tab_search) {
                    fragment = new FragmentSearch();
                    // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                    TITLE = "Search Restaurants";


                } else if (tabId == R.id.tab_orders) {


                        fragment = new FragmentHome();
                        // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        TITLE = "Orders";



                } else if (tabId == R.id.tab_profile) {
                    fragment = new FragmentHome();
                    // Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                    TITLE = "Profile";


                } /*else if (tabId == R.id.tab_logout) {

                    if (userDetails.get(SessionManager.KEY_USERID).equals("0")) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        sessionmanager.logoutUser();
                        finish();
                    }

                }*/



                setupFragment(fragment , TITLE);



            }
        });







        Dexter.withActivity(DashBoardActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // startService(new Intent(getBaseContext(), MyLocationService.class));
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            AlertForLocationServices();
                        }
                        else
                        {
                            GPSTracker gps = null;
                            //  LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            // startService(new Intent(getBaseContext(), MyLocationService.class));
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                            {

                                // create class object
                                 gps = new GPSTracker(context);

                                Location myLocation = gps.getLocation();

                                // check if GPS enabled
                                if (gps.canGetLocation()) {

                                    double latitude = gps.getLatitude();

                                    double longitude = gps.getLongitude();

                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();

                                        tvTitle.setText(addresses.get(0).getSubLocality());

                                    } catch (IOException e) {
                                        tvTitle.setText("");
                                        e.printStackTrace();
                                    }


                                    // \n is for new line
                                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Current Location : " + "Your Location is - \nLat: " + latitude + "\nLong: " + longitude);
                                } else {
                                    // can't get location
                                    // GPS or Network is not enabled
                                    // Ask user to enable GPS/network in settings
                                    gps.showSettingsAlert();
                                }
                            }
                            else
                            {
// can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gps.showSettingsAlert();
                            }

                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        /* ... */
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest
                                                                           permission, PermissionToken token) {
                        /* ... */}
                }).check();




        Fragment fragment = new FragmentHome();
        setupFragment(fragment,getString(R.string.app_name));


    }


    private void AlertForLocationServices()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(false);

        builder.setMessage(getString(R.string.app_name) + " would like to use your current location to customize your experience.");


        /*builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });*/
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                //Toast.makeText(context , "Please enable GPS",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Enable GPS");
                //Log.d(TAG , "Device not supported SIM, Please enable GPS ");

            }
        });

        try {
            AlertDialog alert11 = builder.create();
            alert11.show();

            Button buttonbackground = alert11.getButton(DialogInterface.BUTTON_NEGATIVE);
            //buttonbackground.setBackgroundColor(Color.BLUE);
            buttonbackground.setTextColor(Color.parseColor("#3F51B5"));

            Button buttonbackground1 = alert11.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonbackground1.setTextColor(Color.parseColor("#3F51B5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //buttonbackground1.setBackgroundColor(Color.parseColor("#3F51B5"));


        //builder.show();


    }

    private void setupFragment(Fragment fragment,String title) {
        if (fragment != null)
        {


            try {


                //getSupportActionBar().setTitle(title);
                tvTitle.setText(title);


                setTitle(title);


                if (NetConnectivity.isOnline(context)) {


                    FragmentManager fragmentManager = getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();


                   /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);*/

                } else {
                    // checkInternet();
                    Toast.makeText(context, "" + getString(R.string.no_network2), Toast.LENGTH_SHORT).show();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }



        } else {

            //setTitle(getString(R.string.app_name));
            tvTitle.setText(title);
        }

    }

}
