package com.rossin.sam.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaybackActivityFragment extends Fragment {
    private TracksData mTracksData;
    private MediaPlayer mMediaPlayer;
    private boolean mPlaying = true;
    private Handler mSeekBarHandler = new Handler();
    private SeekBar mSeekBar;
    private Runnable updateSeekBar;

    public PlaybackActivityFragment() {
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mMediaPlayer != null) mMediaPlayer.release();
        mSeekBarHandler.removeCallbacks(updateSeekBar);
        mMediaPlayer = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback, container, false);


        //get extra
        //data form [artistId, artistName, albumName, songName, albumImageURL, streamURL]
        mTracksData = (TracksData) getActivity().getIntent().
                getSerializableExtra(SongsActivityFragment.SER_EXTRA);
        int position = getActivity().getIntent().getIntExtra(SongsActivityFragment.INT_EXTRA, 0);

        ArrayList<String> data = mTracksData.getTrack(position);

        //fill out fields of view
        TextView artistView = (TextView) rootView.findViewById(R.id.text_view_playback_artist);
        artistView.setText(mTracksData.getArtistName());

        TextView albumView = (TextView) rootView.findViewById(R.id.text_view_playback_album);
        albumView.setText(data.get(0));

        TextView songView = (TextView) rootView.findViewById(R.id.text_view_playback_song);
        songView.setText(data.get(1));

        ImageView imageView = (ImageView) rootView.findViewById(R.id.playback_image);
        Picasso.with(getActivity()).load(data.get(2)).into(imageView);

        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        //set up onClickListeners
        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.button_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final ImageButton playButton = (ImageButton) rootView.findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaying) pause(playButton);
                else play(playButton);
            }
        });

        //create runnable
        updateSeekBar = new Runnable(){
            @Override
            public void run() {
                int time = mMediaPlayer.getCurrentPosition();
                mSeekBar.setProgress(time);
                if(mMediaPlayer.isPlaying()) mSeekBarHandler.postDelayed(this, 100);
            }
        };

        //make the media player
        makeMediaPlayer(data.get(3));

        return rootView;
    }

    //pause the player
    private void pause(ImageButton playButton){
        if(mMediaPlayer.isPlaying()){
            playButton.setImageResource(android.R.drawable.ic_media_play);
            mPlaying = false;
            mMediaPlayer.pause();
        }
    }

    //play the song
    private void play(ImageButton playButton){
        mPlaying = true;
        if(mMediaPlayer.isPlaying()){
            return;
        }
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        mMediaPlayer.start();
        mSeekBarHandler.postDelayed(updateSeekBar, 100);
    }

    //create the media player
    private void makeMediaPlayer(String url){
        //set up media playback
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == mMediaPlayer) {
                    mSeekBar.setMax(mp.getDuration());
                    mp.start();
                    mSeekBarHandler.postDelayed(updateSeekBar, 100);
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        }catch(IOException e){
            Toast.makeText(getActivity(), R.string.preview_not_found,
                    Toast.LENGTH_SHORT).show();
        }

    }


}
