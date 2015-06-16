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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Sam on 6/16/2015.
 */
public class ArtistAdapter extends BaseAdapter implements ListAdapter{
    public static final String LOG_TAG = ArtistAdapter.class.getSimpleName();
    private List<Artist> artists;
    private Context context;
    private LayoutInflater inflater;
    private int resourceId;
    private int textId;
    private int imageId;

    public ArtistAdapter(Context context, int resourceId, int textId, int imageId){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        artists = new ArrayList<Artist>();
        this.context = context;
        this.textId = textId;
        this.imageId = imageId;
        this.resourceId = resourceId;
    }

    public void clear(){
        artists.clear();
    }

    public void add(List<Artist> newArtists){
        for(Artist artist: newArtists){
            artists.add(artist);
        }notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView name;
        ImageView thumbnail;

        //inflate view if needed
        if (convertView == null){
            view = inflater.inflate(resourceId, parent, false);
        }else{
            view = convertView;
        }

        //try to find the necessary fields
        try{
            name = (TextView) view.findViewById(textId);
            thumbnail = (ImageView) view.findViewById(imageId);
        }catch (ClassCastException e){
            Log.e(LOG_TAG, "List element has improper format.");
            throw new IllegalStateException(
                    "List element has improper format.", e);
        }

        Artist artist = artists.get(position);
        name.setText(artist.name);
        if(!artist.images.isEmpty()) {
            Picasso.with(context).load(artist.images.get(1).url).into(thumbnail);
        }

        return view;
    }
}
