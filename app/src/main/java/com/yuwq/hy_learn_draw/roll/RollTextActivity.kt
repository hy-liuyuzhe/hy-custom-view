package com.yuwq.hy_learn_draw.roll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuwq.hy_learn_draw.R
import kotlin.random.Random

class RollTextActivity : AppCompatActivity() {

    lateinit var layoutRollNumber: RollNumberLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_text)
        layoutRollNumber = findViewById(R.id.layoutRollNumber)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.adapter = MyAdapter()
    }

    class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return object : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
            ) {
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        }

        override fun getItemCount(): Int = 50

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
        if (desiredValue > 0) {
            desiredValue = -100
        } else {
            desiredValue = 1000
        }
        layoutRollNumber.setValue(desiredValue)
    }


}