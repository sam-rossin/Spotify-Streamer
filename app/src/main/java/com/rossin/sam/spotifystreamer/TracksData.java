package com.rossin.sam.spotifystreamer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Sam on 6/18/2015.
 * A serializable data type to pass track data to the playback activity.
 */
public class TracksData implements Serializable {
    static final long serialVersionUID = -687991492884005033L;

    private String artistName;
    private ArrayList<ArrayList<String>> tracks;
    private int size;

    public TracksData(List<Track> trackList, String artistName){
        size = trackList.size();
        this.artistName = artistName;
        tracks = new ArrayList<>();
        for(int i = 0; i < trackList.size(); i++){
            //albumName, songName, albumImageURL, trackStreamURL
            ArrayList<String> temp = new ArrayList<>();
            Track song = trackList.get(i);
            temp.add(song.album.name);
            temp.add(song.name);
            if(song.album.images != null){
                temp.add(song.album.images.get(0).url);
            }temp.add(song.preview_url);

            tracks.add(temp);
        }
    }


    public String getArtistName(){
        return artistName;
    }

    public ArrayList<String> getTrack(int position){
        return tracks.get(position);
    }

    public int size(){
        return size;
    }
}
