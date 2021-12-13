package com.yuwq.hy_learn_draw

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity

/**
 * @author liuyuzhe
 */
class DrawMixModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_mix_mode)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val mixModeView = findViewById<MixModeView>(R.id.mixed_mode_draw_view)
        findViewById<View>(R.id.draw_model_select).setOnClickListener {
            Log.d("liuyuzhe", "onCreate: ")
            startActivity(Intent(this, MainActivity::class.java))
        }

        val seekBar = findViewById<SeekBar>(R.id.mixed_mode_seek_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mixModeView.alpha = progress / 100f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}