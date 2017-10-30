package com.therisingtechie.geello.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.crashlytics.android.Crashlytics;
import com.facebook.BuildConfig;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.firebase.client.Firebase;
import com.therisingtechie.geello.injection.component.ApplicationComponent;
import com.therisingtechie.geello.injection.module.ApplicationModule;
import com.therisingtechie.geello.injection.component.DaggerApplicationComponent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by chiranjeevi mateti on 30-Sep-17.
 */

public class MyApplication  extends Application {
    private MyApplication applicationComponent;

    public static final String TAG = MyApplication.class
            .getSimpleName();

    ApplicationComponent mApplicationComponent;



    private static MyApplication mInstance;
    public Context context ;


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);



    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.therisingtechie.geello",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.print("Error :"+e.getMessage());

        } catch (NoSuchAlgorithmException e) {


            System.out.print("Error :"+e.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;



        AndroidNetworking.initialize(getApplicationContext());
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

        context = getApplicationContext();
        FacebookSdk.sdkInitialize(this);

        printHashKey();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }




        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //Initializing firebase
        try {
            Firebase.setAndroidContext(getApplicationContext());


            Fabric.with(this, new Crashlytics());

            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)
                    .build();
            Fabric.with(fabric);


            Fabric.with(getApplicationContext(), new Crashlytics());


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public MyApplication getApplicationComponent() {
        return this.applicationComponent;
    }





}
