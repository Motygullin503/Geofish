package ru.ksu.motygullin.geofish.geofishAPI;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.ksu.motygullin.geofish.entities.CreatedPost;
import ru.ksu.motygullin.geofish.entities.Posts;
import ru.ksu.motygullin.geofish.entities.RegModel;


public interface GeoFishAPI {


    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/signup")
    Call<RegModel> registerWithEmail(@Query("email") String email,
                                     @Query("password") String password,
                                     @Query("first_name") String firstName,
                                     @Query("last_name") String lastName,
                                     @Query("city") String city,
                                     @Query("birthday") String birthday,
                                     @Query("photo") String photo);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/signup")
    Call<RegModel> registerWithSocials(@Query("uid") String uid,
                                       @Query("first_name") String firstName,
                                       @Query("last_name") String lastName,
                                       @Query("city") String city,
                                       @Query("birthday") String birthday,
                                       @Query("photo") String photo);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/login")
    Call<RegModel> authentificate(@Query("email") String email,
                                  @Query("password") String password);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/users/{user_id}/posts")
    Call<CreatedPost> createPost(@Path("user_id") Integer user_id, @Query("text") String text,
                                 @Query("photo") String photo_link,
                                 @Query("video") String video_link,
                                 @Query("latitude") Double latitude, @Query("longitude") Double longitude);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/users/{id}")
    Call<CreatedPost> getUserById(@Path("id") Integer id);


    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @POST("/api/v1/users/{id}/posts")
    Call<Posts> getPostsById(@Path("id") Integer id);




}
