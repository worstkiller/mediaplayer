package com.android.incred.musicplayer.repository;

import com.android.incred.musicplayer.models.MusicModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by incred on 8/12/17.
 */

public interface WebInterface {

    @GET("studio")
    Call<List<MusicModel>> getMusicRepo();

}
