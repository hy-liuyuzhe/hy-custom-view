package com.yuwq.hy_learn_draw;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * @author liuyuzhe
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
