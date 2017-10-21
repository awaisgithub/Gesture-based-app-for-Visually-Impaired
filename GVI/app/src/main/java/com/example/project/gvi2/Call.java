package com.example.project.gvi2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Created by LENOVO on 2/17/2016.
 */
public class Call extends ActionBarActivity implements View.OnClickListener { //also make boolean type flags to see if this layout is active or not and accordingly manipualte the if's in onSensorChanged() in MyService.


    TextToSpeech t1;
    List<String> listStrings = new ArrayList<String>();
    String number; //number to be dialled
    String final_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        final_number="";
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    sound_feedback();
                }
            }
        });


        Button mClickButton1 = (Button)findViewById(R.id.button1);
        mClickButton1.setOnClickListener(this);
        Button mClickButton2 = (Button)findViewById(R.id.button2);
        mClickButton2.setOnClickListener(this);
        Button mClickButton3 = (Button)findViewById(R.id.button3);
        mClickButton3.setOnClickListener(this);
        Button mClickButton4 = (Button)findViewById(R.id.button4);
        mClickButton4.setOnClickListener(this);
        Button mClickButton5 = (Button)findViewById(R.id.button5);
        mClickButton5.setOnClickListener(this);
        Button mClickButton6 = (Button)findViewById(R.id.button6);
        mClickButton6.setOnClickListener(this);
        Button mClickButton7 = (Button)findViewById(R.id.button7);
        mClickButton7.setOnClickListener(this);
        Button mClickButton8 = (Button)findViewById(R.id.button8);
        mClickButton8.setOnClickListener(this);
        Button mClickButton9 = (Button)findViewById(R.id.button9);
        mClickButton9.setOnClickListener(this);
        Button mClickButton0 = (Button)findViewById(R.id.button0);
        mClickButton0.setOnClickListener(this);
        Button mClickButtonstar = (Button)findViewById(R.id.buttonStar);
        mClickButtonstar.setOnClickListener(this);
        Button mClickButtonhash = (Button)findViewById(R.id.buttonHash);
        mClickButtonhash.setOnClickListener(this);
        Button mClickButtondial = (Button)findViewById(R.id.buttonDial);
        mClickButtondial.setOnClickListener(this);


        mClickButton1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("One added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("1");
                return true;
            }
        });
        mClickButton2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Two added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("2");
                return true;
            }
        });
        mClickButton3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Three added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("3");
                return true;
            }
        });
        mClickButton4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Four added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("4");
                return true;
            }
        });
        mClickButton5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Five added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("5");
                return true;
            }
        });
        mClickButton6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Six added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("6");
                return true;
            }
        });
        mClickButton7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Seven added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("7");
                return true;
            }
        });
        mClickButton8.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Eight added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("8");
                return true;
            }
        });
        mClickButton9.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Nine added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("9");
                return true;
            }
        });
        mClickButton0.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Zero added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("0");
                return true;
            }
        });
        mClickButtonstar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Star added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("*");
                return true;
            }
        });
        mClickButtonhash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                t1.speak("Hash added", TextToSpeech.QUEUE_FLUSH, null);
                listStrings.add("#");
                return true;
            }
        });
        mClickButtondial.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                if(listStrings.size()>0) {
                    number = listStrings.toString();
                    char[] c = number.toCharArray();
                    for (int i = 0; i < number.length(); i++) {

                        if ((c[i] == '[') || (c[i] == ',') || (c[i] == ']') || (c[i] == ' '))
                        {
                            continue;
                        }
                        else
                        {
                            final_number += c[i];
                        }
                    }
                    t1.speak("Dialing " + final_number, TextToSpeech.QUEUE_FLUSH, null);
                    while(t1.isSpeaking()) {}
                    dialCall();
                    refresher();
                    reOpen();
                }
                else {
                    t1.speak("Please enter a number to call", TextToSpeech.QUEUE_FLUSH, null);
                    while (t1.isSpeaking()) {
                    }
                }
                return true;
            }
        });
    }
    public void refresher()
    {
        t1.speak("Calling", TextToSpeech.QUEUE_FLUSH, null);
        while(t1.isSpeaking()) {}
    }

    public void reOpen()
    {
        Intent defineIntent_relaunchCall = new Intent(getApplicationContext(),Call.class);
        defineIntent_relaunchCall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        defineIntent_relaunchCall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(defineIntent_relaunchCall);
    }


    public void dialCall()
    {
        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL); //
        phoneCallIntent.setPackage("com.android.phone");
        phoneCallIntent.setData(Uri.parse("tel:" + final_number)); //edit here and see what string to use
        startActivity(phoneCallIntent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:       //erase
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    if(listStrings.size()>0) {
                        String temp=listStrings.get(listStrings.size()-1);
                        t1.speak(" Deleting "+temp, TextToSpeech.QUEUE_FLUSH, null);
                        listStrings.remove(listStrings.size() - 1);
                    }
                    else {
                        t1.speak("No digit to delete", TextToSpeech.QUEUE_FLUSH, null);
                        while (t1.isSpeaking()) {
                        }
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:     //recall
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    String temp_number="";
                    if (listStrings.size() > 0) {
                        String number1 = listStrings.toString();
                        char[] c = number1.toCharArray();
                        for (int i = 0; i < number1.length(); i++) {

                            if ((c[i] == '[') || (c[i] == ',') || (c[i] == ']') || (c[i] == ' ')) {
                                continue;
                            } else {
                                temp_number += c[i];
                            }
                        }
                        t1.speak("" + temp_number, TextToSpeech.QUEUE_ADD, null);
                        //      while (t1.isSpeaking()) {}

                    } else {
                        t1.speak("no number entered so far", TextToSpeech.QUEUE_ADD, null);
                        //    while (t1.isSpeaking()) {}
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    public void onClick(View v ) {
        switch (v.getId()) {
            case  R.id.button1: {
                // do something for button 1 click
                t1.speak("One", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }

            case  R.id.button2: {
                t1.speak("Two", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button3: {
                t1.speak("Three", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button4: {
                t1.speak("Four", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button5: {
                t1.speak("Five", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button6: {
                t1.speak("Six", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button7: {
                t1.speak("Seven", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button8: {
                t1.speak("Eight", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button9: {
                t1.speak("Nine", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.button0: {
                t1.speak("Zero", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.buttonStar: {
                t1.speak("Star", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.buttonHash: {
                t1.speak("Hash", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
            case  R.id.buttonDial: {
                t1.speak("Dial Button", TextToSpeech.QUEUE_FLUSH, null);
                break;
            }
        }
    }

    void sound_feedback()
    {
        String call_app_feedback = "Call opened. Tap for listening digit. Long Press for selecting digit. Press Volume up key to remove the last added digit. Press Volume down key to listen the entered number so far. Tap the call button below to dial call. Use power button to end call.";
        t1.speak(call_app_feedback, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        if(t1 != null)
            t1.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
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
