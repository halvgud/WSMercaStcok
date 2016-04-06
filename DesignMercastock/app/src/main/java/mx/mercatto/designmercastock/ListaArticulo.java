package mx.mercatto.designmercastock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ListaArticulo extends AppCompatActivity {

//private static String TAG_ID_LISTAARTICULO="cat_id" ;//
    //private static final String TAG_NAME_LISTAARTICULO = "NombreArticulo";//
    //private static final String TAG_DATA_LISTAARTICULO = "datos";//Falta
    //private static final String TAG_UNIDAD_LISTAARTICULO ="Unidad";
    //private static  final  String TAG_EXIST_LISTAARTICULO = "Existencia";
    //private static  final  String TAG_ID_INVENTARIO_LISTAARTICULO = "idInventario";

    //private static final String MAP_API_URL_LISTAARTICULO = "http://192.168.1.17/wsMercaStock/articulo/obtener";
    private BackGroundTask bgt;
    public ListView list;
    String categori="";
    String fd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        bgt = new BackGroundTask(Configuracion.getApiUrlArticulo(), "POST", jsonObj1);

            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(Configuracion.getDatos());

            for(int i = 0; i < android.length(); i++){

                JSONObject c = android.getJSONObject(i);

                String name = c.getString(Configuracion.getDescripcioArticulo());
                String art_id = c.getString(Configuracion.getIdArticulo());
                String unidad = c.getString(Configuracion.getUnidadArticulo());
                String exitencia = c.getString(Configuracion.getExistenciaArticulo());

                HashMap<String, String> map = new HashMap<String, String>();

                map.put(Configuracion.getDescripcioArticulo(), name);
                map.put(Configuracion.getIdArticulo(), art_id);
                map.put(Configuracion.getUnidadArticulo(), unidad);
                map.put(Configuracion.getExistenciaArticulo(), exitencia);

                map.put(Configuracion.getIdInventarioArticulo(),c.getString(Configuracion.getIdInventarioArticulo()));
                oslist.add(map);

                list=(ListView)findViewById(R.id.ListView);

                final ListAdapter adapter = new ListaAdaptador(ListaArticulo.this, oslist,
                        R.layout.list_v,
                        new String[] {Configuracion.getDescripcioArticulo() }, new int[] {R.id.name})
                        ;
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Toast.makeText(ListaArticulo.this, "Se ha seleccionado " + oslist.get(+position).get(Configuracion.getDescripcioArticulo()), Toast.LENGTH_SHORT).show();
                        String articulo2 = oslist.get(+position).get(Configuracion.getDescripcioArticulo());
                        String unidad2 = oslist.get(+position).get(Configuracion.getUnidadArticulo());
                        String existencia2 = oslist.get(+position).get(Configuracion.getExistenciaArticulo());
                        String idInventario = oslist.get(+position).get(Configuracion.getIdInventarioArticulo());
                        String idArticulo = oslist.get(+position).get(Configuracion.getIdArticulo());
                        Intent myIntent = new Intent(ListaArticulo.this, FormularioArticulo.class);
                        myIntent.putExtra("articulo2", articulo2);
                        myIntent.putExtra("unidad2", unidad2);
                        myIntent.putExtra("existencia2", existencia2);
                        myIntent.putExtra("idInventario",idInventario);
                        myIntent.putExtra("art_id",idArticulo);
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
