package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d("LOG-LOGIN","Usuario loggeado: " + currentUser.getEmail());
            Intent mIntent = new Intent(LoginActivity.this, InicioClienteActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
        else {
            Log.d("LOG-LOGIN", "Usuario no loggeado");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();

        TextInputEditText etCorreo = findViewById(R.id.et_correo);
        TextInputEditText etContrasenya = findViewById(R.id.et_contrasenya);

        TextInputLayout tilEmail = findViewById(R.id.tilEmail);
        TextInputLayout tilContrasenya = findViewById(R.id.tilContrasenya);

        TextView tvRecuperarContrasenya = findViewById(R.id.tv_recuperar_contrasenya);
        tvRecuperarContrasenya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RecuperarContrasenyaActivity.class));
            }
        });

        TextView tvRegistrarse = findViewById(R.id.tv_registro);
        tvRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, Registro.class));
            }
        });

        Button btnIngresar = findViewById(R.id.btn_ingresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFormValid = true;
                String correo = etCorreo.getText().toString();
                String contrasenya = etContrasenya.getText().toString();
                if(TextUtils.isEmpty(correo)){
                    tilEmail.setError("Escriba su correo electrónico");
                    isFormValid = false;
                }else{
                    tilEmail.setError("");
                }

                if(TextUtils.isEmpty(contrasenya)){
                    tilContrasenya.setError("Escriba su contraseña");
                    isFormValid = false;
                }else{
                    tilContrasenya.setError("");
                }

                if(isFormValid){
                    mAuth.signInWithEmailAndPassword(correo, contrasenya).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LOG-LOGIN", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(LoginActivity.this, InicioClienteActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                String errorMessage;
                                switch (errorCode){
                                    case "ERROR_INVALID_EMAIL":
                                        errorMessage = "Mal formato del correo";
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        errorMessage = "Su contraseña debe contener al menos 6 caracteres";
                                        break;
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        errorMessage = "Este correo ya lo usa otra cuenta";
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        errorMessage = "Correo/Contraseña incorrecta";
                                        break;
                                    default:
                                        errorMessage = "Hubo un error en su inicio de sesión, intentelo de nuevo: " + errorCode;
                                }
                                Toast.makeText(LoginActivity.this, errorMessage,
                                        Toast.LENGTH_SHORT).show();
                                Log.d("LOG-REGISTRO", errorCode);
                            }
                        }
                    });
                }
            }
        });
    }


}