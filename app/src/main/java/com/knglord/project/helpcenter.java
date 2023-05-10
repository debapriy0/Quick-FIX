package com.knglord.project;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class helpcenter extends AppCompatActivity{
    TextView nvdevice,nvuname;
    public ImageView navlogo;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpcenter);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //drawerLayout.clearFocus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("Information");
        setSupportActionBar(toolbar);
        toolbar.setCollapsible(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.getMenu().findItem(R.id.Admin).setVisible(false);
        FirebaseUser xuser = FirebaseAuth.getInstance().getCurrentUser();
        if(xuser==null){
            navigationView.getMenu().findItem(R.id.Account).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Pendingorder).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Myref).setEnabled(false);
            navigationView.getMenu().findItem(R.id.Logout).setVisible(false);
        }
        if(xuser != null) {
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
            View head = navigationView.getHeaderView(0);
            navlogo = head.findViewById(R.id.mkview);
            nvdevice = head.findViewById(R.id.udevice);
            nvuname = head.findViewById(R.id.uname);
            nvdevice.setText(Build.MODEL);
            Glide.with(helpcenter.this).load(xuser.getPhotoUrl()).into(navlogo);
            Pattern pat = Pattern.compile("\\b\\w+@\\b");
            Matcher mat = pat.matcher(xuser.getEmail());
            while (mat.find()) {
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
                        Intent homeintent = new Intent(getApplicationContext(),MainActivity.class);
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
                        Intent pendingintent = new Intent(getApplicationContext(),pendingorder.class);
                        startActivity(pendingintent);
                        overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        break;
                    case R.id.Helpcenter:
                        drawer.closeDrawers();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
