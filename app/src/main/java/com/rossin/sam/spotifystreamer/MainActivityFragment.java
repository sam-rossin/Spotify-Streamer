package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ArrayAdapter<String> mArtistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //create the adapter to fill list view

        mArtistAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_artist, R.id.textview_artist_name, new ArrayList<String>());


        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_results);

        //bind results
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                String artist = mArtistAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SongsActivity.class).
                        putExtra(Intent.EXTRA_TEXT, artist);
                startActivity(intent);
            }
        });

        //bind edit text thing
        EditText editText = (EditText) rootView.findViewById(R.id.search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    CharSequence word = v.getText();
                    new FetchArtistTask().execute((String) word);
                    handled = true;
                }return handled;
            }
        });
        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String,Void,List<String>> {
        protected List<String> doInBackground(String... params){
            if(params.length==0) return null;
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);
            List<Artist> artists = results.artists.items;
            List<String> names = new ArrayList<String>();
            for(Artist artist: artists) names.add(artist.name);
            return names;
        }

        @Override
        protected void onPostExecute(List<String> names) {
            if(names != null){
                mArtistAdapter.clear();
                mArtistAdapter.addAll(names);
            }
        }
    }
}
