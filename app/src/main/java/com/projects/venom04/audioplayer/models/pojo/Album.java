package com.projects.venom04.audioplayer.models.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Venom on 30/09/2017.
 */

public class Album implements Serializable {

    private String id;
    private String artist;
    private String album;
    private String cover;
    private ArrayList<Audio> audios;
    private String numSongsForArtist;

    public Album(String id, String artist, String album, String cover, ArrayList<Audio> audios) {
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.cover = cover;
        this.audios = audios;
    }

    public Album(String id, String artist, String album, String cover, ArrayList<Audio> audios, String numSongsForArtist) {
        this.id = id;
        this.artist = artist;
        this.album = album;
        this.cover = cover;
        this.audios = audios;
        this.numSongsForArtist = numSongsForArtist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ArrayList<Audio> getAudios() {
        return audios;
    }

    public void setAudios(ArrayList<Audio> audios) {
        this.audios = audios;
    }

    public String getNumSongsForArtist() {
        return numSongsForArtist;
    }

    public void setNumSongsForArtist(String numSongsForArtist) {
        this.numSongsForArtist = numSongsForArtist;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", cover='" + cover + '\'' +
                ", audios=" + audios +
                '}';
    }
}
