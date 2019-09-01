package com.ht117.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import com.ht117.htui.Utils
import kotlinx.android.synthetic.main.activity_welcome.*
import java.util.*

class WelcomeActivity : Activity(), SeekBar.OnSeekBarChangeListener {

    private var handler: Handler? = null

    private val randomProgress: Runnable = object : Runnable {
        override fun run() {
            val prog = numberLoadingView!!.getProgress()
            if (prog < Utils.PERCENT) {
                val rnd = Random()
                val rndVal = rnd.nextInt(10)
                numberLoadingView!!.setProgress(prog + rndVal)
            }
            if (numberLoadingView!!.getProgress() < Utils.PERCENT) {
                handler!!.postDelayed(this, (Utils.SECOND / 4).toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnPlay.setOnClickListener {
            clickPlay()
        }

        val duration = circleCountDownView!!.getDuration() / Utils.SECOND
        tvDuration.text = String.format("Duration: %ds", duration)

        seekDuration.setOnSeekBarChangeListener(this)
        seekDuration.progress = duration

        handler = Handler()
        handler!!.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvDuration.text = String.format("Duration: %ds", seekBar.progress)
        countDownView!!.setDuration(seekDuration!!.progress)
        circleCountDownView!!.setDuration(seekDuration!!.progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        countDownView!!.setDuration(seekBar.progress)
        circleCountDownView!!.setDuration(seekBar.progress)
    }

    private fun clickPlay() {
        countDownView!!.start()
        circleCountDownView!!.start()
        numberLoadingView!!.reset()
        handler!!.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
    }
}
