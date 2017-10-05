package com.projects.venom04.audioplayer.models.pojo;

import java.io.Serializable;

/**
 * Created by Venom on 25/09/2017.
 */

public class Audio implements Serializable {

    private long id;
    private String path;
    private String artistId;
    private String artist;
    private String albumId;
    private String album;
    private String title;
    private long duration;

    public Audio(long id, String path, String artistId, String artist, String albumId, String album, String title, long duration) {
        this.id = id;
        this.path = path;
        this.artistId = artistId;
        this.artist = artist;
        this.albumId = albumId;
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

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
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

    @Override
    public String toString() {
        return "Audio{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }
}
