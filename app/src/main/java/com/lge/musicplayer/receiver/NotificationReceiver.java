package com.lge.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.lge.musicplayer.App;
import com.lge.musicplayer.service.MusicService;

import static com.lge.musicplayer.service.MusicService.MUSIC_ACTION_PLAY;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_MUSIC_PLAY = "com.lge.www.action.music.play";
    public static final String ACTION_MUSIC_NEXT = "com.lge.www.action.music.next";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (App.app.getMusicPlayerService() == null) {
            return;
        }
        String action = intent.getAction();
        try {
            switch (action) {
                case ACTION_MUSIC_PLAY:
                    if (MusicService.MUSIC_CURRENT_ACTION == MUSIC_ACTION_PLAY) {
                        App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_PAUSE, "");
                    } else {
                        App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_CONTINUE_PLAY, "");
                    }
                    break;
                case ACTION_MUSIC_NEXT:
                    App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_NEXT, "");
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
