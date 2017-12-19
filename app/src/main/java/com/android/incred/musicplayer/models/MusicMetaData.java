package com.android.incred.musicplayer.models;

/**
 * Created by incred on 19/12/17.
 */

public class MusicMetaData {
    private int nextPosition;
    private int previousPosition;
    private MusicModel mMusicModel;

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(final int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(final int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public MusicModel getMusicModel() {
        return mMusicModel;
    }

    public void setMusicModel(final MusicModel musicModel) {
        mMusicModel = musicModel;
    }
}
