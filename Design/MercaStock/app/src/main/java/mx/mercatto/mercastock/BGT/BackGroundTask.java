package mx.mercatto.mercastock.BGT;

/**
 * Created by Ryu on 23/03/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentArticulo;
import mx.mercatto.mercastock.FragmentCategoria;
import mx.mercatto.mercastock.FragmentFormularioArticulo;
import mx.mercatto.mercastock.FragmentLogin;
import mx.mercatto.mercastock.FragmentSesion;
import mx.mercatto.mercastock.ListaAdaptador;
import mx.mercatto.mercastock.ListaSucursal;
import mx.mercatto.mercastock.R;
import mx.mercatto.mercastock.SucursalAdapter;


public class BackGroundTask extends AsyncTask<String, String, JSONObject> {
    JSONObject postparams = null;
    String URL = null;
    String method = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public static Integer CodeResponse;


    /**
     * 1.- Login
     * 2.- ListaSucursal
     * 3.- Lista de Categorias
     * 4.- Lista de Articulos
     */
    Integer Codigo;
    Activity activity;
    ProgressDialog asyncDialog;
    int caso9;


    public BackGroundTask(String url, String method, JSONObject params, Activity activity, Integer codigo) {
        this.URL = url;
        this.postparams = params;
        this.method = method;
        this.Codigo = codigo;
        this.activity = activity;
        if (activity!= null)
        asyncDialog = new ProgressDialog(activity);
    }

    JSONArray _JsonGenerico = null;
    ArrayList<HashMap<String, String>> _Listado = new ArrayList<>();
    public ListView list;
    ListAdapter adapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
             asyncDialog.setProgress(0);
        }

        switch (Codigo) {
            case 1: {
                asyncDialog.setMessage("Cargando Usuario");
                if(activity!=null) {
                    asyncDialog.show();
                }
            }
            break;
            case 2: {
                //asyncDialog.setMessage("Cargando Lista de Sucursales");
            }
            break;
            case 3: {
                asyncDialog.setMessage("Cargando Lista de Categorias");
                if(activity!=null) {
                    asyncDialog.show();
                }
            }
            break;
            case 4: {
                asyncDialog.setMessage("Cargando Lista de Artículos");
                if(activity!=null) {
                    asyncDialog.show();
                }
            }break;
            case 5:{
               // asyncDialog.setMessage("Cargando Configuraciones...");

            }break;
            case 6:{
               /* asyncDialog.setMessage("Guardando Inventario");
                if(activity!=null) {
                    asyncDialog.show();
                }*/
            }break;
            case 8:{
                /* asyncDialog.setMessage("Cargando Artículo");
                if(activity!=null) {
                    asyncDialog.show();
                }*/
            }break;
            case 9:{
                 asyncDialog.setMessage("Estableciendo conexión");
                if(activity!=null) {
                    asyncDialog.show();
                }
            }break;
            case 10:{

                asyncDialog.setMessage("Enviando Petición: Por favor espere");
                if(activity!=null) {
                    asyncDialog.show();
                }
            }
            break;
            case 11:{


            }
            break;
        }


    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            if (method.equals("POST")) {
                HttpPost httpPost = new HttpPost(URL);
                StringEntity entity = new StringEntity(postparams.toString(), HTTP.UTF_8);
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                CodeResponse = response.getStatusLine().getStatusCode();
                is = httpEntity.getContent();
            } else if (method == "GET") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(URL);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            Log.d("a", json);
            switch(Codigo){
                case 1:{}break;
                case 2:{

                }break;
                case 3:{


                }break;
                case 4:{


                }break;

            }

        } catch (UnsupportedEncodingException e) {
            showToast(e.getMessage());
         } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return jObj;

    }
    public static String ClaveApi = "Default";
    public static String User = "Default";
    Spinner listaSucSpinner;
    Button guardar;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            switch (Codigo){
                case 1:{
                 //   Login(file_url);
                    jObj=null;
                    //spinnerSucursal2(file_url);
                }break;
                case 2:{
                   // spinnerSucursal(file_url);
                    jObj=null;
                }break;
                case 3:{
                    //ListViewCategorias(file_url);
                //    activity.invalidateOptionsMenu();
               //     jObj=null;
                }break;
                case 4:{
                 //   ListViewArticulos(file_url);
                 //   jObj=null;
                }break;
                case 5:{
                    //CargarConfiguraciones(file_url);
                    jObj=null;
                 //   BackGroundTask bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,activity,2);
                 //   bgt.execute();
                }break;
                case 6:{
                    //FormularioArticulo(file_url);
                   // jObj=null;
                }break;
                case 7:{
                   // RegistrarUsuario(file_url);
                  //  jObj=null;
                }break;
                case 8:{
                  //  FormularioArticulo(file_url);
                 //   jObj=null;
                }break;
                case 9:{
                    caso9=1;
                    //cargarListadoSucursal(file_url);

                    jObj=null;

                }break;
                case 10:{
                    //cambiarPIN();
                //    jObj=null;
                }break;
                case 11:{
                    //api(file_url);
                   /* try {
                        Integer ss=file_url.getInt("estado");
                        if (ss.equals(9)&&!Configuracion.settings.getString("usuario","").equals("")) {
                            FragmentCategoria fragment = new FragmentCategoria();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();


                        }
                        else  if(ss.equals(9)&&Configuracion.settings.getString("usuario","").equals("")){
                            FragmentLogin fragment = new FragmentLogin();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }
                        else {
                            showToast("Este usuario no tiene clave api");
                            FragmentSesion fragment = new FragmentSesion();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    jObj=null;
                }break;
                case 12:{
                   // Login2(file_url);
                    jObj=null;
                }break;
                case 13:{
                    //api(file_url);

                    jObj=null;
                }break;
            }

            //caso9=0;


        } catch (Exception e) {
            //showToast(e.toString());
            if(caso9==1) {
                showToast("No se encontraron sucursales en la Dirección IP");
                // listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                listaSucSpinner.setAdapter(null);
                guardar = (Button) activity.findViewById(R.id.button7);
                guardar.setEnabled(false);
                caso9=0;
            }
        }
        if(activity!=null) {
            asyncDialog.dismiss();
        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ArrayList<ListaSucursal>  countryList= new ArrayList<>();

    private void api(JSONObject file_url) {

    }

}

