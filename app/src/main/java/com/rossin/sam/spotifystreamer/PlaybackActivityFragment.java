package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaybackActivityFragment extends Fragment {

    public PlaybackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback, container, false);


        //get extra
        //data form [artistId, artistName, albumName, songName, albumImageURL, streamURL]
        ArrayList<String> data = getActivity().getIntent().
                getStringArrayListExtra(Intent.EXTRA_TEXT);

        //fill out fields of view
        TextView artistView = (TextView) rootView.findViewById(R.id.text_view_playback_artist);
        artistView.setText(data.get(1));

        TextView albumView = (TextView) rootView.findViewById(R.id.text_view_playback_album);
        albumView.setText(data.get(2));

        TextView songView = (TextView) rootView.findViewById(R.id.text_view_playback_song);
        songView.setText(data.get(3));

        ImageView imageView = (ImageView) rootView.findViewById(R.id.playback_image);
        Picasso.with(getActivity()).load(data.get(4)).into(imageView);


        return rootView;
    }
}
