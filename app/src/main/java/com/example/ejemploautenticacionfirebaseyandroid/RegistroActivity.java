package com.example.ejemploautenticacionfirebaseyandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.example.ejemploautenticacionfirebaseyandroid.modelo.Usuario;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RegistroActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText clave;
    private EditText confirmarClave;
    private EditText numDocumento;
    private EditText nombreCompleto;
    private EditText edad;
    private Button guardar;
    private Button volver;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombreCompleto = findViewById(R.id.txtNombre);
        numDocumento = findViewById(R.id.txtDocumento);
        edad = findViewById(R.id.txtEdad);

        usuario = findViewById(R.id.txtUsuario);
        clave = findViewById(R.id.txtClave);
        confirmarClave = findViewById(R.id.txtConfirmarClave);

        guardar = findViewById(R.id.btnGuardar);
        volver = findViewById(R.id.btnVolver);

        firebaseAuth = FirebaseAuth.getInstance();

        inicializarFirebase();


        numDocumento.addTextChangedListener(validarCampo);
        nombreCompleto.addTextChangedListener(validarCampo);
        edad.addTextChangedListener(validarCampo);
        usuario.addTextChangedListener(validarCampo);
        clave.addTextChangedListener(validarCampo);
        confirmarClave.addTextChangedListener(validarCampo);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valorDocumento = numDocumento.getText().toString().trim();
                String valorNombre = nombreCompleto.getText().toString().trim();
                String valorEdad = edad.getText().toString().trim();

                String user = usuario.getText().toString().trim();
                String pass = clave.getText().toString().trim();
                String pass2 = confirmarClave.getText().toString().trim();

                if (pass.equals(pass2)){
                    Usuario usuario = new Usuario();
                    usuario.setEmail(user);
                    usuario.setClave(pass);
                    usuario.setNumeroDocumento(valorDocumento);
                    usuario.setEdad(valorEdad);
                    usuario.setNombre(valorNombre);

                    databaseReference.child("Usuario").child(usuario.getNumeroDocumento()).setValue(usuario);

                    firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(),
                            usuario.getClave()).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                task.getResult();
                                Toast.makeText(RegistroActivity.this,"Error en el registro", Toast.LENGTH_LONG).show();
                            }else{
                                Intent ventanaLogin = new Intent(RegistroActivity.this, MainActivity.class);
                                startActivity(ventanaLogin);
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegistroActivity.this,"Las cootraseñas no coinciden.", Toast.LENGTH_LONG).show();

                }



            }
        });

    }


    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private TextWatcher validarCampo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Hello
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
            String valorDocumento = numDocumento.getText().toString().trim();
            String valorNombre = nombreCompleto.getText().toString().trim();
            String valorEdad = edad.getText().toString().trim();

            String user = usuario.getText().toString().trim();
            String pass = clave.getText().toString().trim();
            String pass2 = confirmarClave.getText().toString().trim();

            guardar.setEnabled(
                    !valorDocumento.isEmpty()&&
                    !valorNombre.isEmpty()&&
                    !valorEdad.isEmpty()&&
                    !user.isEmpty()&&
                    !pass.isEmpty()&&
                    !pass2.isEmpty()&&
                    user.matches(regex)&&
                    pass.length() > 5 &&
                    pass2.length() > 5 &&
                    pass.equals(pass2)&&
                    valorDocumento.length() > 5
            );
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //Hello
        }
    };

}
