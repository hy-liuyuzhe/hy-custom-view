package com.yuwq.hy_learn_draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.yuwq.hy_learn_draw.roll.RollNumberLayout
import kotlin.random.Random

class RollTextActivity : AppCompatActivity() {
    lateinit var layoutRollNumber: RollNumberLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_text)
        layoutRollNumber = findViewById(R.id.layoutRollNumber)
    }

    fun plus(view: View) {
        val n = layoutRollNumber.lastDesiredValue.toString().toInt()
        val finalN = n + Random.nextInt(10)
        Log.d("liuyuzhe", "finalN: $finalN");
        layoutRollNumber.setValue(finalN)
    }

    fun minus(view: View) {
        val n = layoutRollNumber.lastDesiredValue.toString().toInt()
        layoutRollNumber.setValue(n - Random.nextInt(10))
    }

    var desiredValue = 2000
    fun plus2(view: View) {
        if (desiredValue>0){
            desiredValue = -100
        }else{
            desiredValue = 1000
        }
        layoutRollNumber.setValue(desiredValue)
    }


}