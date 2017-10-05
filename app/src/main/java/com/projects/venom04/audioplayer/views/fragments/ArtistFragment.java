package com.projects.venom04.audioplayer.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.controllers.AudioFileManager;
import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.models.pojo.Artist;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.views.activities.AlbumActivity;
import com.projects.venom04.audioplayer.views.adapters.ExpandableArtistsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 04/10/2017.
 */

public class ArtistFragment extends Fragment
        implements ExpandableListView.OnChildClickListener {

    private static final String TAG = "ArtistFragment";

    @BindView(R.id.expandable_listview_artists)
    ExpandableListView mExpLvArtists;

    private ExpandableArtistsAdapter mAdapter;

    private List<Artist> mHeaderDataList;
    private HashMap<Artist, ArrayList<Album>> mChildDataMap;

    public static ArtistFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHeaderDataList = new ArrayList<>();
        mChildDataMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, view);

        initData();
        mAdapter = new ExpandableArtistsAdapter(getContext(), mHeaderDataList, mChildDataMap);
        mExpLvArtists.setAdapter(mAdapter);
        mExpLvArtists.setOnChildClickListener(this);

        return view;
    }

    private void initData() {
        AudioFileManager audioFileManager = AudioFileManager.getInstance();
        ArrayList<Artist> artistsList = audioFileManager.loadAllArtists(getContext());

        for (Artist artist : artistsList) {
            mHeaderDataList.add(artist);
            mChildDataMap.put(artist, artist.getAlbums());
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        Album album = mChildDataMap.get(mHeaderDataList.get(groupPosition)).get(childPosition);

        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(AudioPlayerUtils.ALBUMS, album);
        startActivity(intent);
        return false;
    }
}
