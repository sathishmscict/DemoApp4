package com.therisingtechie.geello;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.therisingtechie.geello.adapter.RestaurantsAdapterRecyclerView;
import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.fragments.FragmentHome;
import com.therisingtechie.geello.fragments.FragmentOrders;
import com.therisingtechie.geello.fragments.FragmentProfile;
import com.therisingtechie.geello.fragments.FragmentSearch;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.helper.GPSTracker;
import com.therisingtechie.geello.helper.NetConnectivity;
import com.therisingtechie.geello.model.RestaurantsDataResponse;
import com.therisingtechie.geello.request.RestaurantDataRequest;
import com.therisingtechie.geello.session.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewDashboardActivity extends AppCompatActivity {

    @BindView(R.id.llHome)
    LinearLayout llHome;

    @BindView(R.id.viewHome)
    View viewHome;

    @BindView(R.id.llSearch)
    LinearLayout llSearch;

    @BindView(R.id.viewSearch)
    View viewSearch;

    @BindView(R.id.llOrders)
    LinearLayout llOrders;

    @BindView(R.id.viewOrders)
    View viewOrders;

    @BindView(R.id.llProfile)
    LinearLayout llProfile;

    @BindView(R.id.viewProfile)
    View viewProfile;



    @BindView(R.id.llCustomMenu)
    LinearLayout llCustomMenu;



    @BindView(R.id.rvRestaurants)
    RecyclerView rvRestaurants;

    @BindView(R.id.rvPopularRestaurants)
    RecyclerView rvPopularRestaurants;

    @BindView(R.id.tvSortRestaurants)
    TextView tvSortRestaurants;

    @BindView(R.id.llHomeContainer)
    LinearLayout  llHomeContainer;

    @BindView(R.id.container_body)
    FrameLayout container_body;

    private String TAB_HOME = "Geello";
    private String TAB_SEARCH = "Search Category";

    private String TAB_ORDERS = "Orders";
    private String TAB_PROFILE = "Profile";
    private Context context = this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails = new HashMap<String, String>();

    private String TAG = NewDashboardActivity.class.getSimpleName();
    private TextView tvTitle;
    private Geocoder geocoder;
    private String TITLE;

    int OFFSET_VALUE = 50;
    private int RESTAURANT_PAGE_COUNTER = 1;
    private SpotsDialog spotsDialog;
    private RestaurantsAdapterRecyclerView adapter;
    private RestaurantsAdapterRecyclerView adapter_popular;
    private List<RestaurantsDataResponse.Datum> list_PopularRestaurants = new ArrayList<RestaurantsDataResponse.Datum>();

    private List<RestaurantsDataResponse.Datum> list_AllRestaurants = new ArrayList<RestaurantsDataResponse.Datum>();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) toolbar.findViewById(R.id.tvTitle);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        spotsDialog = new SpotsDialog(context);
        spotsDialog.setCancelable(false);

        sessionManager = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();


        LinearLayoutManager layoutManager_popular_categories = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvPopularRestaurants.setLayoutManager(layoutManager_popular_categories);


        LinearLayoutManager layoutManager_main_categories = new LinearLayoutManager(context);
        rvRestaurants.setLayoutManager(layoutManager_main_categories);



        geocoder = new Geocoder(this, Locale.getDefault());


        tvSortRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(llCustomMenu) ;
            }
        });

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCustomLayout(TAB_HOME);
                FillHomeData();
            }
        });
       // llHome.performClick();
        llOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCustomLayout(TAB_ORDERS);
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCustomLayout(TAB_PROFILE);
            }
        });

        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setCustomLayout(TAB_SEARCH);
            }
        });


        if (userDetails.get(SessionManager.KEY_USER_ID).equals("0")) {
            Intent intet = new Intent(context, SplashScreenActivity.class);
            //intet =  new Intent(context , LoginActivity.class);
            startActivity(intet);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            return;
        } else {
           // setupCurrentLocation();

        }




        rvPopularRestaurants.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvPopularRestaurants, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

				/*SetCategoryDetails("category", listMainCategories.get(position).getCategoryId(), listMainCategories.get(position).getCategoryName());

				Intent intent = new Intent(context, SubCategories.class);
				startActivity(intent);
				finish();*/
                Toast.makeText(context, "Title", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        rvRestaurants.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(context, rvRestaurants, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

				/*SetCategoryDetails("category", listMainCategories.get(position).getCategoryId(), listMainCategories.get(position).getCategoryName());

				Intent intent = new Intent(context, SubCategories.class);
				startActivity(intent);
				finish();*/
                Toast.makeText(context, "Title", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        FillHomeData();




    }

    private void FillHomeData() {
        getPopularRestarantsDetailsFromServer();
        getAllRestaurantDetailsFromServer();

        llHomeContainer.setVisibility(View.VISIBLE);
        container_body.setVisibility(View.GONE);
        setupCurrentLocation();
    }
    //onCreate Completed



    private void getAllRestaurantDetailsFromServer()
    {


        CommonMethods.showDialog(spotsDialog);

        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        RestaurantDataRequest restaurantDataRequest = new RestaurantDataRequest();
        restaurantDataRequest.start = RESTAURANT_PAGE_COUNTER;

        restaurantDataRequest.count = OFFSET_VALUE;
        restaurantDataRequest.search = "";


        Log.d(TAG, "URL api/restros/getRestros : " + CommonMethods.WEBSITE + "api/restros/getRestros?token="+ userDetails.get(SessionManager.KEY_TOKEN) +"body=" + restaurantDataRequest.toString());

        apicInterface.getAllRestaurantDetailsFromServer(userDetails.get(SessionManager.KEY_TOKEN), restaurantDataRequest).enqueue(new Callback<RestaurantsDataResponse>() {


            @Override
            public void onResponse(Call<RestaurantsDataResponse> call, Response<RestaurantsDataResponse> response) {

                Log.d(TAG, "GetAllRestaurantsData Response Code : " + response.code());

                if (response.code() == 200) {

                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();


                    if (error_status == true) {



                        list_AllRestaurants= response.body().getData();


                        try {
                            adapter = new RestaurantsAdapterRecyclerView(context, list_AllRestaurants,"nprmal");
                            rvRestaurants.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CommonMethods.hideDialog(spotsDialog);


                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(context, response.code());
                }
                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<RestaurantsDataResponse> call, Throwable t) {

                CommonMethods.onFailure(context, TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });
    }

    private void getPopularRestarantsDetailsFromServer()
    {

        CommonMethods.showDialog(spotsDialog);
        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        RestaurantDataRequest restaurantDataRequest = new RestaurantDataRequest();
        restaurantDataRequest.start = RESTAURANT_PAGE_COUNTER;

        restaurantDataRequest.count = OFFSET_VALUE;
        restaurantDataRequest.search = "";


        Log.d(TAG, "URL api/restros/getPopularRestros : " + CommonMethods.WEBSITE + "api/restros/getPopularRestros?token="+ userDetails.get(SessionManager.KEY_TOKEN) +"&body=" + restaurantDataRequest.toString());

        apicInterface.getPopularRestaurantDetailsFromServer(userDetails.get(SessionManager.KEY_TOKEN), restaurantDataRequest).enqueue(new Callback<RestaurantsDataResponse>() {


            @Override
            public void onResponse(Call<RestaurantsDataResponse> call, Response<RestaurantsDataResponse> response) {

                Log.d(TAG, "GetPopularRestaurantsData Response Code : " + response.code());

                if (response.code() == 200) {

                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();


                    if (error_status == true) {


                        List<RestaurantsDataResponse.Datum> sads = response.body().getData();

                        list_PopularRestaurants = response.body().getData();


                        try {
                            adapter_popular = new RestaurantsAdapterRecyclerView(context, list_PopularRestaurants,"popular");
                            rvPopularRestaurants.setAdapter(adapter_popular);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CommonMethods.hideDialog(spotsDialog);


                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(context, response.code());
                }
                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<RestaurantsDataResponse> call, Throwable t) {

                CommonMethods.onFailure(context, TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });

    }

    private void setupCurrentLocation() {
        Dexter.withActivity(NewDashboardActivity.this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override

                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        // startService(new Intent(getBaseContext(), MyLocationService.class));
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            AlertForLocationServices();
                        } else {
                            GPSTracker gps = null;
                            //  LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            // startService(new Intent(getBaseContext(), MyLocationService.class));
                            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

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
                                        tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_location, 0);

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
                            } else {
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
                        /* ... */

                        token.continuePermissionRequest();

                    }


                }).check();
    }


    private void AlertForLocationServices() {


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

    private void setCustomLayout(String type) {

        Fragment fragment = null;


        if (type.equals(TAB_HOME)) {
            fragment = new FragmentHome();
            TITLE = TAB_HOME;
            viewHome.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            viewOrders.setBackgroundColor(getResources().getColor(R.color.white));
            viewSearch.setBackgroundColor(getResources().getColor(R.color.white));
            viewProfile.setBackgroundColor(getResources().getColor(R.color.white));
           // setupFragment(fragment, TITLE);

        } else if (type.equals(TAB_SEARCH)) {
            TITLE = TAB_SEARCH;
            fragment = new FragmentSearch();
            viewHome.setBackgroundColor(getResources().getColor(R.color.white));
            viewOrders.setBackgroundColor(getResources().getColor(R.color.white));
            viewSearch.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            viewProfile.setBackgroundColor(getResources().getColor(R.color.white));
            setupFragment(fragment, TITLE);

        } else if (type.equals(TAB_ORDERS)) {
            TITLE = TAB_ORDERS;
            fragment = new FragmentOrders();
            viewHome.setBackgroundColor(getResources().getColor(R.color.white));
            viewOrders.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            viewSearch.setBackgroundColor(getResources().getColor(R.color.white));
            viewProfile.setBackgroundColor(getResources().getColor(R.color.white));
            setupFragment(fragment, TITLE);

        } else if (type.equals(TAB_PROFILE)) {
            TITLE = TAB_PROFILE;
            fragment = new FragmentProfile();
            viewHome.setBackgroundColor(getResources().getColor(R.color.white));
            viewOrders.setBackgroundColor(getResources().getColor(R.color.white));
            viewSearch.setBackgroundColor(getResources().getColor(R.color.white));
            viewProfile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        setupFragment(fragment, TITLE);
        }

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    private void setupFragment(Fragment fragment, String title) {


        llHomeContainer.setVisibility(View.GONE);
        container_body.setVisibility(View.VISIBLE);


        if (title.toString().toLowerCase().equals("geello")) {

            // tvTitle.setText(title);
            setupCurrentLocation();
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_location, 0);
            //setupHomeFragment();

            // SortFlag = true;


            hideSoftKeyboard();
        } else {
            hideSoftKeyboard();
            //SortFlag = false;
           // llHome.setVisibility(View.GONE);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvTitle.setText(title);

        }
        if (fragment != null) {


            try {


                //getSupportActionBar().setTitle(title);


                // setTitle(title);


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


        }

    }


    public void showPopup(View v)
    {


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.layout_custom_filter, null);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //TODO do sth here on dismiss
            }
        });


        final LinearLayout llSort = (LinearLayout) popupView.findViewById(R.id.llSort);

        final LinearLayout llPrice = (LinearLayout) popupView.findViewById(R.id.llPrice);

        final LinearLayout llDietary = (LinearLayout) popupView.findViewById(R.id.llDietary);
        llPrice.setVisibility(View.GONE);
        llDietary.setVisibility(View.GONE);


        final Button btnDone = (Button) popupView.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        final Button btnSort = (Button) popupView.findViewById(R.id.btnSort);
        final Button btnPrice = (Button) popupView.findViewById(R.id.btnPrice);
        final Button btnDietary = (Button) popupView.findViewById(R.id.btnDietary);

        Button btnRecommended = (Button) popupView.findViewById(R.id.btnRecommended);
        Button btnMostPopular = (Button) popupView.findViewById(R.id.btnMostPopular);
        final Button btnDeliveryTime = (Button) popupView.findViewById(R.id.btnDeliveryTime);


        btnSort.setTextColor(getResources().getColor(R.color.colorPrimary));
        btnPrice.setTextColor(getResources().getColor(R.color.text_color));
        btnDietary.setTextColor(getResources().getColor(R.color.text_color));

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDietary.setVisibility(View.GONE);
                llPrice.setVisibility(View.GONE);
                llSort.setVisibility(View.VISIBLE);


                try {
                    //LinearLayout llmain = (LinearLayout) dialog.findViewById(R.id.llSort);
                    llSort.setVisibility(LinearLayout.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    animation.setDuration(500);
                    llSort.setAnimation(animation);
                    llSort.animate();
                    animation.start();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }

                //  btnSort.setTypeface(btnSort.getTypeface(), Typeface.BOLD);
                // btnPrice.setTypeface(btnPrice.getTypeface(), Typeface.NORMAL);
                //btnDietary.setTypeface(btnDietary.getTypeface(), Typeface.NORMAL);

                btnSort.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnPrice.setTextColor(getResources().getColor(R.color.text_color));
                btnDietary.setTextColor(getResources().getColor(R.color.text_color));


            }
        });


        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDietary.setVisibility(View.GONE);
                llPrice.setVisibility(View.VISIBLE);
                llSort.setVisibility(View.GONE);

                try {
                    //LinearLayout llmain = (LinearLayout) dialog.findViewById(R.id.llSort);
                    llPrice.setVisibility(LinearLayout.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    animation.setDuration(500);
                    llPrice.setAnimation(animation);
                    llPrice.animate();
                    animation.start();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }


                // btnSort.setTypeface(btnSort.getTypeface(), Typeface.NORMAL);
                //btnPrice.setTypeface(btnPrice.getTypeface(), Typeface.BOLD);
                //btnDietary.setTypeface(btnDietary.getTypeface(), Typeface.NORMAL);

                btnSort.setTextColor(getResources().getColor(R.color.text_color));
                btnPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnDietary.setTextColor(getResources().getColor(R.color.text_color));

            }
        });

        btnDietary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDietary.setVisibility(View.VISIBLE);
                llPrice.setVisibility(View.GONE);
                llSort.setVisibility(View.GONE);

                try {
                    //LinearLayout llmain = (LinearLayout) dialog.findViewById(R.id.llSort);
                    llDietary.setVisibility(LinearLayout.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    animation.setDuration(500);
                    llDietary.setAnimation(animation);
                    llDietary.animate();
                    animation.start();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();

                }


                // btnSort.setTypeface(btnSort.getTypeface(), Typeface.NORMAL);
                //btnPrice.setTypeface(btnPrice.getTypeface(), Typeface.NORMAL);
                //btnDietary.setTypeface(btnDietary.getTypeface(), Typeface.BOLD);
                btnSort.setTextColor(getResources().getColor(R.color.text_color));
                btnPrice.setTextColor(getResources().getColor(R.color.text_color));
                btnDietary.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });


        popupWindow.showAsDropDown(v);
    }
    /*Filter Realted Code*/

}
