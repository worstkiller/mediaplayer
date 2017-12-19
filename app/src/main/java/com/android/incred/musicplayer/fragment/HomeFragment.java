package com.android.incred.musicplayer.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.incred.musicplayer.R;
import com.android.incred.musicplayer.activity.MainActivity;
import com.android.incred.musicplayer.adapter.MusicCollectionAdapter;
import com.android.incred.musicplayer.callbacks.OnItemClickListener;
import com.android.incred.musicplayer.models.MusicModel;
import com.android.incred.musicplayer.repository.OnMusicListener;
import com.android.incred.musicplayer.repository.RepositoryProvider;
import com.android.incred.musicplayer.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by incred on 18/12/17.
 */

public class HomeFragment extends Fragment {


    private static final String TAG = "HomeFragment";
    @BindView(R.id.rvMusicCollection)
    RecyclerView mRvMusicCollection;
    Unbinder unbinder;
    @BindView(R.id.pbLoader)
    ProgressBar mPbLoader;
    @BindView(R.id.tvErrorLoading)
    AppCompatButton mTvErrorLoading;
    @BindView(R.id.llError)
    LinearLayout mLlError;
    private MusicCollectionAdapter mMusicCollectionAdapter;
    private List<MusicModel> mMusicModelList = new ArrayList<>();

    /*
        returns a factory fragment instance
         */
    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        getMusicData();
    }

    private void getMusicData() {
        //get the server response here
        hideShowProgress(true);
        RepositoryProvider.getMusicOnlineRepo(getMusicListener());
    }

    private void setUpRecyclerView() {
        //create basic recyclerview setup
        mMusicCollectionAdapter = new MusicCollectionAdapter(mMusicModelList, getOnItemClickListener());
        mRvMusicCollection.setHasFixedSize(true);
        mRvMusicCollection.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvMusicCollection.setAdapter(mMusicCollectionAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private OnMusicListener getMusicListener() {
        //return a music listener callback
        return new OnMusicListener() {
            @Override
            public void onMusicLoaded(final List<MusicModel> musicModels) {
                //handle the response
                hideShowProgress(false);
                hideShowError(false);
                mMusicModelList.clear();
                mMusicModelList.addAll(musicModels);
                mMusicCollectionAdapter.notifyDataSetChanged();
                Log.d(TAG, " music loaded " + musicModels.size());
            }

            @Override
            public void onError() {
                //handle the error
                hideShowProgress(false);
                hideShowError(true);
                Log.d(TAG, " music not loaded ");
            }
        };
    }

    private OnItemClickListener getOnItemClickListener() {
        //return a click listener
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                //here handle the click inputs from recycler view
                switch (view.getTag().toString()) {
                    case Constants.TAG_ITEM:
                        openPlayerFragment(position);
                        break;
                    case Constants.TAG_MENU:
                        openOverflowMenu(view, position);
                        break;
                }
            }
        };
    }

    private void openPlayerFragment(int position) {
        //this will open the current music song in next fragment &
        //adding all songs to activity for accessible to player fragment
        ((MainActivity) getActivity()).addAllSongs(mMusicModelList);
        ((MainActivity) getActivity()).replaceFragment(PlayerFragment.getInstance(position), true);
    }

    private void openOverflowMenu(View view, final int position) {
        //here create a overflow menu and show the options
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.BOTTOM);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                //here handle the menu click
                if (item.getItemId() == R.id.menuPlay) {
                    openPlayerView(mMusicModelList.get(position));
                } else if (item.getItemId() == R.id.menuPlaylist) {
                    addToPlayList(mMusicModelList.get(position));
                }
                return false;
            }
        });
    }

    private void openPlayerView(final MusicModel musicModel) {
        //pass this to the player view
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).playMusic(musicModel);
        }
    }

    private void addToPlayList(final MusicModel musicModel) {
        //pass this to the playlist
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).addToPlayList(musicModel);
        }
    }

    private void showMessage(String msg) {
        //show the msg
        if (isAdded()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvErrorLoading)
    public void onViewClicked() {
        //here try again loading the response from the server
        hideShowError(false);
        getMusicData();
    }

    private void hideShowError(boolean isShow) {
        //hide or show the error layout
        if (isShow) {
            mLlError.setVisibility(View.VISIBLE);
        } else {
            mLlError.setVisibility(View.GONE);
        }
    }

    private void hideShowProgress(boolean isShow) {
        //hide or show the loader progress
        if (isShow) {
            mPbLoader.setVisibility(View.VISIBLE);
        } else {
            mPbLoader.setVisibility(View.GONE);
        }
    }
}
