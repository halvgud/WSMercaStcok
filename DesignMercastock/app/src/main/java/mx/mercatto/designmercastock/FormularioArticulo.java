package mx.mercatto.designmercastock;
<<<<<<< HEAD
/*
=======

import android.content.Intent;
>>>>>>> origin/master
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.view.View.OnKeyListener;
import android.text.TextWatcher;
import android.text.Editable;

public class FormularioArticulo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_articulo);
        setTitle(getIntent().getExtras().getString("articulo2"));
        EditText txt1 = (EditText) findViewById(R.id.editText3);
        txt1.addTextChangedListener(new TextWatcher() {
            String value="";
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
                //Toast.makeText(getApplicationContext(), "Your 2 message.",
                    //    Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(getApplicationContext(), "Your3 toast message.",
                  //      Toast.LENGTH_SHORT).show();

            }
        });

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

    public void aceptar() {
        Toast t=Toast.makeText(this,"Se ha guardado correctamente.", Toast.LENGTH_SHORT);
        t.show();
        finish();
    }

    public void cancelar() {
        //EditText text = (EditText)findViewById(R.id.editText3);
        //String value = text.getText().toString();
        //if(value==""){
        //findViewById(R.id.button3).setEnabled(true);
    }
    public void validar() {
        EditText text = (EditText)findViewById(R.id.editText3);
        String value = text.getText().toString();
        //Toast t=Toast.makeText(this, "Se ha guardado correctamente."+value, Toast.LENGTH_SHORT);
        //t.show();
        String gg = "";
        if(value.equals(gg)){
            findViewById(R.id.button3).setEnabled(false);
        }
        else{
            findViewById(R.id.button3).setEnabled(true);
        }
    }
<<<<<<< HEAD
}
*/
=======
}
>>>>>>> origin/master
