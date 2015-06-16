package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.http.QueryMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class SongsActivityFragment extends Fragment {
    SongsAdapter mSongsAdapter;
    List<Track> trackList;

    public SongsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        //get the tracks
        String name = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        mSongsAdapter = new SongsAdapter(getActivity(),
                R.layout.list_item_song, R.id.text_view_song, R.id.text_view_album,
                R.id.song_icon);

        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_songs_results);

        listView.setAdapter(mSongsAdapter);

        new FetchTracksTask().execute(name);

        return rootView;
    }

    public class FetchTracksTask extends AsyncTask<String,Void,Tracks> {
        protected Tracks doInBackground(String... params){
            if(params.length==0) return null;

            //do the fetching
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("country", getString(R.string.us_country_code));
            return spotify.getArtistTopTrack(params[0], options);
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            trackList = tracks.tracks;
            if(trackList != null && !trackList.isEmpty()){
                mSongsAdapter.clear();
                mSongsAdapter.add(trackList);
            }else{
                Toast.makeText(getActivity(), R.string.tracks_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
