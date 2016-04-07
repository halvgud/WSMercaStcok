package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FragmentCategoria extends Fragment {
    //private static final String MAP_API_URL = "http://192.168.1.56/wsMercaStock/categoria";
    private BackGroundTask bgt;
    public FragmentCategoria() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departamento, container, false);
        getActivity().setTitle("Lista de Categorias");
        cargarListadoCategoria();
        return rootView;
    }


    public void cargarListadoCategoria() {
        bgt = new BackGroundTask(Configuracion.getApiUrlCategoria(), "GET", null,getActivity(),3);
        try {
           bgt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
