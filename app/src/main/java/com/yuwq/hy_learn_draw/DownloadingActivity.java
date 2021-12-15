package com.yuwq.hy_learn_draw;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuwq.hy_learn_draw.java.DownloadingView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liuyuzhe
 */
public class DownloadingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading);
        DownloadingView downloading = findViewById(R.id.downloading);
        findViewById(R.id.btnSuccess).setOnClickListener(v -> {
            downloading.performAnimation();
        });
        findViewById(R.id.btnDegree).setOnClickListener(v -> {
            downloading.setTestDegree(((EditText)findViewById(R.id.degree)).getText().toString());
            downloading.invalidate();
        });
    }

}
