package com.projects.venom04.audioplayer.controllers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.projects.venom04.audioplayer.models.pojo.Album;
import com.projects.venom04.audioplayer.models.pojo.Artist;
import com.projects.venom04.audioplayer.models.pojo.Audio;

import java.util.ArrayList;

/**
 * Created by Venom on 25/09/2017.
 */

public class AudioFileManager {
    private static final String TAG = "AudioFileManager";

    private static AudioFileManager mInstance;

    private AudioFileManager() {}

    public static AudioFileManager getInstance() {
        if (mInstance == null) {
            mInstance = new AudioFileManager();
        }
        return mInstance;
    }

    public ArrayList<Artist> loadAllArtists(Context context) {
        ArrayList<Artist> artistsList = new ArrayList<>();

        String[] projection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
        };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long artist_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                    String artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                    int number_of_albums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));

                    Artist artist = new Artist(artist_id, artist_name, number_of_albums, loadAllAlbumsContainingArtistId(context, artist_id));
                    artistsList.add(artist);
                }
            }
            cursor.close();
        }

        return artistsList;
    }

    private ArrayList<Album> loadAllAlbumsContainingArtistId(Context context, long artist_id) {
        ArrayList<Album> albumsList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Artists.Albums.getContentUri("external", artist_id);
        Cursor cursor = context.getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                    String album_artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                    String album_cover = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    String album_num_songs_for_artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS_FOR_ARTIST));

                    Album album = new Album(album_id, album_artist_name, album_name, album_cover, loadAllAudios(context, album_id), album_num_songs_for_artist);
                    albumsList.add(album);
                }
            }
            cursor.close();
        }

        return albumsList;
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
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                    String album_artist_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                    String album_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                    String album_cover = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                    Album album = new Album(album_id, album_artist_name, album_name, album_cover, loadAllAudios(context, album_id));
                    albumsList.add(album);
                }
            }
            cursor.close();
        }

        return albumsList;
    }

    public ArrayList<Audio> loadAllAudios(Context context, String album_id) {
        ArrayList<Audio> audiosList = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        if (album_id != null)
            selection = selection + " AND " + MediaStore.Audio.Albums.ALBUM_ID + " = " + album_id;
        String[] projection = {
                MediaStore.Audio.Media._ID, // Uri id of the file
                MediaStore.Audio.Media.DATA, // Path of the file
                MediaStore.Audio.Media.ARTIST_ID,
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
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artistId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Audio audio = new Audio(id, path, artistId, artist, albumId, album, title, duration);
                    audiosList.add(audio);
                }
            }
            cursor.close();
        }

        return audiosList;
    }
}
