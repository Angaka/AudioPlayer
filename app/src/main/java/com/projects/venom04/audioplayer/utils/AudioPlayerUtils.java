package com.projects.venom04.audioplayer.utils;

/**
 * Created by Venom on 25/09/2017.
 */

public class AudioPlayerUtils {

    public static final String PREFS = "prefs";
    public static final String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int REQUEST_CODE = 200;

    public static final String MEDIA = "media";
    public static final String MUSICS = "musics";

    public static boolean isLatin(String value) {
        return value.matches("[ \\w]+");
    }
}
