package mx.mercatto.designmercastock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

public class FormularioArticulo extends AppCompatActivity {

    private static final String TAG_NAME = "NombreArticulo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_articulo);
        setTitle("Artículo");

        EditText txt1 = (EditText) findViewById(R.id.editText3);
        TextView txtV = (TextView) findViewById(R.id.textView5);
        txtV.setText(getIntent().getExtras().getString("articulo2"));
        TextView txtV2 = (TextView) findViewById(R.id.textView4);
        txtV2.setText("Cantidad por "+getIntent().getExtras().getString("unidad2")+":");
        txt1.addTextChangedListener(new TextWatcher() {
            String value = "";
            String gg = "";

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(getApplicationContext(), "Your1 message.",
                //      Toast.LENGTH_SHORT).show();
                EditText text = (EditText) findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    findViewById(R.id.button3).setEnabled(false);
                } else {
                    findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                EditText text = (EditText) findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    findViewById(R.id.button3).setEnabled(false);
                } else {
                    findViewById(R.id.button3).setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                EditText text = (EditText) findViewById(R.id.editText3);
                String value = text.getText().toString();
                String gg = "";
                if (value.equals(gg)) {
                    findViewById(R.id.button3).setEnabled(false);
                } else {
                    findViewById(R.id.button3).setEnabled(true);
                }
            }
        });

    }
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void Confirmacion(View View) {
        String txtexistencia=getIntent().getExtras().getString("exitencia2");
        EditText valor;
        valor = (EditText) findViewById(R.id.editText3);
        showToast(Double.parseDouble(getIntent().getExtras().getString("existencia2"))+" "+Double.parseDouble(valor.getText().toString()));
        if (Double.parseDouble(getIntent().getExtras().getString("existencia2"))==(Double.parseDouble(valor.getText().toString()))) {
showToast(":)");
        }
        else {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
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
    }

    public void aceptar() {
        Toast t=Toast.makeText(this,"Se ha guardado correctamente.", Toast.LENGTH_SHORT);
        t.show();
        finish();
    }

    public void cancelar() {

    }
}

