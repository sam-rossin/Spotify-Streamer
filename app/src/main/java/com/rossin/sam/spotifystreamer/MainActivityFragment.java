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
import android.widget.Toast;

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
    List<Artist> artists;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //create the adapter to fill list view
        //we use a SimpleAdapter

        mArtistAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_artist, R.id.textview_artist_name, new ArrayList<String>());
        mArtistAdapter.setNotifyOnChange(false);


        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_results);

        //bind results
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                String artist_id = artists.get(position).id;
                Intent intent = new Intent(getActivity(), SongsActivity.class).
                        putExtra(Intent.EXTRA_TEXT, artist_id);
                startActivity(intent);
            }
        });

        //bind edit text thing
        EditText editText = (EditText) rootView.findViewById(R.id.search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    CharSequence word = v.getText();
                    new FetchArtistTask().execute(word.toString());
                    handled = true;
                }return handled;
            }
        });
        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String,Void,List<Artist>> {
        protected List<Artist> doInBackground(String... params){
            if(params.length==0) return null;

            //do the fetching
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager results = spotify.searchArtists(params[0]);

            //parse the result
            return results.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> new_artists) {
            artists = new_artists;
            if(artists != null && !artists.isEmpty()){
                mArtistAdapter.clear();
                for(Artist artist: artists){
                    mArtistAdapter.add(artist.name);
                }mArtistAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(), R.string.artist_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
