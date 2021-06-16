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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ingenieriasoftware.sefitness.models.Usuario;
import com.ingenieriasoftware.sefitness.utils.DatePickerFragment;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestoreInstance;

    private TextInputLayout mLytNombre;
    private TextInputEditText mNombre;
    private TextInputLayout mLytCorreo;
    private TextInputEditText mCorreo;
    private Spinner mGenero;
    private TextInputLayout mLytFecha;
    private TextInputEditText mFechaNacimiento;
    private TextInputLayout mLytContrasenya;
    private TextInputEditText mContrasenya;
    private TextInputLayout mLytConfirmacionContrasenya;
    private TextInputEditText mConfirmacionContrasenya;
    private TextInputLayout mLytPadecimientos;
    private TextInputEditText mPadecimientos;
    private TextInputLayout mLytObjetivos;
    private TextInputEditText mObjetivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mFirestoreInstance = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        initUIComponents();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d("LOG-REGISTRO","Usuario loggeado: " + currentUser.getEmail());
            Intent mIntent = new Intent(Registro.this, InicioClienteActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        }
        else {
            Log.d("LOG-REGISTRO", "Usuario no loggeado");
        }
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

    private void initUIComponents(){
        mLytNombre = findViewById(R.id.tilNombre);
        mNombre = findViewById(R.id.et_nombre);
        mLytCorreo = findViewById(R.id.tilCorreo);
        mCorreo = findViewById(R.id.et_correo);
        mGenero = (Spinner) findViewById(R.id.spinnerGenero);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenero.setAdapter(adapter);
        mLytFecha = findViewById(R.id.tilFechaNacimiento);
        mFechaNacimiento = findViewById(R.id.et_fecha_nacimiento);
        mLytContrasenya = findViewById(R.id.tilContrasenya);
        mContrasenya = findViewById(R.id.et_contrasenya);
        mLytConfirmacionContrasenya = findViewById(R.id.tilConfirmacionContrasenya);
        mConfirmacionContrasenya = findViewById(R.id.et_contrasenya_2);
        mLytPadecimientos = findViewById(R.id.tilPadecimientos);
        mPadecimientos = findViewById(R.id.et_padecimientos);
        mLytObjetivos = findViewById(R.id.tilObjetivos);
        mObjetivos = findViewById(R.id.et_objetivos);

        mFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        TextView iniciarSesion = findViewById(R.id.tv_iniciar_sesion);
        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this, LoginActivity.class));
            }
        });

        Button btnIngresar = findViewById(R.id.btn_mandar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarFormulario()){
                    if(validarConfirmacionContrasenya()){
                        registrarUsuario();
                    }
                }
                //
            }
        });
    }



    private void registrarUsuario(){
        String nombre = mNombre.getText().toString();
        String correo = mCorreo.getText().toString();
        String genero = mGenero.getSelectedItem().toString();
        String fechaNacimiento = mFechaNacimiento.getText().toString();
        String contrasenya = mContrasenya.getText().toString();
        String padecimientos = mPadecimientos.getText().toString();
        String objetivos = mObjetivos.getText().toString();
        Usuario nuevoUsuario = new Usuario(nombre, "Cliente", genero, correo, fechaNacimiento, padecimientos, objetivos);
        mAuth.createUserWithEmailAndPassword(correo, contrasenya)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG-REGISTRO", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            nuevoUsuario.setUid(user.getUid());
                            mFirestoreInstance.collection("users")
                                    .document(user.getUid())
                                    .set(nuevoUsuario.getFirebaseObject())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Registro.this, "Usuario creado correctamente.",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Registro.this, InicioClienteActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Registro.this, "Hubo un error en su registro, intentelo de nuevo.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //If sign in fails, display a message to the user.
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
                                default:
                                    errorMessage = "Hubo un error en su registro, intentelo de nuevo";
                            }
                            Toast.makeText(Registro.this, errorMessage,
                                    Toast.LENGTH_SHORT).show();
                            Log.d("LOG-REGISTRO", errorCode);
                        }
                    }
                });
    }

    private boolean validarFormulario(){

        if(!validarCampo(mNombre, mLytNombre, "Escriba su nombre completo") |
            !validarCampo(mCorreo, mLytCorreo, "Escriba su correo electrónico") |
            !validarCampo(mFechaNacimiento, mLytFecha, "Escriba su fecha de nacimiento") |
            !validarCampo(mContrasenya, mLytContrasenya, "Escriba su contraseña") |
            !validarCampo(mConfirmacionContrasenya, mLytConfirmacionContrasenya, "Escriba la confirmación de su contraseña") |
            !validarCampo(mPadecimientos, mLytPadecimientos, "Escriba sus padecimientos") |
            !validarCampo(mObjetivos, mLytObjetivos, "Escriba sus objetivos")){
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

    private boolean validarConfirmacionContrasenya(){
        String contrasenya = mContrasenya.getText().toString();
        String contrasenya2 = mConfirmacionContrasenya.getText().toString();
        if(contrasenya.equals(contrasenya2)){
            mLytConfirmacionContrasenya.setError("");
            Log.d("LOG-REGISTRO", "Contraseñas iguales");
            return true;
        }
        Log.d("LOG-REGISTRO", "Contraseñas no iguales");
        mLytConfirmacionContrasenya.setError("Las contraseñas no coinciden");
        return false;
    }

}