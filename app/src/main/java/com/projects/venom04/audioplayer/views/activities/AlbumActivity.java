package com.projects.venom04.audioplayer.views.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.interfaces.IRecyclerView;
import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.models.pojo.Audio;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.views.adapters.AudiosAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Venom on 01/10/2017.
 */

public class AlbumActivity extends AppCompatActivity implements IRecyclerView {

    private static final String TAG = "AlbumActivity";

    @BindView(R.id.image_view_cover)
    ImageView mIvCover;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view_musics)
    RecyclerView mRvMusics;

    private ArrayList<Audio> mAudiosList;
    private AudiosAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Album album = (Album) getIntent().getSerializableExtra(AudioPlayerUtils.ALBUMS);
        mAudiosList = album.getAudios();

        getSupportActionBar().setTitle(album.getAlbum());
        getSupportActionBar().setSubtitle(album.getArtist());

        Picasso.with(this).load(Uri.parse("file://" + album.getCover())).into(mIvCover);

        mAdapter = new AudiosAdapter(this, mAudiosList, this);

        mRvMusics.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvMusics.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvMusics.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        mRvMusics.addItemDecoration(dividerItemDecoration);
        mRvMusics.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(View view, int position) {

    }
}
