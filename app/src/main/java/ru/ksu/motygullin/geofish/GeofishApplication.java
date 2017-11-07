package ru.ksu.motygullin.geofish;

import android.app.Application;

import com.vk.sdk.VKSdk;

public class GeofishApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
