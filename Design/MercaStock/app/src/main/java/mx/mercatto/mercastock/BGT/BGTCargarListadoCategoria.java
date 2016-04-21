package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.ListaAdaptador;

import mx.mercatto.mercastock.Main;
import mx.mercatto.mercastock.R;

/**
 * Created by Ryu on 16/04/2016.
 */
public class BGTCargarListadoCategoria extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Activity activity;
    ProgressDialog asyncDialog;
    public static JSONArray _JsonGenerico = null;
    public static ArrayList<HashMap<String, String>>_Listado;

    public BGTCargarListadoCategoria(String url, Activity activity) {
        this.URL = url;
        this.activity = activity;

        if (activity!= null)
            asyncDialog = new ProgressDialog(activity);
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(activity!=null) {
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Cargando Lista de Categorias");
            asyncDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        _Listado = new ArrayList<>();
        _JsonGenerico = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));

        } catch (UnsupportedEncodingException e) {
           // showToast(e.getMessage());
            bandera=false;
        } catch (JSONException e) {
         //   showToast(e.getMessage());
            bandera=false;
        } catch (Exception e) {
            //showToast(e.getMessage());
            bandera=false;
        }
        return jObj;

    }
boolean bandera=true;
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            if(bandera)
            ListViewCategorias(file_url);
            else{

                FragmentConexionPerdida fragment = new FragmentConexionPerdida();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            }
            jObj=null;
               }
        catch (Exception e) {
            throw e;
        }
        finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }

    public ListView list;
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    private void ListViewCategorias(JSONObject file_url){
        try {
            _JsonGenerico = file_url.getJSONArray(Configuracion.getDatos());

            for (int i = 0; i < _JsonGenerico.length(); i++) {
                JSONObject c = _JsonGenerico.getJSONObject(i);
                String cat_id = c.getString(Configuracion.getIdCategoria());
                String nombreCategoria = c.getString(Configuracion.getDescripcionCategoria());
                 int cantidad = c.getInt(Configuracion.getCantidadCategoria());
                 int procesado = c.getInt(Configuracion.getProcesadoCategoria());
                HashMap<String, String> map = new HashMap<>();
                map.put(Configuracion.getDescripcionCategoria(),nombreCategoria);
                map.put(Configuracion.getCantidadCategoria(), Integer.toString(cantidad));
                map.put(Configuracion.getIdCategoria(), cat_id);
                map.put(Configuracion.getProcesadoCategoria(), Integer.toString(procesado));
             map.put("proceso",Integer.toString(procesado)+"/"+Integer.toString(cantidad));
                _Listado.add(map);
                list = (ListView) activity.findViewById(R.id.ListView);

                ListAdapter adapter = new ListaAdaptador(activity, _Listado,
                        R.layout.list_v,
                        new String[]{Configuracion.getDescripcionCategoria(), "proceso"}, new int[]{R.id.descripcionColumna, R.id.api});

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



}
