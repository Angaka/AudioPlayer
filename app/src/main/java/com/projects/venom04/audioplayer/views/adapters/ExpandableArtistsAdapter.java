package com.projects.venom04.audioplayer.views.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.models.pojo.Artist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 04/10/2017.
 */

public class ExpandableArtistsAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "ExpandableArtistsAdapte";

    private Context mContext;
    private List<Artist> mHeaderDataList;
    private HashMap<Artist, ArrayList<Album>> mChildDataMap;

    static class ViewHeaderHolder {
        @BindView(R.id.text_view_artist)
        TextView mTvArtist;
        @BindView(R.id.text_view_albums_nb)
        TextView mTvAlbumsNb;

        private ViewHeaderHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewChildHolder {
        @BindView(R.id.image_view_cover)
        RoundedImageView mIvCover;
        @BindView(R.id.text_view_album)
        TextView mTvAlbum;
        @BindView(R.id.text_view_audios_nb)
        TextView mTvAudiosNb;

        private ViewChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public ExpandableArtistsAdapter(Context context,
                                    List<Artist> headerDataList,
                                    HashMap<Artist, ArrayList<Album>> childDataMap) {
        mContext = context;
        mHeaderDataList = headerDataList;
        mChildDataMap = childDataMap;
    }

    @Override
    public int getGroupCount() {
        return mHeaderDataList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mHeaderDataList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildDataMap.get(mHeaderDataList.get(groupPosition)).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildDataMap.get(mHeaderDataList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHeaderHolder holder;
        Artist artist = (Artist) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expanded_group_artist, viewGroup, false);
            holder = new ViewHeaderHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHeaderHolder) convertView.getTag();
        }

        holder.mTvArtist.setText(artist.getArtist());
        holder.mTvAlbumsNb.setText(String.format(mContext.getString(R.string.artist_album_total), artist.getTotalAlbums()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ViewChildHolder holder;
        Album album = (Album) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expanded_child_album, viewGroup, false);
            holder = new ViewChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }

        if (album.getCover() != null)
            Picasso.with(mContext).load(Uri.parse("file://" + album.getCover())).into(holder.mIvCover);
        else
            holder.mIvCover.setImageResource(R.drawable.cover_default);
        holder.mTvAlbum.setText(album.getAlbum());

        if (Integer.valueOf(album.getNumSongsForArtist()) == album.getAudios().size())
            holder.mTvAudiosNb.setText(String.format(mContext.getString(R.string.artist_song_total), album.getAudios().size()));
        else
            holder.mTvAudiosNb.setText(String.format(mContext.getString(R.string.artist_song_total_out_of), Integer.valueOf(album.getNumSongsForArtist()), album.getAudios().size()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
