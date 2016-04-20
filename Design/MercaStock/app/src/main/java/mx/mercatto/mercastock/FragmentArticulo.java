package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import mx.mercatto.mercastock.BGT.BGTCargarListadoArticulo;

public class FragmentArticulo extends Fragment {

    String cat_id="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_articulo, container, false);
        Bundle args = getArguments();
        String articulo = args.getString("articulo");
        cat_id = args.getString(Configuracion.getIdCategoria());
        getActivity().setTitle("Lista de " + articulo);

        cargarListadoArticulo();
        return rootView;
    }
    public void cargarListadoArticulo() {
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Configuracion.getIdCategoria(), cat_id);
            BGTCargarListadoArticulo bgt = new BGTCargarListadoArticulo(Configuracion.getApiUrlArticulo(), getActivity(), jsonObj1);
            bgt.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
