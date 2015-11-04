package com.debugcomputercompany.sfotipymusic.models;

/**
 * Created by Junior on 30/10/2015.
 */
public class Song {
    private long id;
    private String songName;
    private String songArtist;
    private float songStarts;

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public float getSongStarts() {
        return songStarts;
    }

    public void setSongStarts(float songStarts) {
        this.songStarts = songStarts;
    }
}
