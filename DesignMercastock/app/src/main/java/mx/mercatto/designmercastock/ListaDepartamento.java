package mx.mercatto.designmercastock;
import android.content.Intent;
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
    private static final String MAP_API_URL = "http://192.168.1.97/wsMercaStock/categoria";
    private BackGroundTask bgt;

public ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_departamento);
        setTitle("Lista de Categorias");
        //buildCountryDropDown();
        cargarListadoCategoria();
    }

    JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria() {
        // Building post parameters, key and value pair


        bgt = new BackGroundTask(MAP_API_URL, "GET", null);

        try {
            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(TAG_DATA);
            for(int i = 0; i < android.length(); i++){
                JSONObject c = android.getJSONObject(i);
                final String cat_id= c.getString(TAG_ID);
                // Storing  JSON item in a Variable
                //String ver = c.getString(TAG_VER);
                String name = c.getString(TAG_NAME);
                String api = c.getString(TAG_QTY);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<String, String>();

                //map.put(TAG_VER, ver);
                map.put(TAG_NAME, name);
                map.put(TAG_QTY, api);
                map.put(TAG_ID,cat_id);

                oslist.add(map);
                list=(ListView)findViewById(R.id.ListView);

                ListAdapter adapter = new SimpleAdapter(ListaDepartamento.this, oslist,
                        R.layout.list_v,
                        new String[] {TAG_NAME, TAG_QTY,TAG_ID }, new int[] {R.id.name, R.id.api,R.id.cat_id});

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(ListaDepartamento.this, "Se ha seleccionado "+oslist.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                        String articulo =oslist.get(+position).get("nombre");
                        String categoria =oslist.get(+position).get("cat_id");
                        Intent myIntent = new Intent(ListaDepartamento.this,ListaArticulo.class);
                        myIntent.putExtra("articulo", articulo);
                        myIntent.putExtra("cat_id",categoria);
                                startActivity(myIntent);
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
