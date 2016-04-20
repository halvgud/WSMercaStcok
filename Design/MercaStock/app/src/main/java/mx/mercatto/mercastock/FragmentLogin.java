package mx.mercatto.mercastock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;



public class FragmentLogin extends Fragment implements View.OnClickListener {
    Activity activity;
    private BackGroundTask bgt;
    TextView txSucursal;


    EditText txtusuario;
    EditText txtpassword;
    String id_sucursal;

    public FragmentLogin() {
        // Constructor vacío
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        getActivity().setTitle("MercaStock");
        Button upButton = (Button) rootView.findViewById(R.id.button2);
        //Button upButton2 = (Button) rootView.findViewById(R.id.button);
        upButton.setOnClickListener(this);
        //upButton2.setOnClickListener(this);
        Main.idSesion=0;
        Main.controlUsuario=-1;
        txSucursal=(TextView) rootView.findViewById(R.id.textView13);

       SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
      SharedPreferences.Editor editor = settings.edit();
        String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        txtusuario = (EditText) rootView.findViewById(R.id.editText);
        if (auth_token_string != "") {
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        } else {
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        }
        cargarListadoSucursal(rootView);

        txtusuario.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtusuario.getText().toString();
                value2 = txtpassword.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg)) && (value1.length() > 1 && value2.length() == 4)) {
                    getView().findViewById(R.id.button2).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button2).setEnabled(false);
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
        txtpassword.addTextChangedListener(new TextWatcher() {
            String value1 = "";
            String value2 = "";
            String value3 = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtusuario.getText().toString();
                value2 = txtpassword.getText().toString();

                if ((!value1.equals(gg) && !value2.equals(gg)) && (value1.length() > 1 && value2.length() == 4 )) {
                    getView().findViewById(R.id.button2).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button2).setEnabled(false);
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


    public void buttonClicked(View view) {
        //if (view.getId() == R.id.button) {
            // button1 action
       // } else if (view.getId() == R.id.button2) {
            //button2 action
       // } else if (view.getId() == R.id.button3) {
            //button3 action
        //}
    }

    @Override
    public void onClick(View v) {


        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //String auth_token_string = settings.getString("ClaveApi", ""*//*default value*//*);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("idSucursal",id_sucursal);
        editor.putString("sucursal",txSucursal.getText().toString());
        editor.apply();
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("usuario", usuario);
            jsonObj1.put("contrasena", password);
            // Create the POST object and add the parameters
            bgt = new BackGroundTask(Configuracion.getApiUrlLogIn(), "POST", jsonObj1,getActivity(),1);
            bgt.execute();
            //Main.CambiarEstadoSucursal(false);


        } catch(JSONException e){
            showToast(e.toString());
        }catch (Exception e){
            showToast(e.toString());
        }

    }

     public void onBackPressed()  {
        getActivity().finish();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
               /* FragmentSesion fragment2 = new FragmentSesion();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();*/
            }
        }.start();
        //NavigationView navigationView = (NavigationView) getView().findViewById(R.id.nav_view);
//navigationView.removeViewAt(0);
    }

    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void cargarListadoSucursal(View rootView) {
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Configuracion.getApiUrlSucursal(), id_sucursal);
            bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,getActivity(),2);
            bgt.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
