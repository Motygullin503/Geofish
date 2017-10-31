package ru.ksu.motygullin.geofish.geofishAPI;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.ksu.motygullin.geofish.entities.RegModel;


public interface GeoFishAPI {

    @POST("/api/v1/signup")
    Call<RegModel> registerWithEmail(@Query("email") String email,
                                     @Query("password") String password,
                                     @Query("first_name") String firstName,
                                     @Query("last_name") String lastName,
                                     @Query("city") String city,
                                     @Query("birthday") String birthday,
                                     @Query("photo") String photo);

    @POST("/api/v1/signup")
    Call<RegModel> registerWithSocials(@Query("uid") String uid,
                                       @Query("first_name") String firstName,
                                       @Query("last_name") String lastName,
                                       @Query("city") String city,
                                       @Query("birthday") String birthday,
                                       @Query("photo") String photo);


}
