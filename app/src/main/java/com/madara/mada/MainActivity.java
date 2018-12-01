package com.madara.mada;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText correo, pass;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    public static final String USER="USUARIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        correo=findViewById(R.id.edtUsuario);
        pass=findViewById(R.id.edtPassword);
        progressDialog=new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        checkCurrentUser();

    }

    public void loguearUsuario(View view) {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = correo.getText().toString().trim();
        String password = pass.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Iniciando...");
        progressDialog.show();
        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                Toast.makeText(MainActivity.this, "Bienvenido: " + correo.getText(), Toast.LENGTH_LONG).show();
                                Intent intencion = new Intent(getApplication(), MenuActivity.class);
                                intencion.putExtra(USER, user);
                                startActivity(intencion);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Debe verificar su email...", Toast.LENGTH_LONG).show();
                                firebaseAuth.getCurrentUser();
                                firebaseAuth.signOut();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(MainActivity.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void Registrate(View view){
        Intent intent = new Intent(this, RegistrarActivity.class);
        startActivity(intent);
    }

    public void ResetPass(View view){
        if(TextUtils.isEmpty(correo.getText().toString().trim())){
            Toast.makeText(this,"se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(correo.getText().toString().trim());
        Toast.makeText(this,"Restaure su contraseña mediante el link del correo electronico enviado", Toast.LENGTH_LONG).show();
        pass.requestFocus();
    }
    public void checkCurrentUser(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.isEmailVerified()){
            Intent intencion = new Intent(getApplication(),MenuActivity.class);
            intencion.putExtra(USER, user);
            startActivity(intencion);
        }
    }
}
