package com.example.project.gvi2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver {
    public AutoStart() {
    }

    @Override
    public void onReceive(Context arg0, Intent arg1)
    {   //calls MyService
        Intent intent = new Intent(arg0,MyService.class);
        arg0.startService(intent);
    }

}

