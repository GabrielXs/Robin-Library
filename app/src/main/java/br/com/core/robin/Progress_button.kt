package br.com.core.robin

import Component.CircularProgressButton.animatedDrawables.ProgressType
import Component.CircularProgressButton.customViews.RobinCircularProgressButton
import Component.CircularProgressButton.utils.morphAndRevert
import Component.CircularProgressButton.utils.morphDoneAndRevert
import Component.CircularProgressButton.utils.morphStopRevert
import Util.getBitmapFromVectorDrawable
import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_progress_button.*

//import br.com.simplepass.loadingbutton.customViews.CircularProgressButton


class Progress_button : AppCompatActivity() {
    //    private lateinit var button : CircularProgressButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_button)
        imgbtnTest0.run { setOnClickListener { morphDoneAndRevert(this@Progress_button) } }

        imgbtnTest1.run {
            setOnClickListener {
                progressType = ProgressType.INDETERMINATE
                startAnimation()
                ValueAnimator.ofFloat(0F, 100F).apply {
                    duration = 1500
                    startDelay = 500
                    addUpdateListener { animation ->
                        (it as RobinCircularProgressButton).setProgress(animation.animatedValue as Float)

                    }
                }.start()

                Handler().run {
                    postDelayed({
                        doneLoadingAnimation(
                            ContextCompat.getColor(context, android.R.color.black),
                            getBitmapFromVectorDrawable(context,R.drawable.ic_success)
                        )
                    },2500)
                    postDelayed({revertAnimation()},3500)
                }
            }
        }

        imgbtnTest2.run {
            setOnClickListener { morphStopRevert() }
        }

        imgbtnTeste3.run {
            setOnClickListener { morphAndRevert{
                Toast.makeText(this@Progress_button,"Fim da Animação",Toast.LENGTH_LONG).show()
                imgbtnTeste4.run { morphDoneAndRevert(this@Progress_button) }

            } }
        }


        imgbtnTeste4.run {
            setOnClickListener {
                startAnimation()
            }
        }

    }


}
