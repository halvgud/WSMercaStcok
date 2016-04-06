package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentArticulo extends Fragment {

    private static final String TAG_ID = "cat_id";//
    private static final String TAG_NAME = "NombreArticulo";//
    private static final String TAG_DATA = "datos";//Falta
    private static final String TAG_QTY = "CANTIDAD";//Falta
    private static final String MAP_API_URL = "http://192.168.1.56/wsMercaStock/articulo";
    private BackGroundTask bgt;
    public ListView list;
    String categori="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        String articulo = args.getString("articulo");
        String art_id = args.getString("art_id");
        String cat_id = args.getString("cat_id");
        getActivity().setTitle("Lista de " + articulo);
        categori=cat_id;

        View rootView = inflater.inflate(R.layout.fragment_lista_articulo, container, false);
        cargarListadoCategoria(rootView);
        return rootView;
    }
///////////////////Temporal Prueba///////////////////////////////

    JSONArray android = null;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    public void cargarListadoCategoria(View rootView) {
        try {
        // Building post parameters, key and value pair
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("cat_id", categori);
            bgt = new BackGroundTask(MAP_API_URL, "POST", jsonObj1,getActivity(),4);
            bgt.execute();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
    /////////////////////////////////////////////////
}
