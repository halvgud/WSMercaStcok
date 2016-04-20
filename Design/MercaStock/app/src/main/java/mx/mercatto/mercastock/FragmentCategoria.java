package mx.mercatto.mercastock;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mx.mercatto.mercastock.BGT.BGTCargarListadoCategoria;



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
        BGTCargarListadoCategoria bgt;
        bgt = new BGTCargarListadoCategoria(Configuracion.getApiUrlCategoria(),getActivity());
        try {
           bgt.execute();
        } catch (Exception e) {
            throw e;
        }
    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first

    }


   }
