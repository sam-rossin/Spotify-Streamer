package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    TracksData mTracksData;
    List<String> mArtistData;
    public static final String INT_EXTRA = "com.rossin.sam.spotifystreamer.extra.int";
    public static final String SER_EXTRA = "com.rossin.sam.spotifystreamer.extra.ser";

    public SongsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);

        //retrieve extra
        mArtistData  = getActivity().getIntent().
                getStringArrayListExtra(Intent.EXTRA_TEXT);

        //set up listView adapter
        mSongsAdapter = new SongsAdapter(getActivity(),
                R.layout.list_item_song, R.id.text_view_song, R.id.text_view_album,
                R.id.song_icon);

        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_songs_results);

        //set on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                intent.putExtra(INT_EXTRA, position);
                intent.putExtra(SER_EXTRA, mTracksData);
                startActivity(intent);
            }
        });

        listView.setAdapter(mSongsAdapter);

        new FetchTracksTask().execute(mArtistData.get(0));

        return rootView;
    }

    public class FetchTracksTask extends AsyncTask<String,Void,Tracks> {
        protected Tracks doInBackground(String... params){
            if(params.length==0) return null;

            //do the fetching
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put("country", getString(R.string.us_country_code));
            return spotify.getArtistTopTrack(params[0], options);
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            List<Track> trackList = tracks.tracks;
            if(trackList != null && !trackList.isEmpty()){
                mSongsAdapter.clear();
                mSongsAdapter.add(trackList);
            }else{
                Toast.makeText(getActivity(), R.string.tracks_not_found,
                        Toast.LENGTH_SHORT).show();
            }if(trackList != null){
                mTracksData = new TracksData(trackList, mArtistData.get(1));
            }
        }
    }
}
