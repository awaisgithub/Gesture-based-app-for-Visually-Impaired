package com.example.project.gvi2;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by LENOVO on 5/2/2016.
 */
public class Organiser extends Activity {
    TextToSpeech t1;
    Calendar cal;
    int second,minute,hour,year,dayofweek,dayofmonth,mMonth,ampm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser);
        Button btn_organiser = (Button)findViewById(R.id.button_organiser);
        btn_organiser.setOnTouchListener(speakTouchListener);
        getDateTime();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    sound_feedback();
                }
            }
        });

    }

    private View.OnTouchListener speakTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                getDateTime();
                sound_feedback();
            }
            return true;
        }
    };

    void getDateTime()
    {
        //TIME
        cal = Calendar.getInstance();

        second = cal.get(Calendar.SECOND);
        minute = cal.get(Calendar.MINUTE);
        //12 hour format
     //   hour = cal.get(Calendar.HOUR_OF_DAY);
        hour = cal.get(Calendar.HOUR);
        ampm=cal.get(Calendar.AM_PM);
        //
        //DATE
        year = cal.get(Calendar.YEAR);
        dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        mMonth = cal.get(Calendar.MONTH);
        //
    }

    public String amPm(int hour2)
    {
         if(ampm==0)
            return(". A, M");
         else
              return (". P, M");
    }
    public int newHour(int hour1)
    {
        System.out.println("YOKOHAMA123(1=  "+hour1);
        if(hour1>12)
            hour1-=12;
        else if(hour1==0)
            hour1=12;
        System.out.println("YOKOHAMA123(1.1=  "+hour1);
        return hour1;
    }

    void sound_feedback()// sunday 1 and sat is 7 currently
    {
        t1.speak("The date today is "+dayofmonth+getDayOfMonthSuffix(dayofmonth)+" "+getMonth(mMonth+1)+ ","+year+". And the time is "+newHour(hour)+" "+minute+amPm(hour)+" "+". To ree listen, tap on screen or shake device to re start.", TextToSpeech.QUEUE_FLUSH, null);//change to note
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }


    String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }
    @Override
    protected void onPause() {
        if(t1 != null)
            t1.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        getDateTime();
        sound_feedback();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
}
