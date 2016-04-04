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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
    /**
     * 1.- Login
     * 2.- ListaSucursal
     * 3.- Lista de Categorias
     * 4.- Lista de Articulos
     */
    Integer Codigo;
    Activity activity;
    ProgressDialog asyncDialog;
    ArrayList<ListaSucursal> listaSuc= new ArrayList<>();
    public BackGroundTask(String url, String method, JSONObject params, Activity activity, Integer codigo) {
        this.URL = url;
        this.postparams = params;
        this.method = method;
        this.Codigo = codigo;
        this.activity = activity;
        asyncDialog = new ProgressDialog(activity);
    }

    JSONArray _JsonGenerico = null;
    ArrayList<HashMap<String, String>> _Listado = new ArrayList<>();
    public ListView list;
    ListAdapter adapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        switch (Codigo) {
            case 1: {
                asyncDialog.setMessage("Cargando Usuario");
            }
            break;
            case 2: {
                asyncDialog.setMessage("Cargando Lista de Sucursales");
            }
            break;
            case 3: {
                asyncDialog.setMessage("Cargando Lista de Categorias");
            }
            break;
            case 4: {
                asyncDialog.setMessage("Cargando Lista de Articulos");
            }
            break;
        }
        asyncDialog.setIndeterminate(false);
        asyncDialog.setCancelable(false);
        asyncDialog.setProgress(0);
        asyncDialog.show();

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
            switch(Codigo){
                case 1:{}break;
                case 2:{
                    _JsonGenerico = jObj.getJSONArray(TAG_DATOS);
                    for (int i = 0; i < _JsonGenerico.length(); i++) {
                        JSONObject c = _JsonGenerico.getJSONObject(i);
                        String idSucursal = c.getString(TAG_ID_SUCURSAL);
                        String nombreSucursal = c.getString(TAG_NOMBRE_SUCURSAL);
                        listaSuc.add(new ListaSucursal(idSucursal, nombreSucursal.toUpperCase()));
                    }
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
    private final static String TAG_DATOS = "datos";
    private final static String TAG_NOMBRE_ARTICULO = "NombreArticulo";

    public static String ClaveApi = "";
    private static final String TAG_ID_SUCURSAL = "idSucursal";
    private static final String TAG_NOMBRE_SUCURSAL = "nombre";
    //Spinner listaSucSpinner;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            switch (Codigo){
                case 1:{
                    Login(file_url);
                }break;
                case 2:{
                    spinnerSucursal(file_url);
                }break;
                case 3:{
                    ListViewCategorias(file_url);
                }break;
                case 4:{
                    ListViewArticulos(file_url);
                }break;
            }


        } catch (Exception e) {
            showToast(e.toString());
        }
        asyncDialog.dismiss();
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    private void Login(JSONObject file_url){
        try{

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        //String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        SharedPreferences.Editor editor = settings.edit();
            switch (CodeResponse) {
                case 200: {
                    JSONObject datos = file_url.getJSONObject("datos");
                    ClaveApi = datos.getString("claveApi");
                    editor.putString("ClaveApi", ClaveApi);
                    editor.apply();
                    FragmentCategoria fragment = new FragmentCategoria();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
                    Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(300);
                    // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
                break;
                case 401:
                    showToast(("Usuario y/o password incorrectas"));
                    // activity.findViewById(R.id.button2).setEnabled(false);
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
            JSONArray countries = file_url.getJSONArray(TAG_DATOS);
            for (int i = 0; i < countries.length(); i++) {
                JSONObject c = countries.getJSONObject(i);
                String id = c.getString(TAG_ID_SUCURSAL);
                String name = c.getString(TAG_NOMBRE_SUCURSAL);

                // add Country
                countryList.add(new ListaSucursal(id, name.toUpperCase()));
            }
            TextView txtSucursal = (TextView) activity.findViewById(R.id.textView13);
            txtSucursal.setText(countryList.get(0).toString());
        }catch(JSONException e)
        {
            showToast(e.getMessage());
        }
       // listaSucSpinner = (Spinner) activity.findViewById(R.id.spinSucursal);
        //SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, listaSuc);
      //  listaSucSpinner.setAdapter(cAdapter);
     //   listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        /*    @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListaSucursal selectedCountry = listaSuc.get(position);
                showToast(selectedCountry.getName() + " was selected!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }
    private final static String TAG_ID_CATEGORIA="cat_id";
    private final static String TAG_ID_ARTICULO="art_id";
    private final static String TAG_NOMBRE_CATEGORIA = "nombre";
    private final static String TAG_CANTIDAD = "CANTIDAD";
    private void ListViewCategorias(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(TAG_DATOS);

            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject c = _JsonGenerico.getJSONObject(i);

                String cat_id = c.getString(TAG_ID_CATEGORIA);
                //String art_id = c.getString(TAG_ID_ARTICULO);
                String nombreCategoria = c.getString(TAG_NOMBRE_CATEGORIA);
                String cantidad = c.getString(TAG_CANTIDAD);
                HashMap<String, String> map = new HashMap<>();
                map.put(TAG_NOMBRE_CATEGORIA,nombreCategoria);
                map.put(TAG_CANTIDAD, cantidad);
                map.put(TAG_ID_CATEGORIA, cat_id);
                //map.put(TAG_ID_ARTICULO, art_id);

                _Listado.add(map);
                list = (ListView) activity.findViewById(R.id.ListView);

                ListAdapter adapter = new SimpleAdapter(activity, _Listado,
                        R.layout.list_v,
                        new String[]{TAG_NOMBRE_CATEGORIA, TAG_CANTIDAD, TAG_ID_CATEGORIA}, new int[]{R.id.descripcionColumna, R.id.api, R.id.cat_id});

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

    private static final String TAG_UNIDAD="Unidad";
    private void ListViewArticulos(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(TAG_DATOS);
            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject jsonTemporal = _JsonGenerico.getJSONObject(i);
                String nombreArticulo = jsonTemporal.getString(TAG_NOMBRE_ARTICULO);
                String idCategoria = jsonTemporal.getString(TAG_UNIDAD);
                String idArticulo = jsonTemporal.getString(TAG_ID_ARTICULO);
                HashMap<String, String> mappeo = new HashMap<>();
                mappeo.put(TAG_NOMBRE_ARTICULO, nombreArticulo);
                mappeo.put(TAG_ID_ARTICULO, idArticulo);
                mappeo.put(TAG_UNIDAD,idCategoria);
                _Listado.add(mappeo);

            }
            list = (ListView) activity.findViewById(R.id.ListView1);

            adapter = new SimpleAdapter(activity, _Listado,
                    R.layout.list_v,
                    new String[]{TAG_NOMBRE_ARTICULO}, new int[]{R.id.descripcionColumna});
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(activity, "Se ha seleccionado " + _Listado.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                    String descripcionArticulo = _Listado.get(+position).get(TAG_NOMBRE_ARTICULO);
                    String art_id = _Listado.get(+position).get(TAG_ID_ARTICULO);
                    String unidad = _Listado.get(+position).get(TAG_UNIDAD);
                    FragmentFormularioArticulo fragment = new FragmentFormularioArticulo();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    Bundle args = Bundle.EMPTY;
                    if (args == null) {
                        args = new Bundle();
                    } else {
                        args = new Bundle(args);
                    }
                    args.putString("art_id", art_id);
                    args.putString("Unidad", unidad);
                    args.putString("NombreArticulo", descripcionArticulo);
                    fragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
            });
        }catch (JSONException e){
            showToast(e.getMessage());
        }
    }
}

