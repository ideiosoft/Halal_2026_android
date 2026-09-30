package com.dapl.halaladvisor.extra;

import static com.dapl.halaladvisor.application_data.AppData.THIRD_PARTY_LOGIN;

import com.dapl.halaladvisor.Model.AddProductRequest;
import com.dapl.halaladvisor.Model.AddressModelResponse;
import com.dapl.halaladvisor.Model.DeleteImageRes;
import com.dapl.halaladvisor.Model.NearbyRestaurantResponse;
import com.dapl.halaladvisor.Model.ProductResponse;
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes;
import com.dapl.halaladvisor.Model.featuredimages.FeaturedImagesRes;
import com.dapl.halaladvisor.Model.fetchallreviews.FetchAllReviewsRes;
import com.dapl.halaladvisor.Model.fetchreview.FetchReviewRes;
import com.dapl.halaladvisor.Model.getgalleryimagesres.GetGalleryImagesRes;
import com.dapl.halaladvisor.Model.getgooglereviews.GetGoogleReviewsRes;
import com.dapl.halaladvisor.Model.getmenures.GetMenuRes;
import com.dapl.halaladvisor.Model.getofferres.GetOfferListingRes;
import com.dapl.halaladvisor.Model.getplaceidres.GetPlaceIdRes;
import com.dapl.halaladvisor.Model.instaresfinal.InstaResFinal;
import com.dapl.halaladvisor.Model.offerredeemres.OfferRedeemRes;
import com.dapl.halaladvisor.Model.profileupdateres.ProfileUpdateRes;
import com.dapl.halaladvisor.Model.socialloginres.SocialLoginRes;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;


public interface ApiService {
    public static final String BASE_URL = "https://admin.halaladvisor.com.au/";// Live URL
    //  public static final String BASE_URL = "https://admin.halaladvisor.com.au/halal_new/";// Live URL
    public static final String BASE_GOOGLE_URL = "https://maps.googleapis.com/maps/api/";// Live URL
    public static final String UPDATE_USER_PROFILE = BASE_URL + "registration/edit_user";
    public static final String GET_ALL_RESTAURANTS = BASE_URL + "resturent/getresturentbycat";
    public static final String ADD_REVIEW = BASE_URL + "resturent/manage_review";
    public static final String DELETE_REVIEW_IMAGE = BASE_URL + "resturent/delete_review_image";
    public static final String TRACK_ACTIVITY = BASE_URL + "resturent/trackActivity";
    public static final String FEATURED_IMAGES = BASE_URL + "resturent/getFeaturedImages";
    public static final String GET_HALAL_INSTA_PROFILE = "https://www.instagram.com/halaladvisor/?__a=1&access_token";
    public static final String REEDEM_OFFER = BASE_URL + "resturent/redeemOffer";
    public static final String FETCH_ALL_REVIEWS = BASE_URL + "resturent/getallreviewsbyrestid";
    public static final String FETCH_PLACE_ID = "https://maps.googleapis.com/maps/api/geocode/json?address=Ogalo%20City%20Sydney&key=AIzaSyAwDJEMBWF47UbCpPlGh-86vBpN7ggsfDo";
    public static final String FETCH_GOOGLE_REVIEWS = BASE_URL + "resturent/getallreviewsbyrestid ";

    public static final String GET_RESTAURANT_API = BASE_URL + "resturent/getRestaurantMenu";
    public static final String GET_GALLERY_IMAGES = BASE_URL + "resturent/resturent_gallery";
    public static final String FETCH_REVIEW = BASE_URL + "resturent/fetch_review";
    public static final String GET_OFFER_API = BASE_URL + "resturent/getOffersListing";
    public static final String GET_NEARBY_RESTAURANTS = BASE_URL + "resturent/getNearByRestruent";
    public static final String MY_DATA = "/users/";

    @Multipart
    @POST(UPDATE_USER_PROFILE)
    Observable<ProfileUpdateRes> updateUserProfile(@Part("u_id") RequestBody u_id, @Part("username") RequestBody username, @Part("useremail") RequestBody useremail, @Part("usermobile") RequestBody usermobile, @Part("location") RequestBody location, @Part MultipartBody.Part profileImage);//image


    //add review with images

    @Multipart
    @POST(ADD_REVIEW)
    Observable<AddReviewRes> addReviewWithImages(@Part("res_id") RequestBody res_id, @Part("userid") RequestBody userid, @Part("rating_count") RequestBody rating_count, @Part("review") RequestBody review, @Part("rating_id") RequestBody rating_id, @Part ArrayList<MultipartBody.Part> reviewImages);//image


