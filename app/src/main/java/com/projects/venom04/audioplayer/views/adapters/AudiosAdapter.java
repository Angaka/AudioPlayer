package com.projects.venom04.audioplayer.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.interfaces.IRecyclerView;
import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 27/09/2017.
 */

public class AudiosAdapter extends RecyclerView.Adapter<AudiosAdapter.ViewHolder> {

    private static final String TAG = "AudiosAdapter";

    private Context mContext;
    private ArrayList<Audio> mDataList;

    private static IRecyclerView mListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_title)
        TextView mTvTitle;
        @BindView(R.id.text_view_artist)
        TextView mTvArtist;
        @BindView(R.id.image_view_state)
        ImageView mIvState;

        private ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public AudiosAdapter(Context context, ArrayList<Audio> dataList, IRecyclerView listener) {
        mContext = context;
        mDataList = dataList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Audio audio = mDataList.get(position);

        holder.mTvTitle.setText(audio.getTitle());
        holder.mTvArtist.setText(audio.getArtist());
        //        holder.mIvState.setImageResource();

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
