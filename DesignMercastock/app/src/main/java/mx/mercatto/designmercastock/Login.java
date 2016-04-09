package mx.mercatto.designmercastock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
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

public class Login extends ActionBarActivity {

    private static final String MAP_API_LOGIN_LOGIN = "http://192.168.1.17/wsMercaStock/usuario/login";

    private static final String TAG_USERNAME = "";
    private static final String TAG_PASSWORD="";
    public static String CLAVEAPI_LOGIN = "";
    private BackGroundTask bgt;
    Spinner listaSucSpinner;
    EditText txtusuario;
    EditText txtpassword;
    ArrayList<listaSucursal> countryList = new ArrayList<listaSucursal>();
    int contador;
    String variable_Usuario_Inicial;
    String variable_Usuario_Final;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuracion.Inicializar();
        setContentView(R.layout.activity_login);
        setTitle("MercaStock");
        cargarListadoSucursal();
        txtusuario   = (EditText)findViewById(R.id.editText);
        txtpassword   = (EditText)findViewById(R.id.editText2);
    }

    public void abrirListaDepartamento(View view){
        Intent intent = new Intent(this, ListaDepartamento.class);
        this.startActivity(intent);
    }
    public void cargarListadoSucursal() {
        // Building post parameters, key and value pair :)
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));
        Log.d("suc", Configuracion.getApiUrlLogin());
        bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null);
        try {
            JSONObject countryJSON = bgt.execute().get();
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(Configuracion.getDatos());

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);
                    String id = c.getString(Configuracion.getIdLogin());
                    String name = c.getString(Configuracion.getDescripcionLogin());

                    countryList.add(new listaSucursal(id, name.toUpperCase()));
                }
            }else{
                findViewById(R.id.button2).setEnabled(false);
                return;
            }
            TextView txtSucursal = (TextView) findViewById(R.id.textView13);
            txtSucursal.setText(countryList.get(0).toString());

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
        SharedPreferences.Editor editor = settings.edit();
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("usuario", usuario);
            jsonObj1.put("contrasena", password);
            bgt = new BackGroundTask(MAP_API_LOGIN_LOGIN, "POST", jsonObj1);
            JSONObject countryJSON = bgt.execute().get();

            switch (BackGroundTask.CodeResponse) {
                case 200: {
                    JSONObject datos = countryJSON.getJSONObject("datos");
                    CLAVEAPI_LOGIN = datos.getString("claveApi");
                    editor.putString("ClaveApi", CLAVEAPI_LOGIN);
                    editor.commit();
                    Intent intent = new Intent(this, ListaDepartamento.class);
                    this.startActivity(intent);
                }
                ;
                break;
                case 400:{

                    String subEstado=countryJSON.getString("estado");
                    switch (subEstado){
                        case "11":
                            showToast(countryJSON.getString("mensaje"));
                            break;
                        default:
                            showToast("Usuario o contraseña incorrectas");
                    }
                    //JSONObject datos = countryJSON.getJSONObject("mensaje");
                    //showToast(datos.getString("mensaje"));
                }break;
                case 401:{

                           /*
                            contador=0;
                        }else{
                            contador++;
                        }*/
                    if(Configuracion.getFlagBloqueoPorIntentos().equals("TRUE")) {
                        if(contador==0) {
                            variable_Usuario_Inicial = txtusuario.getText().toString();
                            variable_Usuario_Final="";
                        }

                        if(variable_Usuario_Inicial.equals(variable_Usuario_Final)){
                            if(contador>=Integer.parseInt(Configuracion.getFlagBloqueoCantidad())) {
                                try {
                                    JSONObject jsonObj2 = new JSONObject();
                                    jsonObj2.put("usuario", usuario);
                                    bgt = new BackGroundTask(Configuracion.getApiUrlBloqueo(), "POST", jsonObj2);
                                    bgt.execute().get();
                                    //JSONObject datos = countryJSON.getJSONObject("datos");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                                if(txtusuario.getText().toString().equals(variable_Usuario_Final)) {
                                    showToast("Se ha bloqueado el usuario por 3 intentos erroneos");
                                }
                                
                                if(!txtusuario.getText().toString().equals(variable_Usuario_Final)){
                                    variable_Usuario_Inicial=txtusuario.getText().toString();
                                    contador=0;
                                }
                                //contador=0;
                            }
                        }else{
                            variable_Usuario_Final = variable_Usuario_Inicial;
                            //contador++;
                            //showToast(("Usuario y/o password incorrectas"));
                        }
                    }
                    if(contador<Integer.parseInt(Configuracion.getFlagBloqueoCantidad())) {
                        contador++;
                        showToast(("Usuario y/o password incorrectas"));

                    }}
                    break;
                default:
                    showToast(Integer.toString(BackGroundTask.CodeResponse));
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