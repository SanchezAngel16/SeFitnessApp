package com.ingenieriasoftware.sefitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterAvances;
import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterCita;
import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterDieta;
import com.ingenieriasoftware.sefitness.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvancesActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ExpandableListAdapterCita listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avances);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        if(mCurrentUser != null){
            db.collection("users").document(mCurrentUser.getUid()).collection("progress")
                    .orderBy("fecha")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                Log.d("LOG-AVANCES", "SI HAY");
                                int cont = 0;
                                for(QueryDocumentSnapshot progreso : task.getResult()){
                                    Log.d("LOG-AVANCES", progreso.getId());
                                    String formattedDate = Utils.getFormattedDate((long)progreso.getData().get("fecha"));
                                    listDataHeader.add(formattedDate);
                                    Map<String, Object> firebaseData = progreso.getData();

                                    String tipoCuerpo = getFirebaseDataField(firebaseData, "tipoCuerpo");
                                    String peso = getFirebaseDataField(firebaseData, "peso");
                                    String estatura = getFirebaseDataField(firebaseData, "estatura");
                                    String imc = getFirebaseDataField(firebaseData, "imc");
                                    String cuello = getFirebaseDataField(firebaseData, "cuello");
                                    String brazoIzquierdo = getFirebaseDataField(firebaseData, "brazoIzquierdo");
                                    String brazoDerecho = getFirebaseDataField(firebaseData, "brazoDerecho");
                                    String antebrazoIzquierdo = getFirebaseDataField(firebaseData, "antebrazoIzquierdo");
                                    String antebrazoDerecho = getFirebaseDataField(firebaseData, "antebrazoDerecho");
                                    String toracico = getFirebaseDataField(firebaseData, "toracico");
                                    String abdominal = getFirebaseDataField(firebaseData, "abdominalMinimo");
                                    String periumbilical = getFirebaseDataField(firebaseData, "periumbilical");
                                    String gluteoMaximo = getFirebaseDataField(firebaseData, "gluteoMaximo");
                                    String musloIzquierdo = getFirebaseDataField(firebaseData, "musloIzquierdo");
                                    String musloDerecho = getFirebaseDataField(firebaseData, "musloDerecho");
                                    String piernaIzquierda = getFirebaseDataField(firebaseData, "piernaIzquierda");
                                    String piernaDerecha = getFirebaseDataField(firebaseData, "piernaDerecha");
                                    String observaciones = getFirebaseDataField(firebaseData, "observaciones");

                                    List<String> newProgress = new ArrayList<String>();
                                    newProgress.add("Informaci??n general\nTipo de cuerpo: " + tipoCuerpo + "\nPeso: "+ peso +"\nEstatura: "+ estatura +"\nI.M.C: " + imc);
                                    newProgress.add("Medidas");
                                    newProgress.add("Cuello: " + cuello);
                                    newProgress.add("Brazo izquierdo: "+ brazoIzquierdo +"\nBrazo derecho: " + brazoDerecho);
                                    newProgress.add("Antebrazo izquierdo: "+ antebrazoIzquierdo +"\nAntebrazo derecho: " + antebrazoDerecho);
                                    newProgress.add("Tor??cico mesoesternal: " + toracico);
                                    newProgress.add("Abdonimal m??nimo: " + abdominal);
                                    newProgress.add("Periumbilical: " + periumbilical);
                                    newProgress.add("Gl??teo m??ximo: " + gluteoMaximo);
                                    newProgress.add("Muslo izquierdo: " + musloIzquierdo + "\nMuslo derecho: " + musloDerecho);
                                    newProgress.add("Pierna m??xima izquierda:" + piernaIzquierda +"\nPierna m??xima derecha: " + piernaDerecha);
                                    newProgress.add("Observaciones: " + observaciones);

                                    //newProgress.add("Especialista: " + cita.getData().get("especialista").toString());
                                    Log.d("LOG-AVANCES", newProgress.get(newProgress.size()-1));
                                    listDataChild.put(listDataHeader.get(cont), newProgress);
                                    Log.d("LOG-AVANCES", "Index:" +cont);
                                    cont++;
                                }
                                // get the listview
                                expListView = (ExpandableListView) findViewById(R.id.listView_avances);

                                // preparing list data
                                //prepareListData();

                                listAdapter = new ExpandableListAdapterCita(AvancesActivity.this, listDataHeader, listDataChild);

                                // setting list adapter
                                expListView.setAdapter(listAdapter);
                            }
                        }
                    });
        }
    }

    private String getFirebaseDataField(Map<String, Object> data, String fieldName){
        return data.get(fieldName).toString();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("20/05/2021");
        listDataHeader.add("17/04/2021");
        listDataHeader.add("18/03/2021");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Informaci??n general\nTipo de cuerpo: \nPeso: \nEstatura: \nI.M.C: ");
        top250.add("Medidas");
        top250.add("Cuello: ");
        top250.add("Brazo izquierdo: \nBrazo derecho: ");
        top250.add("Antebrazo izquierdo: \nAntebrazo derecho: ");
        top250.add("Tor??cico mesoesternal: ");
        top250.add("Abdonimal m??nimo: ");
        top250.add("Periumbilical: ");
        top250.add("Gl??teo m??ximo: ");
        top250.add("Muslo izquierdo: \nMuslo derecho: ");
        top250.add("Pierna m??xima izquierda: \nPierna m??xima derecha: ");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Informaci??n general\nTipo de cuerpo: \nPeso: \nEstatura: \nI.M.C: ");
        nowShowing.add("Medidas");
        nowShowing.add("Cuello: ");
        nowShowing.add("Brazo izquierdo: \nBrazo derecho: ");
        nowShowing.add("Antebrazo izquierdo: \nAntebrazo derecho: ");
        nowShowing.add("Tor??cico mesoesternal: ");
        nowShowing.add("Abdonimal m??nimo: ");
        nowShowing.add("Periumbilical: ");
        nowShowing.add("Gl??teo m??ximo: ");
        nowShowing.add("Muslo izquierdo: \nMuslo derecho: ");
        nowShowing.add("Pierna m??xima izquierda: \nPierna m??xima derecha: ");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Informaci??n general\nTipo de cuerpo: \nPeso: \nEstatura: \nI.M.C: ");
        comingSoon.add("Medidas");
        comingSoon.add("Cuello: ");
        comingSoon.add("Brazo izquierdo: \nBrazo derecho: ");
        comingSoon.add("Antebrazo izquierdo: \nAntebrazo derecho: ");
        comingSoon.add("Tor??cico mesoesternal: ");
        comingSoon.add("Abdonimal m??nimo: ");
        comingSoon.add("Periumbilical: ");
        comingSoon.add("Gl??teo m??ximo: ");
        comingSoon.add("Muslo izquierdo: \nMuslo derecho: ");
        comingSoon.add("Pierna m??xima izquierda: \nPierna m??xima derecha: ");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}