    // @POST(GET RESTAURANT API)

    @FormUrlEncoded
    @POST(GET_RESTAURANT_API)
    Observable<GetMenuRes> getRestaurantMenuApi(@Field("r_id") String r_id);//image

    // @POST(GET gallery API)

    @FormUrlEncoded
    @POST(GET_GALLERY_IMAGES)
    Observable<GetGalleryImagesRes> getGalleryImages(@Field("res_id") String r_id);//image


    // @POST(GET OFFER API)

    @FormUrlEncoded
    @POST(GET_OFFER_API)
    Observable<GetOfferListingRes> getOfferListing(@Field("r_id") String r_id);//image


    // @POST(FETCH REVIEW API)

    @FormUrlEncoded
    @POST(FETCH_REVIEW)
    Observable<FetchReviewRes> fetchReview(@Field("res_id") String r_id, @Field("u_id") String user_id);//image


    // @POST(FETCH REVIEW API)

    @FormUrlEncoded
    @POST(DELETE_REVIEW_IMAGE)
    Observable<DeleteImageRes> deleteReviewImage(@Field("review_id") String review_id, @Field("review_image_id") String review_image_id);//image


    @FormUrlEncoded
    @POST(FETCH_ALL_REVIEWS)
    Observable<FetchAllReviewsRes> fetchAllReviews(@Field("r_id") String r_id);//image


    @GET(FEATURED_IMAGES)
    Observable<FeaturedImagesRes> getFeaturedImages();


    @GET()
    Observable<InstaResFinal> getHalalProfile(@Url String url);


    @GET()
    Observable<InstaResFinal> getRefreshAccessToken(@Url String url);


    @GET()
    Observable<GetPlaceIdRes> getGooglePlaceId(@Url String url);

    @GET()
    Observable<GetGoogleReviewsRes> getPlaceReviews(@Url String url);

    @GET()
    Observable<AddressModelResponse> getPlaceAddress(@Url String url);



    // add and get products in case of failure

    @Multipart
    @POST("api/product")
    Observable<ProductResponse> addProduct(
            @Part("barcode") RequestBody barcode,
            @Part("product_name") RequestBody productName,
            @Part("brand") RequestBody brand,
            @Part("category") RequestBody category,
            @Part("country") RequestBody country,
            @Part("ingredients") RequestBody ingredients,
            @Part("halalStatus") RequestBody halalStatus,
            @Part MultipartBody.Part image
    );

    @GET("api/product/{id}")
    Observable<ProductResponse> getProductDetails(@Path("id") String productId);


    // @POST(REEDEM OFFER API)

    @FormUrlEncoded
    @POST(REEDEM_OFFER)
    Observable<OfferRedeemRes> redeemOfferApi(@Field("offer_id") String offer_id, @Field("user_id") String user_id, @Field("email") String email);//image


    // @POST(REEDEM OFFER API)

    @FormUrlEncoded
    @POST(TRACK_ACTIVITY)
    Observable<AddReviewRes> trackActivity(@Field("activity_type") String activity_type, @Field("user_id") String user_id, @Field("timestamp") String timestamp, @Field("month_name") String month_name, @Field("resturant_id") String resturant_id);
    // @POST(social login API)

    @FormUrlEncoded
    @POST(GET_NEARBY_RESTAURANTS)
    Observable<NearbyRestaurantResponse> getNearByRestaurants(@Field("lattitude") double lattitude, @Field("longitude") double longitude, @Field("presentdate") String presentdate, @Field("time") String time, @Field("u_id") String u_id);

    @FormUrlEncoded
    @POST(GET_NEARBY_RESTAURANTS)
    Observable<NearbyRestaurantResponse> getRestaurantbyCat(@Field("lattitude") double lattitude, @Field("longitude") double longitude, @Field("presentdate") String presentdate, @Field("time") String time, @Field("u_id") String u_id);
    // @POST(social login API)


    @FormUrlEncoded
    @POST(THIRD_PARTY_LOGIN)
    Observable<SocialLoginRes> socialLogin(@Field("third_party_id") String third_party_id, @Field("profileimage") String profileimage, @Field("username") String username, @Field("email") String email, @Field("phone") String phone, @Field("device_type") String device_type, @Field("device_token") String device_token, @Field("lat") String lat, @Field("long") String lng);


}