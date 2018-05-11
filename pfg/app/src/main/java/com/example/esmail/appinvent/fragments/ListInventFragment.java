package com.example.esmail.appinvent.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.esmail.appinvent.AdapterItem;
import com.example.esmail.appinvent.BBDD_Helper;
import com.example.esmail.appinvent.Row;
import com.example.esmail.appinvent.GestionBBDD;
import com.example.esmail.appinvent.R;

import java.util.ArrayList;

public class ListInventFragment extends Fragment {

    private GestionBBDD gestion;
    private ArrayList<Row> al;
    private ListView lv;

    public ListInventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_invent, container, false);
        gestion = new GestionBBDD(v.getContext());
        lv = v.findViewById(R.id.listView);
        actionList();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.e(getTag(), "onViewStateRestored");

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void actionList() {
        al = gestion.verDB();

        AdapterItem adapter = new AdapterItem(getActivity(), al);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cBarras = ((Row) parent.getItemAtPosition(position)).getCBarras();
                Bundle args = new Bundle();
                args.putString("cod-barras", cBarras);
                System.out.println("Cod barras -> " + cBarras);
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment, "ListInventFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}
