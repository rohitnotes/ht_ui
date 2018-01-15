package com.ht117.demo

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.TextView

import com.ht117.htui.Utils
import com.ht117.htui.loading.CircleCountDownView
import com.ht117.htui.loading.SquareCoundDownView
import com.ht117.htui.loading.NumberLoadingView

import java.util.Random

class WelcomeActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private lateinit var seekbar: SeekBar
    private lateinit var squareCoundDownView: SquareCoundDownView
    private lateinit var circleCountDownView: CircleCountDownView
    private lateinit var numberLoadingView: NumberLoadingView
    private lateinit var handler: Handler

    private val randomProgress: Runnable = object : Runnable {
        override fun run() {
            val prog = numberLoadingView.getProgress()
            if (prog < Utils.PERCENT) {
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

        squareCoundDownView = findViewById(R.id.countDownView) as SquareCoundDownView
        circleCountDownView = findViewById(R.id.circleCountDownView) as CircleCountDownView
        numberLoadingView = findViewById(R.id.numberLoadingView) as NumberLoadingView

        val duration = circleCountDownView.duration() / Utils.SECOND
        (findViewById(R.id.tvDuration) as TextView).text = String.format("Duration: %ds", duration)

        seekbar = findViewById(R.id.seekDuration) as SeekBar
        seekbar.setOnSeekBarChangeListener(this)
        seekbar.progress = duration

        handler = Handler()
        handler.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        (findViewById(R.id.tvDuration) as TextView).text = String.format("Duration: %ds", seekBar.progress)
        squareCoundDownView.duration = seekbar.progress
        circleCountDownView.duration = seekbar.progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        squareCoundDownView.duration = seekbar.progress
        circleCountDownView.duration = seekbar.progress
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnPlay -> {
                squareCoundDownView.start()
                circleCountDownView.start()
                numberLoadingView.reset()
                handler.postDelayed(randomProgress, (Utils.SECOND / 4).toLong())
            }
        }
    }
}
