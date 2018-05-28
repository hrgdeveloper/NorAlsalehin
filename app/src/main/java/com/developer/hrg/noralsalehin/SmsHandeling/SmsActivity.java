package com.developer.hrg.noralsalehin.SmsHandeling;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developer.hrg.noralsalehin.R;

public class SmsActivity extends AppCompatActivity {
  FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        fragmentManager=getSupportFragmentManager();
        openFragment(new Register_fragment());
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.smsContainer,fragment);
        fragmentTransaction.commit();
    }
}
