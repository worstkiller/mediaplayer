package com.android.incred.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incred.musicplayer.R;
import com.android.incred.musicplayer.callbacks.OnItemClickListener;
import com.android.incred.musicplayer.models.MusicModel;
import com.android.incred.musicplayer.util.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by incred on 18/12/17.
 */

public class MusicCollectionAdapter extends RecyclerView.Adapter<MusicCollectionAdapter.ViewHolder> {

    private List<MusicModel> mMusicModelList = null;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public MusicCollectionAdapter(final List<MusicModel> musicModelList, final OnItemClickListener onItemClickListener) {
        mMusicModelList = musicModelList;
        mOnItemClickListener = onItemClickListener;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        //convert the drawble into bitmap
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public MusicCollectionAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MusicCollectionAdapter.ViewHolder holder, final int position) {
        MusicModel musicModel = mMusicModelList.get(holder.getAdapterPosition());
        Glide.with(mContext).load(musicModel.getCover_image()).into(holder.mImageCover);
        holder.tvAuthor.setText(musicModel.getArtists());
        holder.tvTitle.setText(musicModel.getSong());
    }

    @Override
    public int getItemCount() {
        return mMusicModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageCover,ivMenu;
        private TextView tvMenu, tvTitle, tvAuthor;
        private CardView mCardView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageCover = itemView.findViewById(R.id.ivCoverImage);
            tvAuthor = itemView.findViewById(R.id.tvSongAuthor);
            tvTitle = itemView.findViewById(R.id.tvSongName);
            ivMenu = itemView.findViewById(R.id.ivMusicMenu);
            ivMenu.setOnClickListener(this);
            ivMenu.setTag(Constants.TAG_MENU);
            mCardView = itemView.findViewById(R.id.cvMusic);
            itemView.setOnClickListener(this);
            itemView.setTag(Constants.TAG_ITEM);
        }

        @Override
        public void onClick(final View view) {
            //here handle the click response
            mOnItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
