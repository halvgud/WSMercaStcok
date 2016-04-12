package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Juan Carlos De León on 10/04/2016.
 */
public class FragmentSucursal extends Fragment {

    private BackGroundTask bgt;
    String sucursal_Seleccionada;

    public FragmentSucursal() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sucursal, container, false);
        getActivity().setTitle("Seleccionar Sucursal");
        cargarListadoSucursal(rootView);
        return rootView;
    }

    protected FragmentActivity mActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity)activity;
    }

    Spinner listaSucSpinner;
    ArrayList<ListaSucursal> countryList = new ArrayList<ListaSucursal>();

    public void cargarListadoSucursal(View rootView) {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,getActivity(),0);

        try {
            JSONObject countryJSON = bgt.execute().get();
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(Configuracion.getDatos());

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);

                    String id = c.getString(Configuracion.getIdRegistro());
                    String name = c.getString(Configuracion.getDescripcionRegistro());

                    countryList.add(new ListaSucursal(id, name.toUpperCase()));
                }
            }else{
                return;
            }

            listaSucSpinner = (Spinner) rootView.findViewById(R.id.spinner);
            SucursalAdapter cAdapter = new SucursalAdapter(mActivity, android.R.layout.simple_spinner_item, countryList);
            listaSucSpinner.setAdapter(cAdapter);

            listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ListaSucursal selectedCountry = countryList.get(position);
                    sucursal_Seleccionada=selectedCountry.toString();
                    TextView txtSucursal = (TextView) getView().findViewById(R.id.textView18);
                    txtSucursal.setText(sucursal_Seleccionada);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}