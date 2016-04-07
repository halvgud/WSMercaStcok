package mx.mercatto.designmercastock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

/**
 * Created by Juan Carlos De León on 31/03/2016.
 */
public class Registro extends ActionBarActivity {

    //private static final String TAG_ID_REGISTRO = "idSucursal";
    //private static final String TAG_NAME_REGISTRO = "nombre";
    //private static final String TAG_DATA_REGISTRO = "datos";
    //private static final String MAP_API_URL_REGISTRO = "http://192.168.1.17/wsMercaStock/sucursal";
    //private static final String MAP_API_LOGIN_REGISTRO = "http://192.168.1.17/wsMercaStock/usuario/registro";
    private String sexo="M";
    private BackGroundTask bgt;
    Spinner listaSucSpinner;
    EditText txtusuario ;
    EditText txtpassword;
    EditText txtpassword2;
    EditText txtnombre ;
    EditText txtapellido ;
    Spinner txtsexo;

    ArrayList<listaSucursal> countryList = new ArrayList<listaSucursal>();
    private String[] arraySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle("Registro");
        cargarListadoSucursal();
        arraySpinner=new String[]{
                "Masculino","Femenino"
        };
        Spinner s = (Spinner) findViewById(R.id.editText10);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sexo = "M";
                } else {
                    sexo = "F";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
         txtusuario = (EditText) findViewById(R.id.editText5);
         txtpassword = (EditText) findViewById(R.id.editText6);
         txtpassword2= (EditText) findViewById(R.id.editText7);
         txtnombre = (EditText) findViewById(R.id.editText8);
         txtapellido = (EditText) findViewById(R.id.editText9);
         txtsexo = (Spinner) findViewById(R.id.editText10);
    }

    public void cargarListadoSucursal() {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(Configuracion.getApiUrlRegistro(), "GET", null);

        try {
            JSONObject countryJSON = bgt.execute().get();
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(Configuracion.getDatos());

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);

                    String id = c.getString(Configuracion.getIdRegistro());
                    String name = c.getString(Configuracion.getDescripcioRegistro());

                    countryList.add(new listaSucursal(id, name.toUpperCase()));
                }
            }else{
                findViewById(R.id.button2).setEnabled(false);
                return;
            }

            listaSucSpinner = (Spinner) findViewById(R.id.editText11);
            SucursalAdapter cAdapter = new SucursalAdapter(this, android.R.layout.simple_spinner_item, countryList);
            listaSucSpinner.setAdapter(cAdapter);

            listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listaSucursal selectedCountry = countryList.get(position);
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
    public void Registro(View view){

        String usuario = txtusuario.getText().toString().toUpperCase();
        String password = txtpassword.getText().toString();
        String password2 = txtpassword2.getText().toString();
        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        String contacto = "";
        String idsucursal = "";
        String claveapi = "";

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings.edit();
        if(usuario.length()>0&&password.length()>0&&password2.length()>0&&nombre.length()>0&&apellido.length()>0) {
            if(password.length()==4&&password2.length()==4){
            if (password.equals(password2)) {
                try {
                    JSONObject jsonObj1 = new JSONObject();
                    jsonObj1.put("idUsuario", "7");
                    jsonObj1.put("usuario", usuario);
                    jsonObj1.put("contrasena", password);
                    jsonObj1.put("nombre", nombre);
                    jsonObj1.put("apellido", apellido);
                    jsonObj1.put("sexo", sexo);
                    jsonObj1.put("contacto", contacto);
                    jsonObj1.put("idSucursal", idsucursal);
                    jsonObj1.put("claveApi", claveapi);
                    // Create the POST object and add the parameters
                    bgt = new BackGroundTask(Configuracion.getApiUrlRegistro(), "POST", jsonObj1);
                    JSONObject countryJSON = bgt.execute().get();

                    switch (BackGroundTask.CodeResponse) {
                        case 200: {
                            showToast("Su registró correctamente");
                            Intent refresh = new Intent(this, Registro.class);
                            this.finish();
                            startActivity(refresh);
                        }
                        ;
                        break;
                        case 401:
                            // showToast(("Usuario y/o password incorrectas"));
                            break;
                        default:
                            showToast(Integer.toString(BackGroundTask.CodeResponse));
                    }
                } catch (JSONException e) {
                    showToast(e.toString());
                } catch (Exception e) {
                    // showToast(e.toString());
                }
            } else {
                showToast("Las contraseñas no coinciden");
            }
        }
            else {
                showToast("Las contraseñas deben contener 4 digitos");
            }
        }
        else {
            showToast("Faltan datos en algún campo");
        }
    }
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void Regresar(View view){
        Intent intent = new Intent(this, Login.class);
        this.startActivity(intent);
    }
}