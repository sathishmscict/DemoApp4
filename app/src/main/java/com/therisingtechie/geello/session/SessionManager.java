package com.therisingtechie.geello.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.therisingtechie.geello.GelloLoginActivity;

import java.util.HashMap;

/**
 * Created by chiranjeevi mateti on 07-Sep-17.
 */

public class SessionManager {


    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "GEELLO";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Check For Activation
    public static final String KEY_CODE = "code", KEY_SMSURL = "smsurl",
            KEY_RECEIVECODE = "reccode";


    public static final String KEY_USER_AVATAR_URL = "UserAvatarURL", KEY_USER_EMAIL = "UserEmail";


    public static final String KEY_FIRST_NAME ="FirstNAme", KEY_LAST_NAME ="LastName", KEY_PROFILE_PIC_URL="ProfilePicURL",KEY_TOKEN="Token",KEY_IS_NEWUSER="IsNewUser";


    public static final String KEY_ENODEDED_STRING = "Encoded_string";
    public static final String KEY_USER_ID = "UserId", KEY_USER_VERIFICATION_STATUS = "VerificationStatus", KEY_USER_GENDER = "Gender", KEY_USER_DOB = "DOB", KEY_USER_REFERAL_CODE = "ReferralCode", KEY_USER_DEVICE_TYPE = "DeviceType", KEY_USER_IS_FIRST_BILL = "IsFirstBill", KEY_USER_IS_ACTIVE = "IsActive", KEY_USER_IS_REFERRED = "IsReferred", KEY_USER_MOBILE = "UserMobile";

    public static final String KEY_CATEGORYID = "categoryId", KEY_CATEGORY_NAME = "categoryName";
    public static final String KEY_VENDORID = "VendorId", KEY_BRANCHIID = "BranchId";
    public static final String KEY_OFFERID = "OfferId", KEY_OFFER_TITLE = "offerTitle";

    public static final String KEY_REFERALID = "referalId";

    public static final String KEY_LATTITUDE = "Lattitude", KEY_LONGTITUDE = "Longtitude", KEY_BRANCHNAME = "CompanyName", KEY_VENDOR_ADDRESS = "companyAddress";

    public static final String KEY_DISTANCE_INTERVAL_IN_KM = "DistanceInKm";
    public static final  String KEY_LOGIN_TYPE="LoginType",KEY_PROVIDER_ID = "PROVIDER_ID";



    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


    public void setGPSLocations(String lattitude, String longtitude, String companyname, String companyAddress) {


        editor.putString(KEY_LATTITUDE, lattitude);
        editor.putString(KEY_LONGTITUDE, longtitude);
        editor.putString(KEY_BRANCHNAME, companyname);
        editor.putString(KEY_VENDOR_ADDRESS, companyAddress);


        editor.commit();
    }


    public void CheckSMSVerificationActivity(String reccode, String actstatus) {

        editor.putString(KEY_RECEIVECODE, reccode);
        editor.putString(KEY_USER_VERIFICATION_STATUS, actstatus);
        editor.commit();

    }

    public void setDistanceInterval(String distanceKM) {
        editor.putString(KEY_DISTANCE_INTERVAL_IN_KM, distanceKM);
        editor.commit();
    }


