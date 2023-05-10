package com.knglord.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class forget_pass extends AppCompatActivity {
    EditText email,name;
    String txtemail,txtname;
    Button reset;
    private FirebaseAuth mAuth;
    public DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        if(Build.VERSION.SDK_INT >= 21){
            Window w =this.getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(this.getResources().getColor(R.color.splash));
        }
        mref = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtemail);
        name = findViewById(R.id.txtName);
        txtname = name.getText().toString();
        txtemail = email.getText().toString();
        reset = findViewById(R.id.btnReset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtname = name.getText().toString();
                txtemail = email.getText().toString();
                txtemail = txtemail.trim();
                if (TextUtils.isEmpty(txtemail)) {
                    Toast.makeText(getApplicationContext(), "Please enter email!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(txtname)) {
                    Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(txtemail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),login.class));
                                    mref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                String stat = String.valueOf(ds.child("email").getValue());
                                                if (Objects.equals(stat, txtemail)) {
                                                    Log.d("name",ds.child("email").getValue(String.class));
                                                    Log.e("pass",ds.child("pass").getValue(String.class));
                                                    //ds.getRef().child("pass").setValue(nameval);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),login.class));
        overridePendingTransition(R.anim.back_transition, R.anim.background);
        this.finish();
    }
}
