package mx.mercatto.mercastock;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class FragmentFormularioArticulo extends Fragment  implements View.OnClickListener{

  //  private static final String TAG_UNIDAD ="Unidad";
  //  private static final String TAG_ID_CATEGORIA="cat_id";
 //   private static String TAG_NOMBRE_ARTICULO="NombreArticulo";
 //   private static String TAG_ID_ARTICULO="art_id";
    private static String NombreArticulo="";
    private static String cat_id="";
    private static String art_id="";
    private BackGroundTask bgt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_formulario_articulo, container, false);
        Bundle args = getArguments();
        NombreArticulo = args.getString(Configuracion.getDescripcioArticulo());
        TAG_VALOR_INVENTARIO = args.getString(Configuracion.getIdInventario());
        cat_id = args.getString(Configuracion.getIdCategoria());
        art_id = args.getString(Configuracion.getIdArticulo());
        getActivity().setTitle("Lista de " + NombreArticulo);

        EditText txt1 = (EditText) rootView.findViewById(R.id.editText3);
        TextView txtV = (TextView) rootView.findViewById(R.id.textView5);
        txtV.setText(args.getString(NombreArticulo));
        TextView txtV2 = (TextView) rootView.findViewById(R.id.textView4);
        txtV2.setText("Cantidad por "+args.getString(Configuracion.getUnidadArticulo())+":");
        Button upButton = (Button) rootView.findViewById(R.id.button3);
        upButton.setOnClickListener(this);

        txt1.addTextChangedListener(new TextWatcher() {
            String value = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                //Toast.makeText(getApplicationContext(), "Your 2 message.",
                //    Toast.LENGTH_SHORT).show();
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                EditText text = (EditText) rootView.findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    rootView.findViewById(R.id.button3).setEnabled(false);
                } else {
                    rootView.findViewById(R.id.button3).setEnabled(true);
                }
                //Toast.makeText(getApplicationContext(), "Your3 toast message.",
                //      Toast.LENGTH_SHORT).show();

            }
        });


        return rootView;
    }
    @Override
    public void onClick(View v) {
        String txtexistencia=TAG_VALOR_INVENTARIO;
        final EditText valor;
        valor = (EditText) v.findViewById(R.id.editText3);
        //showToast(Double.parseDouble(getIntent().getExtras().getString("existencia2"))+" "+Double.parseDouble(valor.getText().toString()));
        if (Double.parseDouble(TAG_VALOR_INVENTARIO)==(Double.parseDouble(valor.getText().toString()))) {
            aceptar(valor.getText().toString());
            //showToast(":)");
        }
        else {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
            dialogo1.setTitle("Aviso");
            dialogo1.setMessage("La cantidad ingresada no concuerda ¿Desea continuar?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptar(valor.getText().toString());

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    cancelar();
                }
            });
            dialogo1.show();
        }
    }

    private static  String TAG_VALOR_INVENTARIO;
    public void aceptar(String valor) {
        try{
            //showToast(TAG_VALOR_ID_INVENTARIO);
            JSONObject jsobj = new JSONObject();
            jsobj.put("idInventario",TAG_VALOR_INVENTARIO);
            jsobj.put("existenciaRespuesta",valor);
            jsobj.put("art_id",Configuracion.getIdArticulo());
            bgt = new BackGroundTask(Configuracion.getApiUrlInventario(), "POST",jsobj,getActivity(),6 );
            bgt.execute();
            Toast t=Toast.makeText(getActivity(),"Se ha guardado correctamente.", Toast.LENGTH_SHORT);
            t.show();
        }catch(JSONException e){
            showToast(e.getMessage());
        }
        finally {
            getActivity().getFragmentManager().popBackStack();
        }
    }

    public void cancelar() {
        //EditText text = (EditText)findViewById(R.id.editText3);
        //String value = text.getText().toString();
        //if(value==""){
        //findViewById(R.id.button3).setEnabled(true);
    }
    public void validar(View rootView) {
        EditText text = (EditText) rootView.findViewById(R.id.editText3);
        String value = text.getText().toString();
        //Toast t=Toast.makeText(this, "Se ha guardado correctamente."+value, Toast.LENGTH_SHORT);
        //t.show();
        String gg = "";
        if(value.equals(gg)){
            rootView.findViewById(R.id.button3).setEnabled(false);
        }
        else{
            rootView.findViewById(R.id.button3).setEnabled(true);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}

