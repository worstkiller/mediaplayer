package com.android.incred.musicplayer.repository;

import com.android.incred.musicplayer.models.MusicModel;
import com.android.incred.musicplayer.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by incred on 18/12/17.
 */

public class RepositoryProvider {

    private RepositoryProvider() {
        //empty constructor
    }

    public static void getMusicOnlineRepo(final OnMusicListener onMusicListener) {
        //here call the api server for data and return
        RetrofitInstance
                .getInterface(Constants.URL)
                .getMusicRepo()
                .enqueue(new Callback<List<MusicModel>>() {
                    @Override
                    public void onResponse(final Call<List<MusicModel>> call, final Response<List<MusicModel>> response) {
                        //handle the response
                        if (response.code() == 200) {
                            onMusicListener.onMusicLoaded(response.body());
                        } else {
                            onMusicListener.onError();
                        }
                    }

                    @Override
                    public void onFailure(final Call<List<MusicModel>> call, final Throwable t) {
                        //handle the error
                        onMusicListener.onError();
                    }
                });
    }
}
