package com.ingenieriasoftware.sefitness;

import com.ingenieriasoftware.sefitness.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterRutina;

import androidx.annotation.NonNull;

public class RutinasActivity extends Activity {
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ExpandableListAdapterRutina listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);
        mAuth = FirebaseAuth.getInstance();

        TextView tvRoutineName = findViewById(R.id.tvRoutineName);
        TextView tvAssignedRoutineDate = findViewById(R.id.tv_AssignedRoutineDate);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.listView_rutinas);
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null){
            DocumentReference userRef = db.collection("users").document(mCurrentUser.getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String currentRoutineId = document.getData().get("rutinaAsignada").toString();
                            DocumentReference assignedRoutine = db.collection("users")
                                    .document(mCurrentUser.getUid())
                                    .collection("routine")
                                    .document(currentRoutineId);
                            assignedRoutine.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("LOG-RUTINAS", "DocumentSnapshot data: " + document.getData().get("Nombre"));
                                            String sRoutineName = document.getData().get("Nombre").toString();
                                            tvRoutineName.setText(sRoutineName);
                                            tvAssignedRoutineDate.setText("Asignada el " +  Utils.getFormattedDate(Long.valueOf(document.getData().get("FechaAsignacion").toString())));
                                            ArrayList<String> exercises = (ArrayList<String>) document.getData().get("Ejercicios");
                                            ArrayList<String> days = (ArrayList<String>) document.getData().get("Dias");
                                            if(exercises != null){
                                                generateExercisesList(exercises, days);
                                                listDataHeader.add("Observaciones");
                                                List<String> observaciones = new ArrayList<>();
                                                observaciones.add(document.getData().get("Observaciones").toString());
                                                listDataChild.put(listDataHeader.get(0), observaciones);
                                                addExerciseToList();
                                            }
                                            /*
                                            assignedRoutine.collection("exercises")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                listDataHeader = new ArrayList<String>();
                                                                listDataChild = new HashMap<String, List<String>>();
                                                                int cont = 0;
                                                                for (QueryDocumentSnapshot ejercicioObject : task.getResult()) {

                                                                    Log.d("LOG-RUTINAS", ejercicioObject.getId() + " => " + ejercicioObject.getData().get("Nombre"));

                                                                    listDataHeader.add(ejercicioObject.getData().get("Nombre").toString());
                                                                    List<String> ejercicio = new ArrayList<String>();
                                                                    ejercicio.add("Tipo de ejercicio: " + ejercicioObject.getData().get("Tipo").toString());
                                                                    ejercicio.add("Repeticiones: " + ejercicioObject.getData().get("Repeticiones").toString());
                                                                    ejercicio.add("Series: " + ejercicioObject.getData().get("Series").toString());
                                                                    ejercicio.add("Intensidad: " + ejercicioObject.getData().get("Intensidad").toString());
                                                                    ejercicio.add("Categoría: " + ejercicioObject.getData().get("Categoria").toString());
                                                                    ejercicio.add("Días: " + Utils.getDaysFromFirebaseFormat(ejercicioObject.getData().get("Dias").toString()));
                                                                    listDataChild.put(listDataHeader.get(cont), ejercicio);
                                                                    cont++;
                                                                }
                                                                listDataHeader.add("Observaciones");
                                                                List<String> observaciones = new ArrayList<>();
                                                                observaciones.add(document.getData().get("Observaciones").toString());
                                                                listDataChild.put(listDataHeader.get(cont), observaciones);
                                                                addExerciseToList();
                                                            } else {
                                                                Log.w("LOG-RUTINAS", "Error getting documents.", task.getException());
                                                            }
                                                        }
                                                    });*/
                                        } else {
                                            Log.d("LOG-RUTINAS", "No such document");
                                        }
                                    } else {
                                        Log.d("LOG-RUTINAS", "get failed with ", task.getException());
                                    }
                                }
                            });
                        } else {
                        }
                    } else {
                    }
                }
            });



        }


        // preparing list data
        /*prepareListData();*/


    }

    private void generateExercisesList(ArrayList<String> exercises, ArrayList<String> days){
        listDataHeader = new ArrayList<String>(exercises.size());
        listDataChild = new HashMap<String, List<String>>(exercises.size());
        for(int i = 0; i < exercises.size(); i++){
            int cont = i;
            db.collection("exercises").document(exercises.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot ejercicioObject = task.getResult();
                    Log.d("LOG-RUTINAS", ejercicioObject.getId() + " => " + ejercicioObject.getData().get("Nombre"));
                    listDataHeader.add(cont, ejercicioObject.getData().get("Nombre").toString());
                    List<String> ejercicio = new ArrayList<String>();
                    ejercicio.add("Tipo de ejercicio: " + ejercicioObject.getData().get("Tipo").toString());
                    ejercicio.add("Repeticiones: " + ejercicioObject.getData().get("Repeticiones").toString());
                    ejercicio.add("Series: " + ejercicioObject.getData().get("Series").toString());
                    ejercicio.add("Intensidad: " + ejercicioObject.getData().get("Intensidad").toString());
                    ejercicio.add("Categoría: " + ejercicioObject.getData().get("Categoria").toString());
                    ejercicio.add("Días: " + Utils.getDaysFromFirebaseFormat(days.get(cont)));
                    listDataChild.put(listDataHeader.get(cont), ejercicio);
                    addExerciseToList();
                }
            });
        }
    }

    private void addExerciseToList(){
        listAdapter = new ExpandableListAdapterRutina(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Ejercicio 1");
        listDataHeader.add("Ejercicio 2");
        listDataHeader.add("Ejercicio 3");
        listDataHeader.add("Observaciones");

        // Adding child data
        List<String> ejercicio1 = new ArrayList<String>();
        ejercicio1.add("Tipo de ejercicio");
        ejercicio1.add("Repeticiones: ");
        ejercicio1.add("Series: ");
        ejercicio1.add("Intensidad: ");
        ejercicio1.add("Categoría: ");

        List<String> ejercicio2 = new ArrayList<String>();
        ejercicio2.add("Tipo de ejercicio: ");
        ejercicio2.add("Repeticiones: ");
        ejercicio2.add("Series: ");
        ejercicio2.add("Intensidad: ");
        ejercicio2.add("Categoria: ");

        List<String> ejercicio3 = new ArrayList<String>();
        ejercicio3.add("Tipo de ejercicio: ");
        ejercicio3.add("Repeticiones: ");
        ejercicio3.add("Series: ");
        ejercicio3.add("Intensidad: ");
        ejercicio3.add("Categoría: ");

        List<String> observaciones = new ArrayList<>();
        observaciones.add("Observacion 1: \nObservacion 2: \nObservacion 3: \nObservacion 4");

        listDataChild.put(listDataHeader.get(0), ejercicio1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), ejercicio2);
        listDataChild.put(listDataHeader.get(2), ejercicio3);
        listDataChild.put(listDataHeader.get(3), observaciones);
    }
}