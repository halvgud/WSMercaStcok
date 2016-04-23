package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import mx.mercatto.mercastock.BGT.BGTCargarListadoCategoria;
import mx.mercatto.mercastock.BGT.BGTLogIn;


public class FragmentCategoria extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departamento, container, false);
        getActivity().setTitle("Lista de Categorias");
        cargarListadoCategoria();

        //Main.setContentView(R.layout.activity_main_logged);
        return rootView;
    }

    public void cargarListadoCategoria() {
        try {
        BGTCargarListadoCategoria bgt;
        JSONObject jsonObj1 = new JSONObject();
        jsonObj1.put("claveApi", Configuracion.settings.getString("ClaveApi",""));
        // Create the POST object and add the parameters
        bgt = new BGTCargarListadoCategoria(Configuracion.getApiUrlCategoria(),getActivity(),jsonObj1);

           bgt.execute();
        } catch (JSONException e) {
           // throw e;
        }
    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first

    }


   }
