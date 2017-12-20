package com.android.incred.musicplayer.activity;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incred.musicplayer.R;
import com.android.incred.musicplayer.fragment.HomeFragment;
import com.android.incred.musicplayer.fragment.PlayerFragment;
import com.android.incred.musicplayer.models.MusicMetaData;
import com.android.incred.musicplayer.models.MusicModel;
import com.android.incred.musicplayer.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.flContainer)
    FrameLayout mFlContainer;
    @BindView(R.id.ivPlayPauseButton)
    ImageView mIvPlayPauseButton;
    @BindView(R.id.tvSongName)
    TextView mTvSongName;
    @BindView(R.id.tvSongSinger)
    TextView mTvSongSinger;
    @BindView(R.id.rlPlayerHolder)
    RelativeLayout mRlPlayerHolder;
    private MediaPlayer mMediaPlayer;
    private List<MusicModel> mMusicModelList = new ArrayList<>();
    private List<MusicModel> mAllSongs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        replaceFragment(HomeFragment.getInstance(), false);
        initMembers();
    }

    private void initMembers() {
        //initialize the members here
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //media prepare
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                //media player prepared
                togglePlayPausePlayer();
                PlayerFragment playerFragment = getPlayerFragment();
                if (playerFragment != null) {
                    playerFragment.onPreparedCalled();
                }
            }
        });

        //media completion
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mediaPlayer) {
                //handle the media play completion
                handleOnCompletionLogic();
                PlayerFragment playerFragment = getPlayerFragment();
                if (playerFragment != null) {
                    playerFragment.handleOnCompletionLogic();
                }
            }
        });
    }

    public void handleOnCompletionLogic() {
        //handle the playback completion logic
        setPlayAndPause(false);
        if (mMusicModelList.size() > 0) {
            for (MusicModel musicModel : mMusicModelList) {
                if (mTvSongName.getText().toString().equals(musicModel.getSong())
                        &&
                        mTvSongSinger.getText().toString().equals(musicModel.getArtists())) {
                    //a song is matched with playing one, remove the current playing one
                    mMusicModelList.remove(musicModel);
                }
                //check if the size is still larger than 0 if so play the song from top else hide the player
                if (mMusicModelList.size() > 0) {
                    playMusic(mMusicModelList.get(0));
                } else {
                    mMediaPlayer.reset();
                    showHideBottomPlayer(false);
                }
            }
        } else {
            mMediaPlayer.reset();
            showHideBottomPlayer(false);
        }
    }

    public void replaceFragment(Fragment fragment, final boolean backStack) {
        //open the fragment
        String tag = fragment.getClass().getCanonicalName();
        if (backStack) {
            getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment, tag).addToBackStack(tag).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment, tag).commit();
        }
    }

    @OnClick(R.id.ivPlayPauseButton)
    public void onViewClicked() {
        //handle the play pause logic
        togglePlayPausePlayer();
    }

    private void togglePlayPausePlayer() {
        //here handle the logic for play and pause the player
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setPlayAndPause(false);
        } else {
            mMediaPlayer.start();
            setPlayAndPause(true);
        }
    }

    public void playMusic(final MusicModel musicModel) {
        //here play the music with data in below
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(musicModel.getUrl());
            mMediaPlayer.prepareAsync();
            if (!isPlayerFragment()) {
                setPlayAndPause(true);
                showHideBottomPlayer(true);
                mTvSongName.setText(musicModel.getSong());
                mTvSongSinger.setText(musicModel.getArtists());
            }
        } catch (IOException | IllegalStateException exp) {
            exp.printStackTrace();
            showMessage(getString(R.string.error_unable_play));
        }
    }

    public void showMessage(final String msg) {
        //here show the message if something went wrong
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showHideBottomPlayer(boolean isTrue) {
        //this hides and shows the layout with animation
        if (isTrue) {
            mRlPlayerHolder.setAnimation(Util.showHideAnim(this, true));
            mRlPlayerHolder.setVisibility(View.VISIBLE);
        } else {
            mRlPlayerHolder.setAnimation(Util.showHideAnim(this, false));
            mRlPlayerHolder.setVisibility(View.GONE);
        }
    }

    private void setPlayAndPause(boolean isPlay) {
        //here decide the logic for play and pause
        if (isPlay) {
            mIvPlayPauseButton.setBackgroundResource(R.drawable.ic_action_pause);
        } else {
            mIvPlayPauseButton.setBackgroundResource(R.drawable.ic_action_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void addToPlayList(MusicModel musicModel) {
        //this adds the music player object to the current playing list
        mMusicModelList.add(musicModel);
    }

    public void addAllSongs(List<MusicModel> modelList) {
        //this adds all the song here for reference in player fragment
        mAllSongs.clear();
        mAllSongs.addAll(modelList);
    }

    public MusicMetaData getSongFromPosition(int position) {
        //pass position to get the next or previous song
        MusicMetaData musicMetaData = new MusicMetaData();
        for (int count = 0; count < mAllSongs.size(); count++) {
            if (count == position) {
                musicMetaData.setMusicModel(mAllSongs.get(count));
                if (count - 1 >= 0) {
                    musicMetaData.setPreviousPosition(count - 1);
                } else {
                    musicMetaData.setPreviousPosition(-1);
                }
                if (count + 1 <= mAllSongs.size()) {
                    musicMetaData.setNextPosition(count + 1);
                } else {
                    musicMetaData.setNextPosition(-1);
                }
            }
        }
        return musicMetaData;
    }

    private boolean isPlayerFragment() {
        //this returns if current fragment is player fragment
        Fragment fragment = getFragmentManager().findFragmentByTag(PlayerFragment.class.getCanonicalName());
        if (fragment instanceof PlayerFragment) {
            return true;
        } else {
            return false;
        }
    }

    private PlayerFragment getPlayerFragment() {
        //returns a player fragment
        Fragment fragment = getFragmentManager().findFragmentByTag(PlayerFragment.class.getCanonicalName());
        if (fragment instanceof PlayerFragment) {
            return (PlayerFragment) fragment;
        } else {
            return null;
        }
    }

    public int getDuration() {
        //this returns total time in milliseconds
        if (mMediaPlayer.isPlaying()) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public boolean isPlaying() {
        //this returns if media player is playing
        return mMediaPlayer.isPlaying();
    }

    public void stop() {
        //stop the media player
        mMediaPlayer.stop();
    }

    public void reset() {
        //reset the media player
        mMediaPlayer.reset();
    }

    public void pause() {
        //pause the media player
        mMediaPlayer.pause();
    }

    public void start() {
        //start the media player
        mMediaPlayer.start();
    }

    public void release() {
        //release media player
        mMediaPlayer.release();
    }

    public int getCurrentPosition() {
        //return current position
        return mMediaPlayer.getCurrentPosition();
    }
}
