package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class RecuperarContrasenyaActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextInputLayout mTilCorreo;
    private TextInputEditText mCorreo;
    private Button mBtnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenya);

        mAuth = FirebaseAuth.getInstance();

        mTilCorreo = findViewById(R.id.tilCorreo);
        mCorreo = findViewById(R.id.et_email);
        mBtnEnviar = findViewById(R.id.btn_mandar);
        mBtnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFormValid()){
                    String correo = mCorreo.getText().toString();
                    mAuth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RecuperarContrasenyaActivity.this, "Se ha enviado un correo con las instrucciones para recuperar su contraseña", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RecuperarContrasenyaActivity.this, "Hubo un error, ingrese un correo que ya esté registrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isFormValid(){
        if(mCorreo.getText().toString().equals("")){
            mTilCorreo.setError("Escribe tu correo");
            return false;
        }else{
            mTilCorreo.setError("");
            return true;
        }
    }
}