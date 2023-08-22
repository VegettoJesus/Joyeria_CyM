package com.joyeriacym.joyeria_cym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joyeriacym.joyeria_cym.entidades.Persona;

import java.util.UUID;

public class ActivityRegistrarUser extends AppCompatActivity {
    String sexo = "";
    RadioButton radioButtonMasculino, radioButtonFemenino;
    EditText textNombres,textApellidos, textDNI, textCelular, textEmail, textDireccion, textPassword;
    Button buttonRegistrar;
    FirebaseDatabase database;
    DatabaseReference reference;
    Persona persona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inicializarFirebase();
        textNombres = findViewById(R.id.textNombres);
        textApellidos = findViewById(R.id.textApellidos);
        textCelular = findViewById(R.id.textCelular);
        textDNI = findViewById(R.id.textDNI);
        textEmail = findViewById(R.id.textEmail);
        textDireccion = findViewById(R.id.textDireccion);
        textPassword = findViewById(R.id.textPassword);
        radioButtonMasculino = findViewById(R.id.radioButtonMasculino);
        radioButtonFemenino = findViewById(R.id.radioButtonFemenino);
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        radioButtonMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonMasculino.isChecked()) {
                    sexo = "Masculino";
                }
            }
        });

        radioButtonFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButtonFemenino.isChecked()) {
                    sexo = "Femenino";
                }
            }
        });
        buttonRegistrar.setOnClickListener(view -> {
            capturarDatos();
            String mensaje = "";
            reference.child("Persona").child(persona.getId()).setValue(persona);
            mensaje = "¡Se registro correctamente!";
            AlertDialog.Builder ventana = new AlertDialog.Builder(ActivityRegistrarUser.this);
            ventana.setTitle("Mensaje informativo");
            ventana.setMessage(mensaje);
            ventana.setPositiveButton("ACEPTAR", null);
            ventana.create().show();
        });

    }
    private  void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }
    private void capturarDatos(){
        String nombres = textNombres.getText().toString();
        String apellidos = textApellidos.getText().toString();
        String celular = textCelular.getText().toString();
        String dni = textDNI.getText().toString();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();
        String direccion = textDireccion.getText().toString();
        String rol="User";
        persona = new Persona(UUID.randomUUID().toString(),nombres, apellidos, dni, sexo, celular, direccion, rol, email, password);
    }
}