package com.therisingtechie.geello;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.therisingtechie.geello.session.SessionManager;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;

public class DashBoardActivity extends AppCompatActivity {

    private Context context=this;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetails= new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sessionManager  = new SessionManager(context);
        userDetails = sessionManager.getSessionDetails();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intet = new Intent(context , SocialRegistrationActivity.class);
                //intet =  new Intent(context , SplashScreenActivity.class);
                startActivity(intet);
                finish();


            }
        });

        if(userDetails.get(SessionManager.KEY_USER_ID).equals("0"))
        {
            Intent intet = new Intent(context , SplashScreenActivity.class);
            //intet =  new Intent(context , LoginActivity.class);
            startActivity(intet);
            finish();

        }


    }

}
