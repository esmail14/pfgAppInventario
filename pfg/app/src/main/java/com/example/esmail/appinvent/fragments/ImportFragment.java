package com.example.esmail.appinvent.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.esmail.appinvent.AdapterImport;
import com.example.esmail.appinvent.GestionBBDD;
import com.example.esmail.appinvent.Import;
import com.example.esmail.appinvent.Iniciar;
import com.example.esmail.appinvent.MainActivity;
import com.example.esmail.appinvent.R;
import com.example.esmail.appinvent.Row;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private Class<?> mClss;
    private final static String NOMBRE_DIRECTORIO = "archivos";
    private final static String NOMBRE_DOCUMENTO = "productos.csv";
    private File f;

    private RecyclerView recyclerView;

    public ImportFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_import, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //lanza los permisos
        launchActivity(Iniciar.class);

        GestionBBDD gestion = new GestionBBDD(getActivity());

        recyclerView = getActivity().findViewById(R.id.rvImport);
        Context context = view.getContext();

        Import im=new Import(gestion,context);
        im.actionImport();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Row> al = gestion.verDB();

        AdapterImport adapterImport=new AdapterImport(al);
        recyclerView.setAdapter(adapterImport);
    }





    /**
     * Revisa los permisos
     *
     * @param clss
     */
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * permiso de escritura
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.permisosAlmacenamiento), Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
