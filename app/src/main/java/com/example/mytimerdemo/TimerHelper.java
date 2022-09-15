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

public class TimerHelper {

    private int limit;
    private int limitForCounter;
    private final int min = 60 * 1000;
    private final int sec = 1000;
    private final int onePer;

    private float startMin = 0;
    private float startSec = 0;
    private float startProgress = 0;

    int revMin = 0;
    int revSec = 0 ;

    public TimerHelper(Date currTime, int limit){


        this.limit = limit;
        onePer = limit /100;

        calculateTimeDiff(currTime);
    }

    private void calculateTimeDiff(Date currTime) {

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currTime);

        int buffMin = calendar.get(Calendar.MINUTE) - 6;
        int buffSec = calendar.get(Calendar.SECOND) - 30;

        calendar.set(Calendar.MINUTE , buffMin);
        calendar.set(Calendar.SECOND , buffSec);

        setInitValues(calendar);
    }

    private void setInitValues(Calendar startCalendar) {

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());

        startMin = calendar.get(Calendar.MINUTE) - startCalendar.get(Calendar.MINUTE);
        startSec =  calendar.get(Calendar.SECOND) - startCalendar.get(Calendar.SECOND);

        Log.d("TAG", "setInitValues: " +
                "\nstart min : " + startCalendar.get(Calendar.MINUTE) + " start sec : " + startCalendar.get(Calendar.SECOND) +
                "\ncur min : " + calendar.get(Calendar.MINUTE) + " cur sec : " + calendar.get(Calendar.SECOND));

        float part = ((startMin * 60 * 1000) + (startSec * 1000));
        startProgress = (part / limit) * 100;

        limitForCounter =  limit - (int) part;
        limitForCounter =  limit - (int) part;
    }

    public void getProgress(TimerHelperIndicatorListener listener) {

        int count1 = (int) startProgress;

        new CountDownTimer(limitForCounter, onePer){
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

    /*public void getMinute(TimerHelperMinuteIndicatorListener listener) {

        revMin = limit / min - (int) startMin;
        if(revMin - 1 <= 0){
            revMin = 1;
        }
        new CountDownTimer(limit, min){


            @Override
            public void onTick(long l) {
                listener.getMinute(false, --revMin ,0);
            }

            @Override
            public void onFinish() {
                countTimers(revMin, listener);
            }

        }.start();

    }

    private void countTimers(int rev, TimerHelperMinuteIndicatorListener listener) {
        listener.getMinute(true, rev,0);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            int rev1 = rev;

            @Override
            public void run() {
                listener.getMinute(true, ++rev1, 0);
                countTimers(rev1, listener);
            }
        },60 * 1000);
    }*/


    public void getMinuteNSec(TimerHelperMinuteIndicatorListener listener) {
        revSec = (int) startSec;
        if(revMin - 1 <= 0){
            revMin = 1;
        }

        new CountDownTimer(limitForCounter, sec){
            @Override
            public void onTick(long l) {
                revSec = secCal(--revSec);
                if(revSec == 59) --revMin;

                listener.getMinute(false, revMin, revSec);
            }

            @Override
            public void onFinish() {
                countSecTimers( listener);
            }

        }.start();

    }

    private int secCal(int sec) {
        if(sec < 0) return 59;
        if(sec > 60) return 0;
        return sec;
    }

    private void countSecTimers( TimerHelperMinuteIndicatorListener listener) {

        revSec = secCal(++revSec);
        if(revSec == 59) ++revMin;

        listener.getMinute(true, revMin, revSec);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                listener.getMinute(true, revMin, revSec);
                countSecTimers( listener);
            }
        }, 1000);
    }

    public interface TimerHelperIndicatorListener{
        void getProgress(boolean isFinished, int progress);
    }

    public interface TimerHelperMinuteIndicatorListener{
        void getMinute(boolean isFinished, int min, int sec);
    }

}
