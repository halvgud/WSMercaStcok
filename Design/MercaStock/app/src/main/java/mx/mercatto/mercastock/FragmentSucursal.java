package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Juan Carlos De León on 10/04/2016.
 */
public class FragmentSucursal extends Fragment implements View.OnClickListener  {

    private BackGroundTask bgt;
    EditText txtIp;
    InputMethodManager imm;

    public FragmentSucursal() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sucursal, container, false);
        getActivity().setTitle("Configurar Servidor Sucursal");

        txtIp= (EditText)rootView.findViewById(R.id.editText13);
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
                    getView().findViewById(R.id.button6).setEnabled(true);
                   //getView().findViewById(R.id.button9).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button6).setEnabled(false);
                    //getView().findViewById(R.id.button9).setEnabled(false);
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
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        switch(v.getId())
        {
            case R.id.button6 :
                peticion();
                break;
            case R.id.button9: {
                //ListaSucursal selectedCountry = countryList.get(position);

               // prueba = (TextView) activity.findViewById(R.id.textView17);
                //prueba.setText(selectedCountry.toString());
                //BackGroundTask.sucursalSeleccionada=BackGroundTaskselectedCountry.toString();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sucursal", BackGroundTask.sucursalSeleccionada.toString());
                editor.apply();
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
                //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                //SharedPreferences.Editor editor = settings.edit();

            }
                break;
            // similarly for other buttons
        }
    }

    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
    }

    public void peticion() {
        String ip = txtIp.getText().toString();
        try {
            //JSONObject jsobj = new JSONObject();
            //jsobj.put("idSucursal","");
            //jsobj.put("nombre","");
            bgt = new BackGroundTask("http://" + ip + "/wsMercaStock/sucursal", "GET", null, getActivity(), 9);
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