package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Juan Carlos De León on 10/04/2016.
 */
public class FragmentSesion extends Fragment implements View.OnClickListener {

    TextView txtNombre;
    EditText txtPin;
    BackGroundTask bgt;
    public static  int contador2=0;
    public FragmentSesion() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sesion, container, false);
        getActivity().setTitle("Sesión");

        Button upButton = (Button) rootView.findViewById(R.id.button5);
        upButton.setOnClickListener(this);
        Button upButton2 = (Button) rootView.findViewById(R.id.button4);
        upButton2.setOnClickListener(this);

        String nombre= Configuracion.settings.getString("nombre","");
        txtNombre=(TextView)rootView.findViewById(R.id.textView18);
        txtNombre.setText("Usuario: "+nombre);
        txtPin= (EditText)rootView.findViewById(R.id.editText4);

        txtPin.addTextChangedListener(new TextWatcher() {
            String value1 = "";

            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                value1 = txtPin.getText().toString();

                if ((!value1.equals(gg) && (value1.length() == 4))) {
                    getView().findViewById(R.id.button5).setEnabled(true);
                } else {
                    getView().findViewById(R.id.button5).setEnabled(false);
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
        switch(v.getId())
        {
            case R.id.button5 :
                String usuario = BackGroundTask.User;
                String password = txtPin.getText().toString();
                try {
                    JSONObject jsonObj1 = new JSONObject();
                    jsonObj1.put("usuario", usuario);
                    jsonObj1.put("contrasena", password);
                    bgt = new BackGroundTask(Configuracion.getApiUrlLogIn(), "POST", jsonObj1,getActivity(),12);
                    bgt.execute();

                } catch(JSONException e){
                    showToast(e.toString());
                }catch (Exception e){
                    showToast(e.toString());
                }
                break;
            case R.id.button4: {
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("usuario", "");
                editor.putString("ClaveApi", "");
                editor.putString("nombre","");
                editor.apply();
                //editor.putString("sucursal", "");

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
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
