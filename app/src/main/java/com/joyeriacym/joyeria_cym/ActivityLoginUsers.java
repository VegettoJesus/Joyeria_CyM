package com.joyeriacym.joyeria_cym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.joyeriacym.joyeria_cym.R;

public class ActivityLoginUsers extends AppCompatActivity {
    EditText textEmail, textPassword;
    Button buttonIniciarSesion;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        asignarButtonMain();
        asignarButtonSesion();
    }
    private void asignarButtonMain(){
        textView2 = findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLoginUsers.this,ActivityRegistrarUser.class);
                startActivity(intent);
            }
        });
    }
    private void asignarButtonSesion(){
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLoginUsers.this, ActivityMainUser.class);
                startActivity(intent);
            }
        });
    }
}