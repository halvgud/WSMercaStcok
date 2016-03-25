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
    private static final String MAP_API_URL = "http://192.168.1.97/wsMercaStock/categoria";
    private BackGroundTask bgt;
    Spinner listaCatSpinner;


/////////////////////
public ListView list;
    public String[] sistemas = {"Ubuntu", "Android", "iOS", "Windows", "Mac OSX",
            "Google Chrome OS", "Debian", "Mandriva", "Solaris", "Unix"};
    public String[] sistemas2={"0"};
    /////////////////////
    ArrayList<listaCategoria> countryList = new ArrayList<listaCategoria>();
    ArrayList<listaCategoria> countryList2 = new ArrayList<listaCategoria>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_departamento);
        setTitle("Lista de Categorias");
        //buildCountryDropDown();
        cargarListadoCategoria();

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        list = (ListView)findViewById(R.id.mainListView);
//      ////////////////////////////////////////////////////  //ListAdapter = new ArrayAdapter<String>(ActivityName.this, R.layout.activity_lista_departamento,sistemas);
//        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sistemas);
//        list.setAdapter(adaptador);
//        list.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "Ha pulsado el item " + position, Toast.LENGTH_SHORT).show();
//
//            }
//
//        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void buildCountryDropDown() {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(MAP_API_URL, "GET", null);

        try {
            JSONObject countryJSON = bgt.execute().get();
            // Getting Array of countries
            JSONArray countries = countryJSON.getJSONArray(TAG_DATA);

            // looping through All countries
            for (int i = 0; i < countries.length(); i++) {

                JSONObject c = countries.getJSONObject(i);

                // Storing each json item in variable
                //String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME) + "--------" + c.getString("CANTIDAD");

                // add Country
                countryList.add(new listaCategoria(Integer.toString(i), name.toUpperCase()));
            }

            // bind adapter to spinner
            listaCatSpinner = (Spinner) findViewById(R.id.spinner2);
            //CategoriaAdapter cAdapter = new CategoriaAdapter(this, android.R.layout.simple_spinner_item, countryList);
            //listaCatSpinner.setAdapter(cAdapter);

            listaCatSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listaCategoria selectedCountry = countryList.get(position);
                    //showToast(selectedCountry.getName() + " was selected!");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }//----------------------------------------------->
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

                // Storing  JSON item in a Variable
                //String ver = c.getString(TAG_VER);
                String name = c.getString(TAG_NAME);
                String api = c.getString(TAG_QTY);

                // Adding value HashMap key => value

                HashMap<String, String> map = new HashMap<String, String>();

                //map.put(TAG_VER, ver);
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
