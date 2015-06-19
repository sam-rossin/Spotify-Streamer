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
    private int mPosition;
    private int mDuration;

    private TextView artistView;
    private TextView albumView;
    private TextView songView;
    private ImageView imageView;
    private TextView pastView;
    private TextView remainingView;

    public PlaybackActivityFragment() {
    }

    @Override
    public void onStop() {
        deleteMediaPlayer();
        super.onStop();
    }

    private void deleteMediaPlayer(){
        if(mMediaPlayer != null) mMediaPlayer.release();
        mSeekBarHandler.removeCallbacks(updateSeekBar);
        mMediaPlayer = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback, container, false);


        //get extra data
        mTracksData = (TracksData) getActivity().getIntent().
                getSerializableExtra(SongsActivityFragment.SER_EXTRA);
        mPosition = getActivity().getIntent().getIntExtra(SongsActivityFragment.INT_EXTRA, 0);

        //get views
        artistView = (TextView) rootView.findViewById(R.id.text_view_playback_artist);
        albumView = (TextView) rootView.findViewById(R.id.text_view_playback_album);
        songView = (TextView) rootView.findViewById(R.id.text_view_playback_song);
        imageView = (ImageView) rootView.findViewById(R.id.playback_image);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        pastView = (TextView) rootView.findViewById(R.id.text_view_time_past);
        remainingView = (TextView) rootView.findViewById(R.id.text_view_time_remaining);

        //set up seekBar on change listener
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar == mSeekBar && fromUser){
                    if (mMediaPlayer != null){
                        mMediaPlayer.seekTo(progress);
                        setProgressText(progress);
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //set up onClickListeners
        ImageButton previousButton = (ImageButton) rootView.findViewById(R.id.button_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });

        ImageButton nextButton = (ImageButton) rootView.findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
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

        //create runnable to track seek bar progress
        updateSeekBar = new Runnable(){
            @Override
            public void run() {
                int time = mMediaPlayer.getCurrentPosition();
                setProgress(time);
                if(mMediaPlayer.isPlaying()) mSeekBarHandler.postDelayed(this, 100);
            }
        };

        startTrack();

        return rootView;
    }


    //Starts the player
    private void startTrack(){
        //fill views
        ArrayList<String> data = mTracksData.getTrack(mPosition);
        artistView.setText(mTracksData.getArtistName());
        albumView.setText(data.get(0));
        songView.setText(data.get(1));
        Picasso.with(getActivity()).load(data.get(2)).into(imageView);

        //make the media player
        makeMediaPlayer(data.get(3));
    }

    //pause the player
    private void pause(ImageButton playButton){
        mPlaying = false;
        playButton.setImageResource(android.R.drawable.ic_media_play);
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    //set song progress
    private void setProgress(int progress){
        mSeekBar.setProgress(progress);
        setProgressText(progress);
    }

    private void setProgressText(int progress){
        pastView.setText(String.format("%d:%02d", (progress / 1000) / 60, (progress / 1000) % 60));
        remainingView.setText(String.format("%d:%02d", ((mDuration-progress)/1000)/60,
                ((mDuration-progress)/1000)%60));
    }

    //play the song
    private void play(ImageButton playButton){
        mPlaying = true;
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        mMediaPlayer.start();
        mSeekBarHandler.postDelayed(updateSeekBar, 100);
    }

    //go to the next song
    private void next(){
        if(mPosition < mTracksData.size() -1){
            deleteMediaPlayer();
            mPosition++;
            setProgress(0);
            startTrack();
        }else{
            Toast.makeText(getActivity(), R.string.last_track, Toast.LENGTH_SHORT).show();
        }
    }

    //go to previous song
    private void previous(){
        if(mPosition > 0){
            deleteMediaPlayer();
            mPosition--;
            setProgress(0);
            startTrack();
        }else{
            Toast.makeText(getActivity(), R.string.first_track, Toast.LENGTH_SHORT).show();
        }
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
                    mDuration = mp.getDuration();
                    mSeekBar.setMax(mDuration);
                    setProgress(0);
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
