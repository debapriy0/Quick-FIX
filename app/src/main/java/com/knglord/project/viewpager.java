package com.knglord.project;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class viewpager extends PagerAdapter {
    Context context;
    LayoutInflater lf;

    public viewpager(Context context){
        this.context = context;
    }

    public int[] textview = { R.string.op1,R.string.op2,R.string.op3 };

    @Override
    public int getCount() {
        return textview.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        lf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = lf.inflate(R.layout.activity_viewpager,container,false);
        TextView op = v.findViewById(R.id.op);
        op.setText(textview[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}