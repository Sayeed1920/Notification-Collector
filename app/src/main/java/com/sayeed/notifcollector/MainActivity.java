package com.sayeed.notifcollector;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Notification Collector চালু আছে।\n\nদুটি পারমিশন দিন:\n1. Notification Access\n2. Phone & Call Log");
        tv.setPadding(40, 100, 40, 40);
        tv.setTextSize(16);

        Button btnNotif = new Button(this);
        btnNotif.setText("Notification Access দিন");
        btnNotif.setOnClickListener(v ->
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)));

        Button btnCall = new Button(this);
        btnCall.setText("Call / Phone Permission দিন");
        btnCall.setOnClickListener(v ->
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG
                }, 1));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.addView(tv);
        layout.addView(btnNotif);
        layout.addView(btnCall);
        setContentView(layout);
    }
}
