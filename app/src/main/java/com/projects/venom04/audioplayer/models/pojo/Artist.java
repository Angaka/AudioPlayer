package com.projects.venom04.audioplayer.models.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Venom on 04/10/2017.
 */

public class Artist implements Serializable {

    private long id;
    private String artist;
    private int totalAlbums;
    private ArrayList<Album> albums;

    public Artist(long id, String artist, int totalAlbums, ArrayList<Album> albums) {
        this.id = id;
        this.artist = artist;
        this.totalAlbums = totalAlbums;
        this.albums = albums;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getTotalAlbums() {
        return totalAlbums;
    }

    public void setTotalAlbums(int totalAlbums) {
        this.totalAlbums = totalAlbums;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(ArrayList<Album> albums) {
        this.albums = albums;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", artist='" + artist + '\'' +
                ", totalAlbums=" + totalAlbums +
                ", albums=" + albums +
                '}';
    }
}
