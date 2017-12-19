package com.android.incred.musicplayer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incred.musicplayer.R;
import com.android.incred.musicplayer.activity.MainActivity;
import com.android.incred.musicplayer.models.MusicMetaData;
import com.android.incred.musicplayer.models.MusicModel;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.android.incred.musicplayer.util.Constants.EXTRA_MUSIC_BUNDLE;

/**
 * Created by incred on 19/12/17.
 */

public class PlayerFragment extends Fragment {

    @BindView(R.id.ivFullCoverImage)
    ImageView mIvFullCoverImage;
    @BindView(R.id.tvSongName)
    TextView mTvSongName;
    @BindView(R.id.tvSongAuthor)
    TextView mTvSongAuthor;
    @BindView(R.id.tvStartTime)
    TextView mTvStartTime;
    @BindView(R.id.tvEndTime)
    TextView mTvEndTime;
    @BindView(R.id.ivPlayPauseButton)
    ImageView mIvPlayPauseButton;
    @BindView(R.id.ivPlayPreviousButton)
    ImageView mIvPlayPreviousButton;
    @BindView(R.id.ivPlayNextButton)
    ImageView mIvPlayNextButton;
    @BindView(R.id.sbTimer)
    AppCompatSeekBar mSbTimer;
    Unbinder unbinder;
    private int musicPosition = -1;
    private MusicMetaData mMusicMetaData;

    /**
     * takes position and returns a player fragment
     *
     * @param position
     * @return
     */
    public static PlayerFragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MUSIC_BUNDLE, position);
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        musicPosition = getArguments().getInt(EXTRA_MUSIC_BUNDLE);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPlayerView(musicPosition);
    }

    private void initPlayerView(final int musicPosition) {
        //here init the player view and position and data related
        mMusicMetaData = getMusicMetaData(musicPosition);
        setUIData(mMusicMetaData);
    }

    private void setUIData(MusicMetaData musicMetaData) {
        //here set the ui data
        MusicModel mMusicModel = musicMetaData.getMusicModel();
        if (mMusicModel != null) {
            mTvEndTime.setText(getString(R.string.time_start));
            mTvStartTime.setText(getString(R.string.time_start));
            mTvSongAuthor.setText(mMusicModel.getArtists());
            mTvSongName.setText(mMusicModel.getSong());
            if (musicMetaData.getNextPosition() == -1) {
                mIvPlayNextButton.setEnabled(false);
            } else {
                mIvPlayNextButton.setEnabled(true);
            }
            if (musicMetaData.getPreviousPosition() == -1) {
                mIvPlayPreviousButton.setEnabled(false);
            } else {
                mIvPlayPreviousButton.setEnabled(true);
            }
            Glide.with(getContext()).load(mMusicModel.getCover_image()).into(mIvFullCoverImage);
        } else {
            //no song found
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivPlayPauseButton, R.id.ivPlayPreviousButton, R.id.ivPlayNextButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivPlayPauseButton:
                handlePlayAndPause();
                break;
            case R.id.ivPlayPreviousButton:
                initPlayerView(mMusicMetaData.getPreviousPosition());
                break;
            case R.id.ivPlayNextButton:
                initPlayerView(mMusicMetaData.getNextPosition());
                break;
        }
    }

    private void handlePlayAndPause() {
        //this handles the play and pause logic
      
    }

    private MusicMetaData getMusicMetaData(int musicPosition) {
        //this returns the meta model from the activity based on position
        return ((MainActivity) getActivity()).getSongFromPosition(musicPosition);
    }

    private void setPlayAndPause(boolean isPlay) {
        //here decide the logic for play and pause
        if (isPlay) {
            mIvPlayPauseButton.setBackgroundResource(R.drawable.ic_action_pause);
        } else {
            mIvPlayPauseButton.setBackgroundResource(R.drawable.ic_action_play);
        }
    }

}