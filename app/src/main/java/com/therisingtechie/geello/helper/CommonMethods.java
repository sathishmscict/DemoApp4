package com.therisingtechie.geello.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.therisingtechie.geello.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * Created by chiranjeevi mateti on 03-Oct-17.
 */

public class CommonMethods {

    public static final String WEBSITE="http://api-geello.shreecode.com/";

    public static final Integer MY_SOCKET_TIMEOUT_MS=60000;




 /*   Registration Related Constatnts*/

    public static final int LOGIN_TYPE_FACEBOOK = 1;
    public static final int LOGIN_TYPE_GOOGLE = 2;
    public static final int LOGIN_TYPE_DIRECT = 3;


    /*Food Type Related Constatnts*/

    public static final int RESTRO_TYPE_VEG = 1;
    public static final int RESTRO_TYPE_NON_VEG = 2;
    public static final int RESTRO_TYPE_JAIN = 3;

/*Food Taste Type Related Constants*/
public static final int RESTRO_PRODUCT_TASTE_SPICY = 1;
    public static final int RESTRO_PRODUCT_TASTE_MILD = 2;
    public static final int RESTRO_PRODUCT_TASTE_MEDIUM = 3;
    public static final int RESTRO_PRODUCT_TASTE_SWEET=4;


    /*Is Active Field Any Object Related Constants*/
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;

    /*Role Filed Related Keys*/
    public static final int DELIVERY_BOY = 1;
    public static final int CUSTOMER = 2;


    /*Error code related keys*/
    public static final int ERROR_CODE_INVALID_CREDENTIALS = 1002;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND_HTTP_EXCEPTION=404;
    public static final int ERROR_CODE_VALIDATION_FAILED = 422;
    public static final int TOKEN_EXPIRED = 466;
    public static final int TOKEN_REQUIRED = 477;


    public static  void showError(Context context , String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }


    public static String setCurrentDate(int year, int month, int day) {

        String mm="";
        ++month;
        if(month <= 9)
        {
            mm ="0"+String.valueOf(month);

        }
        else
        {
            mm =String.valueOf(month);
        }

        String  str_day = "";
        if(day <= 9 )
        {


            str_day = "0"+String.valueOf(day);
        }
        else
        {
            str_day = String.valueOf(day);
        }

        return str_day+"-"+mm+"-"+year;

    }


    public static String setCurrentTime(int hour, int minute)
    {

        String MERIDIAN="";
        if(hour > 12)
        {
            hour -=12;
            MERIDIAN = "PM";

        }
        else if(hour == 0)
        {
            hour = 12;

            MERIDIAN="AM";

        }
        else if(hour == 12)
        {
            MERIDIAN = "PM";

        }
        else
        {
            MERIDIAN="AM";

        }


        String min;
        if(minute <=9)
        {

            min  = "0"+String.valueOf(minute);
        }
        else
        {
            min  =String.valueOf(minute);

        }

        return hour+":"+min+" "+MERIDIAN;

    }

    public static final String convertEncodedString(String str) {
        String enoded_string = null;
        try {
            enoded_string = URLEncoder.encode(str, "utf-8").replace(".", "%2E");
            enoded_string = URLEncoder.encode(str, "utf-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enoded_string;
    }

    public static final String convertToJsonDateFormat(String cur_date) {

        Log.d("Passed Date : ", cur_date);
        SimpleDateFormat dateFormat = null;
        Date date = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault());

//String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = format.parse(cur_date);
            System.out.println(date);
        } catch (Exception e) {
            Log.d("Convert DataFormat :: ", e.getMessage());
        }


        //Date date = new Date();

        return dateFormat.format(date);


    }


    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    public static final boolean checkEmail(String email) {
        System.out.println("Email Validation:==>" + email);
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static void hideDialog(SpotsDialog spotsDialog) {
        try {
            if (spotsDialog.isShowing()) {
                spotsDialog.hide();
                spotsDialog.dismiss();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showDialog(SpotsDialog spotsDialog) {
        try {
            if (!spotsDialog.isShowing()) {
                spotsDialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("Encoded String : ", encodedImage);
        return encodedImage;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        Log.d("Decoded String   : ", "" + BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /**
     * Converting dp to pixel
     */
    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void onFailure(Context context, String TAG, Throwable t) {
        //  Toast.makeText(context, "Unable to submit post to API.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Unable to submit post to API. Message  = " + t.getMessage());
        Log.d(TAG, "Unable to submit post to API. LocalizedMessage = " + t.getLocalizedMessage());
        Log.d(TAG, "Unable to submit post to API. Cause = " + t.getCause());
        Log.d(TAG, "Unable to submit post to API. StackTrace = " + t.getStackTrace());

        if (t.getMessage().equals("timeout")) {


            Toast.makeText(context, "Cannot process with the operation.No network connection! Please check your internet connection.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Sorry , try again...", Toast.LENGTH_SHORT).show();
        }


    }


    //Get Current date in dd-MM-yyyy format
    public static final String getDateCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void showServerError(Context context, Integer errorCode) {


        Toast.makeText(context, context.getString(R.string.server_error) + " " + errorCode, Toast.LENGTH_SHORT).show();

    }

    public static void showErrorMessageWhenStatusNot200(Context context, int statuscode) {

        Toast.makeText(context, context.getString(R.string.str_error_servercode_not_200) + "\n Error code :" + statuscode, Toast.LENGTH_SHORT).show();

    }

    public static void showAlertDialog(Context context,String title,String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });

        builder.show();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


/*    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


}
