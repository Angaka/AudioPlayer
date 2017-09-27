package com.projects.venom04.audioplayer.controllers;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.util.ArrayList;

/**
 * Created by Venom on 25/09/2017.
 */

public class AudioFileManager {
    private static final String TAG = "AudioFileManager";
    private Context mContext;

    public AudioFileManager(Context context) {
        mContext = context;
    }

    public ArrayList<Audio> loadAllAudios() {
        ArrayList<Audio> audios = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media._ID, // Uri id of the file
                MediaStore.Audio.Media.DATA, // Path of the file
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
        };

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                Audio audio = new Audio(id, path, artist, album, title, duration);
                audios.add(audio);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return audios;
    }
}
