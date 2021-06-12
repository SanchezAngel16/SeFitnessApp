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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterCita;
import com.ingenieriasoftware.sefitness.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CitasActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ExpandableListAdapterCita listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citas);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        if(mCurrentUser != null){
            db.collection("users").document(mCurrentUser.getUid()).collection("citas")
                    .orderBy("fechaInMillis")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                Log.d("LOG-CITAS", "SI HAY");

                                int cont = 0;
                                for(QueryDocumentSnapshot cita : task.getResult()){
                                    String formattedDate = Utils.getFormattedDate((long)cita.getData().get("fechaInMillis"));
                                    listDataHeader.add(formattedDate);
                                    List<String> newCita = new ArrayList<String>();
                                    newCita.add("Especialista: " + cita.getData().get("especialista").toString());
                                    listDataChild.put(listDataHeader.get(cont), newCita);
                                    cont++;
                                }
                                // get the listview
                                expListView = (ExpandableListView) findViewById(R.id.listView_citas);

                                // preparing list data
                                //prepareListData();

                                listAdapter = new ExpandableListAdapterCita(CitasActivity.this, listDataHeader, listDataChild);

                                // setting list adapter
                                expListView.setAdapter(listAdapter);
                            }
                        }
                    });
        }


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("21 de mayo del 2021 a las 10:00 hrs");
        listDataHeader.add("22 de mayo del 2021 a las 13:00 hrs");

        // Adding child data
        List<String> cita1 = new ArrayList<String>();
        cita1.add("Lugar: ");
        cita1.add("Entrenador: ");

        List<String> cita2 = new ArrayList<String>();
        cita2.add("Lugar: ");
        cita2.add("Nutriologo: ");


        listDataChild.put(listDataHeader.get(0), cita1);
        listDataChild.put(listDataHeader.get(1), cita2);
    }
}