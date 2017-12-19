package com.android.incred.musicplayer.repository;

import com.android.incred.musicplayer.models.MusicModel;

import java.util.List;

/**
 * Created by incred on 18/12/17.
 */

public interface OnMusicListener {
    void onMusicLoaded(List<MusicModel> musicModels);
    void onError();
}
