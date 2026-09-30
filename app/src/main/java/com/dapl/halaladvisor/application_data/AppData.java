package com.dapl.halaladvisor.application_data;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kaushik on 26-08-2016.
 */
public class AppData {

    //public static final String BASE_URL = "http://192.168.0.12/resturant/";
    //public static final String BASE_URL = "http://111.93.227.162/resturant/";
    public static final String BASE_URL = "https://www.admin.halaladvisor.com.au/";// Live URL
 //   public static final String BASE_URL = "https://admin.halaladvisor.com.au/";// Live URL
   // public static final String BASE_URL = "https://admin.halaladvisor.com.au/halal_new/";// Dev URL

    public static final String LOGIN_URL = BASE_URL + "registration/signin";
    public static final String DELETE_USER_URL = BASE_URL + "registration/deleteuser";
    public static final String SIGNUP_URL = BASE_URL + "registration/signup";
    public static final String RESTAURANT_CATEGORY_URL = BASE_URL + "resturent/cats";
    public static final String RESTAURANT_LIST_URL = BASE_URL + "resturent/getresturentbycat";
    public static final String OFFER_LIST_URL = BASE_URL + "resturent/getresturentofferslisting";
    public static final String GET_CUISINE_LIST = BASE_URL + "resturent/topcuisines";
    public static final String RESTAURANT_DETAILS_URL = BASE_URL + "resturent/getresturentbyid";
    public static final String RESTAURANT_DETAILS_COUNT = BASE_URL + "resturent/trackAnalytics";
    public static final String GET_ALL_REVIEWS_URL = BASE_URL + "resturent/getallreviewsbyrestid";
    public static final String ADD_EDIT_REVIEWS_URL = BASE_URL + "resturent/manage_review";
    public static final String GET_GALLERY_URL = BASE_URL + "resturent/get_gallery";
    public static final String GALLERY_PHOTO_UPLOAD_URL = BASE_URL + "resturent/add_gallery";
    public static final String ADD_RESTAURANT_URL = BASE_URL + "resturent/suggested_restaurant";

    public static final String GET_SEARCH_PARAM_URL = BASE_URL + "resturent/get_search_params";
    public static final String GET_SEARCH_RESULT_URL = BASE_URL + "resturent/get_search_results";
    public static final String FORGOT_PASSWORD_URL = BASE_URL + "registration/forgotpassword";


    public static final String THIRD_PARTY_REGISTRATION = BASE_URL + "registration/third_party_register";
    public static final String THIRD_PARTY_LOGIN = BASE_URL + "registration/third_party_login";
    public static final String GET_PROFILE_DATA = BASE_URL + "registration/get_user";
    public static final String SEND_FEEDBACK = BASE_URL + "home/sendFeedback";
    public static final String UPDATE_USER_PROFILE = BASE_URL + "registration/edit_user";

    public static final String UPDATE_LOCATION_URL = BASE_URL + "registration/updateLocaton";
    public static final String UPDATE_LOCATION_WITH_STATE_URL = BASE_URL + "halal_api/token.php";
    public static final String SPECIAL_OFFERS_URL = BASE_URL + "resturent/special_offer_restaurent";
    public static final String LOGOUT_URL = BASE_URL + "registration/logOut";
    public static final String SPECIAL_OFFER_DETAILS_URL = BASE_URL + "resturent/special_offer_restaurent_details";
    public static final String REDEEM_OFFER_URL = BASE_URL + "resturent/redeem_now";
    public static final String VOUCHER_LIST_URL = BASE_URL + "resturent/my_voucher_list";




    public static final String LOCATION_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=ORIGIN_LAT,ORIGIN_LON&destinations=DEST_LAT,DEST_LON&key=API_KEY";


    //    variable used for location permission (marshmallow)
    public static final int READ_LOCATION_PERMISSIONS_REQUEST = 99;
    public static final int GALLERY_INTENT_CALLED = 11;
    public static final int CAPTURE_INTENT_CALLED = 13;
    public static final int GALLERY_KITKAT_INTENT_CALLED = 12;

    //    public static boolean GPS_PERMISSION_REQUEST_GRANTED = false;
    public static final int MEDIA_TYPE_IMAGE = 10;
    /*variable used for accessing camera (marshmallow)*/
    public static final int MY_REQUEST_CODE = 101;
    // Storage Permissions for marshmallow device
    public static final int REQUEST_EXTERNAL_STORAGE = 19;
    /*To test password with at least
     * 1number,1letter,1 special character in following pattern
      * */
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    public static final String FROM_NOTIFICATION = "from_notification";
    public static final String FROM_SCHEDULED_NOTIFICATION = "from_scheduled_notification";
    public static final String HEIGHT_TAG = "HGH";
    public static final double PIC_RATIO_VALUE = 3.0;
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String VERSION_CHECK_URL ="https://admin.halaladvisor.com.au/halal_api/check_version.php" ;
    public static Context ctx;
    public static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static boolean IS_RESTAURANT_BY_CATEGORY = true;
    private static Pattern pattern;
    private static Matcher matcher;

    private static Typeface fontArial = null;
    private static Typeface fontArialBd = null;
    private static Typeface fontArialIt = null;
    private static Typeface fontGeoMe = null;
    private static Typeface fontCenturyGothic = null;
    private static Typeface fontCenturyBold = null;


    public static boolean isValidate(Context context, EditText etEmail, EditText etPassword) {

        ctx = context;
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (!email.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
            if (!isValidEmail(email)) {
                etEmail.setError("Email format Error");
                return true;
            }
            if (!isValidPassword(password)) {
                etPassword.setError("Password must contains atleast an alphabet, a number and a special character");
                return true;
            }
        } else {
            if (email.trim().equalsIgnoreCase("")) {
                etEmail.setError("Email is Empty");
                return true;
            }
            if (password.trim().equalsIgnoreCase("")) {
                etPassword.setError("Password is Empty");
                return true;
            }
            Toast.makeText(context, "Email or Password is Empty", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /*Email validation*/
    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    /*To test password with: 6 to 20 characters string with at least one digit, one upper case letter,
    * one lower case letter and one special symbol (“@#$%”).
    * */
            /*"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"*/

    /*password validation*/
    public static boolean isValidPassword(String password) {
//        Pattern pattern = Patterns.EMAIL_ADDRESS;
//        return pattern.matcher(email).matches();
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void changefonts(TextView textViewpre_font,
                                   Context mContext, String font) {

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + font);

        try {
            textViewpre_font.setTypeface(tf);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /*
    * Setting marque on textview
    * */
    public static void marqueCheckboxText(TextView textView) {

        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(5);
        textView.setSelected(true);
    }
}
