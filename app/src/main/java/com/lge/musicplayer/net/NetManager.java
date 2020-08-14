package com.lge.musicplayer.net;

import com.lge.musicplayer.BuildConfig;
import com.lge.musicplayer.api.MusicApi;
import com.lge.musicplayer.bean.PaySongBean;
import com.lge.musicplayer.bean.RecommandSongListBean;
import com.lge.musicplayer.bean.SearchSongBean;
import com.lge.musicplayer.bean.SongBillListBean;
import com.lge.musicplayer.net.log.AGLogInterceptor;

import java.util.Map;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetManager {

    private Retrofit mRetrofit;
    private MusicApi mMusicApi;
    private OkHttpClient mClient;

    /**
     * 播放音乐
     *
     * @param songid
     * @param callBack
     */
    public void getPaySongData(String songid, NetCallBack<PaySongBean> callBack) {
        Map<String, String> params = NetHelper.getMusicApiCommonParams("baidu.ting.song.play");
        params.put("songid", songid);
        createMusicApi()
                .getPaySongData(params)
                .enqueue(callBack);
    }


    /**
     * 推荐列表
     * @param songid
     * @param num
     * @param callBack
     */
    public void getRecommandSongList(String songid, String num, NetCallBack<RecommandSongListBean> callBack) {
        Map<String, String> params = NetHelper.getMusicApiCommonParams("baidu.ting.song.getRecommandSongList");
        params.put("songid", songid);
        params.put("num", num);
        createMusicApi()
                .getRecommandSongList(params)
                .enqueue(callBack);
    }

    /**
     * 搜索
     * @param query
     * @param callBack
     */
    public void getSearchSongData(String query, NetCallBack<SearchSongBean> callBack) {
        Map<String, String> params = NetHelper.getMusicApiCommonParams("baidu.ting.search.catalogSug");
        params.put("query", query);
        createMusicApi()
                .getSearchSongData(params)
                .enqueue(callBack);
    }

    /**
     * @param type 1-新歌榜,2-热歌榜,11-摇滚榜,12-爵士,16-流行,21-欧美金曲榜,22-经典老歌榜,23-情歌对唱榜,24-影视金曲榜,25-网络歌曲榜
     * @param size  返回条目数量
     * @param offset 获取偏移
     * @param callBack
     */
    public void getSongBillListData(int type, int size, int offset, NetCallBack<SongBillListBean> callBack) {
        Map<String, String> params = NetHelper.getMusicApiCommonParams("baidu.ting.billboard.billList");
        params.put("type", String.valueOf(type));
        params.put("size", String.valueOf(size));
        params.put("offset", String.valueOf(offset));
        createMusicApi()
                .getSongBillListData(params)
                .enqueue(callBack);
    }


    private static NetManager instance;
    private NetManager() {}
    public static NetManager getInstance() {
        if (instance == null) {
            synchronized (NetManager.class) {
                if (instance == null) {
                    instance = new NetManager();
                }
            }
        }
        return instance;
    }
    MusicApi createMusicApi() {

        if (mClient == null) {
            OkHttpClient.Builder   builder = new OkHttpClient().newBuilder();
            if(BuildConfig.DEBUG){
                builder.addNetworkInterceptor(new AGLogInterceptor());
            }
            mClient = builder.build();
        }


        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(MusicApi.MUSIC_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mClient)
                    .build();
        }

        if (mMusicApi == null) {
            mMusicApi = mRetrofit.create(MusicApi.class);
        }
        return mMusicApi;


    }
}
