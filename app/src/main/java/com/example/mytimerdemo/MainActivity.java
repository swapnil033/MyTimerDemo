package com.example.mytimerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.CircularPropagation;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    CircularProgressIndicator statusTimer;
    TextView tvTimer;
    TextView tvTimer1;
    TextView tvTimerCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTimer = findViewById(R.id.statusTimer);
        tvTimer = findViewById(R.id.tvTimer);
        tvTimer1 = findViewById(R.id.tvTimer1);
        tvTimerCurr = findViewById(R.id.tvTimerCurr);

        //make fake start time
        Calendar calendar = GregorianCalendar.getInstance();

        Calendar startTime = GregorianCalendar.getInstance();
        startTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE) - 6);
        //startTime.set(Calendar.SECOND, startTime.get(Calendar.SECOND));
        getCurrTime(startTime);

        /*
        * curtent time : 11:00 am
        * String time : 10:55 am
        * limit : 7 min
        * */

        TimerHelper2 helper = new TimerHelper2(startTime.getTime() ,  7);

        helper.getProgress(new TimerHelper2.TimerHelperIndicatorListener() {
            @Override
            public void getProgress(boolean isFinished, int progress) {
                statusTimer.setTrackColor(getResources()
                        .getColor(isFinished ? R.color.colorRed : R.color.colorAccent));

                progress = 100 - progress;
                Log.e("TAG", "getProgress: " + progress );
                statusTimer.setProgress(progress);
            }
        });

        helper.getMinuteNSec(new TimerHelper2.TimerHelperMinuteIndicatorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void getMinute(boolean isFinished, String min, String sec) {

                tvTimer.setTextColor(getResources()
                        .getColor(isFinished ? R.color.colorRed : R.color.colorAccent));

                tvTimer.setText(min + "\nMin");

                tvTimer1.setTextColor(getResources()
                        .getColor(isFinished ? R.color.colorRed : R.color.colorAccent));

                tvTimer1.setText(min + " : " + sec);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void getCurrTime(Calendar calendar) {
        tvTimerCurr.setText(calendar.get(Calendar.HOUR) +" : " +calendar.get(Calendar.MINUTE) +" : " + calendar.get(Calendar.SECOND));

        /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getCurrTime(GregorianCalendar.getInstance());
            }
        }, 1000);*/

    }


}