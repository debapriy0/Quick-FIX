package com.knglord.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.InterpolatorRes;
import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pendingorder extends AppCompatActivity{
    private TextView nvuname,nvdevice,ptv,dntv,adp,cost;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    ProgressDialog progress;
    ImageView navlogo;
    FirebaseUser xuser;
    String devicename,problem;
    private LinearLayout lin,mainL,detailsL,l,line;
    Bitmap bill;
    DatabaseReference mref;
    LottieAnimationView lv;
    private Button bt;
    private Boolean check=false;
    RelativeLayout view;
    SwipeRefreshLayout swiperefresh;
    @SuppressLint({"RestrictedApi", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendingorder);
        lin=findViewById(R.id.linlayout);
        swiperefresh=findViewById(R.id.swiperefresh);
        view = findViewById(R.id.info);
        view.setVisibility(View.GONE);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View head = navigationView.getHeaderView(0);
        navlogo = head.findViewById(R.id.mkview);
        nvdevice = head.findViewById(R.id.udevice);
        nvuname = head.findViewById(R.id.uname);
        nvdevice.setText(Build.MODEL);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        mref = FirebaseDatabase.getInstance().getReference("users-order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser != null)
            Glide.with(pendingorder.this).load(xuser.getPhotoUrl()).into(navlogo);
        //drawerLayout.clearFocus();
        view();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("Pending Requests");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()){
                System.out.println("Match: " + mat.group());
                nvuname.setText(mat.group());
            }
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                DrawerLayout drawer = findViewById(R.id.dl);
                switch (id) {
                    case R.id.Home:
                        Intent homeintent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(homeintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.login:
                        Intent login=new Intent(getApplicationContext(),login.class);
                        startActivity(login);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Account:
                        Intent accountintent = new Intent(getApplicationContext(), account.class);
                        startActivity(accountintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Myorder:
                        Intent myorderintent = new Intent(getApplicationContext(),myorder.class);
                        startActivity(myorderintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Pendingorder:
                        drawer.closeDrawers();
                        break;
                    case R.id.Helpcenter:
                        Intent helpintent = new Intent(getApplicationContext(),helpcenter.class);
                        startActivity(helpintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Myref:
                        Intent myrefintent = new Intent(getApplicationContext(),myref.class);
                        startActivity(myrefintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Aboutus:
                        Intent aboutintent = new Intent(getApplicationContext(), aboutus.class);
                        startActivity(aboutintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        drawer.closeDrawers();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public void view(){
        if(mref != null) {
            progress = new ProgressDialog(pendingorder.this);
            progress.setTitle("Loading");
            progress.setMessage("Checking From Server");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull final DataSnapshot dataSnapshot) {
                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        String stat = String.valueOf(ds.child("status").getValue());
                        if (Objects.equals(stat,String.valueOf(0))) {
                            Log.d("new ", stat);
                            check=true;
                            //lv = new LottieAnimationView(getApplicationContext());               //animation Lottie
                            l = new LinearLayout(getApplicationContext());
                            mainL = new LinearLayout(getApplicationContext());
                            detailsL = new LinearLayout(getApplicationContext());
                            ImageView img = new ImageView(getApplicationContext());
                            line = new LinearLayout(getApplicationContext());
                            line.setBackgroundColor(Color.parseColor("#cfcfcf"));
                            LinearLayout.LayoutParams linel = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
                            linel.setMargins(20, 15,8,-15);
                            line.setLayoutParams(linel);
//                            lv.setAnimation(R.raw.deleteslash);
//                            lv.pauseAnimation();
//                            lv.setLayoutParams(new LayoutParams(700, 150));
//                            lv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                            lv.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    lv.animate().translationX(100).setDuration(1000).start();
//                                    lv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 150));
//                                    lv.playAnimation();
//                                    lv.addAnimatorListener(new AnimatorListenerAdapter() {
//                                        @Override
//                                        public void onAnimationEnd(Animator animation) {
//                                            super.onAnimationEnd(animation);
//
//                                        }
//                                    });
//                                }
//                            });
                            l.setElevation(5);
                            l.setBackgroundResource(R.drawable.order_item_bg);
                            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp1.setMargins(5, 5,5,15);
                            l.setLayoutParams(lp1);
                            l.setPadding(5, 0, 0, 0);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.setMargins(5, 5, 5, 0);
                            l.setOrientation(LinearLayout.VERTICAL);
                            mainL.setOrientation(LinearLayout.HORIZONTAL);
                            detailsL.setOrientation(LinearLayout.VERTICAL);
                            final int x= Integer.parseInt(String.valueOf(ds.child("id").getValue()));
                            l.setId(x);
                            Log.e("id -- ", String.valueOf(x));
                            img.setImageResource(R.drawable.ic_home);
                            img.setLayoutParams(new LayoutParams(200, 200));
                            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            mainL.addView(img);
                            dntv = new TextView(getApplicationContext());
                            ptv = new TextView(getApplicationContext());
                            adp = new TextView(getApplicationContext());
                            cost = new TextView(getApplicationContext());
                            bt = new Button(getApplicationContext());
                            adp.setLayoutParams(lp);
                            dntv.setLayoutParams(lp);
                            ptv.setLayoutParams(lp);
                            cost.setLayoutParams(lp);
                            LinearLayout.LayoutParams btlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            //btlp.setMarginStart(10);
                            btlp.setMargins(10,0, 0, -10);
                            bt.setLayoutParams(btlp);
                            bt.setBackgroundResource(R.drawable.remove_item_bt);
                            bt.setGravity(Gravity.CENTER);
                            bt.setText("Remove Item");
                            bt.setTextColor(Color.parseColor("#F66262"));
                            //Log.e("snapshot", String.valueOf(ds.getChildren()));
                            Log.e("dskey////", String.valueOf(ds.getKey()));
                            Log.e("dsval////", String.valueOf(ds.getValue()));
                            //Log.e("dskey////val", String.valueOf(ds.child("devicename").getValue()));
                            //Toast.makeText(getApplicationContext(),"mref"+mref,Toast.LENGTH_LONG);
                            //if (Objects.equals(ds.getKey(), xuser.getUid())) {
                            Log.d("check-", String.valueOf(check));
                            Log.e("mref-", String.valueOf(mref));
                            adp.setText("Other Problem - "+ ds.child("additionprob").getValue());
                            cost.setText("Cost \u20B9"+ ds.child("cost").getValue());
                            //Log.e("ds", String.valueOf(ds));
                            dntv.setText("Device Name - "+ ds.child("devicename").getValue());
                            ptv.setText("Problem Selected - "+String.valueOf(ds.child("problems").getValue()).replace("[","").replace("]", ""));
                            detailsL.addView(dntv);
                            detailsL.addView(ptv);
                            detailsL.addView(adp);
                            detailsL.addView(cost);
                            mainL.addView(detailsL);
                            l.addView(mainL);
                            l.addView(line);
                            l.addView(bt);
                            //l.addView(lv);
                            lin.addView(l);
                            //lin.addView(img);
                            //lin.addView(imgc);
                            //}
                            bt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //delete(x);
                                    //mref.child(ds.getKey()).removeValue();
                                    //lin.removeAllViews();
                                    l=findViewById(x);
                                    Log.e("layout -- ", String.valueOf(l.getId()));
                                    l.animate().setDuration(1000).translationXBy(1500).start();
                                    l.animate().setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            Toast.makeText(pendingorder.this, "Order Deleted", Toast.LENGTH_SHORT).show();
                                            mref.child(ds.getKey()).removeValue();
                                            lin.removeAllViews();
                                        }
                                    });
                                }
                            });
                        }
                    }
                    Log.d("check_after-", String.valueOf(check));
                    if(!check){
                        view.setVisibility(View.VISIBLE);
                        Log.e("view==firstinside/", String.valueOf(view.getVisibility()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            progress.dismiss();
        }
//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                lin.removeAllViews();
//        //        final ImageView img = new ImageView(this);
//        //        ImageView imgc = new ImageView(this);
//                if(mref != null) {
//                    mref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NotNull final DataSnapshot dataSnapshot) {
//                            for (final DataSnapshot ds : dataSnapshot.getChildren()) {
//                                String stat = String.valueOf(ds.child("status").getValue());
//                                if (Objects.equals(stat,String.valueOf(0))) {
//                                    Log.d("new ", stat);
//                                    check=true;
//                                    //lv = new LottieAnimationView(getApplicationContext());               //animation Lottie
//                                    l = new LinearLayout(getApplicationContext());
//                                    mainL = new LinearLayout(getApplicationContext());
//                                    detailsL = new LinearLayout(getApplicationContext());
//                                    ImageView img = new ImageView(getApplicationContext());
//                                    line = new LinearLayout(getApplicationContext());
//                                    line.setBackgroundColor(Color.parseColor("#cfcfcf"));
//                                    LinearLayout.LayoutParams linel = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5));
//                                    linel.setMargins(20, 15,8,-15);
//                                    line.setLayoutParams(linel);
//                                    l.setElevation(5);
//                                    l.setBackgroundResource(R.drawable.order_item_bg);
//                                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    lp1.setMargins(5, 5,5,15);
//                                    l.setLayoutParams(lp1);
//                                    l.setPadding(5, 0, 0, 0);
//                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    lp.setMargins(5, 5, 5, 0);
//                                    l.setOrientation(LinearLayout.VERTICAL);
//                                    mainL.setOrientation(LinearLayout.HORIZONTAL);
//                                    detailsL.setOrientation(LinearLayout.VERTICAL);
//                                    final int x= Integer.parseInt(String.valueOf(ds.child("id").getValue()));
//                                    l.setId(x);
//                                    Log.e("id -- ", String.valueOf(x));
//                                    img.setImageResource(R.drawable.ic_home);
//                                    img.setLayoutParams(new LayoutParams(200, 200));
//                                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                                    mainL.addView(img);
//                                    dntv = new TextView(getApplicationContext());
//                                    ptv = new TextView(getApplicationContext());
//                                    adp = new TextView(getApplicationContext());
//                                    cost = new TextView(getApplicationContext());
//                                    bt = new Button(getApplicationContext());
//                                    adp.setLayoutParams(lp);
//                                    dntv.setLayoutParams(lp);
//                                    ptv.setLayoutParams(lp);
//                                    cost.setLayoutParams(lp);
//                                    LinearLayout.LayoutParams btlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    //btlp.setMarginStart(10);
//                                    btlp.setMargins(10,0, 0, -10);
//                                    bt.setLayoutParams(btlp);
//                                    bt.setBackgroundResource(R.drawable.remove_item_bt);
//                                    bt.setGravity(Gravity.CENTER);
//                                    bt.setText("Remove Item");
//                                    bt.setTextColor(Color.parseColor("#F66262"));
//                                    //Log.e("snapshot", String.valueOf(ds.getChildren()));
//                                    Log.e("dskey////", String.valueOf(ds.getKey()));
//                                    Log.e("dsval////", String.valueOf(ds.getValue()));
//                                    //Log.e("dskey////val", String.valueOf(ds.child("devicename").getValue()));
//                                    //Toast.makeText(getApplicationContext(),"mref"+mref,Toast.LENGTH_LONG);
//                                    //if (Objects.equals(ds.getKey(), xuser.getUid())) {
//                                    Log.d("check-", String.valueOf(check));
//                                    Log.e("mref-", String.valueOf(mref));
//                                    adp.setText("Other Problem - "+ ds.child("additionprob").getValue());
//                                    cost.setText("Cost \u20B9"+ ds.child("cost").getValue());
//                                    //Log.e("ds", String.valueOf(ds));
//                                    dntv.setText("Device Name - "+ ds.child("devicename").getValue());
//                                    ptv.setText("Problem Selected - "+String.valueOf(ds.child("problems").getValue()).replace("[","").replace("]", ""));
//                                    detailsL.addView(dntv);
//                                    detailsL.addView(ptv);
//                                    detailsL.addView(adp);
//                                    detailsL.addView(cost);
//                                    mainL.addView(detailsL);
//                                    l.addView(mainL);
//                                    l.addView(line);
//                                    l.addView(bt);
//                                    //l.addView(lv);
//                                    lin.addView(l);
//                                    //lin.addView(img);
//                                    //lin.addView(imgc);
//                                    //}
//                                    bt.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            //delete(x);
//                                            //mref.child(ds.getKey()).removeValue();
//                                            //lin.removeAllViews();
//                                            l=findViewById(x);
//                                            Log.e("layout -- ", String.valueOf(l.getId()));
//                                            l.animate().setDuration(1000).translationXBy(1500).start();
//                                            l.animate().setListener(new AnimatorListenerAdapter() {
//                                                @Override
//                                                public void onAnimationEnd(Animator animation) {
//                                                    super.onAnimationEnd(animation);
//                                                    Toast.makeText(pendingorder.this, "Order Deleted", Toast.LENGTH_SHORT).show();
//                                                    mref.child(ds.getKey()).removeValue();
//                                                    lin.removeAllViews();
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            }
//                            Log.d("check_after-", String.valueOf(check));
//                            if(!check){
//                                view.setVisibility(View.VISIBLE);
//                                Log.e("view==inside/", String.valueOf(view.getVisibility()));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError error) {
//                        }
//                    });
//                }
//                swiperefresh.setRefreshing(false);
//            }
//        });
    }

    private void delete(final int idx){
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull final DataSnapshot dataSnapshot) {
//                Log.e("info ----- ", String.valueOf(ds.child("id")));
                Log.e("key ", String.valueOf(idx));
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Log.e("l -- ", String.valueOf(l.getId()));
                    String s = String.valueOf(ds.child("id").getValue());
                    //Log.e("l -- ", s);
                    if (Objects.equals(s, String.valueOf(idx))) {
                        l=findViewById(idx);
                        Log.e("layout -- ", String.valueOf(l.getId()));
                        l.animate().setDuration(500).translationXBy(1500).start();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
