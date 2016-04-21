package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.mercatto.mercastock.BGT.BGTConfigurarServidorSucursal;
import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentLogin;
import mx.mercatto.mercastock.R;


public class FragmentConexionPerdida extends Fragment implements View.OnClickListener {
    EditText txtIp;
    View rootView;
    InputMethodManager imm;
    public FragmentConexionPerdida() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_conexion_perdida, container, false);
        getActivity().setTitle("Error al intentar conectar");
        //showToast("Error en la conexión");
        Main.controlUsuario=-1;
        if(!Configuracion.settings.getString("sucursal","").equals("")){
            rootView.findViewById(R.id.button6).setEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
        if(!Configuracion.settings.getString("ip","").equals("")){
            txtIp.setText(Configuracion.settings.getString("ip", ""));
        }

        Button upButton = (Button) rootView.findViewById(R.id.button6);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button);
        upButton2.setOnClickListener(this);
        txtIp.addTextChangedListener(new TextWatcher() {
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                String valorIp = txtIp.getText().toString();
                if (!valorIp.equals(gg) && valorIp.length()>=7) {
                    rootView.findViewById(R.id.button6).setEnabled(true);
                    //getView().findViewById(R.id.button9).setEnabled(true);
                } else {
                    rootView.findViewById(R.id.button6).setEnabled(false);
                    //getView().findViewById(R.id.button9).setEnabled(false);
                    //getView().findViewById(R.id.button6).setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return rootView;
    }
    @Override
    public void onClick(View v) {
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        switch(v.getId())
        {
            case R.id.button6: {
                //peticion();
                getActivity().finish();
                Intent intent = getActivity().getIntent();
                startActivity(intent);
                FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                FragmentManager fragmentManager = this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
            break;
            case R.id.button: {
                FragmentSucursal fragment = new FragmentSucursal();
                FragmentManager fragmentManager = this.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
    public void peticion() {
        BGTConfigurarServidorSucursal bgt;
        String ip = txtIp.getText().toString();
        try {
            bgt = new BGTConfigurarServidorSucursal("http://" + ip + "/wsMercaStock/sucursal", getActivity());
            bgt.execute();
        } catch (Exception e) {
            showToast("No jala");
            e.printStackTrace();
        }
    }
   }
