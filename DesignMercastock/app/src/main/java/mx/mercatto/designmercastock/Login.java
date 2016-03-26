package mx.mercatto.designmercastock;

import android.content.Intent;
<<<<<<< HEAD
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
=======
>>>>>>> origin/master
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.content.Context;

public class Login extends AppCompatActivity {

    private static final String TAG_ID = "idSucursal";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_DATA = "datos";
    private static final String MAP_API_URL = "http://192.168.1.41/wsMercaStock/sucursal";
    private static final String MAP_API_LOGIN = "http://192.168.1.41/wsMercaStock/usuario/login";
    private static final String TAG_USERNAME = "";
    private static final String TAG_PASSWORD="";
    public static String ClaveApi = "";
    private BackGroundTask bgt;
    Spinner listaSucSpinner;
    EditText txtusuario;
    EditText txtpassword;
    ArrayList<listaSucursal> countryList = new ArrayList<listaSucursal>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        if (auth_token_string!=""){
            Intent intent = new Intent(this, ListaDepartamento.class);
            this.startActivity(intent);
        }
        else {

            setContentView(R.layout.activity_login);
            setTitle("MercaStock");
            cargarListadoSucursal();
            txtusuario = (EditText) findViewById(R.id.editText);
            txtpassword = (EditText) findViewById(R.id.editText2);
        }
=======
        setContentView(R.layout.activity_login);
        setTitle("MercaStock");
        cargarListadoSucursal();
        txtusuario   = (EditText)findViewById(R.id.editText);
        txtpassword   = (EditText)findViewById(R.id.editText2);

>>>>>>> origin/master
    }

    public void abrirListaDepartamento(View view){
        Intent intent = new Intent(this, ListaDepartamento.class);
        this.startActivity(intent);
    }
    public void cargarListadoSucursal() {
        // Building post parameters, key and value pair
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(MAP_API_URL, "GET", null);

        try {
            JSONObject countryJSON = bgt.execute().get();
            // Getting Array of countries
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(TAG_DATA);

                // looping through All countries
                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);

                    // add Country
                    countryList.add(new listaSucursal(id, name.toUpperCase()));
                }
            }else{
                findViewById(R.id.button2).setEnabled(false);
                return;
            }

            // bind adapter to spinner
            listaSucSpinner = (Spinner) findViewById(R.id.spinner1);
            SucursalAdapter cAdapter = new SucursalAdapter(this, android.R.layout.simple_spinner_item, countryList);
            listaSucSpinner.setAdapter(cAdapter);



            listaSucSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listaSucursal selectedCountry = countryList.get(position);
                    showToast(selectedCountry.getName() + " was selected!");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void LogIn(View view){
        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);

        SharedPreferences.Editor editor = settings.edit();
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("usuario", usuario);
            jsonObj1.put("contrasena", password);
            // Create the POST object and add the parameters
            bgt = new BackGroundTask(MAP_API_LOGIN, "POST", jsonObj1);
            JSONObject countryJSON = bgt.execute().get();
<<<<<<< HEAD
            switch (BackGroundTask.CodeResponse){
                case 200: {
                    JSONObject datos = countryJSON.getJSONObject("datos");
                    ClaveApi=datos.getString("claveApi");
                    editor.putString("ClaveApi", ClaveApi);
                    editor.commit();
                    Intent intent = new Intent(this, ListaDepartamento.class);
                    this.startActivity(intent);};break;
                case 401: showToast(("Usuario y/o password incorrectas"));break;
                default : showToast(Integer.toString(BackGroundTask.CodeResponse));
=======
            switch (countryJSON.getString("estado")){
                case "1": //showToast("usuario y pwd correctas");
                    Intent intent = new Intent(this, ListaDepartamento.class);
                    this.startActivity(intent);break;
                case "8": showToast(countryJSON.getString("mensaje"));break;
>>>>>>> origin/master
            }
        } catch(JSONException e){
        showToast(e.toString());
        }catch (Exception e){
            showToast(e.toString());
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}