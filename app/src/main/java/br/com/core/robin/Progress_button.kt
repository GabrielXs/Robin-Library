package br.com.core.robin

import Component.RobinProgressButton
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.CircularProgressDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.transition.CircularPropagation
import android.widget.Button
//import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import kotlinx.android.synthetic.main.activity_main.*


class Progress_button : AppCompatActivity() {
//    private lateinit var button : CircularProgressButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_button)


    }

}
