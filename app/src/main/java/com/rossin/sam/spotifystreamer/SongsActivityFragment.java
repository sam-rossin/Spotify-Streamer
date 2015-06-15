package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class SongsActivityFragment extends Fragment {
    ArrayAdapter<String> mSongsAdapter;

    public SongsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);
        //fake data
        String[] temp_data = new String[10];
        String name = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        for(int i=0; i<10; i++) temp_data[i] = name;


        List<String> song = new ArrayList<String>(Arrays.asList(temp_data));

        mSongsAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_song, R.id.text_view_song, song);

        //get reference to listview
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_songs_results);

        listView.setAdapter(mSongsAdapter);

        return rootView;
    }
}
