package mx.mercatto.designmercastock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ListaArticulo extends AppCompatActivity {

    private static final String TAG_ID = "cat_id";//
    private static final String TAG_NAME = "descripcion";//
    private static final String TAG_DATA = "datos";//Falta
    private static final String TAG_QTY = "CANTIDAD";//Falta
    private static final String MAP_API_URL = "http://192.168.1.53/wsMercaStock/articulo";
    private BackGroundTask bgt;
    public ListView list;
    String categori="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulo);
        setTitle("Lista de " + getIntent().getExtras().getString("articulo"));
        categori=getIntent().getExtras().getString("cat_id");
        cargarListadoCategoria();
    }
///////////////////Temporal Prueba///////////////////////////////

    JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria() {
        try {
        // Building post parameters, key and value pair
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("cat_id", categori);
        // Create the POST object and add the parameters
        bgt = new BackGroundTask(MAP_API_URL, "POST", jsonObj1);

      //  bgt = new BackGroundTask(MAP_API_URL+categori, "POST", null);



            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(TAG_DATA);
            for(int i = 0; i < android.length(); i++){
                JSONObject c = android.getJSONObject(i);

                // Storing  JSON item in a Variable
                //String ver = c.getString(TAG_VER);
                String name = c.getString(TAG_NAME);
                String api = c.getString(TAG_ID);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<String, String>();

                //map.put(TAG_VER, ver);
                map.put(TAG_NAME, name);
                map.put(TAG_ID, api);

                oslist.add(map);
                list=(ListView)findViewById(R.id.ListView);

                ListAdapter adapter = new SimpleAdapter(ListaArticulo.this, oslist,
                        R.layout.list_v,
                        new String[] {TAG_NAME, TAG_ID }, new int[] {R.id.name, R.id.api});

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(ListaArticulo.this, "Se ha seleccionado " + oslist.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                        String articulo2 =oslist.get(+position).get("nombre");
                        Intent myIntent = new Intent(ListaArticulo.this,FormularioArticulo.class);
                        myIntent.putExtra("articulo2", articulo2);
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
    /////////////////////////////////////////////////
}
