package com.knglord.project;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    EditText userName,password;
    Button signIn,registration,forgot;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT >= 21){
            Window w =this.getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.setStatusBarColor(this.getResources().getColor(R.color.splash));
        }
        mAuth = FirebaseAuth.getInstance();
        userName=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        signIn=(Button)findViewById(R.id.btnSignIn);
        registration=(Button)findViewById(R.id.btnRegistration);
        forgot = findViewById(R.id.forgot);
        signIN();
    }

    public void signIN() {

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = userName.getText().toString();

                final String Password = password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && email.equals("admin@cse.com") && Password.equals("admin1098")) {
                            Toast.makeText(getApplicationContext(), "Welcome admin", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), admin_view.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        }
                        else if(task.isSuccessful() && !email.equals("admin@cse.com") && !Password.equals("admin1098")){
                            Pattern pat = Pattern.compile("\\b\\w+@\\b");
                            Matcher mat =  pat.matcher(email);
                            while (mat.find()){
                                System.out.println("Match: " + mat.group());
                            Toast.makeText(getApplicationContext(), "Welcome "+mat.group(), Toast.LENGTH_LONG).show();}
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.next_transition, R.anim.next_background);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration.setTextColor(Color.BLACK);
                Intent intent = new Intent(login.this, registration.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot.setTextColor(Color.BLACK);
                Intent intent = new Intent(login.this, forget_pass.class);
                startActivity(intent);
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
