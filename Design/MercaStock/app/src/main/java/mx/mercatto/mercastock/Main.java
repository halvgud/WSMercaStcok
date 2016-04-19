package mx.mercatto.mercastock;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.util.Log;
import android.view.MenuInflater;
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
import android.view.MotionEvent;
import android.view.View;
>>>>>>> origin/master
>>>>>>> origin/master
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import org.json.JSONObject;

import mx.mercatto.mercastock.BGT.BackGroundTask;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PROJECT_NUMBER="917548048883";
public static int controlUsuario =-1;
public static int idSesion=0;
    public static int inicio=0;
    public  static  int ClAp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(controlUsuario ==-1){
        setContentView(R.layout.activity_main);
        //idSesion=0;
        }
        else if(controlUsuario ==0)
        setContentView(R.layout.activity_main_logged);
        else
        setContentView(R.layout.activity_main_logged_user);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d("Registration id", registrationId);
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public  void  onDrawerStateChanged(int newState){
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            Configuracion.Inicializar(this);

//        Log.d("pppppp", Configuracion.settings.getString("usuario", ""));

            revisarApi();

    }

    public void revisarApi() {
        BackGroundTask bgt;
        try {
            Configuracion.settings=PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("claveApi",Configuracion.settings.getString("ClaveApi",""));
            bgt = new BackGroundTask("http://192.168.1.17/wsMercaStock/usuario/api", "POST", jsonObj1 ,this,13);
            bgt.execute();
        } catch (Exception e){
        }
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.content_main);

        if (currentFragment instanceof FragmentLogin) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
        if (currentFragment instanceof FragmentCategoria) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
        if (currentFragment instanceof FragmentArticulo) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }
        }
        if (currentFragment instanceof FragmentFormularioArticulo) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }
        }
        if (currentFragment instanceof FragmentPassword) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                FragmentCategoria fragment2 = new FragmentCategoria();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
            }
        }
        if (currentFragment instanceof FragmentSucursal) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
            }
        }
        if (currentFragment instanceof RegistroUsuario) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }
        }
        if (currentFragment instanceof FragmentSesion) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
    public static boolean b = false;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.crearusuario) {
            RegistroUsuario fragment = new RegistroUsuario();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

        }else if(id==R.id.cerrarsesion){
/*
            Configuracion.settings= PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            Configuracion.editor = Configuracion.settings.edit();
            Configuracion.editor.putString("usuario", "");
            Configuracion.editor.putString("ClaveApi", "");
            Configuracion.editor.putString("nombre", "");
            Configuracion.editor.putString("controlusuario", "-1");
            Configuracion.editor.apply();
            //idSesion=0;

            controlUsuario =Integer.parseInt(Configuracion.settings.getString("controlusuario",""));*/
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();

            editor.putString("usuario", "");
            editor.putString("ClaveApi", "");
            editor.putString("sucursal", "");
            editor.putString("ip", "");
            editor.putString("idsucursal", "");
            editor.apply();
            idSesion=0;
            controlUsuario =-1;
            inicio=0;
            finish();
            Intent intent = getIntent();
            startActivity(intent);
            FragmentLogin fragment = new FragmentLogin();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        if (idSesion == 1) {
            //editor.putString("usuario", "");
            //editor.putString("ClaveApi", "");
            //editor.putString("sucursal", "");
            //editor.putString("ip", "");
            //editor.putString("idsucursal", "");
            //editor.apply();
            //idSesion=0;
        }
        super.onDestroy();
    }


    @Override
    public  void onResume(){
        int y=idSesion;
        if(idSesion==1)
        revisarApi();

        super.onResume();
    }
    @Override
    public  void onStop(){
        //BackGroundTask.ClAp=11;
        super.onStop();
    }

}
