package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentCategoria extends Fragment {
    Activity activity;
    private BackGroundTask bgt;

    public FragmentCategoria() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_departamento, container, false);
        getActivity().setTitle("Lista de Categorias");
        //revisarApi();
        cargarListadoCategoria();

        return rootView;
    }

    public void cargarListadoCategoria() {
        bgt = new BackGroundTask(Configuracion.getApiUrlCategoria(), "GET", null,getActivity(),3);
        Log.d("fef", Configuracion.getApiUrlCategoria());
        try {
           bgt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void revisarApi() {

        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("claveApi", Configuracion.settings.getString("ClaveApi",""));
           // bgt = new BackGroundTask("http://192.168.1.17/wsMercaStock/usuario/api", "POST", jsonObj1 ,getActivity(),11);
           // bgt.execute();

            /*FragmentSesion fragment2 = new FragmentSesion();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, fragment2);
            fragmentTransaction.commit();*/
        } catch (Exception e){
                showToast(e.toString());
        }
        //if(BackGroundTask.ClaveApi.equals("")){

       // }

    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    /*public void onBackPressed() {
        getActivity().moveTaskToBack(true);
        getActivity().finish();
    }*/
    //@Override
    public void onBackPressed() {
        FragmentCategoria fragment = new FragmentCategoria();
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main,fragment).hide(this).commit();
    }
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        revisarApi();
        // Get the Camera instance as the activity achieves full user focus
        //FragmentLogin.ClaveApi=0;
    }

   }
