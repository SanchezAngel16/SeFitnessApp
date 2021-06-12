package com.ingenieriasoftware.sefitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GastoCaloricoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_calorico);

        Spinner spinnerObjetivos = findViewById(R.id.spinnerObjetivo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.objetivos_entrenamiento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerObjetivos.setAdapter(adapter);

        Spinner spinnerDiasSemana = (Spinner) findViewById(R.id.spinnerEntrenamientoSemana);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.dias_semana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiasSemana.setAdapter(adapter);
    }
}