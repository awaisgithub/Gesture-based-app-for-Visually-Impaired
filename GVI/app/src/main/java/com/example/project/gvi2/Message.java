package com.example.project.gvi2;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.hardware.Sensor;

/**
 * Created by LENOVO on 3/31/2016.
 */
public class Message extends Activity implements SensorEventListener {
    public static boolean onLong;//long is pressed
    public static boolean isActive; //we are in messaging app
    boolean contact_selection_check;
    StringBuffer output;
    int message_pointer;//to point selected message
    ContentResolver contentResolver;
    Cursor cursor,cursor1,temp_cursor;
    Uri CONTENT_URI = Uri.parse("content://sms/inbox");// /coversations (alternate)
    TextToSpeech t1;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    Button btn_messaging;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float last_x = 0, last_y = 0, last_z = 0;
    public Vibrator v;
    int unreadMessagesCount;
    String contact_msgData = "";//the message
    String contact_phone_no = "";//number
    String contact_name = "";     // name
    String  SmsMessageId=""; //id
    int top=1; //check variable to make the unread message list circular queue
    boolean check=false; //if any message is selected
    boolean  open_thread_check=false; //for multiple messagess of same thread
    String contact_thread_id="",temp_contact_thread_id=""; //for a selected thread
    String [] same_thread= new String[100];
    int [][] temp_same_thread_occurence = new int[100][2];
    int row=0,temp_row=0;
    List<String> l1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        l1 = new ArrayList<String>();
        onLong = false;
        isActive = true;
        contact_selection_check = false;
        message_pointer = -1;
        output = new StringBuffer();
        contentResolver = getContentResolver();
        cursor = getContentResolver().query(CONTENT_URI, null, "read = 0", null, null); //if read==1 then only read messages. 0 for unread.
        temp_cursor= getContentResolver().query(CONTENT_URI, null, "read = 0", null, null);
        cursor1=getContentResolver().query(CONTENT_URI, null, "read = 0", null, null);
        startManagingCursor(cursor);
        cursor.moveToFirst();
        if(cursor1.moveToFirst())
            do {
                l1.add(cursor1.getString(cursor1.getColumnIndex("_id")));
            }while(cursor1.moveToNext());
        for(Object object : l1) {
            String element = (String) object;
            System.out.println("OKAY="+element);
        }
        unreadMessagesCount = cursor.getCount(); //total number of unread messages
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    sound_feedback(unreadMessagesCount);
                }
            }
        });
        btn_messaging = (Button) findViewById(R.id.button_messaging);
        btn_messaging.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {    //LONG PRESS
                // TODO Auto-generated method stub
                onLong = true;
                sensor_switchOn(); //registers accelerometer
                return true;
            }
        });
        btn_messaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
    }

    private String getContactName(String phone) //fetches name of a contact from a number
    {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor1 = getContentResolver().query(uri, projection, null, null, null);
        if (cursor1.moveToFirst())
        {
            return cursor1.getString(0);
        }
        else
        {
            return "unknown number";
        }
    }

    void sensor_switchOn()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    void sound_feedback(int msg_count)
    {
        String messages_app_feedback = "Messages opened. You have. " + msg_count + "Unread messages. Give gesture to read message. Use Volume keys to select the sender.";
        if(msg_count>0)
            t1.speak(messages_app_feedback, TextToSpeech.QUEUE_FLUSH, null);
        else if(msg_count==0)
            t1.speak("Messages opened. You have. no "  + "Unread message. ", TextToSpeech.QUEUE_FLUSH, null);
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
                    System.out.println("TESTEROR(up)="+top);
                    //TODO
                    if (unreadMessagesCount > 0)
                    {
                        check = true;
                        if (top == 1||top==2)
                        {
                            cursor.moveToLast();
                            top = unreadMessagesCount;
                        } else if (top > 1)
                        {
                            cursor.moveToPrevious();
                            --top;
                        }
                        readMessages();
                    }
                    else
                        t1.speak("No unread messages ", TextToSpeech.QUEUE_FLUSH, null);
                    return true;
                }

            case KeyEvent.KEYCODE_VOLUME_DOWN://down key
                if (action == KeyEvent.ACTION_DOWN)
                {
                    System.out.println("TESTEROR(down)="+top);
                    if(unreadMessagesCount>0)
                    {
                        check = true;
                        if (top == 1)
                        {
                            cursor.moveToFirst();
                            ++top;
                        } else if (top > 1 && top < unreadMessagesCount) {
                            cursor.moveToNext();
                            ++top;
                        } else if (top == unreadMessagesCount) {
                            cursor.moveToLast();
                            top = 1;
                        }
                        readMessages();
                    }
                    else
                        t1.speak("No unread messages ", TextToSpeech.QUEUE_FLUSH, null);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void readMessages()     //navigates between unread messages only(doesnot read) ; selects contact on vol. key
    {
        contact_msgData = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        contact_phone_no = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        contact_name = getContactName(contact_phone_no);
        SmsMessageId= cursor.getString(cursor.getColumnIndex("_id"));
        contact_thread_id=cursor.getString(cursor.getColumnIndexOrThrow("thread_id")); //to mark a message as read

        //get all sms_id's, on readselectedmessage, make a loop on the basis of that thread_ID and match it with sms_id(list) and read out that message.


        String messages_readMessage = "Message from ";
        if (contact_name != "unknown number")
        {
            t1.speak(messages_readMessage, TextToSpeech.QUEUE_FLUSH, null);
            while (t1.isSpeaking()) {}
            t1.speak(contact_name, TextToSpeech.QUEUE_FLUSH, null);
        }
        else
        {
            t1.speak(messages_readMessage, TextToSpeech.QUEUE_FLUSH, null);
            while (t1.isSpeaking()) {}
            t1.speak(contact_phone_no, TextToSpeech.QUEUE_FLUSH, null);
        }
        if(unreadMessagesCount==0)
        {
            t1.speak("No unread messages ", TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        deltaX = Math.abs(last_x - sensorEvent.values[0]);
        deltaY = Math.abs(last_y - sensorEvent.values[1]);
        deltaZ = Math.abs(last_z - sensorEvent.values[2]);

        if ((deltaX >= -0.5 && deltaY >= 3 && deltaZ >=5 ))//gesture for reading unread Message (threshold values)
        {
            if(unreadMessagesCount>0&&check)
            {
                readSelectedMessage();
                while (t1.isSpeaking()) {
                }
                openMessageThread();   //marks a message as read
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reOpen();
            }
            else
                t1.speak("Please select a message using volume keys ", TextToSpeech.QUEUE_FLUSH, null);
        }

        last_x = sensorEvent.values[0];
        last_y = sensorEvent.values[1];
        last_z = sensorEvent.values[2];


    }
    public void reOpen()
    {
        Intent defineIntent_relaunchMessaages = new Intent(getApplicationContext(),Message.class);
        defineIntent_relaunchMessaages.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        defineIntent_relaunchMessaages.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(defineIntent_relaunchMessaages);
    }

    public void readSelectedMessage() //to read the message body of the selected thread (cursor pointing currently)
    {
        deltaX = 0; //resetting values
        deltaY = 0;
        deltaZ =0;
        sensorManager.unregisterListener(this);


        Cursor cc1;
        String msg_id="";
        cc1=getContentResolver().query(CONTENT_URI, null, "thread_id="+contact_thread_id, null, null);
        if(cc1.moveToFirst())
        {
            do {
                msg_id=cc1.getString(cc1.getColumnIndex("_id"));
                for(Object object : l1) {
                    String element = (String) object;
                    if(element.equals(msg_id))    //strict comparison
                    {
                        if(check) {
                            contact_msgData=cc1.getString(cc1.getColumnIndexOrThrow("body"));
                            t1.speak("The message is. " + contact_msgData, TextToSpeech.QUEUE_FLUSH, null);
                            while (t1.isSpeaking()) {
                            }
                        }
                    }
                }
            }while(cc1.moveToNext());
        }

        else
            t1.speak("Please, Select a message using volume keys first.", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void openMessageThread() //removing shutter notification
    {
        Intent defineIntent_inbox = new Intent(Intent.ACTION_VIEW); //for opening thread
        defineIntent_inbox.setData(Uri.parse("content://mms-sms/conversations/" + contact_thread_id));
        startActivity(defineIntent_inbox);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    protected void onPause() {
        isActive=false;
        if(t1 != null)
            t1.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        isActive = true;
        sound_feedback(unreadMessagesCount);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        if(cursor!=null && cursor1!=null)
        {
            cursor.close();
            cursor1.close();
        }
        super.onDestroy();
        isActive=false;
    }


}
