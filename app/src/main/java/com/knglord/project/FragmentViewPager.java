package com.knglord.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentViewPager extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        final ImageView imageView = (ImageView) v.findViewById(R.id.image);
        imageView.setBackgroundResource(getArguments().getInt("img"));
        imageView.setId(getArguments().getInt("id"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("id--", String.valueOf(imageView.getId()));
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra("id",String.valueOf(imageView.getId()));
                //startActivity(intent);
            }
        });

        return v;
    }

    public static FragmentViewPager newInstance(int image,int x) {

        FragmentViewPager f = new FragmentViewPager();
        Bundle b = new Bundle();
        b.putInt("img", image);
        b.putInt("id",x);
        f.setArguments(b);
        return f;
    }
}
