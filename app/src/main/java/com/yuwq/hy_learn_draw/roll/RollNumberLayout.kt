package com.yuwq.hy_learn_draw.roll

import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import com.yuwq.hy_learn_draw.R

/**
 * @author liuyuzhe
 */
class RollNumberLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private lateinit var translateAnimation: Animation
    var lastDesiredValue: Int = 0
    private var nextTextView: TextView
    var currentTextView: TextView
    private val DURATION = 250L

    init {
        inflate(context, R.layout.layout_roll_number, this)
        nextTextView = this.findViewById<TextView>(R.id.nextTextView)
        currentTextView = this.findViewById<TextView>(R.id.currentTextView)

        nextTextView.translationY = height.toFloat()
        setValue(0)
    }

    fun setValue(desiredValue: Int) {
        Log.d("liuyuzhe", "desired: $desiredValue");
        if (this::translateAnimation.isInitialized && !translateAnimation.hasEnded()) {
            Log.d("liuyuzhe", "点的过快: $lastDesiredValue");
            translateAnimation.setAnimationListener(null)
            translateAnimation.cancel()
            translateAnimation.reset()
            currentTextView.text = lastDesiredValue.toString()
            nextTextView.text = lastDesiredValue.toString()
        }

        if (Math.abs(desiredValue - lastDesiredValue) > 10) {
            val fixedContent = if (desiredValue > lastDesiredValue) (desiredValue - 10).toString() else (desiredValue + 10).toString()
            Log.d("liuyuzhe", "fixedContent: $fixedContent");
            currentTextView.text = fixedContent
            nextTextView.text = fixedContent
        }

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
            translateAnimation = TranslateAnimation(0f, 0f, nextTextView.height.toFloat(), 0f)
            translateAnimation.duration = DURATION
            translateAnimation.setAnimationListener(object : AnimatorListenerAdapter(),
                Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    currentTextView.text = (oldValue - 1).toString()
                    currentTextView.translationY = 0f
                    if (oldValue + 1 != desiredValue) {
                        setValue(desiredValue)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            translateAnimation.start()
            nextTextView.animation = translateAnimation
        } else if (oldValue < desiredValue) {
            nextTextView.text = (oldValue + 1).toString()

            currentTextView.animate().translationY(height.toFloat()).setDuration(DURATION).start()
            translateAnimation = TranslateAnimation(0f, 0f, -nextTextView.height.toFloat(), 0f)
            translateAnimation.duration = DURATION
            translateAnimation.setAnimationListener(object : AnimatorListenerAdapter(),
                Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    currentTextView.text = (oldValue + 1).toString()
                    currentTextView.translationY = 0f
                    if (oldValue + 1 != desiredValue) {
                        setValue(desiredValue)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
            translateAnimation.start()
            nextTextView.animation = translateAnimation
        }
    }
}