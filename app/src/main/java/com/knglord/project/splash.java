package com.knglord.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("mypref", MODE_PRIVATE);
        Boolean firstStart = pref.getBoolean("firstStart",true);

        if(firstStart){
            startActivity(new Intent(getApplicationContext(),info.class));
            pref = getSharedPreferences("mypref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstStart", false);
            editor.apply(); // apply changes
            // first start, show your dialog | first-run code goes here
        }
        else {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
