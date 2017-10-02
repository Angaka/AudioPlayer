package com.projects.venom04.audioplayer.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.controllers.AudioFileManager;
import com.projects.venom04.audioplayer.models.interfaces.IRecyclerView;
import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.views.activities.AlbumActivity;
import com.projects.venom04.audioplayer.views.adapters.AlbumsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 30/09/2017.
 */

public class AlbumsFragment extends Fragment implements IRecyclerView {

    private static final String TAG = "AlbumsFragment";

    @BindView(R.id.recycler_view_albums)
    RecyclerView mRvAlbums;

    private ArrayList<Album> mAlbumsList;
    private AlbumsAdapter mAdapter;

    public static AlbumsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, view);

        AudioFileManager audioFileManager = AudioFileManager.getInstance();
        mAlbumsList = audioFileManager.loadAllAlbums(getContext());
        mAdapter = new AlbumsAdapter(getContext(), mAlbumsList, this);

        mRvAlbums.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRvAlbums.setLayoutManager(gridLayoutManager);
        mRvAlbums.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onItemClicked(View view, int position) {
        Intent intent = new Intent(getActivity(), AlbumActivity.class);
        intent.putExtra(AudioPlayerUtils.ALBUMS, mAlbumsList.get(position));
        startActivity(intent);
    }
}
