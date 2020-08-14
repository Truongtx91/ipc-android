package com.lge.musicplayer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lge.musicplayer.App;
import com.lge.musicplayer.IMusicPlayer;
import com.lge.musicplayer.IMusicPlayerListener;
import com.lge.musicplayer.R;
import com.lge.musicplayer.bean.SongListBean;
import com.lge.musicplayer.iml.AgSeekBarChangeListener;
import com.lge.musicplayer.service.MusicService;
import com.lge.musicplayer.ui.PlayerDiscView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicActivity extends AppCompatActivity  {

    @BindView(R.id.musics_player_disc_view)
    PlayerDiscView mPlayerDiscView;
    @BindView(R.id.musics_player_background)
    ImageView mMusicsPlayerBackground;
    @BindView(R.id.player_disc)
    ImageView mPlayerDisc;
    @BindView(R.id.player_disc_image)
    ImageView mPlayerDiscImage;
    @BindView(R.id.player_disc_container)
    RelativeLayout mPlayerDiscContainer;
    @BindView(R.id.player_needle)
    ImageView mPlayerNeedle;
    @BindView(R.id.musics_player_name)
    TextView mMusicsPlayerName;
    @BindView(R.id.musics_player_songer_name)
    TextView mMusicsPlayerSongerName;
    @BindView(R.id.musics_player_current_time)
    TextView mMusicsPlayerCurrentTime;

    @BindView(R.id.musics_player_total_time)
    TextView mMusicsPlayerTotalTime;
    @BindView(R.id.musics_player_progress_container)
    LinearLayout mMusicsPlayerProgressContainer;
    @BindView(R.id.musics_player_play_prev_btn)
    ImageButton mMusicsPlayerPlayPrevBtn;
    @BindView(R.id.musics_player_play_ctrl_btn)
    ImageButton mMusicsPlayerPlayCtrlBtn;
    @BindView(R.id.musics_player_play_next_btn)
    ImageButton mMusicsPlayerPlayNextBtn;
    @BindView(R.id.musics_player_loading_view)
    View mMusicsPlayerLoadingView;
    @BindView(R.id.musics_player_container)
    RelativeLayout mMusicsPlayerContainer;
    private IMusicPlayer mMusicPlayerService;


    @BindView(R.id.musics_player_seekbar)
    SeekBar mMusicsPlayerSeekBar;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("mm:ss");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);
        setTitle("");
        mMusicPlayerService = App.app.getMusicPlayerService();
        try {
            mMusicPlayerService.registerListener(mPlayerListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mMusicsPlayerSeekBar.setOnSeekBarChangeListener(new AgSeekBarChangeListener(mMusicPlayerService));
        onPlay();

    }

    private void setTitleAndBackground(String title, String backgroundUrl) {
        setTitle(title);
        Glide.with(App.app).load(backgroundUrl)
                .bitmapTransform(new BlurTransformation(App.app))
                .into(mMusicsPlayerBackground);
    }


    IMusicPlayerListener mPlayerListener = new IMusicPlayerListener.Stub() {
        @Override
        public void action(int action, Message msg) throws RemoteException {
            mHandler.sendMessage(msg);
        }
    };


    private void updateSeek(Message msg) {
        int currentPosition = msg.arg1;
        int totalDuration = msg.arg2;
        mMusicsPlayerTotalTime.setText(mFormatter.format(totalDuration));
        mMusicsPlayerCurrentTime.setText(mFormatter.format(currentPosition));
        mMusicsPlayerSeekBar.setMax(totalDuration);
        mMusicsPlayerSeekBar.setProgress(currentPosition);
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MusicService.MUSIC_ACTION_SEEK_PLAY:
                    updateSeek(msg);
                    break;
                case MusicService.MUSIC_ACTION_PLAY:
                    onPlay();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private void onPlay() {
        mMusicPlayerService = App.app.getMusicPlayerService();
        try {
            SongListBean songListBean =
                    (SongListBean) mMusicPlayerService.getCurrentSongInfo().obj;
            if (songListBean == null) {
                return;
            }
            setTitleAndBackground(songListBean.title,
                    songListBean.pic_big);
            mMusicsPlayerPlayCtrlBtn.setImageResource(R.drawable.btn_pause_pressed);
            mPlayerDiscView.loadAlbumCover(songListBean.pic_big);
            mPlayerDiscView.startPlay();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            mMusicPlayerService.unregisterListener(mPlayerListener);
            mPlayerListener = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /******************************************************************/

    @OnClick({R.id.musics_player_play_prev_btn, R.id.musics_player_play_ctrl_btn, R.id.musics_player_play_next_btn})
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.musics_player_play_prev_btn:
                    mMusicPlayerService.action(MusicService.MUSIC_ACTION_PREVIOUS, "");

                    break;
                case R.id.musics_player_play_ctrl_btn:

                    onPayBtnPress();

                    break;
                case R.id.musics_player_play_next_btn:

                    mMusicPlayerService.action(MusicService.MUSIC_ACTION_NEXT, "");

                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void onPayBtnPress() throws RemoteException {
        switch (MusicService.MUSIC_CURRENT_ACTION) {
            case MusicService.MUSIC_ACTION_PLAY:
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_PAUSE, "");
                mMusicsPlayerPlayCtrlBtn.setImageResource(R.drawable.btn_play_selector);
                break;
            case MusicService.MUSIC_ACTION_STOP:
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_PLAY, "");
                mMusicsPlayerPlayCtrlBtn.setImageResource(R.drawable.btn_pause_pressed);
                break;
            case MusicService.MUSIC_ACTION_PAUSE:
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_CONTINUE_PLAY, "");
                mMusicsPlayerPlayCtrlBtn.setImageResource(R.drawable.btn_pause_pressed);
                break;
        }
    }


}
