package com.rossin.sam.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Sam on 6/16/2015.
 */
public class SongsAdapter extends BaseAdapter implements ListAdapter {
    public static final String LOG_TAG = SongsAdapter.class.getSimpleName();
    private List<Track> tracks;
    private Context context;
    private LayoutInflater inflater;
    private int resourceId;
    private int songId;
    private int albumId;
    private int imageId;

    public SongsAdapter(Context context, int resourceId, int songId, int albumId, int imageId) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tracks = new ArrayList<Track>();
        this.context = context;
        this.songId = songId;
        this.albumId = albumId;
        this.imageId = imageId;
        this.resourceId = resourceId;
    }

    public void clear() {
        tracks.clear();
    }

    public void add(List<Track> newTracks) {
        for (Track track : newTracks) {
            tracks.add(track);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView song;
        TextView album;
        ImageView thumbnail;

        //inflate view if needed
        if (convertView == null) {
            view = inflater.inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        //try to find the necessary fields
        try {
            song = (TextView) view.findViewById(songId);
            album = (TextView) view.findViewById(albumId);
            thumbnail = (ImageView) view.findViewById(imageId);
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "List element has improper format.");
            throw new IllegalStateException(
                    "List element has improper format.", e);
        }

        Track track = tracks.get(position);
        if (track.name != null) song.setText(track.name);
        if (track.album != null) {
            album.setText(track.album.name);
            if (track.album.images != null && !track.album.images.isEmpty()) {
                Picasso.with(context).load(track.album.images.get(1).url).into(thumbnail);
            }
        }
        return view;
    }
}
