package com.example.project.gvi2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by LENOVO on 4/28/2016.
 */
public class Training extends Activity implements SensorEventListener {

    TextToSpeech t1;
    int count_vol_up,count_vol_down,count_tap,count_long_press;
    boolean vol_up,vol_down,tap,long_press;
    private Button btn_tap;
    int input_locater; // check for training input
    boolean onLong;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float last_x=0, last_y=0, last_z=0;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OKAY(onCreate_TRAINING");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        count_vol_up=0;
        count_vol_down=0;
        vol_up=false;
        vol_down=false;
        count_tap=0;
        tap=false;
        input_locater=0;
        count_long_press=0;
        long_press=false;
        onLong=false;

        Button btn_tap = (Button)findViewById(R.id.button_training);
        btn_tap.setOnTouchListener(speakTouchListener);
        btn_tap.setOnLongClickListener(speakHoldListener);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

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
                if(input_locater==2&&count_tap==0) {
                    count_tap++;
                    t1.speak(" Screen tapped correctly, proceeding to the next input.", TextToSpeech.QUEUE_ADD, null);
                    //     while(t1.isSpeaking()){}
                    //     t1.stop();
                    testLongPress();
                }
                else if(input_locater==2&&count_tap>0)
                {
                    t1.speak(" Wrong input, please try again.", TextToSpeech.QUEUE_ADD, null);
                }
            }
            return true;
        }
    };

    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            if(input_locater==3&&count_long_press==0) {
                long_press=true;
                onLong=true;
                count_long_press++;
                t1.speak(" Long press input entered correctly, proceeding to the next input.", TextToSpeech.QUEUE_ADD, null);
                //    while(t1.isSpeaking()){}
                //    t1.stop();
                input_locater++;
                t1.speak("For left gesture hold yor device vertically and tilt the head in left direction with a long press. ", TextToSpeech.QUEUE_ADD, null);
                //    while (t1.isSpeaking()) {}
                //     t1.stop();
            }
            else if(input_locater==3&&count_long_press>0)
            {
                t1.speak(" Wrong input, please try again.", TextToSpeech.QUEUE_ADD, null);
            }
            else  if(input_locater==4&&long_press==true)
            {
                onLong=true;
                sensor_switchOn();
            }
            else  if(input_locater==5&&long_press==true)
            {
                onLong=true;
                sensor_switchOn();
            }
            else  if(input_locater==6&&long_press==true)
            {
                onLong=true;
                sensor_switchOn();
            }
            return true;
        }
    };



    void sound_feedback()
    {
        String note="This is the g v i training section. We will guide you to through a series of steps for guide. This training comprises of verbal instructions from the system, and your actions.";
        t1.speak(note, TextToSpeech.QUEUE_FLUSH, null);//change to note
        //     while(t1.isSpeaking()){}    remove

        //    t1.stop();
        String note1="There are seven inputs in total, which are required for operating this system. We will guide you through each one of them, step wise. The inputs are, pressing, Volume up Button, pressing, Volume down Button, giving tap command on screen, giving long press command on screen, giving Left Gesture, giving Right Gesture, and, giving Forward Gesture. Listen to system instructions, and respond accordingly.";
        t1.speak(note1, TextToSpeech.QUEUE_ADD, null);//change to note
        //   while(t1.isSpeaking()){}
        //   t1.stop();
        String note2="Before we start, kindly check that the power button ends call accessibility feature, is checked in the settings, and, there is no security application installed, in your phone.";
        t1.speak(note2, TextToSpeech.QUEUE_ADD, null);//change to note
        //     while(t1.isSpeaking()){}

        //     t1.stop();
        testVolUp();
    }

    void  testVolUp()
    {

        String note1="This is the Volume Up testing. Press the Volume Up Button after 1 second. ";
        t1.speak(note1, TextToSpeech.QUEUE_ADD, null);
        //   while(t1.isSpeaking()){} remove
        //    t1.stop();  remove
        //input_locater++;
        System.out.println("YOKOHOMA="+input_locater+ "       count_vol_up="+count_vol_up);
    }

    void testVolDown()
    {
        String note2="This is the Volume Down testing. Press the Volume Down Button now. ";
        t1.speak(note2, TextToSpeech.QUEUE_ADD, null);
        //    while(t1.isSpeaking()){}
        //    t1.stop();
        input_locater++;
        System.out.println("YOKOHOMA="+input_locater+ "       count_vol_up="+count_vol_up);
    }

    void testTap()
    {
        String note3="This is the Tap Screen testing. Tap the screen gently, now. ";
        t1.speak(note3, TextToSpeech.QUEUE_ADD, null);
        //    while(t1.isSpeaking()){}
        //    t1.stop();
        input_locater++;
        System.out.println("YOKOHOMA="+input_locater+ "       count_vol_up="+count_vol_up);
    }

    void testLongPress()
    {
        String note4="Now we come up to long press. Tap, and hold, on the screen for at least two seconds. ";
        t1.speak(note4, TextToSpeech.QUEUE_ADD, null);
        //   while(t1.isSpeaking()){}
        //    t1.stop();
        input_locater++;
        System.out.println("YOKOHOMA="+input_locater+ "       count_vol_up="+count_vol_up);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) //on KeyPress
    {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP://up key
                if (action == KeyEvent.ACTION_DOWN)
                {
                    System.out.println("YOKOHOMA="+input_locater+ "       count_vol_up="+count_vol_up);
                    if(count_vol_up==0&&input_locater==0) //i_l=1
                    {
                        vol_up=true;
                        count_vol_up++;
                        t1.speak(" Volume Up pressed correctly, Proceeding to the next input.", TextToSpeech.QUEUE_ADD, null);
                        //     while(t1.isSpeaking()){}
                        //      t1.stop();
                        testVolDown();
                    }
                    else
                    {
                        t1.speak(" Wrong input, volume up, please try again.", TextToSpeech.QUEUE_ADD, null);
                    }

                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN://down key
                if (action == KeyEvent.ACTION_DOWN)
                {
                    if(count_vol_down==0&&input_locater==1)
                    {
                        vol_down=true;
                        count_vol_down++;
                        t1.speak(" Volume Down pressed correctly, Proceeding to the next input.", TextToSpeech.QUEUE_ADD, null);
                        //       while(t1.isSpeaking()){}
                        //       t1.stop();
                        testTap();
                    }
                    else
                    {
                        t1.speak(" Wrong input, volume down, please try again.", TextToSpeech.QUEUE_ADD, null);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    void reSetter()
    {
        deltaX = 0; //resetting values
        deltaY = 0;
        deltaZ =0;
        sensorManager.unregisterListener(this);
        onLong=false;
    }

    void sensor_switchOn()    //registers accelerometer
    {
        last_x=0;
        last_y=0;
        last_z=0;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void testingEnd()
    {
        String end_note=". You have reached the end of g v i training. Shake the device to reset application to begin using g v i.";
        t1.speak(end_note, TextToSpeech.QUEUE_ADD, null);
        //  while(t1.isSpeaking()){}
        //   t1.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(onLong)
        {
            deltaX = Math.abs(last_x - sensorEvent.values[0]);
            deltaY = Math.abs(last_y - sensorEvent.values[1]);
            deltaZ = Math.abs(last_z - sensorEvent.values[2]);



            if (deltaX >= -0.4 && deltaY >= -0.4 && deltaZ >=6&&input_locater==6  )// (forward gesture)
            {
                t1.speak("Forward gesture correctly, This was the last input. No further inputs left to test.", TextToSpeech.QUEUE_ADD, null);
                //    while(t1.isSpeaking()){}
                //     t1.stop();
                input_locater++;
                reSetter();
                testingEnd();
            }

            else if ((float) Math.round(sensorEvent.values[0])>8.0000&&input_locater==4) //left gesture
            {
                t1.speak("Left gesture correctly, Proceeding to the next input. For right gesture, hold yor device vertically, and tilt the head, in right direction, with a long press.", TextToSpeech.QUEUE_ADD, null);
                //    while(t1.isSpeaking()){}
                //    t1.stop();
                input_locater++;
                reSetter();
            } else if ((float) Math.round(sensorEvent.values[0])<-8.0000&&input_locater==5) //right gesture
            {
                t1.speak("Right gesture correctly, Proceeding to the next input. For forward gesture, hold yor device vertically, and tilt the head, in downward direction, with a long press.", TextToSpeech.QUEUE_ADD, null);
                //    while(t1.isSpeaking()){}
                //     t1.stop();
                input_locater++;
                reSetter();
            }

            last_x = sensorEvent.values[0];
            last_y = sensorEvent.values[1];
            last_z = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        if(t1 != null) {
            t1.stop();
            t1.speak(" ", TextToSpeech.QUEUE_FLUSH, null);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        t1.speak("G V I application resumed", TextToSpeech.QUEUE_FLUSH, null);
        sound_feedback();
        //    while(t1.isSpeaking()){}
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
