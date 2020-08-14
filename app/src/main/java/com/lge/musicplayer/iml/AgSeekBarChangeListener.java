package com.lge.musicplayer.iml;

import android.os.RemoteException;
import android.widget.SeekBar;

import com.lge.musicplayer.App;
import com.lge.musicplayer.IMusicPlayer;
import com.lge.musicplayer.service.MusicService;


public class AgSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private IMusicPlayer mMusicPlayerService;

    public AgSeekBarChangeListener(IMusicPlayer musicPlayerService) {
        mMusicPlayerService = musicPlayerService;
    }

    /******************************************************************/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            seekBar.setProgress(seekBar.getProgress());
            if (mMusicPlayerService!=null) {
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_SEEK_PLAY, String.valueOf(seekBar.getProgress()));
            }else{
                mMusicPlayerService = App.app.getMusicPlayerService();
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_SEEK_PLAY, String.valueOf(seekBar.getProgress()));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
