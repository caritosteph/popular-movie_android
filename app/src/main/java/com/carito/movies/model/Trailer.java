package com.carito.movies.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by carito on 2/28/16.
 */
public class Trailer implements Parcelable {

    @Expose @SerializedName("id")
    private String id;
    @Expose @SerializedName("iso_639_1")
    private String iso6391;
    @Expose @SerializedName("key")
    private String key;
    @Expose @SerializedName("name")
    private String name;
    @Expose @SerializedName("site")
    private String site;
    @Expose @SerializedName("size")
    private int size;
    @Expose @SerializedName("type")
    private String type;

    public Trailer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeString(this.type);
    }

    protected Trailer(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public Uri buildPosterTrailer(String imgURL,String video) {
        Uri builtUri = Uri.parse(imgURL).buildUpon()
                .appendEncodedPath(video)
                .appendEncodedPath("0.jpg")
                .build();
        return builtUri;
    }

    public static final class Response {

        @Expose @SerializedName("id")
        private String id;
        @Expose @SerializedName("results")
        private ArrayList<Trailer> results = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ArrayList<Trailer> getResults() {
            return results;
        }

        public void setResults(ArrayList<Trailer> results) {
            this.results = results;
        }

    }

}
