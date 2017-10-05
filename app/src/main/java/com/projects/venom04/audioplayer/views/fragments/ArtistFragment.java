package com.projects.venom04.audioplayer.views.fragments;

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
import com.projects.venom04.audioplayer.views.adapters.ExpandableArtistsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 04/10/2017.
 */

public class ArtistFragment extends Fragment {

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

        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mExpLvArtists.expandGroup(i);
        }

        return view;
    }

    private void initData() {
        AudioFileManager audioFileManager = AudioFileManager.getInstance();
        ArrayList<Artist> artistsList = audioFileManager.loadAllArtists(getContext());

        for (Artist artist : artistsList) {
            mHeaderDataList.add(artist);
            mChildDataMap.put(artist, artist.getAlbums());
            Log.d(TAG, "artist: " + artist.getArtist());
            for (Album album : artist.getAlbums())
                Log.d(TAG, "album: " + album.getAlbum());
        }
    }
}
