package com.projects.venom04.audioplayer.models.pojo;

import java.util.ArrayList;

/**
 * Created by Venom on 30/09/2017.
 */

public class Album {

    private String id;
    private String artist;
    private String album;
    private String cover;
    private ArrayList<Audio> audios;

    public Album(String id, String artist, String album, String cover, ArrayList<Audio> audios) {
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.cover = cover;
        this.audios = audios;
    }
}
