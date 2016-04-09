package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentArticulo extends Fragment {

    private BackGroundTask bgt;
    String cat_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_articulo, container, false);
        Bundle args = getArguments();
        String articulo = args.getString("articulo");
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
