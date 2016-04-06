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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class FormularioArticulo extends AppCompatActivity {
   // private static String TAG_ID_INVENTARIO ="idInventario";
    private static  String TAG_VALOR_INVENTARIO;
    //private static final String MAP_API_URL_FORMULARIOARTICULO = "http://192.168.1.17/wsMercaStock/articulo/actualizar";
    private BackGroundTask bgt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_articulo);
        setTitle("Artículo");
        TAG_VALOR_INVENTARIO = getIntent().getExtras().getString(Configuracion.getIdInventario());
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
        final EditText valor;
        valor = (EditText) findViewById(R.id.editText3);
        //showToast(Double.parseDouble(getIntent().getExtras().getString("existencia2"))+" "+Double.parseDouble(valor.getText().toString()));
        if (Double.parseDouble(getIntent().getExtras().getString("existencia2"))==(Double.parseDouble(valor.getText().toString()))) {
        aceptar(valor.getText().toString());
            //showToast(":)");
        }
        else {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
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

    public void aceptar(String valor) {
        try{
            //showToast(TAG_VALOR_ID_INVENTARIO);
        JSONObject jsobj = new JSONObject();
        jsobj.put("idInventario",TAG_VALOR_INVENTARIO);
        jsobj.put("existenciaRespuesta",valor);
            jsobj.put("art_id",getIntent().getExtras().getString("art_id"));
        bgt = new BackGroundTask(Configuracion.getApiUrlInventario(), "POST",jsobj );
            JSONObject json = bgt.execute().get();
        Toast t=Toast.makeText(this,"Se ha guardado correctamente.", Toast.LENGTH_SHORT);
        t.show();
        }catch(JSONException e){
            showToast(e.getMessage());
        }catch (InterruptedException e){}
        catch(ExecutionException e){}
        finally {
            finish();
        }
    }

    public void cancelar() {

    }
}

