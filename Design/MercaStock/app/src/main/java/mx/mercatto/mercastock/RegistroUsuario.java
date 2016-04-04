package mx.mercatto.mercastock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RegistroUsuario extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String TAG_ID = "idSucursal";
    private static final String TAG_NAME = "nombre";
    private static final String TAG_DATA = "datos";
    private static final String MAP_API_URL = "http://192.168.1.56/wsMercaStock/sucursal";
    private static final String MAP_API_LOGIN = "http://192.168.1.56/wsMercaStock/usuario/registro";
    private String sexo="M";
    private BackGroundTask bgt;


    //private OnFragmentInteractionListener mListener;

    public RegistroUsuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroUsuario.
     */
    // TODO: Rename and change types and number of parameters
    private String[] arraySpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_formulario_articulo, container, false);
        //super.onCreateView(savedInstanceState);

        //cargarListadoSucursal(rootView);
        arraySpinner=new String[]{
                "Masculino","Femenino"
        };
        Spinner s = (Spinner) getActivity().findViewById(R.id.spinnerSexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sexo = "M";
                } else {
                    sexo = "F";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        txtusuario = (EditText) getActivity().findViewById(R.id.editText5);
        txtpassword = (EditText) getActivity().findViewById(R.id.editText6);
        txtpassword2= (EditText) getActivity().findViewById(R.id.editText7);
        txtnombre = (EditText) getActivity().findViewById(R.id.editText8);
        txtapellido = (EditText) getActivity().findViewById(R.id.editText9);
        txtsexo = (Spinner) getActivity().findViewById(R.id.spinnerSexo);
        return rootView;
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro_usuario, container, false);
    }
*/
    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/




    EditText txtusuario ;
    EditText txtpassword;
    EditText txtpassword2;
    EditText txtnombre ;
    EditText txtapellido ;
    Spinner txtsexo;
    Spinner listaSucSpinner;
    ArrayList<ListaSucursal> countryList = new ArrayList<ListaSucursal>();
    public void cargarListadoSucursal(View rootView) {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(MAP_API_URL, "GET", null,getActivity(),0);

        try {
            JSONObject countryJSON = bgt.execute().get();
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(TAG_DATA);

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String name = c.getString(TAG_NAME);

                    countryList.add(new ListaSucursal(id, name.toUpperCase()));
                }
            }else{
                getActivity().findViewById(R.id.button2).setEnabled(false);
                return;
            }

            listaSucSpinner = (Spinner) getActivity().findViewById(R.id.spinnerRegistroUsuario);
            SucursalAdapter cAdapter = new SucursalAdapter(getActivity(), android.R.layout.simple_spinner_item, countryList);
            listaSucSpinner.setAdapter(cAdapter);

            listaSucSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ListaSucursal selectedCountry = countryList.get(position);
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

    public void Registro(View view){

        String usuario = txtusuario.getText().toString().toUpperCase();
        String password = txtpassword.getText().toString();
        String password2 = txtpassword2.getText().toString();
        String nombre = txtnombre.getText().toString();
        String apellido = txtapellido.getText().toString();
        String contacto = "";
        String idsucursal = "";
        String claveapi = "";

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        SharedPreferences.Editor editor = settings.edit();
        if(usuario.length()>0&&password.length()>0&&password2.length()>0&&nombre.length()>0&&apellido.length()>0) {
            if(password.length()==4&&password2.length()==4){
                if (password.equals(password2)) {
                    try {
                        JSONObject jsonObj1 = new JSONObject();
                        jsonObj1.put("idUsuario", "7");
                        jsonObj1.put("usuario", usuario);
                        jsonObj1.put("contrasena", password);
                        jsonObj1.put("nombre", nombre);
                        jsonObj1.put("apellido", apellido);
                        jsonObj1.put("sexo", sexo);
                        jsonObj1.put("contacto", contacto);
                        jsonObj1.put("idSucursal", idsucursal);
                        jsonObj1.put("claveApi", claveapi);
                        // Create the POST object and add the parameters
                        bgt = new BackGroundTask(MAP_API_LOGIN, "POST", jsonObj1,getActivity(),0);
                        JSONObject countryJSON = bgt.execute().get();

                        switch (BackGroundTask.CodeResponse) {
                            case 200: {
                                showToast("Su registró correctamente");
                               // Intent refresh = new Intent(this, Registro.class);
                              //  this.finish();
                              //  startActivity(refresh);
                            }
                            ;
                            break;
                            case 401:
                                // showToast(("Usuario y/o password incorrectas"));
                                break;
                            default:
                                showToast(Integer.toString(BackGroundTask.CodeResponse));
                        }
                    } catch (JSONException e) {
                        showToast(e.toString());
                    } catch (Exception e) {
                        // showToast(e.toString());
                    }
                } else {
                    showToast("Las contraseñas no coinciden");
                }
            }
            else {
                showToast("Las contraseñas deben contener 4 digitos");
            }
        }
        else {
            showToast("Faltan datos en algún campo");
        }
    }
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
    public void Regresar(View view){
     //   Intent intent = new Intent(this, Login.class);
    //    this.startActivity(intent);
    }
}
