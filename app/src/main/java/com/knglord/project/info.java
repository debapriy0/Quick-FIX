package com.knglord.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class info extends AppCompatActivity {
    ImageView pre,next,move;
    TextView info;
    LinearLayout dot;
    TextView[] dots;
    ViewPager slideviewpager;
    int currenttext;
    viewpager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        info = findViewById(R.id.info);
        pre = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        move = findViewById(R.id.move);
        dot = findViewById(R.id.dot);
        slideviewpager = findViewById(R.id.viewpager);
        vp = new viewpager(this);
        slideviewpager.setAdapter(vp);
        pre.setVisibility(View.INVISIBLE);
        move.setVisibility(View.INVISIBLE);
        adddot(0);
        slideviewpager.addOnPageChangeListener(lt);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideviewpager.setCurrentItem(currenttext+1);
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideviewpager.setCurrentItem(currenttext-1);
            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.next_transition, R.anim.next_background);
            }
        });
    }

    private void adddot(int pos){
        dots = new TextView[3];
        dot.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(45);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            dot.addView(dots[i]);
        }
        if(dots.length>0)
            dots[pos].setTextColor(getResources().getColor(R.color.splash));
    }

    ViewPager.OnPageChangeListener lt = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            adddot(position);
            currenttext=position;
            if(position==0){
                pre.setVisibility(View.INVISIBLE);
                move.setVisibility(View.INVISIBLE);
                next.setEnabled(true);
            }
            else if(position==dots.length-1){
                pre.setVisibility(View.VISIBLE);
                next.setVisibility(View.INVISIBLE);
                move.setVisibility(View.VISIBLE);
            }
            else {
                pre.setVisibility(View.VISIBLE);
                move.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}