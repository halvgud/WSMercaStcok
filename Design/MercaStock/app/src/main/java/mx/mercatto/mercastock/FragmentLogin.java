package mx.mercatto.mercastock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentLogin extends Fragment implements View.OnClickListener {
    public static final String ARG_ARTICLES_NUMBER = "articles_number";

    private static final String TAG_ID = "idSucursal";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_DATA = "datos";
   // private static final String MAP_API_URL = "http://192.168.1.56/wsMercaStock/sucursal";
   // private static final String MAP_API_LOGIN = "http://192.168.1.56/wsMercaStock/usuario/login";
    private static final String TAG_USERNAME = "";
    private static final String TAG_PASSWORD="";
    public static String ClaveApi = "";
    private BackGroundTask bgt;

    EditText txtusuario;
    EditText txtpassword;
    ArrayList<ListaSucursal> countryList = new ArrayList<ListaSucursal>();
    public FragmentLogin() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
    Configuracion.Inicializar(getActivity());
         Button upButton = (Button) rootView.findViewById(R.id.button2);
        upButton.setOnClickListener(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        if (auth_token_string!=""){
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        }
        else {
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        }

        return rootView;
    }

    public void cargarListadoSucursal(View rootView) {
        if(Configuracion.Finalizado==true){
        Log.d("aqui_no", Configuracion.getApiUrlSucursal(true));
        bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(true), "GET", null,getActivity(),2);
        bgt.execute();
        }
        /*else{
            cargarListadoSucursal(rootView);
        }*/
    }

    @Override
    public void onClick(View v) {
     //   Main.CambiarEstadoSucursal(false);
        Main.b=false;
        getActivity().invalidateOptionsMenu();
        Log.d("kkk",getActivity().getTitle().toString());
        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);

        SharedPreferences.Editor editor = settings.edit();
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
