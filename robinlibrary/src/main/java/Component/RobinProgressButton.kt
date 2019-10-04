package Component

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.widget.Button
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import br.com.core.robinlibrary.R


class RobinProgressButton(context: Context) : Button(context) {
    private enum class State {
        PROGRESS, IDLE
    }

    public class LoadingButton : AppCompatButton {
        constructor(context: Context) : super(context) {
            init(context)
        }

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            init(context)
        }

        constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        ) {
            init(context)
        }

        constructor(
            context: Context,
            attrs: AttributeSet,
            defStyleAttr: Int,
            defStyleRes: Int
        ) : super(context, attrs, defStyleAttr) {
            init(context)
        }

        private fun init(context: Context) {
            val mGradientDrawable = ContextCompat.getDrawable(context, R.drawable.shape_default) as GradientDrawable
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                background = mGradientDrawable
            }else{
                this.setBackgroundResource(R.drawable.shape_default)
            }

            val cornerAnimation = ObjectAnimator.ofFloat(mGradientDrawable, "cornerRadius", 0.0f, 100.0f)

            val widthAnimation = ValueAnimator.ofInt(100,100)
            widthAnimation.addUpdateListener {
                val value = it.animatedValue as Int
                val layoutParams = layoutParams
                layoutParams.width = value
                setLayoutParams(layoutParams)
            }

        }


        /**
         * Method called to start the animation. Morphs in to a ball and then starts a loading spinner.
         */
        fun startAnimation() {
            if (mState !== State.IDLE) {
                return
            }

            val initialWidth = width
            val initialHeight = height

            val initialCornerRadius = 0
            val finalCornerRadius = 1000

            mState = State.PROGRESS
            mIsMorphingInProgress = true
            this.text = null
            isClickable = false

            val toWidth = 300 //some random value...

            val cornerAnimation = ObjectAnimator.ofFloat(
                mGradientDrawable,
                "cornerRadius",
                0.0f,
                100.0f
            )

            val widthAnimation = ValueAnimator.ofInt(initialWidth, toWidth)
            widthAnimation.addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams = layoutParams
                layoutParams.width = `val`
                setLayoutParams(layoutParams)
            }

            val heightAnimation = ValueAnimator.ofInt(initialHeight, toWidth)
            heightAnimation.addUpdateListener { valueAnimator ->
                val `val` = valueAnimator.animatedValue as Int
                val layoutParams = layoutParams
                layoutParams.height = `val`
                setLayoutParams(layoutParams)
            }

            mMorphingAnimatorSet = AnimatorSet()
            mMorphingAnimatorSet.setDuration(300)
            mMorphingAnimatorSet.playTogether(cornerAnimation, widthAnimation, heightAnimation)
            mMorphingAnimatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mIsMorphingInProgress = false
                }
            })
            mMorphingAnimatorSet.start()
        }


    }

}