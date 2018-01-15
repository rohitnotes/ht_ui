package com.ht117.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.ht117.htui.Utils
import com.ht117.htui.loading.CircleCountDownView
import com.ht117.htui.loading.NumberLoadingView
import com.ht117.htui.loading.SquareCountDownView
import java.util.*

class WelcomeActivity : Activity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private lateinit var seekbar: SeekBar
    private lateinit var squareCountDownView: SquareCountDownView
    private lateinit var circleCountDownView: CircleCountDownView
    private lateinit var numberLoadingView: NumberLoadingView
    private lateinit var handler: Handler

    private val randomProgress: Runnable = object : Runnable {
        override fun run() {
            val prog = numberLoadingView.getProgress()
            if (prog <= Utils.PERCENT) {
                val rnd = Random()
                val rndVal = rnd.nextInt(10)
                numberLoadingView.setProgress(prog + rndVal)
            }
            if (numberLoadingView.getProgress() < Utils.PERCENT) {
                handler.postDelayed(this, (Utils.SECOND / 4).toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        findViewById(R.id.btnPlay).setOnClickListener(this)

        squareCountDownView = findViewById(R.id.squareCountDownView) as SquareCountDownView
        circleCountDownView = findViewById(R.id.circleCountDownView) as CircleCountDownView
        numberLoadingView = findViewById(R.id.numberLoadingView) as NumberLoadingView

        val duration = circleCountDownView.duration / Utils.SECOND
        (findViewById(R.id.tvDuration) as TextView).text = String.format("Duration: %ds", duration)

        seekbar = findViewById(R.id.seekDuration) as SeekBar
        seekbar.setOnSeekBarChangeListener(this)
        seekbar.progress = duration

        handler = Handler()
        handler.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        (findViewById(R.id.tvDuration) as TextView).text = String.format("Duration: %ds", seekBar.progress)
        squareCountDownView.duration = seekbar.progress * Utils.SECOND
        circleCountDownView.duration = seekbar.progress * Utils.SECOND
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        squareCountDownView.duration = seekbar.progress * Utils.SECOND
        circleCountDownView.duration = seekbar.progress * Utils.SECOND
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnPlay -> {
                squareCountDownView.start()
                circleCountDownView.start()
                numberLoadingView.reset()
                handler.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
            }
        }
    }
}
