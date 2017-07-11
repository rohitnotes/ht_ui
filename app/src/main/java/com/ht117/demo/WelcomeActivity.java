package com.ht117.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ht117.htui.Utils;
import com.ht117.htui.loading.CircleCountDownView;
import com.ht117.htui.loading.CountDownView;
import com.ht117.htui.loading.NumberLoadingView;

import java.util.Random;

public class WelcomeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar seekbar;
    private CountDownView countDownView;
    private CircleCountDownView circleCountDownView;
    private NumberLoadingView numberLoadingView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btnPlay).setOnClickListener(this);

        countDownView = (CountDownView) findViewById(R.id.countDownView);
        circleCountDownView = (CircleCountDownView) findViewById(R.id.circleCountDownView);
        numberLoadingView = (NumberLoadingView) findViewById(R.id.numberLoadingView);

        int duration = circleCountDownView.getDuration() / Utils.SECOND;
        ((TextView)findViewById(R.id.tvDuration)).setText(String.format("Duration: %ds", duration));

        seekbar = (SeekBar)findViewById(R.id.seekDuration);
        seekbar.setOnSeekBarChangeListener(this);
        seekbar.setProgress(duration);

        handler = new Handler();
        handler.postDelayed(randomProgress, Utils.SECOND / 4);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        ((TextView)findViewById(R.id.tvDuration)).setText(String.format("Duration: %ds", seekBar.getProgress()));
        countDownView.setDuration(seekbar.getProgress());
        circleCountDownView.setDuration(seekbar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        countDownView.setDuration(seekbar.getProgress());
        circleCountDownView.setDuration(seekbar.getProgress());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                countDownView.start();
                circleCountDownView.start();
                numberLoadingView.reset();
                handler.postDelayed(randomProgress, Utils.SECOND / 4);
                break;
        }
    }

    final Runnable randomProgress = new Runnable() {
        @Override
        public void run() {
            int prog = numberLoadingView.getProgress();
            if (prog < Utils.PERCENT) {
                Random rnd = new Random();
                int rndVal = rnd.nextInt(10);
                numberLoadingView.setProgress(prog + rndVal);
            }
            if (numberLoadingView.getProgress() < Utils.PERCENT) {
                handler.postDelayed(randomProgress, Utils.SECOND / 4);
            }
        }
    };
}
