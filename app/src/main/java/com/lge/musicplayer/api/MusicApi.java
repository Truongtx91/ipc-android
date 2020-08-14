package com.lge.musicplayer.api;

import com.lge.musicplayer.bean.PaySongBean;
import com.lge.musicplayer.bean.RecommandSongListBean;
import com.lge.musicplayer.bean.SearchSongBean;
import com.lge.musicplayer.bean.SongBillListBean;
import com.lge.musicplayer.bean.SongLrcBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MusicApi {

    String MUSIC_BASE_URL = "https://api.mixcloud.com/spartacus/party-time/";
    String SUB_URL = "v1/restserver/ting";


    @GET(SUB_URL)
    Call<PaySongBean> getPaySongData(@QueryMap Map<String, String> params);


    @GET(SUB_URL)
    Call<SongLrcBean> getSongLrcData(@QueryMap Map<String, String> params);

    @GET(SUB_URL)
    Call<SongBillListBean> getSongBillListData(@QueryMap Map<String, String> params);


    @GET(SUB_URL)
    Call<SearchSongBean> getSearchSongData(@QueryMap Map<String, String> params);

    @GET(SUB_URL)
    Call<SongLrcBean> getSingerSongList(@QueryMap Map<String, String> params);

    @GET(SUB_URL)
    Call<RecommandSongListBean> getRecommandSongList(@QueryMap Map<String, String> params);


    @GET(SUB_URL)
    Call<SongLrcBean> downSong(@QueryMap Map<String, String> params);


    @GET(SUB_URL)
    Call<SongLrcBean> getSingerInfo(@QueryMap Map<String, String> params);


}
