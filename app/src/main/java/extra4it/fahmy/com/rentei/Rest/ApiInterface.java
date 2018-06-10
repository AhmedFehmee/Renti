package extra4it.fahmy.com.rentei.Rest;

import extra4it.fahmy.com.rentei.Model.AcceptModel;
import extra4it.fahmy.com.rentei.Model.AddCarModel;
import extra4it.fahmy.com.rentei.Model.AgencyModel;
import extra4it.fahmy.com.rentei.Model.AgencyPendingOrder;
import extra4it.fahmy.com.rentei.Model.BrandModel;
import extra4it.fahmy.com.rentei.Model.CarModel;
import extra4it.fahmy.com.rentei.Model.CityModel;
import extra4it.fahmy.com.rentei.Model.CompanyModel;
import extra4it.fahmy.com.rentei.Model.Model;
import extra4it.fahmy.com.rentei.Model.NotificationModel;
import extra4it.fahmy.com.rentei.Model.OrderModel;
import extra4it.fahmy.com.rentei.Model.TokenModel;
import extra4it.fahmy.com.rentei.Model.UpdateModel;
import extra4it.fahmy.com.rentei.Model.UserModel;
import extra4it.fahmy.com.rentei.Model.UserPendingModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Fehoo on 3/24/2018.
 */

public interface ApiInterface {
    @POST("scopeDistance")
    Call<AgencyModel> getSearchData(@Query("lat") Double lat, @Query("lang") Double lang);

    @Multipart
    @POST("register")
    Call<UserModel> callUserRegister(@Query("name") String name,
                                     @Query("email") String email,
                                     @Query("password") String password,
                                     @Query("phone") String phone,
                                     @Query("type") String type,
                                     @Query("address") String address,
                                     @Query("lat") String lat,
                                     @Query("lang") String lang,
                                     @Query("start") String start,
                                     @Query("end") String end,
                                     @Part MultipartBody.Part driving_license,
                                     @Part MultipartBody.Part national_identity,
                                     @Query("country_id") String cityId);

    @GET("cities")
    Call<CityModel> getCities();

    @GET("brands")
    Call<BrandModel> getBrands();

    @POST("models")
    Call<Model> getModels(@Query("brand_id") String brandID);

    @POST("show_car")
    Call<CarModel> showCar(@Query("car_id") String carId);

    @POST("user_pending_orders")
    Call<UserPendingModel> getUserPending_orders(@Query("user_id") String userId);

    @POST("user_approved_orders")
    Call<UserPendingModel> getUserActive_orders(@Query("user_id") String userId);

    @POST("notifications")
    Call<NotificationModel> getUserNotifications(@Query("user_id") String userId);

    @POST("show_profile")
    Call<AgencyModel> getCompany(@Query("user_id") String userId);

    @POST("search")
    Call<AgencyModel> searchService(@Query("brand_id") String brandId, @Query("model_id") String modelId,
                                    @Query("year") String year, @Query("country_id") String countryId);

    @POST("order")
    Call<OrderModel> setOrder(@Query("user_id") String userId, @Query("car_id") String carId,
                              @Query("bick_up_date") String pickUpDate, @Query("drop_off_date") String drop_off_date,
                              @Query("bick_up_place") String pickUpPlace, @Query("drop_off_place") String dropOffPlace,
                              @Query("type") String type);

    @Multipart
    @POST("update_profile")
    Call<UpdateModel> updateService(@Query("user_id") String userId, @Query("name") String userName,
                                    @Query("email") String userEmail, @Query("phone") String userPhone,
                                    @Query("type") String userType, @Query("password") String userPassword,
                                    @Query("address") String address,
                                    @Part MultipartBody.Part userPhoto,
                                    @Query("lat") String lat, @Query("lang") String lang,
                                    @Query("start") String userWorkStart, @Query("end") String userWorkEnd);

    @POST("office_pending_orders")
    Call<AgencyPendingOrder> getOfficePending_orders(@Query("office_id") String userId);

    @POST("office_approved_orders")
    Call<AgencyPendingOrder> getOfficeActive_orders(@Query("office_id") String userId);

    @POST("approve_order")
    Call<AcceptModel> acceptOrder(@Query("order_id") String orderId);

    @Multipart
    @POST("add_car")
    Call<AddCarModel> addCar(@Query("user_id") String userId,
                             @Query("brand_id") String brandId,
                             @Query("model_id") String modelId,
                             @Query("year") String year,
                             @Query("color") String color,
                             @Query("office_price") String officePrice,
                             @Query("home_price") String homePrice,
                             @Query("driver_price") String driverPrice,
                             @Query("details") String details,
                             @Part MultipartBody.Part main_photo,
                             @Part MultipartBody.Part first_photo,
                             @Part MultipartBody.Part second_photo);
    @POST("refresh_token")
    Call<TokenModel> refreshToken (@Query("user_id") String userId,
                                   @Query("device_type") String device_type,
                                   @Query("device_token") String device_token);
}

