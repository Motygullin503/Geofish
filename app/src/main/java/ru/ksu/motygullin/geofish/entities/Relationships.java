
package ru.ksu.motygullin.geofish.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relationships {

    @SerializedName("post-photos")
    @Expose
    private PostPhotos postPhotos;
    @SerializedName("post-locations")
    @Expose
    private PostLocations postLocations;
    @SerializedName("post-videos")
    @Expose
    private PostVideos postVideos;

    public PostPhotos getPostPhotos() {
        return postPhotos;
    }

    public void setPostPhotos(PostPhotos postPhotos) {
        this.postPhotos = postPhotos;
    }

    public PostLocations getPostLocations() {
        return postLocations;
    }

    public void setPostLocations(PostLocations postLocations) {
        this.postLocations = postLocations;
    }

    public PostVideos getPostVideos() {
        return postVideos;
    }

    public void setPostVideos(PostVideos postVideos) {
        this.postVideos = postVideos;
    }

}
