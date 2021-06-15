package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ingenieriasoftware.sefitness.utils.DatePickerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestoreInstance;

    private TextInputLayout mTilNombre;
    private TextInputLayout mTilPadecimientos;
    private TextInputLayout mTilObjetivos;
    private TextInputLayout mTilFechaNacimiento;

    private TextInputEditText mNombreCompleto;
    private TextInputEditText mPadecimientos;
    private TextInputEditText mObjetivos;
    private TextInputEditText mFechaNacimiento;

    private Spinner mSpinnerGenero;

    private Button mBtnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        mAuth = FirebaseAuth.getInstance();
        mFirestoreInstance = FirebaseFirestore.getInstance();

        initUI();
        loadFirebaseData();

    }

    private void loadFirebaseData(){
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        mFirestoreInstance.collection("users").document(mCurrentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot user = task.getResult();
                    mNombreCompleto.setText(user.get("name").toString());
                    mFechaNacimiento.setText(user.get("birthDate").toString());
                    mPadecimientos.setText(user.get("ailments").toString());
                    mObjetivos.setText(user.get("objetivos").toString());
                    String genero = user.get("genre").toString().trim();
                    int position = (genero.equals("Masculino")) ? 0 : 1;
                    mSpinnerGenero.setSelection(position);
                }
            }
        });
    }

    private void initUI(){
        mTilNombre = findViewById(R.id.tilNombre);
        mTilPadecimientos = findViewById(R.id.tilPadecimientos);
        mTilObjetivos = findViewById(R.id.tilObjetivos);
        mTilFechaNacimiento = findViewById(R.id.tilFechaNacimiento);


        mNombreCompleto = findViewById(R.id.et_nombre);
        mPadecimientos = findViewById(R.id.et_padecimientos);
        mObjetivos = findViewById(R.id.et_objetivos);
        mFechaNacimiento = findViewById(R.id.et_fecha_nacimiento);
        mFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mSpinnerGenero = (Spinner) findViewById(R.id.spinnerGenero);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGenero.setAdapter(adapter);

        mBtnEditar = findViewById(R.id.btn_editar);
        mBtnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFormValid()){
                    Log.d("LOG-PERFIL", "VALIDO");
                    editProfileUser();
                }else{
                    Log.d("LOG-PERFIL", "NO VALIDO");
                }
            }
        });
    }

    private void editProfileUser(){

        String nombre = mNombreCompleto.getText().toString();
        String padecimientos = mPadecimientos.getText().toString();
        String objetivos = mObjetivos.getText().toString();
        String fechaNacimiento = mFechaNacimiento.getText().toString();
        String genero = mSpinnerGenero.getSelectedItem().toString();

        Map<String,Object> updates = new HashMap<>();
        updates.put("name", nombre);
        updates.put("ailments", padecimientos);
        updates.put("objetivos", objetivos);
        updates.put("birthDate", fechaNacimiento);
        updates.put("genre", genero);

        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        DocumentReference userRef = mFirestoreInstance
                .collection("users")
                .document(mCurrentUser.getUid());

        Log.d("LOG-PERFIL", mCurrentUser.getUid());

        userRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                startActivity(new Intent(PerfilUsuarioActivity.this, InicioClienteActivity.class));
            }
        });

    }

    private void showDatePickerDialog(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                String sDay = (day < 10) ? "0" + day : String.valueOf(day);
                String sMonth = (month+1 < 10) ? "0" + (month+1) : String.valueOf(month+1);

                final String selectedDate = sDay + " / " + sMonth + " / " + year;
                mFechaNacimiento.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private boolean isFormValid(){

        if(!validarCampo(mNombreCompleto, mTilNombre, "Escriba su nombre completo") |
                !validarCampo(mFechaNacimiento, mTilFechaNacimiento, "Escriba su fecha de nacimiento") |
                !validarCampo(mPadecimientos, mTilPadecimientos, "Escriba sus padecimientos") |
                !validarCampo(mObjetivos, mTilObjetivos, "Escriba sus objetivos")){
            return false;
        }
        return true;
    }

    private boolean validarCampo(TextInputEditText campo, TextInputLayout layout, String msgError){
        String texto = campo.getText().toString();
        if(TextUtils.isEmpty(texto)){
            layout.setError(msgError);
            return false;
        }
        layout.setError("");
        return true;
    }
    /*
    private boolean isFormValid(){
        boolean isValid = true;
        String nombre = mNombreCompleto.getText().toString();
        String padecimientos = mPadecimientos.getText().toString();
        String objetivos = mObjetivos.getText().toString();
        String fechaNacimiento = mFechaNacimiento.getText().toString();

        if(TextUtils.isEmpty(nombre)){
            mTilNombre.setError("Escribe un nombre");
        }else{
            isValid = false;
            mTilNombre.setError("");
        }

        if(TextUtils.isEmpty(padecimientos)){
            mTilPadecimientos.setError("Escribe tus padecimientos");
        }else{
            isValid = false;
            mTilPadecimientos.setError("");
        }

        if(TextUtils.isEmpty(objetivos)){
            mTilObjetivos.setError("Escribe tus objetivos");
        }else{
            isValid = false;
            mTilObjetivos.setError("");
        }

        if(TextUtils.isEmpty(fechaNacimiento)){
            mTilFechaNacimiento.setError("Selecciona tu fecha de nacimiento");
        }else{
            isValid = false;
            mTilFechaNacimiento.setError("");
        }
        Log.d("LOG-PERFIL", "Validez:" + isValid);
        return isValid;
    }
    */
}