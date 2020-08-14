package com.lge.musicplayer.net;

import com.lge.musicplayer.App;
import com.lge.musicplayer.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class NetCallBack<M> implements Callback<M> {

    public abstract void onSuccess(M m);

    public void onFailure(String msg) {
        ToastUtil.showShortToast(App.app, msg);
    }

    public void onFinish() {
    }



    @Override
    public void onResponse(Call<M> call, Response<M> response) {
        onSuccess(response.body());
        onFinish();
    }

    @Override
    public void onFailure(Call<M> call, Throwable t) {
        onFailure(t.getMessage());
        onFinish();
    }
}
