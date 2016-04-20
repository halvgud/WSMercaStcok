package mx.mercatto.mercastock.BGT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import mx.mercatto.mercastock.Configuracion;
import mx.mercatto.mercastock.FragmentCategoria;
import mx.mercatto.mercastock.FragmentLogin;
import mx.mercatto.mercastock.FragmentSesion;

import mx.mercatto.mercastock.Main;
import mx.mercatto.mercastock.R;

/**
 * Created by Ryu on 16/04/2016.
 */
public class BGTAPI extends AsyncTask<String, String, JSONObject> {
    String URL = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    JSONObject postparams = null;
    Activity activity;
    ProgressDialog asyncDialog;
    public static String ClaveApi = "Default";
    public static String User = "Default";

    static Integer CodeResponse;
    public BGTAPI(String url, Activity activity, JSONObject postparams) {
        this.URL = url;
        this.activity = activity;
        this.postparams = postparams;
        if (activity!= null)
            asyncDialog = new ProgressDialog(activity);
    }
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            HttpPost httpPost = new HttpPost(URL);
            StringEntity entity = new StringEntity(postparams.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            CodeResponse = response.getStatusLine().getStatusCode();
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
            showToast(e.getMessage());
        } catch (Exception e) {
            showToast(e.getMessage());
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
                    Login(file_url);
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
    public static int ClAp;
    private void Login(JSONObject file_url){
        try {
            ClAp=file_url.getInt("estado");

            if(Main.inicio==1) {
                if (ClAp == 9) {
                    String us=Configuracion.settings.getString("usuario", "");
                    String lo=Configuracion.settings.getString("login", "");
                    if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("") && Configuracion.settings.getString("login", "").equals("true")) {
                        FragmentCategoria fragment = new FragmentCategoria();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    }
                    if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("") && Configuracion.settings.getString("login", "").equals("false")) {
                        FragmentSesion fragment = new FragmentSesion();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    }
                }
                if (ClAp == 11) {
                    if (Main.idSesion == 1 && !Configuracion.settings.getString("usuario", "").equals("")) {
                        FragmentSesion fragment = new FragmentSesion();
                        FragmentManager fragmentManager = activity.getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                    }
                }
            }
            if(Main.inicio==0){
                FragmentLogin fragment = new FragmentLogin();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
                Main.inicio=1;
            }

            jObj=null;
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


