package com.android.incred.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by incred on 18/12/17.
 */

public class MusicModel implements Parcelable {
    @SerializedName("song")
    @Expose
    private String song;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("artists")
    @Expose
    private String artists;
    @SerializedName("cover_image")
    @Expose
    private String cover_image;

    public String getSong() {
        return song;
    }

    public void setSong(final String song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(final String artists) {
        this.artists = artists;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(final String cover_image) {
        this.cover_image = cover_image;
    }

    protected MusicModel(Parcel in) {
        song = in.readString();
        url = in.readString();
        artists = in.readString();
        cover_image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(song);
        dest.writeString(url);
        dest.writeString(artists);
        dest.writeString(cover_image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MusicModel> CREATOR = new Parcelable.Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };
}

