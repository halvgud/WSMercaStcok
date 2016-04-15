package mx.mercatto.mercastock;

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



public class BackGroundTask extends AsyncTask<String, String, JSONObject> {
    JSONObject postparams = null;
    String URL = null;
    String method = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static Integer CodeResponse;

    public static String sucursalSeleccionada="";
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
        asyncDialog.setIndeterminate(false);
        asyncDialog.setCancelable(false);
        asyncDialog.setProgress(0);
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

                asyncDialog.setMessage("Validando Sesión");
                if(activity!=null) {
                    asyncDialog.show();
                }
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
                    Login(file_url);
                    jObj=null;
                    //spinnerSucursal2(file_url);
                }break;
                case 2:{
                    spinnerSucursal(file_url);
                    jObj=null;
                }break;
                case 3:{
                    ListViewCategorias(file_url);
                    jObj=null;
                }break;
                case 4:{
                    ListViewArticulos(file_url);
                    jObj=null;
                }break;
                case 5:{
                    CargarConfiguraciones(file_url);
                    jObj=null;
                    BackGroundTask bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,activity,2);
                    bgt.execute();
                }break;
                case 6:{
                    FormularioArticulo(file_url);
                    jObj=null;
                }break;
                case 7:{
                    RegistrarUsuario(file_url);
                    jObj=null;
                }break;
                case 8:{
                    FormularioArticulo(file_url);
                    jObj=null;
                }break;
                case 9:{
                    caso9=1;
                    cargarListadoSucursal(file_url);

                    jObj=null;

                }break;
                case 10:{
                    cambiarPIN();
                    jObj=null;
                }break;
                case 11:{
                    //api(file_url);
                    try {
                        Integer ss=file_url.getInt("estado");
                        if (ss.equals(9)) {
                            FragmentCategoria fragment = new FragmentCategoria();
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
                    }
                    jObj=null;
                }break;
                case 12:{
                    Login2(file_url);
                    jObj=null;
                }break;
                case 13:{
                    //api(file_url);
                    try {
                        Integer ss=file_url.getInt("estado");
                        if (ss.equals(9)) {
                            FragmentCategoria fragment = new FragmentCategoria();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }
                        else if (Configuracion.settings.getString("usuario","").equals("")){
                            FragmentLogin fragment = new FragmentLogin();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

                            } else {
                            FragmentSesion fragment = new FragmentSesion();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void CargarConfiguraciones(JSONObject file_url){
        try {
            if(file_url!= null) {
                JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());

                // looping through All countries
                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);
                    Configuracion.setDatos(c.getString("parametro").equals("TAG_DATOS") ? c.getString("valor") : Configuracion.getDatos());
                    Configuracion.setIdLogin(c.getString("parametro").equals("TAG_ID_LOGIN") ? c.getString("valor") : Configuracion.getIdLogin());
                    Configuracion.setDescripcionLogin(c.getString("parametro").equals("TAG_DESCRIPCION_LOGIN") ? c.getString("valor") : Configuracion.getDescripcionLogin());
                    Configuracion.setApiUrlLogIn(c.getString("parametro").equals("API_URL_LOGIN") ? c.getString("valor") : Configuracion.getApiUrlLogIn());
                    Configuracion.setApiUrlSucursal(c.getString("parametro").equals("API_URL_SUCURSAL") ? c.getString("valor") : Configuracion.getApiUrlSucursal());
                    Configuracion.setIdCategoria(c.getString("parametro").equals("TAG_ID_CATEGORIA") ? c.getString("valor") : Configuracion.getIdCategoria());
                    Configuracion.setDescripcionCategoria(c.getString("parametro").equals("TAG_DESCRIPCION_CATEGORIA") ? c.getString("valor") : Configuracion.getDescripcionCategoria());
                    Configuracion.setCantidadCategoria(c.getString("parametro").equals("TAG_CANTIDAD_CATEGORIA") ? c.getString("valor") : Configuracion.getCantidadCategoria());
                    Configuracion.setApiUrlCategoria(c.getString("parametro").equals("API_URL_CATEGORIA") ? c.getString("valor") : Configuracion.getApiUrlCategoria());
                    Configuracion.setIdArticulo(c.getString("parametro").equals("TAG_ID_ARTICULO") ? c.getString("valor") : Configuracion.getIdArticulo());
                    Configuracion.setDescripcioArticulo(c.getString("parametro").equals("TAG_DESCRIPCION_ARTICULO") ? c.getString("valor") : Configuracion.getDescripcioArticulo());
                    Configuracion.setUnidadArticulo(c.getString("parametro").equals("TAG_UNIDAD_ARTICULO") ? c.getString("valor") : Configuracion.getUnidadArticulo());
                    Configuracion.setExistenciaArticulo(c.getString("parametro").equals("TAG_EXISTENCIA_ARTICULO") ? c.getString("valor") : Configuracion.getExistenciaArticulo());
                    Configuracion.setIdInventarioArticulo(c.getString("parametro").equals("TAG_CANTIDAD_ARTICULO") ? c.getString("valor") : Configuracion.getIdInventarioArticulo());
                    Configuracion.setApiUrlArticulo(c.getString("parametro").equals("API_URL_ARTICULO") ? c.getString("valor") : Configuracion.getApiUrlArticulo());
                    Configuracion.setIdInventario(c.getString("parametro").equals("TAG_ID_INVENTARIO") ? c.getString("valor") : Configuracion.getIdInventario());
                    Configuracion.setValorInventario(c.getString("parametro").equals("TAG_VALOR_ID_INVENTARIO") ? c.getString("valor") : Configuracion.getValorInventario());
                    Configuracion.setApiUrlInventario(c.getString("parametro").equals("API_URL_INVENTARIO") ? c.getString("valor") : Configuracion.getApiUrlInventario());
                    Configuracion.setIdRegistro(c.getString("parametro").equals("TAG_ID_REGISTRO") ? c.getString("valor") : Configuracion.getIdRegistro());
                    Configuracion.setDescripcionRegistro(c.getString("parametro").equals("TAG_DESCRIPCION_REGISTRO") ? c.getString("valor") : Configuracion.getDescripcionRegistro());
                    Configuracion.setApiUrlRegistro(c.getString("parametro").equals("API_URL_REGISTRO") ? c.getString("valor") : Configuracion.getApiUrlRegistro());
                    //if(getApiUrl().length() == 0) {
                    Configuracion.setApiUrl(c.getString("parametro").equals("API_URL2") ? c.getString("valor") : Configuracion.getApiUrl());
                    //}
                    Configuracion.setConfirmacion_Mensaje_Gurdado(c.getString("parametro").equals("CONFIRMACION_MENSAJE_GUARDADO") ? c.getString("valor") : Configuracion.getConfirmacion_Mensaje_Gurdado());
                    Configuracion.setConfirmacion_Habilitar_Decimales(c.getString("parametro").equals("CONFIRMACION_HABILITAR_DECIMALES") ? c.getString("valor") : Configuracion.getConfirmacion_Habilitar_Decimales());
                    Configuracion.setGranelArticulo(c.getString("parametro").equals("TAG_GRANEL_ARTICULO") ? c.getString("valor") : Configuracion.getGranelArticulo());
                    Configuracion.setClaveArticulo(c.getString("parametro").equals("TAG_CLAVE_ARTICULO") ? c.getString("valor") : Configuracion.getClaveArticulo());
                    Configuracion.setFlagBloqueoPorIntentos(c.getString("parametro").equals("FLAG_BLOQUEO_POR_INTENTOS") ? c.getString("valor") : Configuracion.getFlagBloqueoPorIntentos());
                    Configuracion.setFlagBloqueoCantidad(c.getString("parametro").equals("FLAG_BLOQUEO_CANTIDAD") ? c.getString("valor") : Configuracion.getFlagBloqueoCantidad());
                    Configuracion.setFlagBloqueoTiempo(c.getString("parametro").equals("FLAG_BLOQUEO_TIEMPO") ? c.getString("valor") : Configuracion.getFlagBloqueoTiempo());
                    Configuracion.setApiUrlBloqueo(c.getString("parametro").equals("API_URL_BLOQUEO") ? c.getString("valor") : Configuracion.getApiUrlBloqueo());

                    Configuracion.setidSucursal(c.getString("parametro").equals("TAG_ID_SUCURSAL") ? c.getString("valor") : Configuracion.getIdSucursal());
                    Configuracion.setDescripcionSucursal(c.getString("parametro").equals("TAG_DESCRIPCION_SUCURSAL") ? c.getString("valor") : Configuracion.getDescripcionSucursal());
                    Configuracion.setApiUrlPin(c.getString("parametro").equals("TAG_API_URL_PIN") ? c.getString("valor") : Configuracion.getApiUrlPin());
                    Configuracion.setProcesadoCategoria(c.getString("parametro").equals("TAG_PROCESADO_CATEGORIA") ? c.getString("valor") : Configuracion.getProcesadoCategoria());
                }
            }
            Configuracion.Finalizado=true;
        }catch(JSONException e)
        {
            showToast(e.getMessage());
        }
    }


    private void Login(JSONObject file_url){
        EditText txtusuario;
        EditText txtpassword;
        txtusuario   = (EditText)activity.findViewById(R.id.editText);
        txtpassword   = (EditText)activity.findViewById(R.id.editText2);
        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();
        try{
            //TextView txtusuario = (TextView) activity.findViewById(R.id.editText);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        SharedPreferences.Editor editor = settings.edit();
            switch (CodeResponse) {
                case 200: {

                    JSONObject datos = file_url.getJSONObject("datos");
                    ClaveApi = datos.getString("claveApi");
                    User = datos.getString("usuario");
                    editor.putString("ClaveApi", ClaveApi);
                    editor.putString("usuario", datos.getString("usuario"));
<<<<<<< HEAD
                    //editor.putString("idSucursal","1");
=======
                    editor.putString("nombre", datos.getString("nombre"));
>>>>>>> origin/master
                    editor.apply();
                    FragmentCategoria fragment = new FragmentCategoria();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
                    Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
                break;
                case 400:{

                    String subEstado=file_url.getString("estado");
                    switch (subEstado){
                        case "11":
                            showToast(file_url.getString("mensaje"));
                            break;
                        default:
                            showToast("Usuario o contraseña incorrectas");
                    }
                    //JSONObject datos = countryJSON.getJSONObject("mensaje");
                    //showToast(datos.getString("mensaje"));
                }break;

                case 401:{
                    if(Configuracion.getFlagBloqueoPorIntentos().equals("TRUE")) {
                        if(FragmentLogin.contador==0) {
                            FragmentLogin.variable_Usuario_Inicial = txtusuario.getText().toString();
                            FragmentLogin.variable_Usuario_Final="";
                        }
                        int bloqueo=Integer.parseInt(Configuracion.getFlagBloqueoCantidad());
                        if(FragmentLogin.variable_Usuario_Inicial.equals(FragmentLogin.variable_Usuario_Final)){
                            if((FragmentLogin.contador>=bloqueo)&&FragmentLogin.contador<10) {
                                try {
                                    JSONObject jsonObj2 = new JSONObject();
                                    jsonObj2.put("usuario", usuario);
                                    BackGroundTask bgt = new BackGroundTask(Configuracion.getApiUrlBloqueo(), "POST", jsonObj2, activity, 0);
                                    bgt.execute();
                                    FragmentLogin.contador = 10;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(FragmentLogin.contador==10){
                                showToast(file_url.getString("mensaje"));
                            }
                            if(!txtusuario.getText().toString().equals(FragmentLogin.variable_Usuario_Final)&&FragmentLogin.contador==10){
                                FragmentLogin.variable_Usuario_Final=txtusuario.getText().toString();
                            }
                            if(txtusuario.getText().toString().equals(FragmentLogin.variable_Usuario_Final)&&FragmentLogin.contador>9){
                                FragmentLogin.variable_Usuario_Inicial=txtusuario.getText().toString();
                                FragmentLogin.contador=0;
                            }
                        }else{
                            if(FragmentLogin.contador<Integer.parseInt(Configuracion.getFlagBloqueoCantidad())){
                                FragmentLogin.variable_Usuario_Final = FragmentLogin.variable_Usuario_Inicial;
                            }
                        }
                    }
                    if(FragmentLogin.contador<=Integer.parseInt(Configuracion.getFlagBloqueoCantidad())) {
                        if(FragmentLogin.contador==0){
                            FragmentLogin.contador++;
                        }
                        FragmentLogin.contador++;
                        showToast((file_url.getString("mensaje")));
                    }
                }
            break;
            default:
                    showToast(Integer.toString(BackGroundTask.CodeResponse));
            }
        }catch(JSONException e){
            showToast(e.getMessage());
        }
    }/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Login2(JSONObject file_url){
        String txtUsuario;
        EditText txtPassword;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

        String usuario = settings.getString("usuario", "");
        txtPassword   = (EditText)activity.findViewById(R.id.editText4);


        try{
            String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
            SharedPreferences.Editor editor = settings.edit();
            switch (CodeResponse) {
                case 200: {
                    JSONObject datos = file_url.getJSONObject("datos");
                    ClaveApi = datos.getString("claveApi");
                    User = datos.getString("usuario");
                    editor.putString("ClaveApi", ClaveApi);
                    editor.putString("usuario", datos.getString("usuario"));
                    editor.apply();
                    FragmentCategoria fragment = new FragmentCategoria();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
                    Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
                break;
                case 400:{

                    String subEstado=file_url.getString("estado");
                    switch (subEstado){
                        case "11":
                            showToast(file_url.getString("mensaje"));
                            break;
                        default:
                            showToast("Usuario o contraseña incorrectas");
                    }
                    //JSONObject datos = countryJSON.getJSONObject("mensaje");
                    //showToast(datos.getString("mensaje"));
                }break;

                case 401:{
                    if(Configuracion.getFlagBloqueoPorIntentos().equals("TRUE")) {
                    if(FragmentSesion.contador2==0) {
                        FragmentSesion.contador2 = 1;
                    }
                    }
                    if(FragmentSesion.contador2<=Integer.parseInt(Configuracion.getFlagBloqueoCantidad())) {
                        FragmentSesion.contador2++;
                        showToast((file_url.getString("mensaje")));
                    }
                    else {
                        FragmentSesion.contador2=0;
                        FragmentLogin fragment = new FragmentLogin();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
                        showToast("Se ha excedido el número de intentos permitidos");
                        editor.putString("usuario", "");
                        editor.putString("ClaveApi", "");
                        //editor.putString("sucursal", "");
                        editor.apply();
                    }
                }
                break;
                default:
                    showToast(Integer.toString(BackGroundTask.CodeResponse));
            }
        }catch(JSONException e){
            showToast(e.getMessage());
        }
    }

    ArrayList<ListaSucursal> countryList = new ArrayList<>();
    private void spinnerSucursal(JSONObject file_url){
        try {
            JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());
            for (int i = 0; i < countries.length(); i++) {
                JSONObject c = countries.getJSONObject(i);
                String id = c.getString(Configuracion.getIdSucursal());
                String name = c.getString(Configuracion.getDescripcionSucursal());

                // add Country
                countryList.add(new ListaSucursal(id, name.toUpperCase()));
            }
            TextView txtSucursal = (TextView) activity.findViewById(R.id.textView13);
           txtSucursal.setText(countryList.get(0).toString());
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
          TextView  txSucursal=(TextView) activity.findViewById(R.id.textView13);
            if(!settings.getString("sucursal","").equals("")){
                txSucursal.setText(settings.getString("sucursal",""));
            }
            else {

            }
        }catch(JSONException e)
        {
            showToast(e.getMessage());
        }
       //listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
        //SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, listaSuc);
       // listaSucSpinner.setAdapter(cAdapter);
        //listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
/*
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // ListaSucursal selectedCountry = listaSucSpinner.get(position);
             //   showToast(selectedCountry.getName() + " was selected!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }

    private void ListViewCategorias(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(Configuracion.getDatos());

            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject c = _JsonGenerico.getJSONObject(i);
                String cat_id = c.getString(Configuracion.getIdCategoria());
                String nombreCategoria = c.getString(Configuracion.getDescripcionCategoria());
                String cantidad = c.getString(Configuracion.getCantidadCategoria());
                String procesado = c.getString(Configuracion.getProcesadoCategoria());
                HashMap<String, String> map = new HashMap<>();
                map.put(Configuracion.getDescripcionCategoria(),nombreCategoria);
                map.put(Configuracion.getCantidadCategoria(), cantidad);
                map.put(Configuracion.getIdCategoria(), cat_id);
                map.put(Configuracion.getProcesadoCategoria(),procesado);
                //map.put(TAG_ID_ARTICULO, art_id);

                _Listado.add(map);
                list = (ListView) activity.findViewById(R.id.ListView);

                ListAdapter adapter = new ListaAdaptador(activity, _Listado,
                        R.layout.list_v,
                        new String[]{Configuracion.getDescripcionCategoria(), Configuracion.getProcesadoCategoria()+"/"+Configuracion.getCantidadCategoria()}, new int[]{R.id.descripcionColumna, R.id.api});

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(activity, "Se ha seleccionado " + _Listado.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                        String descripcionArticulo = _Listado.get(+position).get("nombre");
                     //   String art_id = _Listado.get(+position).get("art_id");
                        String cat_id = _Listado.get(+position).get("cat_id");
                        FragmentArticulo fragment = new FragmentArticulo();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        Bundle args = Bundle.EMPTY;
                        if (args == null) {
                            args = new Bundle();
                        } else {
                            args = new Bundle(args);
                        }
                     //   args.putString("art_id", art_id);
                        args.putString("cat_id", cat_id);
                        args.putString("articulo", descripcionArticulo);
                        fragment.setArguments(args);
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    }
                });
            }
        }catch(JSONException e){
            showToast(e.getMessage());
        }
    }



    private void ListViewArticulos(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(Configuracion.getDatos());
            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject jsonTemporal = _JsonGenerico.getJSONObject(i);
                String nombreArticulo = jsonTemporal.getString(Configuracion.getDescripcioArticulo());
                String idCategoria = jsonTemporal.getString(Configuracion.getIdCategoria());
                String idArticulo = jsonTemporal.getString(Configuracion.getIdArticulo());
                String idInventario = jsonTemporal.getString(Configuracion.getIdInventario());
                String Unidad = jsonTemporal.getString(Configuracion.getUnidadArticulo());
                String exitencia = jsonTemporal.getString(Configuracion.getExistenciaArticulo());
                String granel = jsonTemporal.getString(Configuracion.getGranelArticulo());
                String clave = jsonTemporal.getString(Configuracion.getClaveArticulo());
                HashMap<String, String> mappeo = new HashMap<>();
                mappeo.put(Configuracion.getDescripcioArticulo(), nombreArticulo);
                mappeo.put(Configuracion.getIdArticulo(), idArticulo);
                mappeo.put(Configuracion.getIdCategoria(),idCategoria);
                mappeo.put(Configuracion.getIdInventario(),idInventario);
                mappeo.put(Configuracion.getUnidadArticulo(),Unidad);
                mappeo.put(Configuracion.getExistenciaArticulo(), exitencia);
                mappeo.put(Configuracion.getGranelArticulo(),granel);
                mappeo.put(Configuracion.getClaveArticulo(),clave);

                _Listado.add(mappeo);

            }
            list = (ListView) activity.findViewById(R.id.ListView1);

            adapter = new ListaAdaptador(activity, _Listado,
                    R.layout.list_v,
                    new String[]{Configuracion.getDescripcioArticulo()}, new int[]{R.id.descripcionColumna});
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(activity, "Se ha seleccionado " + _Listado.get(+position).get(Configuracion.getDescripcioArticulo()), Toast.LENGTH_SHORT).show();
                    String descripcionArticulo = _Listado.get(+position).get(Configuracion.getDescripcioArticulo());
                    String art_id = _Listado.get(+position).get(Configuracion.getIdArticulo());
                    String unidad = _Listado.get(+position).get(Configuracion.getUnidadArticulo());
                    String idInventario = _Listado.get(+position).get(Configuracion.getIdInventario());
                    FragmentFormularioArticulo fragment = new FragmentFormularioArticulo();
                    String existencia = _Listado.get(+position).get(Configuracion.getExistenciaArticulo());
                    String granel=_Listado.get(+position).get(Configuracion.getGranelArticulo());
                    String clave=_Listado.get(+position).get(Configuracion.getClaveArticulo());

                    FragmentManager fragmentManager = activity.getFragmentManager();
                    Bundle args = Bundle.EMPTY;
                    if (args == null) {
                        args = new Bundle();
                    } else {
                        args = new Bundle(args);
                    }
                    args.putString(Configuracion.getIdArticulo(), art_id);
                    Log.d("art_id", art_id);
                    Log.d("unidad", Configuracion.getUnidadArticulo());
                    Log.d("idInventario",idInventario);
                    args.putString(Configuracion.getIdInventario(), idInventario);
                    args.putString(Configuracion.getUnidadArticulo(), unidad);
                    args.putString(Configuracion.getDescripcioArticulo(), descripcionArticulo);
                    args.putString(Configuracion.getGranelArticulo(),granel);
                    args.putString(Configuracion.getClaveArticulo(),clave);
                    args.putString(Configuracion.getExistenciaArticulo(),existencia);
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
            });
        }catch (JSONException e){
            showToast(e.getMessage());
        }
    }

    private void FormularioArticulo(JSONObject file_url){


    }
    private void RegistrarUsuario(JSONObject file_url){

    }
    public void cargarListadoSucursal(JSONObject file_url) {
        //  List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        //apiParams.add(new BasicNameValuePair("call", "countrylist"));

        // bgt = new BackGroundTask(Configuracion.getApiUrl(), "GET", null,getActivity(),0);
       // if(file_url.equals(null)){
        if (!file_url.equals(null)) {
            try {
                JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());

                if (countries.length() > 0) {
                    for (int i = 0; i < countries.length(); i++) {
                        JSONObject c = countries.getJSONObject(i);
                        String id = c.getString(Configuracion.getIdSucursal());
                        String name = c.getString(Configuracion.getDescripcionSucursal());

                            // add Country
                        countryList.add(new ListaSucursal(id, name.toUpperCase()));
                    }
                } else {
                    showToast(":(");
                    countryList.add(new ListaSucursal("", "".toUpperCase()));
                }
                listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, countryList);
                listaSucSpinner.setAdapter(cAdapter);
                showToast("Se han cargado las sucursales");
                listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ListaSucursal selectedCountry = countryList.get(position);
                        sucursalSeleccionada=selectedCountry.toString();
                        //prueba = (TextView) activity.findViewById(R.id.textView17);
                        //prueba.setText(selectedCountry.toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                guardar = (Button) activity.findViewById(R.id.button9);
                guardar.setEnabled(true);

            }catch(JSONException e){
                e.printStackTrace();
            }

        }
        else {
            showToast("No se ha podido establecer la conexión");
        }
    }
    private void cambiarPIN(){
        switch (CodeResponse) {
            case 200: {
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                showToast("Se ha guardado correctamente");
                FragmentCategoria fragment = new FragmentCategoria();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                v.vibrate(300);
            }break;
            case 401: {
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                showToast("La contraseña es incorrecta");
                v.vibrate(500);
            }break;
            //showToast("Se ha guardado correctamente");
        }
    }
    private void api(JSONObject file_url) {

    }

}

