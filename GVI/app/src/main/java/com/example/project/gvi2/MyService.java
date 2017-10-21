package com.example.project.gvi2;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;
import android.content.Intent;
import android.os.IBinder;


public class MyService extends Service implements SensorEventListener {
    private float last_x=0, last_y=0, last_z=0;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDevice s1;//service object
    private Message m1; //messaging object
    private Contacts con0;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;
    public static Vibrator v;
    public static boolean on=false;
    KeyguardManager myKM; //variable
    PowerManager powerManager;
    private static final int DELAY = 1000000;
    int defTimeOut = 0;

    public MyService() {
    }

    @Override
    public void onCreate() {
        myKM=(KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE); //to check screen lock
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (!on) {

            on = true;
            super.onCreate();
            s1 = new ShakeDevice();
            defTimeOut = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT, DELAY);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, DELAY);
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                // to check if accelerometer exists

                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                vibrateThreshold = accelerometer.getMaximumRange() / 2;
            } else {
                // fail! we dont have an accelerometer!
            }

            //initialize vibration
            v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started ", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        deltaX = Math.abs(last_x - event.values[0]);
        deltaY = Math.abs(last_y - event.values[1]);
        deltaZ = Math.abs(last_z - event.values[2]);
        myKM=(KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE); //to check screen lock
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if(!myKM.isKeyguardLocked()&&powerManager.isScreenOn())
        {

            //vigourous shake
            if ((deltaX >= 10 && deltaY >= 10 && deltaZ >= 10) && (s1.isActive == false) && (s1.on == false)&&(con0.isActive==false)&&(m1.isActive==false)&&(con0.onLong==false)) {
                v.vibrate(500);
                Intent i = new Intent(this, ShakeDevice.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(i);

                return;
            } else if ((deltaX >= 10 && deltaY >= 10 && deltaZ >= 10) && (s1.on == true)&&(con0.onLong==false))      //use single top flag
            {
                v.vibrate(500);
                Toast.makeText(this, "Service Started ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ShakeDevice.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //for starting the app from start ,we use flags
                this.startActivity(i);
                //make changes here if you want your app to return to main screen(user wants to listen to instructions again) after shake
            }
            else if ((deltaX >= 10 && deltaY >= 10 && deltaZ >= 10) && (m1.isActive==true))
            {
                v.vibrate(500);
                Toast.makeText(this, "Service Started ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ShakeDevice.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //for starting the app from start ,we use flags
                this.startActivity(i);
            }
            else if ((deltaX >= 10 && deltaY >= 10 && deltaZ >= 10) && (con0.isActive==true))
            {
                v.vibrate(500);
                Toast.makeText(this, "Service Started ", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ShakeDevice.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //for starting the app from start ,we use flags
                this.startActivity(i);
            }
            last_x = event.values[0];
            last_y = event.values[1];
            last_z = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
