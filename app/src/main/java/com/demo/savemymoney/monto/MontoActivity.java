package com.demo.savemymoney.monto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.login.LoginActivity;
import com.demo.savemymoney.main.MainActivity;

public class MontoActivity extends BaseActivity {

    private TextView tvMonto;
    private EditText txtMonto;
    private EditText txtFechaInicio;
    private Spinner periodo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monto);

        tvMonto = (TextView)findViewById(R.id.tv_Monto);
        txtMonto = (EditText)findViewById(R.id.txt_Monto);
        txtFechaInicio =(EditText)findViewById(R.id.txt_Fecha);
        periodo = (Spinner)findViewById(R.id.spinner);

        String[] opciones = {"Mensual","Quincenal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_periodo_monto,opciones);
        periodo.setAdapter(adapter);

        String nombre = mAuth.getCurrentUser().getDisplayName();
        tvMonto.setText("Hola Bienvenido "+ nombre + ", Ingrese el monto por Periodo");
    }
    @Override
    protected  void  onStart() {

        super.onStart();
        if(!isUserSignedIn()){
            goTo(LoginActivity.class);
        } else
            Toast.makeText(this,"Hello"+mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_LONG);

    }
    public void Ingresar(View view){
     /*   String selecion = periodo.getSelectedItem().toString();
        String fechaInicio = txtFechaInicio.getText().toString();
        String monto = txtMonto.getText().toString();
        int monto_Int = Integer.parseInt(monto); */

        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    public void  signOut(View view){
        mAuth.signOut();
        goTo(LoginActivity.class);
    }
}
