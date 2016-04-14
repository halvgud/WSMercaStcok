package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PROJECT_NUMBER="917548048883";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {

                Log.d("Registration id", registrationId);
                //send this registrationId to your server
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentLogin fragment = new FragmentLogin();
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.content_main);

        if (currentFragment instanceof FragmentLogin) {
            super.onBackPressed();
        }
        if (currentFragment instanceof FragmentCategoria) {
            super.onBackPressed();
        }
        if (currentFragment instanceof FragmentArticulo) {
            getFragmentManager().popBackStack();
        }
        if (currentFragment instanceof FragmentFormularioArticulo) {
            getFragmentManager().popBackStack();
        }
        if (currentFragment instanceof FragmentPassword) {
            getFragmentManager().popBackStack();
        }
        if (currentFragment instanceof FragmentSucursal) {
            getFragmentManager().popBackStack();
        }
        if (currentFragment instanceof RegistroUsuario) {
            getFragmentManager().popBackStack();
        }
}
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
        SeleccionarSucursal = menu.findItem(R.id.seleccionarsucursal);
        CambiarContrasena =  menu.findItem(R.id.cambiarcontrasena);
        ConfigurarServidor =  menu.findItem(R.id.configurarservidor);
        CrearUsuario = menu.findItem(R.id.crearusuario);
      //  return true;
        SeleccionarSucursal.setEnabled(b);
        CambiarContrasena.setEnabled(b);
        ConfigurarServidor.setEnabled(b);
        CrearUsuario.setEnabled(b);
        return true;
    }
    public static boolean b = false;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        menu.setGroupVisible(0, false);


      //  invalidateOptionsMenu();
        return true;
    }

     public static MenuItem SeleccionarSucursal;
    public static MenuItem CambiarContrasena;
    public static MenuItem ConfigurarServidor;
    public static MenuItem CrearUsuario;


    public static void CambiarEstadoSucursal(boolean bandera){
        SeleccionarSucursal.setEnabled(bandera);
    }
    public static void CambiarEstadoContrasena(boolean bandera){
        CambiarContrasena.setEnabled(bandera);
    }
    public static  void CambiarConfigurarServidor(boolean bandera){
        ConfigurarServidor.setEnabled(bandera);
    }
    public static  void CambiarCrearUsuario(boolean bandera){
        CrearUsuario.setEnabled(bandera);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("id", Integer.toString(id));
        //item f = findViewById(R.id.Crear_Usuario);
        if (id == R.id.crearusuario) {
            RegistroUsuario fragment = new RegistroUsuario();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        }else if(id==R.id.configurarservidor){

        }else if(id==R.id.seleccionarsucursal){
            FragmentSucursal fragment = new FragmentSucursal();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        }else if(id==R.id.cambiarcontrasena){
            FragmentPassword fragment = new FragmentPassword();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onDestroy() {
        //super.onPause();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usuario", "");
        editor.putString("ClaveApi", "");
        //editor.putString("usuario", "");
        editor.apply();
        super.onDestroy();
    }
    public void onStop() {
        //super.onPause();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usuario", "");
        editor.putString("ClaveApi", "");
        editor.putString("sucursal", "");
        editor.apply();
        super.onStop();
    }
}
