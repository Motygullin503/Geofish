package ru.ksu.motygullin.geofish.geofishAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final Api ourInstance = new Api();

    private GeoFishAPI api;

    public static Api getInstance() {
        return ourInstance;
    }

    private Api() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://geofish.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(GeoFishAPI.class);
    }

    public GeoFishAPI getApi(){
        return api;
    }
}
