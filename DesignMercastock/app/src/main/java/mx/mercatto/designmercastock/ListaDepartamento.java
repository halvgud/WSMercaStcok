package mx.mercatto.designmercastock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.HashMap;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
public class ListaDepartamento extends AppCompatActivity {

    private static final String TAG_ID = "cat_id";//
    private static final String TAG_NAME = "nombre";//
    private static final String TAG_DATA = "datos";//Falta
    private static final String TAG_QTY = "CANTIDAD";//Falta
    private static final String MAP_API_URL = "http://192.168.1.41/wsMercaStock/categoria";
    private BackGroundTask bgt;
    Spinner listaCatSpinner;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
/////////////////////
public ListView list;
    ArrayList<listaCategoria> countryList = new ArrayList<listaCategoria>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_departamento);
        setTitle("Lista de Categorias");
        cargarListadoCategoria();}

    JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria() {
        bgt = new BackGroundTask(MAP_API_URL, "GET", null);
        try {
            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(TAG_DATA);
            for(int i = 0; i < android.length(); i++){
                JSONObject c = android.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String api = c.getString(TAG_QTY);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_NAME, name);
                map.put(TAG_QTY, api);

                oslist.add(map);
                list=(ListView)findViewById(R.id.ListView);

                ListAdapter adapter = new SimpleAdapter(ListaDepartamento.this, oslist,
                        R.layout.list_v,
                        new String[] {TAG_NAME, TAG_QTY }, new int[] {R.id.name, R.id.api});

                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(ListaDepartamento.this, "You Clicked at "+oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
