package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

        //fake data
        String[] temp_data = {"Jon doe", "bob", "joseph", "Catfish", "wow", "more crap"
                ,"some stuff", "a guy"};
        List<String> list = new ArrayList<String>(Arrays.asList(temp_data));

        mArtistAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_artist, R.id.textview_artist_name, list);


        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_artist_results);

        //bind results
        listView.setAdapter(mArtistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,
                                    int position, long l){
                String artist = mArtistAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), SongsActivity.class).
                        putExtra(Intent.EXTRA_TEXT, artist);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
