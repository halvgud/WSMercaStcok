package mx.mercatto.mercastock;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentFormularioArticulo extends Fragment {

    private static final String TAG_NAME = "NombreArticulo";//
    private static final String TAG_UNIDAD ="Unidad";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View rootView = inflater.inflate(R.layout.fragment_formulario_articulo, container, false);
        Bundle args = getArguments();
        String articulo = args.getString("NombreArticulo");
        getActivity().setTitle("Lista de " + articulo);

        EditText txt1 = (EditText) rootView.findViewById(R.id.editText3);
        TextView txtV = (TextView) rootView.findViewById(R.id.textView5);
        txtV.setText(args.getString(TAG_NAME));
        TextView txtV2 = (TextView) rootView.findViewById(R.id.textView4);
        txtV2.setText("Cantidad por "+args.getString(TAG_UNIDAD)+":");


        txt1.addTextChangedListener(new TextWatcher() {
            String value = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getApplicationContext(), "Your1 message.",
                //      Toast.LENGTH_SHORT).show();
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
    //validar();

       /* txt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText text = (EditText) findViewById(R.id.editText3);
                String value = text.getText().toString();
                //Toast t=Toast.makeText(this, "Se ha guardado correctamente."+value, Toast.LENGTH_SHORT);
                //t.show();
                String gg = "";
                if (value.equals(gg)) {
                    findViewById(R.id.button3).setEnabled(false);
                } else {
                    findViewById(R.id.button3).setEnabled(true);
                }
            }
        });*/


    public void Confirmacion(View View) {

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
        dialogo1.setTitle("Aviso");
        dialogo1.setMessage("La cantidad ingresada no concuerda ¿Desea continuar?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar();

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                cancelar();
            }
        });
        dialogo1.show();
    }

    public void aceptar() {
        Toast t= Toast.makeText(getActivity(), "Se ha guardado correctamente.", Toast.LENGTH_SHORT);
        t.show();
        getActivity().finish();
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
}

