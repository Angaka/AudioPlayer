package com.projects.venom04.audioplayer.controllers;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.util.ArrayList;

/**
 * Created by Venom on 25/09/2017.
 */

public class AudioFileManager {
    private static final String TAG = "AudioFileManager";

    private static AudioFileManager mInstance;

    public AudioFileManager() {}

    public static AudioFileManager getInstance() {
        if (mInstance == null) {
            mInstance = new AudioFileManager();
        }
        return mInstance;
    }

    public ArrayList<Album> loadAllAlbums(Context context) {
        ArrayList<Album> albumsList = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Media.ALBUM);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String cover = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                Album album = new Album(id, artist, albumName, cover, loadAllAudios(context, id));
                albumsList.add(album);
            }
        }

        return albumsList;
    }

    public ArrayList<Audio> loadAllAudios(Context context, String albumId) {
        ArrayList<Audio> audiosList = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        if (albumId != null)
            selection = selection + " AND " + MediaStore.Audio.Albums.ALBUM_ID + " = " + albumId;
        String[] projection = {
                MediaStore.Audio.Media._ID, // Uri id of the file
                MediaStore.Audio.Media.DATA, // Path of the file
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
        };

        Cursor cursor = context.getContentResolver().query(
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
                audiosList.add(audio);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return audiosList;
    }
}
