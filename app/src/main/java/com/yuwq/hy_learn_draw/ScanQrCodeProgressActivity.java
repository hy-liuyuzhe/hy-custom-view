package com.yuwq.hy_learn_draw;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author liuyuzhe
 */
public class ScanQrCodeProgressActivity extends AppCompatActivity {

    private MultipleCircleProgress mMultiplyCircleProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_progress);
        mMultiplyCircleProgress = findViewById(R.id.multiplyCircleProgress);
        mMultiplyCircleProgress.setThirdCircleState(MultipleCircleProgress.CircleStateEnum.failed);
    }

    public void reset(View view) {
        mMultiplyCircleProgress.reset();
    }

    public void firstSuccess(View view) {
        mMultiplyCircleProgress.setFirstCircleState(MultipleCircleProgress.CircleStateEnum.success);
    }

    public void secondFail(View view) {
        mMultiplyCircleProgress.setSecondCircleState(MultipleCircleProgress.CircleStateEnum.failed);
    }
}
