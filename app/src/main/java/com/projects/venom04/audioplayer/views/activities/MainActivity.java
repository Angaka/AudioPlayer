package com.projects.venom04.audioplayer.views.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.controllers.AudioFileManager;
import com.projects.venom04.audioplayer.models.interfaces.IAudio;
import com.projects.venom04.audioplayer.models.pojo.Audio;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.utils.PermissionsService;
import com.projects.venom04.audioplayer.utils.StorageUtil;
import com.projects.venom04.audioplayer.views.fragments.AlbumsFragment;
import com.projects.venom04.audioplayer.views.fragments.ArtistFragment;
import com.projects.venom04.audioplayer.views.fragments.MusicsFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , IAudio
        , View.OnClickListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;


    // Audio Player Bottom bar
    @BindView(R.id.view_audio_player)
    View mViewAudioPlayer;
    @BindView(R.id.text_view_title)
    TextView mTvTitle;
    @BindView(R.id.text_view_artist)
    TextView mTvArtist;
    @BindView(R.id.image_button_previous)
    ImageButton mIvPrevious;
    @BindView(R.id.image_button_play)
    ImageButton mIvPlay;
    @BindView(R.id.image_button_next)
    ImageButton mIvNext;

    private boolean mIsPlayerPaused = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case AudioPlayerUtils.MEDIA:
                    if (mViewAudioPlayer.getVisibility() == View.GONE)
                        mViewAudioPlayer.setVisibility(View.VISIBLE);

                    Audio audio = (Audio) intent.getSerializableExtra(AudioPlayerUtils.MEDIA);
                    mTvTitle.setText(audio.getTitle());
                    mTvArtist.setText(audio.getArtist());
                    break;
            }
        }
    };

    private PermissionsService mPermissionsService;
    private SharedPreferences mPreferences;
    private Snackbar mSnackBarPermissions;

    private AudioFileManager mAudioFileManager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);

        mSnackBarPermissions = Snackbar
                .make(findViewById(android.R.id.content), R.string.permissions_request, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.see_permissions,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ArrayList<String> unacceptedPermissions = mPermissionsService.findUnacceptedPermissions(Arrays.asList(AudioPlayerUtils.PERMISSIONS));
                                String[] permissionsArray = new String[unacceptedPermissions.size()];

                                for (int i = 0; i < unacceptedPermissions.size(); i++)
                                    permissionsArray[i] = unacceptedPermissions.get(i);
                                requestPermissions(permissionsArray, AudioPlayerUtils.REQUEST_CODE);
                            }
                        });

        mPreferences = getSharedPreferences(AudioPlayerUtils.PREFS, Context.MODE_PRIVATE);

        mAudioFileManager = AudioFileManager.getInstance();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);

        if (!isAllPermissionsAccepted() && !mSnackBarPermissions.isShown()) {
            mSnackBarPermissions.show();
        } else {
            mViewPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }

        mIvPrevious.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioPlayerUtils.MEDIA);
        intentFilter.addAction(AudioPlayerUtils.ACTION_STOP);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isAllPermissionsAccepted() {
        mPermissionsService = new PermissionsService(this, mPreferences);

        ArrayList<String> unaskedPermissions = mPermissionsService.findUnAskedPermissions(Arrays.asList(AudioPlayerUtils.PERMISSIONS));

        if (!unaskedPermissions.isEmpty()) {
            String[] permissionsArray = new String[unaskedPermissions.size()];

            for (int i = 0; i < unaskedPermissions.size(); i++)
                permissionsArray[i] = unaskedPermissions.get(i);
            requestPermissions(permissionsArray, AudioPlayerUtils.REQUEST_CODE);
            return false;
        } else {
            ArrayList<String> unacceptedPermission = mPermissionsService.findUnacceptedPermissions(Arrays.asList(AudioPlayerUtils.PERMISSIONS));
            if (!unacceptedPermission.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AudioPlayerUtils.REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++)
                        mPermissionsService.markAsAsked(permissions[i]);

                    ArrayList<String> unacceptedPermissions = mPermissionsService.findUnacceptedPermissions(Arrays.asList(AudioPlayerUtils.PERMISSIONS));
                    if (!unacceptedPermissions.isEmpty()) {
                        if (!mSnackBarPermissions.isShown())
                            mSnackBarPermissions.show();
                    } else {
                        if (mSnackBarPermissions.isShown())
                            mSnackBarPermissions.dismiss();
                        mViewPager.setAdapter(mPagerAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                    }

                }
                break;
        }
    }

    @Override
    public void onSelectedAudioInList(int audioIndex) {
        ArrayList<Audio> audiosList = mAudioFileManager.loadAllAudios(this, null);

        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
        storageUtil.clearCachedAudioPlaylist();
        storageUtil.storeAudios(audiosList);
        storageUtil.storeAudioIndex(audioIndex);

        Intent broadcastIntent = new Intent(AudioPlayerUtils.PLAY_NEW_AUDIO);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_button_previous:
                Intent previousIntent = new Intent(AudioPlayerUtils.ACTION_PREVIOUS);
                sendBroadcast(previousIntent);
                break;
            case R.id.image_button_play:
                Intent playIntent = new Intent();
                if (!mIsPlayerPaused) {
                    playIntent.setAction(AudioPlayerUtils.ACTION_PAUSE);
                    mIvPlay.setImageResource(R.drawable.btn_play);
                    mIsPlayerPaused = true;
                } else {
                    playIntent.setAction(AudioPlayerUtils.ACTION_PLAY);
                    mIvPlay.setImageResource(R.drawable.btn_pause);
                    mIsPlayerPaused = false;
                }
                sendBroadcast(playIntent);
                break;
            case R.id.image_button_next:
                Intent nextIntent = new Intent(AudioPlayerUtils.ACTION_NEXT);
                sendBroadcast(nextIntent);
                break;
        }
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private final int PAGE_COUNT = 3;
        private int mTabTitles[] = new int[]{R.string.musics, R.string.albums, R.string.artists};
        private Context mContext;

        PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MusicsFragment.newInstance();
                case 1:
                    return AlbumsFragment.newInstance();
                case 2:
                    return ArtistFragment.newInstance();
                default:
                    return MusicsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(mTabTitles[position]);
        }
    }
}
