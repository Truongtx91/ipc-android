package com.lge.musicplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lge.musicplayer.App;
import com.lge.musicplayer.IMusicPlayer;
import com.lge.musicplayer.IMusicPlayerListener;
import com.lge.musicplayer.R;
import com.lge.musicplayer.activity.MusicActivity;
import com.lge.musicplayer.bean.SongListBean;
import com.lge.musicplayer.iml.AgSeekBarChangeListener;
import com.lge.musicplayer.service.MusicService;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BottomMusicPlayer extends FrameLayout {
    @BindView(R.id.play_bar_img)
    ImageView mPlayBarImg;
    @BindView(R.id.play_bar_info)
    TextView mPlayBarInfo;
    @BindView(R.id.play_bar_singer)
    TextView mPlayBarSinger;

    @BindView(R.id.play_list)
    ImageView mPlayList;
    @BindView(R.id.control)
    ImageView mControl;
    @BindView(R.id.play_next)
    ImageView mPlayNext;

    @BindView(R.id.musics_player_seek_bar)
    SeekBar mMusicsPlayerSeekBar;
    @BindView(R.id.nav_play)
    LinearLayout mNavPlay;
    @BindView(R.id.linear)
    LinearLayout mLinear;
    private View mRootView;


    public BottomMusicPlayer(Context context) {
        this(context, null);
    }

    public BottomMusicPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMusicPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private IMusicPlayer mMusicPlayerService;

    public void init(Context context) {

        mRootView = View.inflate(context, R.layout.view_music_bottom, null);
        ButterKnife.bind(this, mRootView);
        addView(mRootView);

    }


    public void registerListener(IMusicPlayer musicPlayerService) {
        try {
            mMusicPlayerService = musicPlayerService;
            if(mMusicsPlayerSeekBar != null){
                mMusicsPlayerSeekBar.setOnSeekBarChangeListener(new AgSeekBarChangeListener(mMusicPlayerService));
            }
            mMusicPlayerService.registerListener(mPlayerListener);
            mHandler.sendEmptyMessage(UPDATE_UI);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterListener() {
        try {
            if (mMusicPlayerService != null) {
                mMusicPlayerService.unregisterListener(mPlayerListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
        mMusicsPlayerSeekBar.setMax(totalDuration);
        mMusicsPlayerSeekBar.setProgress(currentPosition);
    }

    private final int UPDATE_UI = 23;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MusicService.MUSIC_ACTION_SEEK_PLAY:
                    updateSeek(msg);
                    break;
                case MusicService.MUSIC_ACTION_PLAY:
                case UPDATE_UI:
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
            mControl.setImageResource(R.mipmap.playbar_btn_pause);
            Glide.with(getContext()).load(songListBean.pic_big)
                    .into(mPlayBarImg);
            mPlayBarInfo.setText(songListBean.title);
            mPlayBarSinger.setText(songListBean.author);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.play_list, R.id.control, R.id.play_next, R.id.nav_play})
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.play_list:

                    break;
                case R.id.play_next:
                    if (mMusicPlayerService != null) {
                        mMusicPlayerService.action(MusicService.MUSIC_ACTION_NEXT, "");
                    }
                    break;
                case R.id.control:
                    onPayBtnPress();
                    break;
                case R.id.nav_play:
                    Intent intent = new Intent(getContext(), MusicActivity.class);
                    getContext().startActivity(intent);
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
                mControl.setImageResource(R.mipmap.playbar_btn_pause);
                break;
            case MusicService.MUSIC_ACTION_STOP:
                mControl.setImageResource(R.mipmap.playbar_btn_play);
                break;
            case MusicService.MUSIC_ACTION_PAUSE:
                mMusicPlayerService.action(MusicService.MUSIC_ACTION_CONTINUE_PLAY, "");
                mControl.setImageResource(R.mipmap.playbar_btn_play);
                break;
        }
    }
}
