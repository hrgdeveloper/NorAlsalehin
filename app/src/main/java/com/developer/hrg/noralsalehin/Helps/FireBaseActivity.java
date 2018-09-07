package com.developer.hrg.noralsalehin.Helps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.hrg.noralsalehin.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FireBaseActivity extends AppCompatActivity {

    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);
        tv_message=(TextView)findViewById(R.id.tv_message_firebase);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Toast.makeText(FireBaseActivity.this, message, Toast.LENGTH_SHORT).show();
        tv_message.setText(message);


    }

    @Override
    public void onBackPressed() {

    }
}
