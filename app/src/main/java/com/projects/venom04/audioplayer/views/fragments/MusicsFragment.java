package com.projects.venom04.audioplayer.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.controllers.AudioFileManager;
import com.projects.venom04.audioplayer.models.interfaces.IAudio;
import com.projects.venom04.audioplayer.models.interfaces.IRecyclerView;
import com.projects.venom04.audioplayer.views.adapters.AudiosAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 25/09/2017.
 */

public class MusicsFragment extends Fragment implements IRecyclerView {

    private static final String TAG = "MusicsFragment";

    @BindView(R.id.recycler_view_musics)
    RecyclerView mRvMusics;

    private AudiosAdapter mAdapter;

    private IAudio mListener;

    public static MusicsFragment newInstance() {

        Bundle args = new Bundle();

        MusicsFragment fragment = new MusicsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (IAudio) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musics, container, false);
        ButterKnife.bind(this, view);

        AudioFileManager audioFileManager = AudioFileManager.getInstance();
        mAdapter = new AudiosAdapter(getContext(), audioFileManager.loadAllAudios(getContext(), null), this);
        
        mRvMusics.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvMusics.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvMusics.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRvMusics.addItemDecoration(dividerItemDecoration);
        mRvMusics.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onItemClicked(View view, int position) {
        Log.d(TAG, "onItemClicked: " + position);
        mListener.onSelectedAudioInList(position);
    }
}
