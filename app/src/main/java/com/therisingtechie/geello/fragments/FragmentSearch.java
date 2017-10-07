package com.therisingtechie.geello.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.therisingtechie.geello.R;
import com.therisingtechie.geello.adapter.CategoryAdapterRecyclerView;
import com.therisingtechie.geello.adapter.RestaurantsAdapterRecyclerView;
import com.therisingtechie.geello.api.ApiClient;
import com.therisingtechie.geello.api.ApiInterface;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.CategoryDataResponse;
import com.therisingtechie.geello.request.RestaurantDataRequest;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSearch extends android.support.v4.app.Fragment {


    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;



    @BindView(R.id.tvSortRestaurants)
    TextView tvSortRestaurants;

    private Context context = getActivity();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private String TAG = FragmentSearch.class.getSimpleName();


    int OFFSET_VALUE = 50;
    private int RESTAURANT_PAGE_COUNTER = 1;
    private SpotsDialog spotsDialog;
    private CategoryAdapterRecyclerView adapter;

    public FragmentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_home, container,false);


        View rootView = inflater.inflate(R.layout.fragment_search, null);
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


        RecyclerView.LayoutManager layoutManager3 = new GridLayoutManager(getActivity(), 2);
        rvCategories.setLayoutManager(layoutManager3);




        rvCategories.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(getActivity(), rvCategories, new CommonMethods.ClickListener() {
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


        rvCategories.addOnItemTouchListener(new CommonMethods.RecyclerTouchListener(getActivity(), rvCategories, new CommonMethods.ClickListener() {
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

    private void getAllRestaurantDetailsFromServer() {


        ApiInterface apicInterface = ApiClient.getClient().create(ApiInterface.class);

        RestaurantDataRequest restaurantDataRequest = new RestaurantDataRequest();
        restaurantDataRequest.start = RESTAURANT_PAGE_COUNTER;

        restaurantDataRequest.count = OFFSET_VALUE;
        restaurantDataRequest.search = "";


        Log.d(TAG, "URL api/categories/get : " + CommonMethods.WEBSITE + "api/categories/get?body=" + restaurantDataRequest.toString());

        apicInterface.getAllCategoriesFromServer(userDetails.get(SessionManager.KEY_TOKEN), restaurantDataRequest).enqueue(new Callback<CategoryDataResponse>() {


            @Override
            public void onResponse(Call<CategoryDataResponse> call, Response<CategoryDataResponse> response) {

                Log.d(TAG, "GetRestaurantsData Response Code : " + response.code());

                if (response.code() == 200) {

                    String str_error = response.body().getMessage();
                    boolean error_status = response.body().getFlag();


                    if (error_status == true) {


                        List<CategoryDataResponse.Datum> Datum = response.body().getData();


                        try {
                            adapter = new CategoryAdapterRecyclerView(getActivity(), Datum);
                            rvCategories.setAdapter(adapter);
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
            public void onFailure(Call<CategoryDataResponse> call, Throwable t) {

                CommonMethods.onFailure(getActivity(), TAG, t);
                CommonMethods.hideDialog(spotsDialog);

            }
        });
    }

    private void getPopularRestarantsDetailsFromServer() {

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