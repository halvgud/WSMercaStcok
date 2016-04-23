package mx.mercatto.mercastock;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

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


public class FragmentSucursal extends Fragment implements View.OnClickListener  {


    EditText txtIp;
    InputMethodManager imm;
    View rootView;
    Button guardar;
    Button probar;
    public FragmentSucursal() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sucursal, container, false);
        getActivity().setTitle("Configurar Servidor Sucursal");

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
        if(!Configuracion.settings.getString("ip","").equals("")){
            txtIp.setText(Configuracion.settings.getString("ip",""));
        }

        if(!Configuracion.settings.getString("sucursal","").equals("")){
            rootView.findViewById(R.id.button6).setEnabled(true);
        }
        Button upButton = (Button) rootView.findViewById(R.id.button6);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button9);
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
                    getView().findViewById(R.id.button6).setEnabled(true);
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
                peticion();
                guardar = (Button) getActivity().findViewById(R.id.button9);
                guardar.setEnabled(false);
                probar = (Button) getActivity().findViewById(R.id.button6);
                probar.setEnabled(false);
            }
                break;

            case R.id.button9: {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sucursal", BGTConfigurarServidorSucursal.sucursalSeleccionada);
                editor.putString("sucursal", BGTConfigurarServidorSucursal.sucursalSeleccionada.toString());
                //editor.putString("idsucursal", BGTConfigurarServidorSucursal.idSucursalSeleccionada.toString());
                editor.putString("ip", txtIp.getText().toString());
                editor.apply();
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();

            }
                break;
            // similarly for other buttons
        }
    }

    public void peticion() {
        BGTConfigurarServidorSucursal bgt;
        String ip = txtIp.getText().toString();
        try {
            //JSONObject jsobj = new JSONObject();
            //jsobj.put("idSucursal","");
            //jsobj.put("nombre","");

            bgt = new BGTConfigurarServidorSucursal("http://" + ip + Configuracion.getApiUrlConfigurarIp(), getActivity());
            bgt.execute();

            //showToast("Se ha establecido la conexión");
        } catch (Exception e) {
            showToast("No jala");
            e.printStackTrace();
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}