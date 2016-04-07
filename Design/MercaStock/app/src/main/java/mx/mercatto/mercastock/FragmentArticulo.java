package mx.mercatto.mercastock;

import android.graphics.Bitmap;
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
    String cat_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_articulo, container, false);
        Bundle args = getArguments();
        String articulo = args.getString("articulo");
       // String art_id = args.getString("art_id");
        cat_id = args.getString(Configuracion.getIdCategoria());
        getActivity().setTitle("Lista de " + articulo);

        cargarListadoCategoria(rootView);
        return rootView;
    }
    public void cargarListadoCategoria(View rootView) {
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Configuracion.getIdCategoria(), cat_id);
            bgt = new BackGroundTask(Configuracion.getApiUrlArticulo(), "POST", jsonObj1,getActivity(),4);
            bgt.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
