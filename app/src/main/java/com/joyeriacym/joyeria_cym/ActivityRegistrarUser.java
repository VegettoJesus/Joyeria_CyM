package com.joyeriacym.joyeria_cym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
            // Verificar que la contraseña tenga al menos 6 caracteres
            if (persona.getPassword().length() < 6) {
                AlertDialog.Builder ventanaError = new AlertDialog.Builder(ActivityRegistrarUser.this);
                ventanaError.setTitle("Error");
                ventanaError.setMessage("La contraseña debe tener al menos 6 caracteres.");
                ventanaError.setPositiveButton("ACEPTAR", null);
                ventanaError.create().show();
                return; // Salir del método si la contraseña es demasiado corta
            }
            final String[] mensaje = {"El correo electrónico ya está en uso.","¡Se registró correctamente!","¡No se ha podido registrar correctamente!"};

            reference.child("Persona").orderByChild("email").equalTo(persona.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // El correo electrónico ya está en uso, muestra un mensaje de error
                        AlertDialog.Builder ventanaError = new AlertDialog.Builder(ActivityRegistrarUser.this);
                        ventanaError.setTitle("Error");
                        ventanaError.setMessage(mensaje[0]);
                        ventanaError.setPositiveButton("ACEPTAR", null);
                        ventanaError.create().show();
                    } else {
                        // El correo electrónico es único, procede con el registro en Auth y la base de datos
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(persona.getEmail(), persona.getPassword())
                                .addOnCompleteListener(ActivityRegistrarUser.this, task -> {
                                    if (task.isSuccessful()) {
                                        // Registro en autenticación exitoso, procede con el registro en la base de datos
                                        reference.child("Persona").child(persona.getId()).setValue(persona);
                                        AlertDialog.Builder ventanaExito = new AlertDialog.Builder(ActivityRegistrarUser.this);
                                        ventanaExito.setTitle("Mensaje informativo");
                                        ventanaExito.setMessage(mensaje[1]);
                                        ventanaExito.setPositiveButton("ACEPTAR", (dialog, which) -> {
                                            Intent intent = new Intent(ActivityRegistrarUser.this, ActivityLoginUsers.class);
                                            startActivity(intent);
                                        });
                                        ventanaExito.create().show();
                                    } else {
                                        AlertDialog.Builder ventanaError = new AlertDialog.Builder(ActivityRegistrarUser.this);
                                        ventanaError.setTitle("Error");
                                        ventanaError.setMessage(mensaje[2]);
                                        ventanaError.setPositiveButton("ACEPTAR", null);
                                        ventanaError.create().show();
                                    }
                                });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejo de error en la consulta
                }
            });
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