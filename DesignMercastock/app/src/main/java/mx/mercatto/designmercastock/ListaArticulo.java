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

private static String TAG_ID ;//
    private static final String TAG_NAME = "descripcion";//
    private static final String TAG_DATA = "datos";//Falta
    private static final String TAG_QTY = "CANTIDAD";//Falta
    private static final String TAG_UNIDAD ="Unidad";
    private static  final  String TAG_EXIST = "NombreArticulo";
    private static final String MAP_API_URL = "http://192.168.1.17/wsMercaStock/articulo";
    private BackGroundTask bgt;
    public ListView list;
    String categori="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG_ID="cat_id";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_articulo);
        setTitle("Lista de " + getIntent().getExtras().getString("articulo2"));
        categori=getIntent().getExtras().getString("cat_id");
        cargarListadoCategoria();
    }

   JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria() {
        try {

        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("cat_id", categori);

        bgt = new BackGroundTask(MAP_API_URL, "POST", jsonObj1);

            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(TAG_DATA);
            for(int i = 0; i < android.length(); i++){
                JSONObject c = android.getJSONObject(i);

                String name = c.getString(TAG_NAME);
                String api = c.getString(TAG_ID);
                String unidad = c.getString(TAG_UNIDAD);
                String exitencia = c.getString(TAG_EXIST);
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_NAME, name);
                map.put(TAG_ID, api);
                map.put(TAG_UNIDAD, unidad);
                map.put(TAG_EXIST,exitencia);
                oslist.add(map);
                list=(ListView)findViewById(R.id.ListView);

                ListAdapter adapter = new ListaAdaptador(ListaArticulo.this, oslist,
                        R.layout.list_v,
                        new String[] {TAG_NAME }, new int[] {R.id.name});

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(ListaArticulo.this, "Se ha seleccionado " + oslist.get(+position).get("descripcion"), Toast.LENGTH_SHORT).show();
                        String articulo2 =oslist.get(+position).get("descripcion");
                        String unidad2=oslist.get(+position).get(TAG_UNIDAD);
                        String existencia2=oslist.get(+position).get("NombreArticulo");
                        Intent myIntent = new Intent(ListaArticulo.this,FormularioArticulo.class);
                        myIntent.putExtra("articulo2", articulo2);
                        myIntent.putExtra("unidad2",unidad2);
                        myIntent.putExtra("existencia2", existencia2);
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
