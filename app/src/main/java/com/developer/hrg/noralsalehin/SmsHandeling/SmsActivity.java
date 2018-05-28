package com.developer.hrg.noralsalehin.SmsHandeling;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.R;

public class SmsActivity extends AppCompatActivity {
  FragmentManager fragmentManager;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo=new UserInfo(SmsActivity.this);

        setContentView(R.layout.activity_sms);
        fragmentManager=getSupportFragmentManager();
        if (userInfo.get_isMobileSent()) {
            openFragment(new Verify_Fragment());
        }else {
            openFragment(new Register_fragment());
        }

    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.smsContainer,fragment);
        fragmentTransaction.commit();
    }
}
