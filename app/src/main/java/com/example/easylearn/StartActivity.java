package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    Button skip_sign_in,cust_sign_in,sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        cust_sign_in=findViewById(R.id.sign_in);
        sign_up=findViewById(R.id.sign_up);
        skip_sign_in=findViewById(R.id.skip_sign_in);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartActivity.this, "This feature is currently under development, please proceed with skip sign in button for now", Toast.LENGTH_SHORT).show();
            }
        });
        cust_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StartActivity.this, "This feature is currently under development, please proceed with skip sign in button for now", Toast.LENGTH_SHORT).show();
            }
        });
        skip_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),HomePage.class);
                startActivity(in);
            }
        });
    }
}