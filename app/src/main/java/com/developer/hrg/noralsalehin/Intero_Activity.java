package com.developer.hrg.noralsalehin;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.hrg.noralsalehin.Helps.UserInfo;
import com.developer.hrg.noralsalehin.Main.MainActivity;
import com.developer.hrg.noralsalehin.SmsHandeling.SmsActivity;

import java.util.ArrayList;

public class Intero_Activity extends AppCompatActivity {
   ArrayList<Intro> introes = new ArrayList<>();
    ViewPager viewPager ;
    MyAdapter myAdapter;
    Button btn_next , btn_skip ;
   UserInfo userInfo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intero_);
        userInfo=new UserInfo(Intero_Activity.this);

        defineList();
        viewPager=(ViewPager)findViewById(R.id.viewpager_intro);
        myAdapter=new MyAdapter(Intero_Activity.this,introes);
        viewPager.setAdapter(myAdapter);
        btn_next=(Button)findViewById(R.id.btn_intro_next);
        btn_skip=(Button)findViewById(R.id.btn_intro_skip);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (viewPager.getCurrentItem()+1==introes.size()) {

                   startActivity(new Intent(Intero_Activity.this, SmsActivity.class));
                    userInfo.set_isFirstTime(false);
                   finish();
               }else {
                   int current = viewPager.getCurrentItem();
                   viewPager.setCurrentItem(current+1);
               }

            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intero_Activity.this,SmsActivity.class));
              userInfo.set_isFirstTime(false);
                finish();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem()+1==introes.size()) {
                    btn_next.setText("پایان");
                    btn_skip.setVisibility(View.GONE);
                }else {
                    btn_next.setText("بعدی");
                    btn_skip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void defineList () {
        Intro intro = new Intro(getString(R.string.A_intro_firstTitle),getString(R.string.A_intro_firstText),R.drawable.paper_plane,android.R.color.holo_red_light);
        Intro intro2 = new Intro(getString(R.string.A_intro_secondTitle),getString(R.string.A_intro_secondText),R.drawable.speak,android.R.color.holo_green_light);
        Intro intro3 = new Intro(getString(R.string.A_intro_therdTitle),getString(R.string.A_intro_therdText),R.drawable.easyuse,android.R.color.holo_orange_dark);
        introes.add(intro);
        introes.add(intro2);
        introes.add(intro3);
    }
    public class MyAdapter extends PagerAdapter{
          Context context;
        ArrayList<Intro> intross ;
        public MyAdapter(Context context ,ArrayList<Intro> intross) {
            this.context=context;
            this.intross=intross;
        }

        @Override
        public int getCount() {
            return intross.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_intro,container,false);

            TextView tv_title=(TextView)view.findViewById(R.id.tv_intro_title);
            TextView tv_des = (TextView)view.findViewById(R.id.tv_intro_des);
            ImageView iv_pic = (ImageView)view.findViewById(R.id.iv_intro);
            tv_title.setText(intross.get(position).getTitle());
            tv_des.setText(intross.get(position).getDes());
            iv_pic.setImageDrawable(getResources().getDrawable(intross.get(position).getPic()));
            view.setBackgroundColor(context.getResources().getColor(intross.get(position).getBackground()));
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((RelativeLayout)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view== ((RelativeLayout)object);
        }
    }
}
