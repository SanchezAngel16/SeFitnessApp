package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SolicitarBajaActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_baja);
        mAuth = FirebaseAuth.getInstance();

        TextInputLayout lytMotivo = findViewById(R.id.tilMotivo);
        TextInputEditText motivo = findViewById(R.id.et_motivo);
        Button btnSolicitarBaja = findViewById(R.id.btn_solicitar_baja);
        btnSolicitarBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() == null) return;
                if(TextUtils.isEmpty(motivo.getText().toString())){
                    lytMotivo.setError("Escriba un motivo");
                }else{
                    lytMotivo.setError("");
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .update("solicitudBaja", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    HashMap<String, String> solicitud = new HashMap<>();
                                    solicitud.put("motivo", motivo.getText().toString());
                                    solicitud.put("estado", "Pendiente");
                                    db.collection("solicitudes").document(mAuth.getCurrentUser().getUid())
                                            .set(solicitud)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent mIntent = new Intent(SolicitarBajaActivity.this, InicioClienteActivity.class);
                                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(mIntent);
                                                    Toast.makeText(SolicitarBajaActivity.this, "Revisaremos su solicitud", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d("LOG-LOGIN","Usuario loggeado: " + currentUser.getEmail());

        }
        else {
            Log.d("LOG-LOGIN", "Usuario no loggeado");
            Intent mIntent = new Intent(SolicitarBajaActivity.this, LoginActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
    }
}