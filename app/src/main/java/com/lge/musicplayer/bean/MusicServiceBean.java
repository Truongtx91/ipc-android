package com.lge.musicplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MusicServiceBean implements Parcelable {
    public ArrayList<SongListBean> song_list;
    public int position;
    public int seekProgress;
    public int totalDuration;
    public String  backgroundUrl;


    public MusicServiceBean() {
    }

    public MusicServiceBean(Parcel in) {
        song_list = in.createTypedArrayList(SongListBean.CREATOR);
        position = in.readInt();
        seekProgress = in.readInt();
        totalDuration = in.readInt();
        backgroundUrl = in.readString();
    }

    public static final Creator<MusicServiceBean> CREATOR = new Creator<MusicServiceBean>() {
        @Override
        public MusicServiceBean createFromParcel(Parcel in) {
            return new MusicServiceBean(in);
        }

        @Override
        public MusicServiceBean[] newArray(int size) {
            return new MusicServiceBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(song_list);
        dest.writeInt(position);
        dest.writeInt(seekProgress);
        dest.writeInt(totalDuration);
        dest.writeString(backgroundUrl);
    }
}
