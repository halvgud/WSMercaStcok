package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mx.mercatto.mercastock.Configuracion;

import mx.mercatto.mercastock.ListaSucursal;
import mx.mercatto.mercastock.R;
import mx.mercatto.mercastock.SucursalAdapter;

public class BGTConfigurarServidorSucursal extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    Activity activity;
    ProgressDialog asyncDialog;


    public BGTConfigurarServidorSucursal(String url, Activity activity) {
        this.URL = url;
        this.activity = activity;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity);
    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line ).append("\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));

        } catch (IOException|JSONException e) {
            showToast(e.getMessage());
        }
        return jObj;

    }

    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);

            cargarListadoSucursal(file_url);
                    jObj=null;
               }
        catch (Exception e) {
            showToast(e.getMessage());
        }
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    ArrayList<ListaSucursal> _listaSucursal = new ArrayList<>();
    Spinner listaSucSpinner;
    Button guardar;
    public static String sucursalSeleccionada="";
    public void cargarListadoSucursal(JSONObject file_url) {
        if (file_url!=null) {
            try {
                JSONArray countries = file_url.getJSONArray(Configuracion.getDatos());

                if (countries.length() > 0) {
                    for (int i = 0; i < countries.length(); i++) {
                        JSONObject c = countries.getJSONObject(i);
                        String id = c.getString(Configuracion.getIdSucursal());
                        String name = c.getString(Configuracion.getDescripcionSucursal());
                        _listaSucursal.add(new ListaSucursal(id, name.toUpperCase()));
                    }
                } else {
                    showToast(":(");
                    _listaSucursal.add(new ListaSucursal("", "".toUpperCase()));
                }
                listaSucSpinner = (Spinner) activity.findViewById(R.id.spinnerRegistroUsuario);
                SucursalAdapter cAdapter = new SucursalAdapter(activity, android.R.layout.simple_spinner_item, _listaSucursal);
                listaSucSpinner.setAdapter(cAdapter);
                showToast("Se han cargado las sucursales");
                listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ListaSucursal selectedCountry = _listaSucursal.get(position);
                        sucursalSeleccionada=selectedCountry.toString();
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

}
