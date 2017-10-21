package com.example.project.gvi2;
//follow coding convention of fucntions .refactor
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.hardware.Sensor;


/**
 * Created by LENOVO on 3/7/2016.
 */
public class Contacts extends Activity implements SensorEventListener {
    String phoneNumber = null;
    Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    String _ID = ContactsContract.Contacts._ID;
    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    StringBuffer output;
    String contacts_app_feedback="Contacts opened. Use volume keys to select contact. Give forward gesture to dial call. Use left and right gestures with long press to use binary search. ";
    ContentResolver contentResolver;
    Cursor cursor;
    List<String> listStrings = new ArrayList<String>();   //
    int contact_pointer; //cursor which points the current contact
    Set<String> hs = new HashSet<>();
    TextToSpeech t1;
    int contact_list_size; //orignal list size
    int binary_contact_list_size;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float last_x=0, last_y=0, last_z=0;
    public Vibrator v;
    String[] pointer_name;
    String temp_name;
    String c_pointer;
    String splitter;
    boolean contact_selection_check; //check to see whether a contact is selected to call for the first time
    public static boolean isActive;// we are in contacts.
    public static boolean onLong;
    private Button btn_contact; //contact button
    private boolean isSpeakButtonLongPressed = false;
    private boolean last_was_right,last_was_left;
    int left_start=0,left_end=0,right_start=0,right_end=0,mean=0; //for contacts binary search
    int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        times=0;
        onLong=false;
        isActive=true;
        contact_selection_check=false;
        contact_pointer=-1;
        temp_name="";
        last_was_right=false;
        last_was_left=false;
        output = new StringBuffer();
        contentResolver = getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {     //if there are contacts then proceed
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    phoneCursor.moveToNext();
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    listStrings.add(name + " : " + phoneNumber);
                    phoneCursor.close();
                }
            }
        }
        Collections.sort(listStrings);
        hs.addAll(listStrings);
        listStrings.clear();
        listStrings.addAll(hs);
        Collections.sort(listStrings);         //sorts alphabetically(A-Z)
        contact_list_size=listStrings.size();
        binary_contact_list_size=contact_list_size;
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    sound_feedback();
                }
            }
        });


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //button_contact
        Button btn_contact = (Button)findViewById(R.id.button_contacts);
        btn_contact.setOnLongClickListener(speakHoldListener);
        btn_contact.setOnTouchListener(speakTouchListener);
    }


    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            onLong = true;
            sensor_switchOn();       //registers accelerometer
            //     Toast.makeText(Contacts.this, "LONG", Toast.LENGTH_SHORT).show();
            isSpeakButtonLongPressed = true;
            return true;
        }
    };

    private View.OnTouchListener speakTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isSpeakButtonLongPressed) {
                    // Do something when the button is released.
                    reSetter();
                    isSpeakButtonLongPressed = false;
                }
            }
            return false;
        }
    };





    void sensor_switchOn()    //registers accelerometer
    {
        last_x=0;
        last_y=0;
        last_z=0;
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void sound_feedback(){

        String temp1,temp2,temp3,temp4; //temporary variables for binary search

        left_start=0;
        left_end=(binary_contact_list_size/2)-1;
        right_start=left_end+1;
        right_end=binary_contact_list_size-1;
        //   System.out.println("TESTING12467START=leftt_start="+left_start+" left_end="+left_end+"+right_start="+right_start+"right_end"+ right_end+ " size="+binary_contact_list_size);
        //split for left_start
        c_pointer = listStrings.get(left_start);//current pointer
        splitter = "[:]"; //value for split
        pointer_name = c_pointer.split(splitter);//has name value after splitting
        temp1=pointer_name[0];
        //split for left_end
        c_pointer = listStrings.get(left_end);//current pointer
        pointer_name = c_pointer.split(splitter);//has name value after splitting
        temp2=pointer_name[0];
        //split for right_start
        c_pointer = listStrings.get(right_start);//current pointer
        pointer_name = c_pointer.split(splitter);//has name value after splitting
        temp3=pointer_name[0];
        //split for right_end
        c_pointer = listStrings.get(right_end);//current pointer
        pointer_name = c_pointer.split(splitter);//has name value after splitting
        temp4=pointer_name[0];

        t1.speak(contacts_app_feedback + "Left chunk from "+temp1+" . To "+temp2+". Right chunk from "+temp3+" . To "+temp4 , TextToSpeech.QUEUE_FLUSH, null);
        //   t2.speak( "Test. Left chunk from "+temp1+" . To "+temp2+". Right chunk from "+temp3+" . To "+temp4 , TextToSpeech.QUEUE_FLUSH, null);
    }

    public void select_contact(int s_id)    //navigates the contact cursor
    {
        if (s_id == 1)   //if vol. up pressed
        {
            contact_selection_check=true;
            if((contact_pointer==0)||(contact_pointer==-1)) //If at start
                contact_pointer = contact_list_size;
            //edit here if you want to go to last contact when the contact_pointer is at the beginning
            //make changes so the repeatition is avoided in names
            c_pointer = listStrings.get(--contact_pointer);//current pointer
            splitter = "[:]"; //value for split
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            t1.speak(pointer_name[0], TextToSpeech.QUEUE_FLUSH, null);
        }
        else if (s_id == 2) //if vol. down pressed
        {
            contact_selection_check=true;
            if(contact_pointer==contact_list_size-1) //If at end
                contact_pointer=-1;
            c_pointer = listStrings.get(++contact_pointer);//current pointer
            splitter = "[:]"; //value for split
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            t1.speak(pointer_name[0], TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    select_contact(1);  //contact navigation/traversing
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    //TODO
                    select_contact(2);   //contact navigation/traversing
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        isActive = true;
        sound_feedback();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isActive=false;
        if(t1 != null)
            t1.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
        isActive=false;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(onLong)
        {
            deltaX = Math.abs(last_x - sensorEvent.values[0]);
            deltaY = Math.abs(last_y - sensorEvent.values[1]);
            deltaZ = Math.abs(last_z - sensorEvent.values[2]);



            if (deltaX >= -0.4 && deltaY >= -0.4 && deltaZ >=6  )//gesture for call (front gesture)
            {
                callingContact();
            }

            if((float)Math.round(sensorEvent.values[0])>8.0000) //left gesture
            {
                t1.speak("Left gesture. ", TextToSpeech.QUEUE_FLUSH, null);
                binaryLeftSelection();
                timesCheck();
                reSetter();
            }

            else if((float)Math.round(sensorEvent.values[0])<-8.0000) //right gesture
            {
                t1.speak("Right gesture. ", TextToSpeech.QUEUE_FLUSH, null);
                binaryRightSelection();
                timesCheck();
                reSetter();
            }

            last_x = sensorEvent.values[0];
            last_y = sensorEvent.values[1];
            last_z = sensorEvent.values[2];
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    void  timesCheck()
    {
        if(times>=3)
        {
            Intent open_contacts = new Intent(getBaseContext(), Contacts.class);
            open_contacts.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open_contacts);
        }
    }

    void binaryLeftSelection()
    {
        String  temp1="",temp2;
        if(binary_contact_list_size>2) {
         /*   binary_contact_list_size = (binary_contact_list_size / 2);
            left_end = binary_contact_list_size;
           left_start=binary_contact_list_size-left_end;  */

            //new

        /*   BILAL
           left_start=left_start;
            right_end=left_end;
            mean=(left_start+right_end)/2;
            binary_contact_list_size = (binary_contact_list_size / 2);
            left_end=mean;
            right_start=left_end+1; */

            //new

            if(last_was_right)
            {
                binary_contact_list_size=binary_contact_list_size/2;
                int mean=(left_start+left_end)/2;
                right_end=left_end;
                left_start=left_start;
                left_end=mean;
                right_start=mean+1;
            }

            else
            {
                binary_contact_list_size=binary_contact_list_size/2;
                left_start=left_start;
                right_end=left_end;
                left_end=(left_start+left_end)/2;
                right_start=left_end+1;
            }



            // binary split
            c_pointer = listStrings.get(left_start);//current pointer
            splitter = "[:]"; //value for split
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            temp_name= temp1=pointer_name[0];

            //set contact_pointer so user can also traverse using vol. keys
            contact_pointer=left_end;

            //split for left_end
            c_pointer = listStrings.get(left_end);//current pointer
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            temp2=pointer_name[0];
            System.out.println("TESTING12467START(LEFT_SELECTION)=left_start= "+left_start+" left_end= "+left_end+" right_start= "+right_start+" right_end "+ right_end+ " size="+binary_contact_list_size);
            t1.speak("Chunk from "+temp1+" . To "+temp2, TextToSpeech.QUEUE_FLUSH, null);
        }

        else
        {
            times++;
            contact_selection_check=true;
            t1.speak(temp_name, TextToSpeech.QUEUE_FLUSH, null);
        }
        last_was_left=true;
        last_was_right=false;
    }

    void binaryRightSelection()
    {
        String temp3="",temp4;
        if(binary_contact_list_size>2) {
        /*    temp = binary_contact_list_size;
            binary_contact_list_size = (binary_contact_list_size / 2);
            right_start = (binary_contact_list_size + 1);
            right_end = temp - 1; */

            //new

            if(last_was_left)
            {
                int mean=(right_start+right_end)/2;
                right_end=right_end;
                left_start=right_start;
                left_end=mean;
                right_start=mean+1;
                binary_contact_list_size=binary_contact_list_size/2;
            }

            else
            {
                left_start=right_start;
                right_end=right_end;
                left_end=(left_start+right_end)/2;
                right_start=left_end+1;
                binary_contact_list_size=binary_contact_list_size/2;

            }
         /*    BILAL
            left_start=right_start;
            right_end=right_end;
            mean=(left_start+right_end)/2;
            binary_contact_list_size = (binary_contact_list_size / 2);
            left_end=mean;
            right_start=left_end+1;  */

            // binary split
            c_pointer = listStrings.get(right_end);//current pointer
            splitter = "[:]"; //value for split
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            temp3=pointer_name[0];

            //set contact_pointer so user can also traverse using vol. keys
            contact_pointer=right_start;

            //split for right_start
            c_pointer = listStrings.get(right_start);//current pointer
            pointer_name = c_pointer.split(splitter);//has name value after splitting
            temp_name= temp4=pointer_name[0];
            System.out.println("TESTING12467START(RIGHT_SELECTION)=left_start= "+left_start+" left_end= "+left_end+" right_start= "+right_start+" right_end "+ right_end+ " size="+binary_contact_list_size);
            t1.speak("Chunk from "+temp4+" . To "+temp3, TextToSpeech.QUEUE_FLUSH, null);
        }

        else {
            times++;
            contact_selection_check=true;
            t1.speak(temp_name, TextToSpeech.QUEUE_FLUSH, null);
        }
        last_was_left=false;
        last_was_right=true;
    }

  /*  double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    } */


    void reSetter()
    {
        deltaX = 0; //resetting values
        deltaY = 0;
        deltaZ =0;
        sensorManager.unregisterListener(this);
        onLong=false;
    }

    void callingContact()
    {
        deltaX = 0; //resetting values
        deltaY = 0;
        deltaZ =0;
        sensorManager.unregisterListener(this);
        if(contact_selection_check) {
            Intent phoneCallIntent = new Intent(Intent.ACTION_CALL); //
            phoneCallIntent.setPackage("com.android.phone");
            phoneCallIntent.setData(Uri.parse("tel:" + pointer_name[1])); //edit here and see what string to use
            t1.speak("Calling " + pointer_name[0], TextToSpeech.QUEUE_FLUSH, null);

            try {     // waits for 2sec(2000msec)
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(phoneCallIntent);

        }
        else
        {
            t1.speak("Please select a contact using volume keys or by gestures.", TextToSpeech.QUEUE_FLUSH, null);
        }
        onLong=false;
        sensor_switchOn();
    }

}
