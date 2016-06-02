package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.UnknownHostException;

import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentCategoria;
import mx.mercatto.mercastock.FragmentConexionPerdida;
import mx.mercatto.mercastock.FragmentLogin;
import mx.mercatto.mercastock.FragmentSesion;

import mx.mercatto.mercastock.FragmentSucursal;
import mx.mercatto.mercastock.Main;
import mx.mercatto.mercastock.R;

public class BGTAPI extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    Boolean FLAG_ASYNCDIALOG=false;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONObject postparams = null;
    Activity activity;
    ProgressDialog asyncDialog;
    public static String ClaveApi = "Default";
    //public static String User = "Default";
public boolean transaccionCompleta=false;
    static Integer CodeResponse;
    public BGTAPI(String url, Activity activity, JSONObject postparams,Boolean flag_asyncdialog) {
        this.URL = url;
        this.activity = activity;
        this.postparams = postparams;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity);
            this.FLAG_ASYNCDIALOG=flag_asyncdialog;
    }
    protected void onPreExecute() {
        super.onPreExecute();
        if(FLAG_ASYNCDIALOG){
            asyncDialog.setIndeterminate(false);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("Reconectando...");
            asyncDialog.show();
        }

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {

            HttpPost httpPost = new HttpPost(URL);
            StringEntity entity = new StringEntity(postparams.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
// The default value is zero, that means the timeout is not used.
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

            HttpClient client = new DefaultHttpClient(httpParameters);

            HttpResponse response = client.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            CodeResponse = response.getStatusLine().getStatusCode();
            is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
transaccionCompleta=true;
        }
         /*catch (UnsupportedEncodingException e) {
            showToast(e.getMessage());
        } catch (Exception e) {
showToast(":(");
            showToast(e.getMessage());
        }
        return jObj;

    }*/
            /*java.net.URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getResponseCode();
            is = conn.getErrorStream();
            if (is == null) {
                is  = conn.getInputStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line ).append("\n");
            }
            is.close();
            json = sb.toString();
            jObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
            transaccionCompleta=true;
        } */catch (IllegalArgumentException|UnknownHostException |JSONException|MalformedURLException |UnsupportedEncodingException|ProtocolException e) {
            e.printStackTrace();
            transaccionCompleta=false;
        }catch(RuntimeException|IOException e){
            //e.printStackTrace();
            transaccionCompleta=false;
        }

            return jObj;
    }
    public void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onPostExecute(JSONObject file_url) {
        try {
            super.onPostExecute(file_url);
            if(transaccionCompleta) {
                //if(!Configuracion.settings.getString("controlusuario","").equals(""))
                  //  Main.controlUsuario = Integer.parseInt(Configuracion.settings.getString("controlusuario", ""));
                Login(file_url);

            }
            else if(activity!=null){
                showToast("Conexion fallida");
           /*     if(Main.inicio==1) {
                    BGTAPI bgt;
                    try {

                        JSONObject jsonObj1 = new JSONObject();
                        jsonObj1.put("claveApi",Configuracion.settings.getString("ClaveApi",""));
                        bgt = new BGTAPI(Configuracion.getApiUrlRevisarApi(), activity,jsonObj1 );

                        bgt.execute();
                    } catch (Exception e){
                        this.showToast(e.getMessage());
                    }

                }else if(!FragmentConexionPerdida.FLAG_CONEXION_PERDIDA){
                    FragmentSucursal fragment = new FragmentSucursal();
                    FragmentManager fragmentManager = activity.getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                }
           */ }else if(FLAG_ASYNCDIALOG){
                    asyncDialog.dismiss();
                    }
                    jObj=null;
               }
        finally{
            if(activity!=null){
                asyncDialog.dismiss();
            }
        }
    }
    public static int ClAp;
    private void Login(JSONObject file_url) {
        if (transaccionCompleta) {
            try {
                ClAp = file_url.getInt("estado");
                //String us = Configuracion.settings.getString("usuario", "");
                //String lo = Configuracion.settings.getString("login", "");
                //if (Main.inicio == 1) {
                    if (ClAp == 9) {

                        if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("") && Configuracion.settings.getString("login", "").equals("true")) {
                            /*Main.controlUsuario =Integer.parseInt(Configuracion.settings.getString("controlusuario",""));
                            activity.finish();
                            Intent intent = activity.getIntent();
                            activity.startActivity(intent);*/
                            FragmentCategoria fragment = new FragmentCategoria();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }
                        else if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("") && Configuracion.settings.getString("login", "").equals("false")) {
                            FragmentSesion fragment = new FragmentSesion();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }
                        else{
                            FragmentLogin fragment = new FragmentLogin();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                            Main.inicio = 1;

                        }
                    }
                    if (ClAp == 11) {
                        if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("")) {
                            FragmentSesion fragment = new FragmentSesion();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        }else{
                            FragmentLogin fragment = new FragmentLogin();
                            FragmentManager fragmentManager = activity.getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                            Main.inicio = 1;
                        }
                    }
           /*     } else {
                    if (Configuracion.settings.getString("ip", "").equals("")) {
                        FragmentSucursal fragment = new FragmentSucursal();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    } else {
                        FragmentLogin fragment = new FragmentLogin();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                        Main.inicio = 1;
                    }
                }*/

                jObj = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}


