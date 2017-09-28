package com.projects.venom04.audioplayer.models.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.projects.venom04.audioplayer.R;
import com.projects.venom04.audioplayer.models.pojo.Audio;
import com.projects.venom04.audioplayer.utils.AudioPlayerUtils;
import com.projects.venom04.audioplayer.utils.StorageUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Venom on 25/09/2017.
 */

public class MediaPlayerService extends Service
        implements MediaPlayer.OnCompletionListener
        , MediaPlayer.OnPreparedListener
        , MediaPlayer.OnErrorListener
        , AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "MediaPlayerService";

    public static final String ACTION_PLAY = "com.projects.venom04.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.projects.venom04.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.projects.venom04.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.projects.venom04.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.projects.venom04.audioplayer.ACTION_STOP";

    private final IBinder mBinder = new LocalBinder();

    private MediaSessionManager mMediaSessionManager;
    private MediaSession mMediaSession;
    private MediaController.TransportControls mTransportsControls;

    private static final int NOTIFICATION_ID = 101;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private int mResumePosition;

    private StorageUtil mStorageUtil;
    private ArrayList<Audio> mAudiosList;
    private int mAudioIndex = -1;
    private Audio mActiveAudio;

    private boolean mOnGoingCall = false;
    private PhoneStateListener mPhoneStateListener;
    private TelephonyManager mTelephonyManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case AudioPlayerUtils.PLAY_NEW_AUDIO:
                    mAudioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
                    if (mAudioIndex != -1 && mAudioIndex < mAudiosList.size()) {
                        mActiveAudio = mAudiosList.get(mAudioIndex);
                    } else {
                        stopSelf();
                    }

                    stopMedia();
                    mMediaPlayer.reset();
                    initMediaPlayer();
                    playMedia();
                    break;
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    pauseMedia();
                    break;
            }

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callStateListener();

        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addAction(AudioPlayerUtils.PLAY_NEW_AUDIO);
        registerReceiver(mReceiver, intentFilter);

        try {
            mStorageUtil = new StorageUtil(getApplicationContext());
            mAudiosList = mStorageUtil.loadAudios();
            mAudioIndex = mStorageUtil.loadAudioIndex();

            if (mAudioIndex != -1 && mAudioIndex < mAudiosList.size())
                mActiveAudio = mAudiosList.get(mAudioIndex);
            else
                stopSelf();
        } catch (NullPointerException e) {
            stopSelf();
        }

        if (!requestAudioFocus())
            stopSelf();

        if (mMediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackState.STATE_PLAYING);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            stopMedia();
            mMediaPlayer.release();
        }
        removeAudioFocus();
        if (mPhoneStateListener != null)
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        removeNotification();
        unregisterReceiver(mReceiver);

        mStorageUtil.clearCachedAudioPlaylist();
    }

    private void callStateListener() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mMediaPlayer != null) {
                            pauseMedia();
                            mOnGoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mMediaPlayer != null) {
                            if (mOnGoingCall) {
                                resumeMedia();
                                mOnGoingCall = false;
                            }
                        }
                        break;
                }
            }
        };
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private boolean requestAudioFocus() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = 0;
        if (mAudioManager != null) {
            result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this);
    }

    private void initMediaSession() throws RemoteException {
        if (mMediaSessionManager != null)
            return;

        mMediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        mMediaSession = new MediaSession(getApplicationContext(), "AudioPlayer");
        mTransportsControls = mMediaSession.getController().getTransportControls();
        mMediaSession.setActive(true);

        mMediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        updateMetaData();

        mMediaSession.setCallback(new MediaSession.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                resumeMedia();
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                skipToNext();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                skipToPrevious();
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                stopSelf();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
            }
        });
    }


    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);

        mMediaPlayer.reset();

        mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mMediaPlayer.setDataSource(mActiveAudio.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }

        mMediaPlayer.prepareAsync();
    }

    private void playMedia() {
        if (!mMediaPlayer.isPlaying())
            mMediaPlayer.start();
        updateMetaData();
        buildNotification(PlaybackState.STATE_PLAYING);
    }

    private void stopMedia() {
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            buildNotification(PlaybackState.STATE_PAUSED);
        }
    }

    private void pauseMedia() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mResumePosition = mMediaPlayer.getCurrentPosition();
            updateMetaData();
            buildNotification(PlaybackState.STATE_PAUSED);
        }
    }

    private void resumeMedia() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.seekTo(mResumePosition);
            mMediaPlayer.start();
            buildNotification(PlaybackState.STATE_PLAYING);
        }
    }

    private void skipToPrevious() {
        if (mAudioIndex == 0) {
            mAudioIndex = mAudiosList.size() - 1;
            mActiveAudio = mAudiosList.get(mAudioIndex);
        } else {
            mActiveAudio = mAudiosList.get(--mAudioIndex);
        }

        mStorageUtil.storeAudioIndex(mAudioIndex);

        stopMedia();
        mMediaPlayer.reset();
        initMediaPlayer();

        updateMetaData();
        buildNotification(PlaybackState.STATE_PLAYING);
    }

    private void skipToNext() {
        if (mAudioIndex == mAudiosList.size() - 1) {
            mAudioIndex = 0;
            mActiveAudio = mAudiosList.get(mAudioIndex);
        } else {
            mActiveAudio = mAudiosList.get(++mAudioIndex);
        }

        mStorageUtil.storeAudioIndex(mAudioIndex);

        stopMedia();
        mMediaPlayer.reset();
        initMediaPlayer();

        updateMetaData();
        buildNotification(PlaybackState.STATE_PLAYING);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d(TAG, "onError: MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(TAG, "onError: MEDIA_ERROR_SERVER_DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(TAG, "onError: MEDIA_ERROR_UNKNOWN " + extra);
                break;
        }
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mAudioIndex == mAudiosList.size() - 1)
            stopMedia();
        else
            skipToNext();
    }

    @Override
    public void onAudioFocusChange(int what) {
        switch (what) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if (mMediaPlayer == null)
                    initMediaPlayer();
                else
                    playMedia();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pauseMedia();
                break;
        }
    }

    private void updateMetaData() {
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);

        mMediaSession.setMetadata(
                new MediaMetadata.Builder()
                        .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, albumArt)
                        .putString(MediaMetadata.METADATA_KEY_ARTIST, mActiveAudio.getArtist())
                        .putString(MediaMetadata.METADATA_KEY_ALBUM, mActiveAudio.getAlbum())
                        .putString(MediaMetadata.METADATA_KEY_TITLE, mActiveAudio.getTitle())
                        .build()
        );
    }

    private void buildNotification(int playbackState) {
        int notificationAction = android.R.drawable.ic_media_pause;
        PendingIntent playPauseAction = null;

        switch (playbackState) {
            case PlaybackState.STATE_PLAYING:
                notificationAction = android.R.drawable.ic_media_pause;
                playPauseAction = playbackAction(1);
                break;
            case PlaybackState.STATE_PAUSED:
                notificationAction = android.R.drawable.ic_media_play;
                playPauseAction = playbackAction(0);
                break;
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);

        Notification.Builder notification = new Notification.Builder(this)
                .setShowWhen(false)
                .setStyle(new Notification.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setContentText(mActiveAudio.getArtist())
                .setContentTitle(mActiveAudio.getAlbum())
                .setContentInfo(mActiveAudio.getTitle())
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", playPauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification.build());
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);

        switch (actionNumber) {
            case 0:
                playbackAction.setAction(ACTION_PLAY);
                break;
            case 1:
                playbackAction.setAction(ACTION_PAUSE);
                break;
            case 2:
                playbackAction.setAction(ACTION_NEXT);
                break;
            case 3:
                playbackAction.setAction(ACTION_PREVIOUS);
                break;
            case 4:
                playbackAction.setAction(ACTION_STOP);
                break;
        }
        return PendingIntent.getService(this, actionNumber, playbackAction, 0);
    }

    private void handleIncomingActions(Intent playBackAction) {
        if (playBackAction == null || playBackAction.getAction() == null)
            return;

        String actionString = playBackAction.getAction();

        switch (actionString) {
            case ACTION_PLAY:
                mTransportsControls.play();
                break;
            case ACTION_PAUSE:
                mTransportsControls.pause();
                break;
            case ACTION_NEXT:
                mTransportsControls.skipToNext();
                break;
            case ACTION_PREVIOUS:
                mTransportsControls.skipToPrevious();
                break;
            case ACTION_STOP:
                mTransportsControls.stop();
                break;
        }
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }
}
