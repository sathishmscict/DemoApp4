package com.therisingtechie.geello.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mingle.sweetpick.BlurEffect;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.therisingtechie.geello.R;
import com.therisingtechie.geello.adapter.RestaurantsAdapterRecyclerView;
import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.CategoryDataResponse;
import com.therisingtechie.geello.model.RestaurantsDataResponse;
import com.therisingtechie.geello.request.RestaurantDataRequest;
import com.therisingtechie.geello.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends android.support.v4.app.Fragment  {


    @BindView(R.id.rvRestaurants)
    RecyclerView rvRestaurants;

    @BindView(R.id.rvPopularRestaurants)
    RecyclerView rvPopularRestaurants;

    @BindView(R.id.tvSortRestaurants)
    TextView tvSortRestaurants;

    @BindView(R.id.rl)
    RelativeLayout r1;



    private SweetSheet mSweetSheet3;

    private Context context = getActivity();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private String TAG = FragmentHome.class.getSimpleName();


    int OFFSET_VALUE = 50;
    private int RESTAURANT_PAGE_COUNTER = 1;
    private SpotsDialog spotsDialog;
    private RestaurantsAdapterRecyclerView adapter;
    private RestaurantsAdapterRecyclerView adapter_popular;
    private List<RestaurantsDataResponse.Datum> list_PopularRestaurants = new ArrayList<RestaurantsDataResponse.Datum>();

    private List<RestaurantsDataResponse.Datum> list_AllRestaurants = new ArrayList<RestaurantsDataResponse.Datum>();;


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_home, container,false);


        View rootView = inflater.inflate(R.layout.fragment_home, null);
 /* For this injection you have to specify the source of the views */
        ButterKnife.bind(this, rootView);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.setCancelable(false);


        sessionManager = new SessionManager(getActivity());
        userDetails = new HashMap<String, String>();
        userDetails = sessionManager.getSessionDetails();


        LinearLayoutManager layoutManager_popular_categories = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvPopularRestaurants.setLayoutManager(layoutManager_popular_categories);


        LinearLayoutManager layoutManager_main_categories = new LinearLayoutManager(getActivity());
       rvRestaurants.setLayoutManager(layoutManager_main_categories);

       // RecyclerView.LayoutManager layoutManager3 = new GridLayoutManager(getActivity(), 1);
        //rvRestaurants.setLayoutManager(layoutManager3);



        tvSortRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (mSweetSheet.isShow()) {
                    mSweetSheet.dismiss();
                }*/

                mSweetSheet3.toggle();
            }
        });
                setupCustomFilterView();


        rvPopularRestaurants.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(getActivity(), rvPopularRestaurants, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

				/*SetCategoryDetails("category", listMainCategories.get(position).getCategoryId(), listMainCategories.get(position).getCategoryName());

				Intent intent = new Intent(context, SubCategories.class);
				startActivity(intent);
				finish();*/
                Toast.makeText(getActivity(), "Title", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        rvRestaurants.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(getActivity(), rvRestaurants, new CommonMethods.ClickListener() {
            @Override
            public void onClick(View view, int position) {

				/*SetCategoryDetails("category", listMainCategories.get(position).getCategoryId(), listMainCategories.get(position).getCategoryName());

				Intent intent = new Intent(context, SubCategories.class);
				startActivity(intent);
				finish();*/
                Toast.makeText(getActivity(), "Title", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        getPopularRestarantsDetailsFromServer();
        getAllRestaurantDetailsFromServer();


        // Inflate the layout for this fragment
        return rootView;
    }
    //onCreat completed



    /**
     * Set Custom Filter View with Price and Size
     */
    private void setupCustomFilterView()
    {


        mSweetSheet3 = new SweetSheet(r1);
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangLayoutAnimation);
        View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_custom_filter, null, false);
        customDelegate.setCustomView(popupView);
        mSweetSheet3.setDelegate(customDelegate);
        mSweetSheet3.setBackgroundEffect(new BlurEffect(3));


       /* view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet3.dismiss();
            }
        });*/

         /*Filter Related UI and code*/
        // fonts1 =  Typeface.createFromAsset(getAssets(), "fonts/MavenPro-Regular.ttf");


   /*     crdSize = (CardView) view.findViewById(R.id.crdSize);

        recyclerview_size = (RecyclerView) view.findViewById(R.id.recyclerview_size);
        RecyclerView.LayoutManager layoutManager_size = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_size.setLayoutManager(layoutManager_size);
        recyclerview_size.setItemAnimator(new DefaultItemAnimator());
*/

       // LinearLayout llPricing = (LinearLayout) view.findViewById(R.id.llPricing);
        // llPricing.setVisibility(View.GONE);
        //crdSize.setVisibility(View.VISIBLE);

       // ProductSizeDisplayAdapter adapter_productsize = new ProductSizeDisplayAdapter(context);
       // recyclerview_size.setAdapter(adapter_productsize);


      //  Button btnGo = (Button) popupView.findViewById(R.id.btnDone);
       // btnGo.setTypeface(CustomFonts.typefaceCondensed(context));

     //   btnGo.setOnClickListener(new View.OnClickListener() {
         //   @Override
        //    public void onClick(View v) {

         /*       list_Products.clear();
                PAGENO = 0;
                TOTAL_PAGES = 0;
                confMenu();
                adapter_NewProduct.notifyDataSetChanged();


                Is_Price_Range_Enalbe = true;
                getAllProductDetailsFromServer();*/
             //   mSweetSheet3.dismiss();


        //    }
     //   });




        final LinearLayout llSort = (LinearLayout) popupView.findViewById(R.id.llSort);

        final LinearLayout llPrice = (LinearLayout) popupView.findViewById(R.id.llPrice);

        final LinearLayout llDietary = (LinearLayout) popupView.findViewById(R.id.llDietary);
        llPrice.setVisibility(View.GONE);
        llDietary.setVisibility(View.GONE);


        final Button btnDone = (Button) popupView.findViewById(R.id.btnDone);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSweetSheet3.dismiss();

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
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);
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
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);
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
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in);
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




        /*Complete Filter Related UI and code*/


    }

    private void getAllRestaurantDetailsFromServer() {


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
                            adapter = new RestaurantsAdapterRecyclerView(getActivity(), list_AllRestaurants,"normal");
                            rvRestaurants.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CommonMethods.hideDialog(spotsDialog);


                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(getActivity(), response.code());
                }
                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<RestaurantsDataResponse> call, Throwable t) {

                CommonMethods.onFailure(getActivity(), TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });
    }

    private void getPopularRestarantsDetailsFromServer() {

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
                            adapter_popular = new RestaurantsAdapterRecyclerView(getActivity(), list_PopularRestaurants,"popular");
                            rvPopularRestaurants.setAdapter(adapter_popular);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        CommonMethods.hideDialog(spotsDialog);


                    }
                } else {
                    CommonMethods.showErrorMessageWhenStatusNot200(getActivity(), response.code());
                }
                CommonMethods.hideDialog(spotsDialog);


            }

            @Override
            public void onFailure(Call<RestaurantsDataResponse> call, Throwable t) {

                CommonMethods.onFailure(getActivity(), TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // pDialog.dismiss();
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
    }


}