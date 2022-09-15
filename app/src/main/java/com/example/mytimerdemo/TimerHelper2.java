package com.example.mytimerdemo;

import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimerHelper2 {

    private int limit = 0;
    private long limitForCounter = 0;
    private int onePer = 0;
    private float startProgress = 0;
    private Date startTime;
    private Date endTime;
    private final Date curTime;
    private int mins;
    private int startMin;
    private int startSec;

    public TimerHelper2(Date startTime, int mins) {
        this.startTime = startTime;
        this.curTime = new Date();
        this.mins = mins;

        //convert minute into long
        limit = mins * 60 * 1000;
        //calculating 1% of limit
        onePer = limit /100;
        // converting endTimer string into Date
        //endTime = setEndTime(startTime);
        initValues();
    }


    private void initValues() {
        long difference_In_Time = curTime.getTime() - startTime.getTime();
        startMin = (int) ((difference_In_Time / (1000 * 60)) % 60);
        startSec = (int) ((difference_In_Time / 1000) % 60);
        float part = ((startMin * 60 * 1000) + (startSec * 1000));
        startProgress = ( part / limit) * 100;
        limitForCounter =  limit - difference_In_Time;
        Log.d("TAG", "initValues: ");
    }

    private Date setEndTime(Date startTime) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(startTime);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - mins);
        return calendar.getTime();
    }


    public void getProgress(TimerHelperIndicatorListener listener) {

        new CountDownTimer( limitForCounter, onePer){
            int count = (int) startProgress;

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                listener.getProgress(false, ++count);
            }

            @Override
            public void onFinish() {
                listener.getProgress(true, 0);
            }
        }.start();

    }

    int revMin = 0;
    int revSec = 0;
    public void getMinuteNSec(TimerHelperMinuteIndicatorListener listener) {

        revMin = startMin;
        revSec = 60 - startSec;
        if(revSec != 0)
            --revMin;

        new CountDownTimer( limitForCounter, 1000){
            @Override
            public void onTick(long l) {
                revSec = secCal(--revSec);
                if(revSec == 59) --revMin;

                callListener(listener, false, revMin, revSec);
            }

            @Override
            public void onFinish() {
                countSecTimers( listener);
            }

        }.start();

    }

    private int secCal(int sec) {
        if(sec < 0) return 59;
        if(sec > 59) return 0;
        return sec;
    }

    private void countSecTimers( TimerHelperMinuteIndicatorListener listener) {

        revSec = secCal(++revSec);
        if(revSec == 0) ++revMin;

        callListener(listener, true, revMin, revSec);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                callListener(listener, true, revMin, revSec);
                countSecTimers( listener);
            }
        }, 1000);
    }

    private void callListener(TimerHelperMinuteIndicatorListener listener, boolean isFinished, int revMin, int revSec) {
        String revMinS = String.valueOf(revMin);
        String revSecS = String.valueOf(revSec);

        if(revMinS.length() == 1)
            revMinS = "0"+revMinS;

        if(revSecS.length() == 1)
            revSecS = "0"+revSecS;

        listener.getMinute(isFinished,revMinS, revSecS);
    }

    public interface TimerHelperIndicatorListener{
        void getProgress(boolean isFinished, int progress);
    }

    public interface TimerHelperMinuteIndicatorListener{
        void getMinute(boolean isFinished, String min, String sec);
    }
}
