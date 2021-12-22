package com.yuwq.hy_learn_draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yuwq.hy_learn_draw.roll.RollNumberLayout

class RollTextActivity : AppCompatActivity() {
    lateinit var layoutRollNumber: RollNumberLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_text)
        layoutRollNumber = findViewById(R.id.layoutRollNumber)
    }

    fun plus(view: View) {
        val n = layoutRollNumber.currentTextView.text.toString().toInt()
        layoutRollNumber.setValue(n + 10)
    }

    fun minus(view: View) {
        val n = layoutRollNumber.currentTextView.text.toString().toInt()
        layoutRollNumber.setValue(n - 10)
    }


}