package com.bignerdranch.android.weingcraft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Alarm_Reciver extends BroadcastReceiver {
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String message = intent.getExtras().getString( "state" );

        Intent service_intent  = new Intent( context, BlindReservation.class );

        service_intent.putExtra( "state",message );

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }else{
            this.context.startService(service_intent);
        }
    }

}
