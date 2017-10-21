package com.example.project.gvi2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;
import android.content.Intent;

import java.util.Locale;

public class ShakeDevice extends ActionBarActivity implements SensorEventListener {


    static boolean isActive = false;
    static boolean on=false;
    static boolean call_click_on=false;
    private MyService m1;//service object
    private Call c1;
    private Vibrator vib;
    TextToSpeech t1;
    String Sound_AppOn="g v i, application turned on";
    String Sound_MainScreen="You are now on home screen, listen to the instructions carefully, Tap one time for Opening Call. Tap two times to open Contacts. Tap three times to open Messages. Tap four times for listening current date and time. Tap five times for opening g v i training section. Kindly set the system volume using volume keys here. If you are using this appication for the first time go to training section first.";
    String Sound_CallApp="Opening Call";
    String Sound_MessagingApp="Opening Messages";
    String Sound_ContactsApp="Opening Contacts";
    SharedPreferences prefs = null;
    static boolean flag_first_run=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_device);


        c1 = new Call();
        vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        call_click_on=false;
        m1=new MyService();
        on=true;
        if (isActive==false)
        {
            if(m1.on==false) {
                Intent i=new Intent(getBaseContext(), MyService.class);     //Service
                startService(i); }
            isActive=true;
        }


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    sound_play();

                }
            }
        });
    }

    void sound_play()
    {
        t1.speak(Sound_AppOn, TextToSpeech.QUEUE_FLUSH, null);
/*    try { //waits for 3secs(3000msec)
        Thread.sleep(3000);

    } catch (InterruptedException e) {
        e.printStackTrace();
    }  */
        t1.speak(Sound_MainScreen, TextToSpeech.QUEUE_ADD, null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shake_device, menu);

        Button btn_call = (Button) findViewById(R.id.button_main_screen);
        btn_call.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;



            @Override
            public boolean onTouch(View v, MotionEvent event) { //listen for taps

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();
                        if (numberOfTaps == 5) {
                            //opening messaging
                            t1.speak("Opening training", TextToSpeech.QUEUE_ADD, null);
                            try {
                                Thread.sleep(1200);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            vib.vibrate(200);
                            vib.vibrate(200);
                            t1.stop();
                            Intent open_training = new Intent(getBaseContext(), Training.class);
                            startActivity(open_training);

                        }
                        else if (numberOfTaps == 4) {    //opening Contacts
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    t1.speak("Opening Organiser", TextToSpeech.QUEUE_ADD, null);
                                    try {
                                        Thread.sleep(1200);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    vib.vibrate(200);
                                    vib.vibrate(200);
                                    Intent open_organiser = new Intent(getBaseContext(), Organiser.class);
                                    startActivity(open_organiser);
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                        else if (numberOfTaps == 3) {    //opening Contacts
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    t1.speak(Sound_MessagingApp, TextToSpeech.QUEUE_ADD, null);
                                    try {
                                        Thread.sleep(1200);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    vib.vibrate(200);
                                    vib.vibrate(200);
                                    Intent open_messaging = new Intent(getBaseContext(), Message.class);
                                    startActivity(open_messaging);
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                        else if (numberOfTaps == 2) {    //opening Contacts
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    t1.speak(Sound_ContactsApp, TextToSpeech.QUEUE_ADD, null);
                                    try {
                                        Thread.sleep(1200);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    vib.vibrate(200);
                                    vib.vibrate(200);
                                    Intent open_contacts = new Intent(getBaseContext(), Contacts.class);////////change to Contacts.class
                                    startActivity(open_contacts);
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                        else if (numberOfTaps == 1) { //opening Call
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    t1.speak(Sound_CallApp, TextToSpeech.QUEUE_ADD, null);
                                    try {
                                        Thread.sleep(1200);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    vib.vibrate(200);
                                    vib.vibrate(200);
                                    Intent open_call = new Intent(getBaseContext(), Call.class);
                                    startActivity(open_call);
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }
        });
        return true;
    }

    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.speak(" ", TextToSpeech.QUEUE_FLUSH, null);
        }
        super.onPause();
        isActive=false;
    }

    @Override         //      (if not called then when app is paused ,sound goes out as required
    protected void onResume() {
        sound_play();
        super.onResume();
        isActive = true;
    }


    @Override
    protected void onDestroy() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
        isActive=false;
        on=false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
/*     //app first run
        prefs = getSharedPreferences("com.example.project.gvi2", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            flag_first_run=true;
            prefs.edit().putBoolean("firstrun", false).apply();
        }
        //on init
        if(flag_first_run)
                    {
                        flag_first_run=false;
                        t1.stop();
                        t1.speak("G V I application installed correctly. Because, this is the first time, we are taking you to the training phase.", TextToSpeech.QUEUE_FLUSH, null);
                        while(t1.isSpeaking()){}
                        Intent open_training = new Intent(getBaseContext(), Training.class);
                        open_training.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(open_training);
                    }
                    else
                       sound_play();*/
//