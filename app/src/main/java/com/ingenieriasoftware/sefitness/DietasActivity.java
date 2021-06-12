package com.ingenieriasoftware.sefitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.ingenieriasoftware.sefitness.adapters.ExpandableListAdapterDieta;

public class DietasActivity extends Activity {

    ExpandableListAdapterDieta listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.listView_dietas);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapterDieta(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Comida 1");
        listDataHeader.add("Comida 2");
        listDataHeader.add("Comida 3");
        listDataHeader.add("Observaciones");

        // Adding child data
        List<String> comida1 = new ArrayList<String>();
        comida1.add("Descripción: ");
        comida1.add("Porciones: ");
        comida1.add("Calorías: ");
        comida1.add("Nutrientes: ");

        List<String> comida2 = new ArrayList<String>();
        comida2.add("Descripción: ");
        comida2.add("Porciones: ");
        comida2.add("Calorías: ");
        comida2.add("Nutrientes: ");

        List<String> comida3 = new ArrayList<String>();
        comida3.add("Descripción: ");
        comida3.add("Porciones: ");
        comida3.add("Calorías: ");
        comida3.add("Nutrientes: ");

        List<String> observaciones = new ArrayList<>();
        observaciones.add("Observacion 1: \nObservacion 2: \nObservacion 3: \nObservacion 4");

        listDataChild.put(listDataHeader.get(0), comida1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), comida2);
        listDataChild.put(listDataHeader.get(2), comida3);
        listDataChild.put(listDataHeader.get(3), observaciones);
    }
}