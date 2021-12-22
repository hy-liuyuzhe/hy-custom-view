package com.yuwq.hy_learn_draw.roll

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.yuwq.hy_learn_draw.R

/**
 * @author liuyuzhe
 */
class RollNumberLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var lastDesiredValue: Int = 0
    var nextTextView: TextView
    var currentTextView: TextView
    val DURATION = 200L

    init {
        inflate(context, R.layout.layout_roll_number, this)
        nextTextView = this.findViewById<TextView>(R.id.nextTextView)
        currentTextView = this.findViewById<TextView>(R.id.currentTextView)

        nextTextView.translationY = height.toFloat()
        setValue(100)
    }

    fun setValue(desiredValue: Int) {
        this.lastDesiredValue = desiredValue
        if (currentTextView.text.isNullOrEmpty()) {
            currentTextView.text = desiredValue.toString()
        }

        val oldValue = currentTextView.text.toString().toInt()
        if (oldValue > desiredValue) {
            nextTextView.text = (oldValue - 1).toString()
            //current is minus to hide
            currentTextView.animate().translationY(-height.toFloat()).setDuration(DURATION).start()
            //next is plus to run the animation
            nextTextView.translationY = nextTextView.height.toFloat()
            nextTextView.animate().translationY(0f).setDuration(DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        currentTextView.text = (oldValue - 1).toString()
                        currentTextView.translationY = 0f
                        if (oldValue + 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }
                }).start()
        } else if (oldValue < desiredValue) {
            nextTextView.text = (oldValue + 1).toString()

            currentTextView.animate().translationY(height.toFloat()).setDuration(DURATION).start()

            nextTextView.translationY = -nextTextView.height.toFloat()
            nextTextView.animate().translationY(0f).setDuration(DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        currentTextView.text = (oldValue + 1).toString()
                        currentTextView.translationY = 0f
                        if (oldValue + 1 != desiredValue) {
                            setValue(desiredValue)
                        }
                    }
                }).start()
        }
    }
}