package mx.mercatto.mercastock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FragmentLogin extends Fragment implements View.OnClickListener {
    public static final String ARG_ARTICLES_NUMBER = "articles_number";

    Activity activity;
    public static String ClaveApi = "";
    private BackGroundTask bgt;

    private BackGroundTask bgt2;
    EditText txtusuario;
    EditText txtpassword;
    TextView listaSucSpinner;
    ArrayList<ListaSucursal> countryList = new ArrayList<ListaSucursal>();
    String id_sucursal;
    public FragmentLogin() {
        // Constructor vacío
    }


    public void changeContent(Document doc, String newname, String newemail) {
        Element root = doc.getDocumentElement();
        NodeList rootlist = root.getChildNodes();
        for (int i = 0; i < rootlist.getLength(); i++) {
            Element person = (Element) rootlist.item(i);
            NodeList personlist = person.getChildNodes();
            Element name = (Element) personlist.item(0);
            NodeList namelist = name.getChildNodes();
            Text nametext = (Text) namelist.item(0);
            String oldname = nametext.getData();
            if (oldname.equals(newname)) {
                Element email = (Element) personlist.item(1);
                NodeList emaillist = email.getChildNodes();
                Text emailtext = (Text) emaillist.item(0);
                emailtext.setData(newemail);
            }
        }
    }

    int x = 1;

    DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article, container, false);
        Configuracion.Inicializar(getActivity());
        getActivity().setTitle("MercaStock");
        Button upButton = (Button) rootView.findViewById(R.id.button2);
        Button upButton2 = (Button) rootView.findViewById(R.id.button);
        upButton.setOnClickListener(this);
        upButton2.setOnClickListener(this);

        //mDrawerLayout = (DrawerLayout) getView().findViewById(R.id.nav_view);
//mDrawerLayout.removeViewAt(0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String auth_token_string = settings.getString("ClaveApi", ""/*default value*/);
        if (auth_token_string != "") {
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        } else {
            txtusuario = (EditText) rootView.findViewById(R.id.editText);
            txtpassword = (EditText) rootView.findViewById(R.id.editText2);
        }
       //spinnerSucursal2();
       // cargarListadoSucursal(rootView);
        cargarListadoSucursal(rootView);

        return rootView;
    }

    public static int contador;
    public static String variable_Usuario_Inicial;
    public static String variable_Usuario_Final;


    public void buttonClicked(View view) {
        if (view.getId() == R.id.button) {
            // button1 action
        } else if (view.getId() == R.id.button2) {
            //button2 action
        } else if (view.getId() == R.id.button3) {
            //button3 action
        }
    }

    @Override
    public void onClick(View v) {
        //______________________________________________   Main.CambiarEstadoSucursal(false);
        // ______________________________________________Main.b=false;
        //--____________________________________________getActivity().invalidateOptionsMenu();
        //______________________________________________Log.d("kkk",getActivity().getTitle().toString());

        String usuario = txtusuario.getText().toString();
        String password = txtpassword.getText().toString();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        //String auth_token_string = settings.getString("ClaveApi", ""*//*default value*//*);

        SharedPreferences.Editor editor = settings.edit();
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("usuario", usuario);
            jsonObj1.put("contrasena", password);
            // Create the POST object and add the parameters
            bgt = new BackGroundTask(Configuracion.getApiUrlLogIn(), "POST", jsonObj1,getActivity(),1);
            bgt.execute();
            //Main.CambiarEstadoSucursal(false);


        } catch(JSONException e){
            showToast(e.toString());
        }catch (Exception e){
            showToast(e.toString());
        }

    /*    FragmentCategoria fragment = new FragmentCategoria();
        FragmentManager fragmentManager = this.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();*/
    }

     public void onBackPressed()  {
        getActivity().finish();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //getActivity().finish();
                //FragmentCategoria fragment = new FragmentCategoria();
                //FragmentManager fragmentManager = activity.getFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.content_main,fragment).addToBackStack(null).commit();
            }
        }.start();
        //NavigationView navigationView = (NavigationView) getView().findViewById(R.id.nav_view);
//navigationView.removeViewAt(0);
    }

    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void cargarListadoSucursal(View rootView) {
        try {
            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put(Configuracion.getApiUrlSucursal(), id_sucursal);
            bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,getActivity(),2);
            bgt.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void cargarListadoSucursal2() {
        List<NameValuePair> apiParams = new ArrayList<NameValuePair>(1);
        apiParams.add(new BasicNameValuePair("call", "countrylist"));

        bgt = new BackGroundTask(Configuracion.getApiUrlSucursal(), "GET", null,getActivity(),2);

        try {
            JSONObject countryJSON = bgt.execute().get();
            if(countryJSON!= null){
                JSONArray countries = countryJSON.getJSONArray(Configuracion.getDatos());

                for (int i = 0; i < countries.length(); i++) {

                    JSONObject c = countries.getJSONObject(i);

                    String id = c.getString(Configuracion.getIdSucursal());
                    String name = c.getString(Configuracion.getDescripcionSucursal());

                    countryList.add(new ListaSucursal(id, name.toUpperCase()));
                }
                TextView txtSucursal = (TextView) getView().findViewById(R.id.textView13);
                txtSucursal.setText(countryList.get(0).toString());

            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
