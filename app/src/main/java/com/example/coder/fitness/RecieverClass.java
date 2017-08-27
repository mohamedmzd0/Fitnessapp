package com.example.coder.fitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecieverClass extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent it) {
        if (!ServicesClass.started)
            context.startService(new Intent(context, ServicesClass.class));
        ServicesClass.PushNotificattion(context);
    }
}
