package rcdiploma.app.Utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rcdiploma.app.RetrofitClass.AddCartData;
import rcdiploma.app.RetrofitClass.AddFcmData;
import rcdiploma.app.RetrofitClass.AddRemoveWishlistData;
import rcdiploma.app.RetrofitClass.DeleteProfileData;
import rcdiploma.app.RetrofitClass.DocumentUploadData;
import rcdiploma.app.RetrofitClass.GetBannerData;
import rcdiploma.app.RetrofitClass.GetCategoryData;
import rcdiploma.app.RetrofitClass.GetCountryData;
import rcdiploma.app.RetrofitClass.GetLoginData;
import rcdiploma.app.RetrofitClass.GetNotificationData;
import rcdiploma.app.RetrofitClass.GetProductData;
import rcdiploma.app.RetrofitClass.GetProductDetailData;
import rcdiploma.app.RetrofitClass.GetSignupData;
import rcdiploma.app.RetrofitClass.GetStateData;
import rcdiploma.app.RetrofitClass.GetSubCategoryData;
import rcdiploma.app.RetrofitClass.GetVideoData;
import rcdiploma.app.RetrofitClass.GetWishlistData;
import rcdiploma.app.RetrofitClass.ImageUploadData;
import rcdiploma.app.RetrofitClass.UpdateCartData;
import rcdiploma.app.RetrofitClass.UpdateProfileData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<GetSignupData> addSignupData(@Field("username") String name,@Field("emailid") String emailid,@Field("password") String password,@Field("contact") String contact,@Field("gender") String gender,@Field("city") String city);

    @FormUrlEncoded
    @POST("login.php")
    Call<GetLoginData> getLoginData(@Field("emailid") String emailid, @Field("password") String password);

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<UpdateProfileData> updateProfileData(@Field("id") String id, @Field("username") String name, @Field("emailid") String emailid, @Field("password") String password, @Field("contact") String contact, @Field("gender") String gender, @Field("city") String city);

    @FormUrlEncoded
    @POST("deleteAccount.php")
    Call<DeleteProfileData> deleteProfileData(@Field("id") String id);

    @GET("getBanner.php")
    Call<GetBannerData> getBannerData();

    @GET("getCategory.php")
    Call<GetCategoryData> getCategoryData();

    @FormUrlEncoded
    @POST("getSubCategory.php")
    Call<GetSubCategoryData> getSubCategoryData(@Field("categoryId") String categoryId);

    @FormUrlEncoded
    @POST("getProduct.php")
    Call<GetProductData> getProductData(@Field("subCategoryId") String subCategoryId,@Field("userId") String userId);

    @FormUrlEncoded
    @POST("addRemoveWishlist.php")
    Call<AddRemoveWishlistData> addRemoveWishlistData(@Field("productId") String productId, @Field("userId") String userId);

    @FormUrlEncoded
    @POST("getProductDetail.php")
    Call<GetProductDetailData> getProductDetailData(@Field("productId") String productId);

    @Multipart
    @POST("addCategory.php")
    Call<ImageUploadData> imageUploadData(@Part("name") RequestBody name, @Part MultipartBody.Part image);

    @Multipart
    @POST("addDocument.php")
    Call<DocumentUploadData> documentUploadData(@Part("name") RequestBody name, @Part MultipartBody.Part document);

    @FormUrlEncoded
    @POST("addFcmToken.php")
    Call<AddFcmData> addFcmData(@Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST("getNotification.php")
    Call<GetNotificationData> getNotificationData(@Field("fcm_user_id") String fcm_user_id);

    @GET("getCountry.php")
    Call<GetCountryData> getCountryData();

    @FormUrlEncoded
    @GET("getState.php")
    Call<GetStateData> getStateData(@Field("countryId") String countryId);

    @GET("getVideo.php")
    Call<GetVideoData> getVideoData();

    @FormUrlEncoded
    @POST("getWishlist.php")
    Call<GetWishlistData> getWishlistData(@Field("userId") String userId);

    @FormUrlEncoded
    @POST("addCart.php")
    Call<AddCartData> addCartData(@Field("userId") String userId, @Field("productId") String productId, @Field("qty") String qty, @Field("price") String price);

    @FormUrlEncoded
    @POST("updateCart.php")
    Call<UpdateCartData> updateCartData(@Field("cartId") String cartId, @Field("qty") String qty, @Field("price") String price);

    @FormUrlEncoded
    @POST("deleteCart.php")
    Call<UpdateCartData> deleteCartData(@Field("cartId") String cartId);

}
