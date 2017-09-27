package com.projects.venom04.audioplayer.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.pojo.Audio;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.views.adapters.ExpandableMusicsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 25/09/2017.
 */

public class MusicsFragment extends Fragment {

    private static final String TAG = "MusicsFragment";

    @BindView(R.id.expandable_lv_musics)
    ExpandableListView mExpLvMusics;

    private List<String> mListHeaderData;
    private HashMap<String, List<Audio>> mListChildData;
    private ArrayList<Audio> mMusics;

    public static MusicsFragment newInstance(ArrayList<Audio> musics) {

        Bundle args = new Bundle();

        MusicsFragment fragment = new MusicsFragment();
        args.putSerializable(AudioPlayerUtils.MUSICS, musics);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMusics = (ArrayList<Audio>) getArguments().getSerializable(AudioPlayerUtils.MUSICS);
        }
        mListHeaderData = new ArrayList<>();
        mListChildData = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musics, container, false);
        ButterKnife.bind(this, view);

        initData();

        ExpandableMusicsAdapter adapter = new ExpandableMusicsAdapter(getContext(), mListHeaderData, mListChildData);
        mExpLvMusics.setAdapter(adapter);
        mExpLvMusics.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        for (int i = 0; i < adapter.getGroupCount(); i++)
           mExpLvMusics.expandGroup(i);

        return view;
    }

    public void initData() {
        char[] alphabet = "#abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (char letter : alphabet) {
            String strLetter = String.valueOf(letter);
            mListHeaderData.add(strLetter);
        }

        for (String letterHeader : mListHeaderData) {
            List<Audio> audios = new ArrayList<>();
            for (Audio audio : mMusics) {
                char firstLetter = audio.getTitle().charAt(0);
                if (letterHeader.equals(String.valueOf(firstLetter).toLowerCase())) {
                    audios.add(audio);
                }
            }
            mListChildData.put(letterHeader, audios);
        }
    }
}
