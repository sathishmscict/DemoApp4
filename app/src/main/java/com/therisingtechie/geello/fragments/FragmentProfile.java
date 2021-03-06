package com.therisingtechie.geello.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.therisingtechie.geello.R;
import com.therisingtechie.geello.adapter.CategoryAdapterRecyclerView;
import com.therisingtechie.geello.session.SessionManager;

import java.util.HashMap;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


public class FragmentProfile extends android.support.v4.app.Fragment {


    private Context context = getActivity();
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails;
    private String TAG = FragmentProfile.class.getSimpleName();


    int OFFSET_VALUE = 50;
    private int RESTAURANT_PAGE_COUNTER = 1;
    private SpotsDialog spotsDialog;
    private Button btnLogout;


    public FragmentProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_home, container,false);


        View rootView = inflater.inflate(R.layout.fragment_profile, null);
 /* For this injection you have to specify the source of the views */
        ButterKnife.bind(this, rootView);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        btnLogout = (Button)rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });
        spotsDialog = new SpotsDialog(getActivity());
        spotsDialog.setCancelable(false);


        sessionManager = new SessionManager(getActivity());
        userDetails = new HashMap<String, String>();
        userDetails = sessionManager.getSessionDetails();


        // Inflate the layout for this fragment
        return rootView;
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