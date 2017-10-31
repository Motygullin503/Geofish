package ru.ksu.motygullin.geofish.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RegModel {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
