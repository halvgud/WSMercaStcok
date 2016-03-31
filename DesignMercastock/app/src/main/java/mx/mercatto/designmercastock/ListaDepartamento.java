    package mx.mercatto.designmercastock;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
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
    import android.os.Handler;

    public class ListaDepartamento extends AppCompatActivity {
        ProgressDialog barProgressDialog;
        private static final String TAG_ID = "cat_id";//1
        private static final String TAG_ID2 = "cat_id";//2
        private static final String TAG_NAME = "nombre";//1
        private static final String TAG_DATA = "datos";//1 y 2
        private static final String TAG_QTY = "CANTIDAD";//
        private static final String TAG_UNIDAD ="Unidad";
        private static final String MAP_API_URL = "http://192.168.1.17/wsMercaStock/categoria";
        private static final String MAP_API_URL2 = "http://192.168.1.17/wsMercaStock/articulo";
        int contador=0;
        private BackGroundTask bgt;
        private static final String TAG_NAME2 = "NombreArticulo";//
        String categori="";
        private static final String TAG_DATA2 = "datos";

        @Override
        public void onBackPressed() {
            if(contador==1) {
                findViewById(R.id.ListView2).setVisibility(View.GONE);
                findViewById(R.id.ListView).setVisibility(View.VISIBLE);
                contador=0;
            }
            else {
                moveTaskToBack(true);
            }
        }

    public ListView list;
        Handler updateBarHandler = new Handler();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_departamento);
            setTitle("Lista de Categorias");
            cargarListadoCategoria();
        }

        JSONArray android = null;
        ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> oslist2 = new ArrayList<HashMap<String, String>>();

        public void cargarListadoCategoria() {

            bgt = new BackGroundTask(MAP_API_URL, "GET", null,this);
            try {
                JSONObject json = bgt.execute().get();
                android = json.getJSONArray(TAG_DATA);
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    final String cat_id= c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);
                    String api = c.getString(TAG_QTY);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME, name);
                    map.put(TAG_QTY, api);
                    map.put(TAG_ID, cat_id);

                    oslist.add(map);
                    list=(ListView)findViewById(R.id.ListView);

                    final ListAdapter adapter = new SimpleAdapter(ListaDepartamento.this, oslist,
                            R.layout.list_v,
                            new String[] {TAG_NAME, TAG_QTY }, new int[] {R.id.name, R.id.api});
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            //Toast.makeText(ListaDepartamento.this, "Se ha seleccionado "+oslist.get(+position).get("nombre"), Toast.LENGTH_SHORT).show();
                            //String articulo =oslist.get(+position).get("nombre");
                             //categori =oslist.get(+position).get("cat_id");
                            //Intent myIntent = new Intent(ListaDepartamento.this,ListaArticulo.class);
                            //myIntent.putExtra("articulo", articulo);
                            //myIntent.putExtra("cat_id", categoria);
                            //startActivity(myIntent);
                            if(contador==0) {
                               // cargaArticulos();

                                cargaArticulos();
                                launchBarDialog(view);
                                findViewById(R.id.ListView).setVisibility(View.GONE);
                                //findViewById(R.id.ListView).setVisibility(View.GONE);
                                findViewById(R.id.ListView2).setVisibility(View.VISIBLE);
                               // list.setAdapter(null);
                                contador=1;

                            }

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

        public void cargaArticulos() {
            try {
                JSONObject jsonObj1 = new JSONObject();
                jsonObj1.put("cat_id", categori);
                bgt = new BackGroundTask(MAP_API_URL2, "POST", jsonObj1,ListaDepartamento.this);
                JSONObject json = bgt.execute().get();
                android = json.getJSONArray(TAG_DATA);
                for(int i = 0; i < android.length(); i++){
                    JSONObject c = android.getJSONObject(i);
                    String name = c.getString(TAG_NAME2);
                   String api = c.getString(TAG_UNIDAD);
                    HashMap<String, String> map2 = new HashMap<String, String>();
                    if(api!=null){
                        map2.put(TAG_NAME2, name);
                        map2.put(TAG_UNIDAD, api);
                    }
                    oslist2.add(map2);
                    list=(ListView)findViewById(R.id.ListView2);
                    ListAdapter adapter = new SimpleAdapter(ListaDepartamento.this, oslist2,
                            R.layout.list_v,
                            new String[] {TAG_NAME2}, new int[] {R.id.name});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(ListaDepartamento.this, "Se ha seleccionado " + oslist2.get(+position).get(TAG_NAME2), Toast.LENGTH_SHORT).show();
                            String articulo2 =oslist2.get(+position).get(TAG_NAME2);
                            String unidad =oslist2.get(+position).get(TAG_UNIDAD);
                            Intent myIntent = new Intent(ListaDepartamento.this,FormularioArticulo.class);
                            myIntent.putExtra(TAG_NAME2, articulo2);
                            myIntent.putExtra(TAG_UNIDAD, unidad);
                            startActivity(myIntent);
                        }
                    });
                }

            } catch (JSONException e) {
                showToast(e.getMessage()); //e.printStackTrace();
            } catch (InterruptedException e) {
                showToast(e.getMessage());// e.printStackTrace();
            } catch (ExecutionException e) {
                showToast(e.getMessage());// e.printStackTrace();
            }
        }
        public void Star(){
            Intent myIntent = new Intent(ListaDepartamento.this,FormularioArticulo.class);

            startActivity(myIntent);
        }
        public void showToast(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        public void launchBarDialog(View view) {

            barProgressDialog = new ProgressDialog(ListaDepartamento.this);

            barProgressDialog.setTitle("Espere por favor");

            barProgressDialog.setMessage("Se esta cargando la lista");

            barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);

            barProgressDialog.setProgress(0);

            barProgressDialog.setMax(100);

            barProgressDialog.show();


           new Thread(new Runnable() {

               @Override
                public void run() {
                    try {//
                        //cargaArticulos();
                        while (barProgressDialog.getProgress() <= barProgressDialog.getMax()) {
                            updateBarHandler.post(new Runnable() {
                                public void run() {
                                    barProgressDialog.incrementProgressBy(100);
                                }
                            });

                            if (barProgressDialog.getProgress() == barProgressDialog.getMax()) {
                                barProgressDialog.dismiss();
                                contador=1;
                                //findViewById(R.id.ListView).setVisibility(View.GONE);
                                //findViewById(R.id.ListView2).setVisibility(View.VISIBLE);

                                break;
                            }
                        }
                    }catch (Exception e){
                    }
            }
           }).start();
        }
    }