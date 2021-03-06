package com.projects.venom04.audioplayer.views.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.interfaces.IRecyclerView;
import com.projects.venom04.audioplayer.models.pojo.Album;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 27/09/2017.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {

    private static final String TAG = "AudiosAdapter";

    private Context mContext;
    private ArrayList<Album> mDataList;

    private IRecyclerView mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_cover)
        ImageView mIvCover;
        @BindView(R.id.text_view_album)
        TextView mTvAlbum;
        @BindView(R.id.text_view_artist)
        TextView mTvArtist;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AlbumsAdapter(Context context, ArrayList<Album> dataList, IRecyclerView listener) {
        mContext = context;
        mDataList = dataList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Album album = mDataList.get(position);

        Picasso.with(mContext).load(Uri.parse("file://" + album.getCover())).into(holder.mIvCover);
        holder.mTvAlbum.setText(album.getAlbum());
        holder.mTvArtist.setText(album.getArtist());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}