    public void setUserImageUrl(String imgURL) {

        editor.putString(KEY_USER_AVATAR_URL, imgURL);
        editor.commit();
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getSessionDetails() {


        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_PROVIDER_ID , pref.getString(KEY_PROVIDER_ID , ""));

        user.put(KEY_LOGIN_TYPE , pref.getString(KEY_LOGIN_TYPE,"0"));

        user.put(KEY_FIRST_NAME, pref.getString(KEY_FIRST_NAME, ""));
        user.put(KEY_LAST_NAME, pref.getString(KEY_LAST_NAME, ""));
        user.put(KEY_PROFILE_PIC_URL, pref.getString(KEY_PROFILE_PIC_URL, ""));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, ""));
        user.put(KEY_IS_NEWUSER, pref.getString(KEY_IS_NEWUSER, "0"));


        user.put(KEY_REFERALID, pref.getString(KEY_REFERALID, "0"));


        user.put(KEY_VENDOR_ADDRESS, pref.getString(KEY_VENDOR_ADDRESS, ""));

        user.put(KEY_DISTANCE_INTERVAL_IN_KM, pref.getString(KEY_DISTANCE_INTERVAL_IN_KM, "10"));

        user.put(KEY_OFFER_TITLE, pref.getString(KEY_OFFER_TITLE, ""));

        user.put(KEY_BRANCHNAME, pref.getString(KEY_BRANCHNAME, ""));

        user.put(KEY_LATTITUDE, pref.getString(KEY_LATTITUDE, "0"));

        user.put(KEY_LONGTITUDE, pref.getString(KEY_LONGTITUDE, "0"));


        user.put(KEY_OFFERID, pref.getString(KEY_OFFERID, "0"));


        user.put(KEY_BRANCHIID, pref.getString(KEY_BRANCHIID, "0"));

        user.put(KEY_VENDORID, pref.getString(KEY_VENDORID, "0"));
        user.put(KEY_CATEGORY_NAME, pref.getString(KEY_CATEGORY_NAME, ""));
        user.put(KEY_CATEGORYID, pref.getString(KEY_CATEGORYID, ""));
        user.put(KEY_ENODEDED_STRING, pref.getString(KEY_ENODEDED_STRING, ""));
        // user.put(KEY_ORDERID, pref.getString(KEY_ORDERID, "0"));
        user.put(KEY_RECEIVECODE, pref.getString(KEY_RECEIVECODE, "0"));
        user.put(KEY_CODE, pref.getString(KEY_CODE, "0"));
        user.put(KEY_SMSURL, pref.getString(KEY_SMSURL, ""));

        user.put(KEY_USER_VERIFICATION_STATUS, pref.getString(KEY_USER_VERIFICATION_STATUS, "0"));
        user.put(KEY_USER_AVATAR_URL, pref.getString(KEY_USER_AVATAR_URL, ""));

       // user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, ""));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, "0"));
        user.put(KEY_USER_GENDER, pref.getString(KEY_USER_GENDER, ""));
        user.put(KEY_USER_DOB, pref.getString(KEY_USER_DOB, ""));
        user.put(KEY_USER_REFERAL_CODE, pref.getString(KEY_USER_REFERAL_CODE, ""));
        user.put(KEY_USER_DEVICE_TYPE, pref.getString(KEY_USER_DEVICE_TYPE, ""));
        user.put(KEY_USER_IS_FIRST_BILL, pref.getString(KEY_USER_IS_FIRST_BILL, "0"));
        user.put(KEY_USER_IS_ACTIVE, pref.getString(KEY_USER_IS_ACTIVE, "1"));
        user.put(KEY_USER_IS_REFERRED, pref.getString(KEY_USER_IS_REFERRED, "0"));

        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, ""));


        return user;
    }

    public void setEncodedImage(String encodeo_image) {


        editor.putString(KEY_ENODEDED_STRING, encodeo_image);

        editor.commit();
    }


    public void createUserSendSmsUrl(String code) {

        editor.putString(KEY_CODE, code);
        //editor.putString(KEY_SMSURL, websiteurl);// http://radiant.dnsitexperts.com/JSON_Data.aspx?type=otp&mobile=9825681802&code=7692
        editor.commit();

    }



    public void setGuestUserDetails()
    {
        editor.putString(KEY_USER_ID ,"-1");
        editor.commit();
    }

    public void setUserDetails(String firstName, String lastName, String email, String mobile, String image, boolean isActive, String smsCode, String token, String id, boolean isNewUser) {


        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_MOBILE, mobile);
        editor.putString(KEY_PROFILE_PIC_URL, image);
        if(isActive)
        {
            editor.putString(KEY_USER_IS_ACTIVE, "1");

        }
        else
        {
            editor.putString(KEY_USER_IS_ACTIVE, "0");

        }
        editor.putString(KEY_CODE, smsCode);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, id);

        if(isNewUser)
        {
            editor.putString(KEY_IS_NEWUSER, "1");
        }
        else
        {
            editor.putString(KEY_IS_NEWUSER, "0");
        }



        editor.commit();

    }


  /*  public void setUserDetails(String name, String email, String userId, String verificationStatus, String gender, String dob, String useravatar, String referalcode, String devicetype, String isActive, String isFirstBill, String isreferred, String usermobile) {


        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_VERIFICATION_STATUS, verificationStatus);
        editor.putString(KEY_USER_GENDER, gender);
        editor.putString(KEY_USER_DOB, dob);
        editor.putString(KEY_USER_AVATAR_URL, useravatar);
        editor.putString(KEY_USER_REFERAL_CODE, referalcode);
        editor.putString(KEY_USER_DEVICE_TYPE, devicetype);
        editor.putString(KEY_USER_IS_FIRST_BILL, isFirstBill);
        editor.putString(KEY_USER_IS_ACTIVE, isActive);
        editor.putString(KEY_USER_IS_REFERRED, isreferred);
        editor.putString(KEY_USER_MOBILE, usermobile);

        editor.commit();


    }*/


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, GelloLoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void setCategoryDetails(String categoryId, String categoryName) {


        editor.putString(KEY_CATEGORYID, categoryId);
        editor.putString(KEY_CATEGORY_NAME, categoryName);

        editor.commit();
    }

    public void setVendorDetails(String branchId, String vendorId) {


        editor.putString(KEY_VENDORID, vendorId);
        editor.putString(KEY_BRANCHIID, branchId);


        editor.commit();
    }

    public void setOfferDetails(String offerid, String offerTitle) {
        editor.putString(KEY_OFFERID, offerid);
        editor.putString(KEY_OFFER_TITLE, offerTitle);

        editor.commit();
    }


    public void setReferalID(String referalid) {

        editor.putString(KEY_REFERALID, referalid);
        editor.commit();
    }

    public void setLoginType(int loginTypeDirect) {

        editor.putString(KEY_LOGIN_TYPE , String.valueOf(loginTypeDirect));
        editor.commit();
    }

    public void setSocialLoginProviderIdDetails(String provider_userid) {
        editor.putString(KEY_PROVIDER_ID , provider_userid);
        editor.commit();
    }
}
