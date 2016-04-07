    package mx.mercatto.designmercastock;

    import android.content.Intent;
    import android.support.v7.app.ActionBarActivity;
    import android.os.Bundle;
    import android.view.View;
    import java.util.ArrayList;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
    import android.widget.AdapterView;
    import android.widget.Toast;
    import java.util.concurrent.ExecutionException;
    import java.util.HashMap;import android.widget.ListAdapter;
    import android.widget.ListView;
    import android.widget.SimpleAdapter;

public class ListaDepartamento extends ActionBarActivity {

    //private static final String TAG_ID_LISTADEPARTAMENTO = "cat_id";//
    //private static final String TAG_NAME_LISTADEPARTAMENTO = "nombre";//
    //private static final String TAG_DATA_LISTADEPARTAMENTO = "datos";//Falta
    //private static final String TAG_QTY_LISTADEPARTAMENTO = "CANTIDAD";//Falta
    //private static final String MAP_API_URL_LISTADEPARTAMENTO= "http://192.168.1.17/wsMercaStock/categoria";
    private BackGroundTask bgt;
String posi;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    ArrayList<listaCategoria> countryList = new ArrayList<listaCategoria>();

public ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_departamento);
        setTitle("Lista de Categorias");
        cargarListadoCategoria();

    }

    JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria() {

        bgt = new BackGroundTask(Configuracion.getApiUrlCategoria(), "GET", null);
        try {
            JSONObject json = bgt.execute().get();
            android = json.getJSONArray(Configuracion.getDatos());
            for (int i = 0; i < android.length(); i++) {
                JSONObject c = android.getJSONObject(i);

                final String cat_id = c.getString(Configuracion.getIdCategoria());

                String name = c.getString(Configuracion.getDescripcionCategoria());
                String api = c.getString(Configuracion.getCantidadCategoria());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(Configuracion.getDescripcionCategoria(), name);
                map.put(Configuracion.getCantidadCategoria(), api);
                map.put(Configuracion.getIdCategoria(), cat_id);

                oslist.add(map);
                list = (ListView) findViewById(R.id.ListView);


                ListAdapter adapter = new ListaAdaptador(ListaDepartamento.this, oslist,
                        R.layout.list_v,
                        new String[]{Configuracion.getDescripcionCategoria(), Configuracion.getCantidadCategoria(), Configuracion.getIdCategoria()}, new int[]{R.id.name, R.id.api});

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        posi=oslist.get(+position).toString();
                        Toast.makeText(ListaDepartamento.this, "Se ha seleccionado " + oslist.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                        String articulo2 =oslist.get(+position).get("nombre");
                        Intent myIntent = new Intent(ListaDepartamento.this,ListaArticulo.class);
                        myIntent.putExtra("articulo2", articulo2);
                        myIntent.putExtra("cat_id",cat_id);

                        startActivity(myIntent);
                    }
                });
            }

        }catch (JSONException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }}
        public void showToast(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }

    }