package com.knglord.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class admin_view extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    ImageView navlogo;
    FirebaseUser xuser;
    DatabaseReference mref,sref;
    TableLayout stk;
    TableRow tbrow,trow;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        stk = (TableLayout) findViewById(R.id.showqs);
        table();
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View head = navigationView.getHeaderView(0);
        navlogo = head.findViewById(R.id.mkview);
        sref = FirebaseDatabase.getInstance().getReference("users");
        mref = FirebaseDatabase.getInstance().getReference();
        xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser != null)
            Glide.with(admin_view.this).load(xuser.getPhotoUrl()).into(navlogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Myref).setVisible(false);
        navigationView.getMenu().findItem(R.id.Account).setVisible(false);
        navigationView.getMenu().findItem(R.id.Home).setVisible(false);
        navigationView.getMenu().findItem(R.id.Myorder).setVisible(false);
        navigationView.getMenu().findItem(R.id.Pendingorder).setVisible(false);
        navigationView.getMenu().findItem(R.id.Helpcenter).setVisible(false);
        navigationView.getMenu().findItem(R.id.Aboutus).setVisible(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                DrawerLayout drawer = findViewById(R.id.dl);
                switch (id) {
                    case R.id.login:
                        Intent login=new Intent(getApplicationContext(), com.knglord.project.login.class);
                        startActivity(login);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Admin:
                        drawer.closeDrawers();
                        break;
                    case R.id.Logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        drawer.closeDrawers();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("token--", "getInstanceId failed", task.getException());
//                            return;
//                        }
//                        final String token = task.getResult().getToken();
//                        Log.d("msg--", token);
//                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
//                        if(sref != null && xuser!=null) {
//                            sref.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
//                                        if (Objects.equals(ds.getKey(), xuser.getUid())) {
//                                            //sref.child(xuser.getUid()).child("token").setValue(token);
//                                            //Log.e("user", String.valueOf(ds.getRef().child("token").setValue(token)));
//                                            ds.getRef().child("token").setValue(token);
//                                            final String SUBSCRIBE_TO = token;
//                                            Log.e("token----", SUBSCRIBE_TO);
//                                            FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//                    }
//                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    public void table(){
        stk.removeAllViews();
        tbrow = new TableRow(getApplicationContext());

        TextView tv1 = new TextView(this);
        tv1.setText(" User ID ");
        tv1.setTextSize(20);
        tv1.setPadding(5, 5, 5, 5);
        tv1.setTextColor(Color.BLACK);
        tv1.setGravity(Gravity.CENTER);
        tbrow.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Order ");
        tv2.setTextSize(20);
        tv2.setPadding(5, 5, 5, 5);
        tv2.setTextColor(Color.BLACK);
        tv2.setGravity(Gravity.CENTER);
        tbrow.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Details ");
        tv3.setTextSize(20);
        tv3.setPadding(5, 5, 5, 5);
        tv3.setTextColor(Color.BLACK);
        tv3.setGravity(Gravity.CENTER);
        tbrow.addView(tv3);
        stk.addView(tbrow);
        addvalue();
    }

    public void addvalue(){
        mref = FirebaseDatabase.getInstance().getReference("users-order");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("ds : ", String.valueOf(ds));
                    Log.d("dskey : ", String.valueOf(ds.getKey()));
                    Log.d("dschild : ", String.valueOf(ds.getChildrenCount()));
                trow = new TableRow(getApplicationContext());
                TextView tv = new TextView(getApplicationContext());
                tv.setText(ds.getKey());
                tv.setTextSize(20);
                tv.setPadding(5, 5, 5, 5);
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                trow.addView(tv);

                TextView tv1 = new TextView(getApplicationContext());
                tv1.setText(String.valueOf(ds.getChildrenCount()));
                tv1.setTextSize(20);
                tv1.setPadding(5, 5, 5, 5);
                tv1.setTextColor(Color.BLACK);
                tv1.setGravity(Gravity.CENTER);
                trow.addView(tv1);

                Button view = new Button(getApplicationContext());
                view.setText("VIEW");
                trow.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), order_details.class);
                        intent.putExtra("uid",ds.getKey());
                        startActivity(intent);
                    }
                });

                stk.addView(trow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }
}
