package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InicioClienteActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_cliente);
        mAuth = FirebaseAuth.getInstance();

        LinearLayout lytPerfilUsuario = findViewById(R.id.lyt_perfil_usuario);
        lytPerfilUsuario.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, PerfilUsuarioActivity.class)));
        LinearLayout lytAvances = findViewById(R.id.lyt_avances);
        lytAvances.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, AvancesActivity.class)));
        LinearLayout lytDietas = findViewById(R.id.lyt_dietas);
        lytDietas.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, DietasActivity.class)));
        LinearLayout lytRutinas = findViewById(R.id.lyt_rutinas);
        lytRutinas.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, RutinasActivity.class)));
        LinearLayout lytGastoCalorico = findViewById(R.id.lyt_gasto_calorico);
        lytGastoCalorico.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, GastoCaloricoActivity.class)));
        LinearLayout lytCitas = findViewById(R.id.lyt_citas);
        lytCitas.setOnClickListener(view -> startActivity(new Intent(InicioClienteActivity.this, CitasActivity.class)));
        LinearLayout lytSolicitarBaja = findViewById(R.id.lyt_solicitar_baja);

        lytSolicitarBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot user = task.getResult();
                            boolean haySolicitud = (boolean)user.getData().get("solicitudBaja");
                            if(haySolicitud){
                                Toast.makeText(InicioClienteActivity.this, "Ya hay una solicitud de baja pendiente", Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(InicioClienteActivity.this, SolicitarBajaActivity.class));
                            }
                        }
                    }
                });
            }
        });
        LinearLayout lytCerrarSesion = findViewById(R.id.lyt_salir);
        lytCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                    Intent mIntent = new Intent(InicioClienteActivity.this, LoginActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d("LOG-REGISTRO","Usuario loggeado: " + currentUser.getEmail());
        }
        else {
            Log.d("LOG-REGISTRO", "Usuario no loggeado");
            startActivity(new Intent(InicioClienteActivity.this, LoginActivity.class));
        }
    }

}