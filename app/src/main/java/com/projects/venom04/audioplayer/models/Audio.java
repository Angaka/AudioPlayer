package com.projects.venom04.audioplayer.models;

/**
 * Created by Venom on 25/09/2017.
 */

public class Audio {

    private long id;
    private String path;
    private String artist;
    private String album;
    private String title;
    private long duration;

    public Audio(long id, String path, String artist, String album, String title, long duration) {
        this.id = id;
        this.path = path;
        this.artist = artist;
        this.album = album;
        this.title = title;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
