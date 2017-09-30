package com.projects.venom04.audioplayer.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Venom on 25/09/2017.
 */

public class AudioPlayerUtils {

    public static final String PREFS = "prefs";
    public static final String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int REQUEST_CODE = 200;

    public static final String MEDIA = "media";
    public static final String AUDIOS = "audios";

    public static final String SERVICE_STATE = "service_state";

    public static final String PLAY_NEW_AUDIO = "com.projects.venom04.audioplayer.PlayNewAudio";

    public static boolean isLatin(String value) {
        return value.matches("[ \\w]+");
    }

    public static String convertMsToHourFormat(long ms) {
        return String.format(Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(ms),
                TimeUnit.MILLISECONDS.toSeconds(ms) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
    }
}
