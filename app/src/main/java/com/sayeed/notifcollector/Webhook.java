package com.sayeed.notifcollector;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.os.Build;

public class Webhook {
    private static final String URL = "https://script.google.com/macros/s/AKfycbzSgyXtB82ORLJ1QNQohPBCQnE9QFH4xoGiUmAJXisCv-R7tamO9OFj0aBO-SpTH1sN/exec";

    public static void send(Context ctx, String appName, String title, String message) {
        String encodedTitle = title == null ? "" : title.replace("\"", "'");
        String encodedMsg = message == null ? "" : message.replace("\"", "'");

        String jsonBody = "{\"appName\":\"" + appName + "\",\"title\":\"" + encodedTitle
        + "\",\"message\":\"" + encodedMsg + "\",\"device\":\"" + Build.MODEL + "\"}";

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {},
                error -> {}) {
            @Override
            public byte[] getBody() {
                return jsonBody.getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        Volley.newRequestQueue(ctx).add(request);
    }
}
