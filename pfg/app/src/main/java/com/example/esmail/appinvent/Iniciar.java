package com.example.esmail.appinvent;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esmail.appinvent.fragments.ImportFragment;
import com.example.esmail.appinvent.fragments.ListInventFragment;

public class Iniciar extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private boolean permiso = false;
    public final static String EXTRA_NOMBRE = "com.example.esmail.inventario.unovarios.esmail";

    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Fragment fragment = null;

    protected GestionBBDD gestion;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);

        //lanza los permisos
        launchActivity(Iniciar.class);

        mFragmentManager=getFragmentManager();
        gestion = new GestionBBDD(this);

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navView = (NavigationView) findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_seccion_1:

                                cameraDialog();
                                break;
                            case R.id.menu_seccion_2:
                                fragment = new ImportFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_3:
                                fragment = new ListInventFragment();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                break;
                        }

                        if (fragmentTransaction) {
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .addToBackStack(null)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 1)
            mFragmentManager.popBackStack();
        else {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        fragment = new ListInventFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, "frag")
                .addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void exportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setTitle(R.string.tituloDialogoMain);   // Título
        builder.setView(editText);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String texto = editText.getText().toString();
                Export export = new Export(gestion, getApplicationContext());
                export.exportarBD(texto);
            }


        });

        builder.create();
        builder.show();
    }


    public void cameraDialog() {
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.sumario_preferencia));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.opciones_iniciar);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int pos) {

                dialog.dismiss();
                switch (pos) {
                    case 0:
                        launchActivity(ScanActivity.class);
                        if (permiso) {
                            Intent intent = new Intent(getApplication(), ScanActivity.class);
                            intent.putExtra(EXTRA_NOMBRE, "1");
                            startActivity(intent);

                        }

                        break;
                    case 1:
                        launchActivity(ScanActivity.class);
                        if (permiso) {
                            Intent intent = new Intent(getApplication(), ScanActivity.class);
                            intent.putExtra(EXTRA_NOMBRE, "2");
                            startActivity(intent);

                        }

                        break;
                }

            }

        });

        b.show();
    }

    /**
     * Permisos
     *
     * @param clss
     */

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            permiso = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        permiso = true;
                    }
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permisosCamara), Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_iniciar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.imprimir:
                break;
            case R.id.exportar:
                exportDialog();
                break;
            case R.id.eliminar:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}