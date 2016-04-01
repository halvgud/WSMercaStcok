package mx.mercatto.designmercastock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Juan Carlos De León on 31/03/2016.
 */
public class Login2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        setTitle("Registro");
    }
    public void abrirRegistro(View view){
        Intent intent = new Intent(this, Registro.class);
        this.startActivity(intent);
    }
}
