package Component.CircularProgressButton.utils

import Component.CircularProgressButton.animatedDrawables.ProgressType
import Component.CircularProgressButton.customViews.RobinProgressButton
import Util.getBitmapFromVectorDrawable
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.*
import android.os.Build
import android.os.Handler
import android.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import br.com.core.robinlibrary.R

internal fun parseGradientDrawable(drawable: Drawable): GradientDrawable =
    when (drawable) {
        is GradientDrawable -> drawable
        is ColorDrawable -> GradientDrawable().apply { setColor(drawable.color) }
        is InsetDrawable -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                drawable.drawable?.let { innerDrawable ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        when (innerDrawable) {
                            is RippleDrawable -> {
                                parseGradientDrawable(innerDrawable.getDrawable(0))
                            }
                            else -> parseGradientDrawable(innerDrawable)
                        }
                    } else {
                        parseGradientDrawable(innerDrawable)
                    }
                }
                    ?: throw RuntimeException("Error reading background... Use a shape or a color in xml!")
            } else {
                throw RuntimeException("Error reading background... Use a shape or a color in xml!")
            }
        }
        is StateListDrawable -> {
            if (drawable.current is GradientDrawable) {
                drawable.current as GradientDrawable
            } else {
                throw RuntimeException("Error reading background... Use a shape or a color in xml!")
            }
        }
        is LayerDrawable -> {
            parseGradientDrawable(drawable.getDrawable(0))
        }
        else -> throw RuntimeException("Error reading background... Use a shape or a color in xml!")
    }

internal fun Context.addLifecycleObserver(observer: LifecycleObserver) {
    when {
        this is LifecycleOwner -> this.lifecycle.addObserver(observer)
        this is ContextThemeWrapper -> this.baseContext.addLifecycleObserver(observer)
        this is androidx.appcompat.view.ContextThemeWrapper -> this.baseContext.addLifecycleObserver(observer)
    }
}


fun RobinProgressButton.morphDoneAndRevert(
    context: Context,
    fillColor: Int = ContextCompat.getColor(context,android.R.color.black),
    bitmap: Bitmap =  getBitmapFromVectorDrawable(context,R.drawable.ic_success),
    doneTime: Long = 3000,
    revertTime: Long = 4000
) {
    progressType = ProgressType.INDETERMINATE
    startAnimation()
    Handler().run {
        postDelayed({ doneLoadingAnimation(fillColor, bitmap) }, doneTime)
        postDelayed({ revertAnimation() }, revertTime)
    }
}


fun RobinProgressButton.morphStopRevert(stopTime:Long = 1000, revertTime: Long = 2000){
    startAnimation()
    Handler().postDelayed({stopAnimation()},stopTime)
    Handler().postDelayed({revertAnimation()},revertTime)
}

fun RobinProgressButton.morphAndRevert(revertTime: Long = 3000, startAnimationCallback:() -> Unit = {}){
    startAnimation (startAnimationCallback)
    Handler().postDelayed({revertAnimation()},revertTime)
}



