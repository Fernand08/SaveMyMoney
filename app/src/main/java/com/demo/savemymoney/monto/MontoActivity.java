package com.demo.savemymoney.monto;

import android.arch.persistence.room.Room;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.data.dao.IncomeDao;
import com.demo.savemymoney.data.db.AppDatabase;
import com.demo.savemymoney.data.entity.Income;

import com.demo.savemymoney.data.repository.IncomeRepository;
import com.demo.savemymoney.login.LoginActivity;
import com.github.clemp6r.futuroid.FutureCallback;


import java.text.SimpleDateFormat;

import java.util.Date;

public class MontoActivity extends BaseActivity {

    private TextView tvMonto;
    private EditText txtMonto;
    private EditText txtFechaInicio;
    private Spinner periodo;

    private IncomeRepository repository;


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


        repository = new IncomeRepository(this);

    }
  @Override
    protected  void  onStart() {


        super.onStart();
      String nombre = mAuth.getCurrentUser().getDisplayName();
      tvMonto.setText("Hola Bienvenido "+ nombre + ", Ingrese el monto por Periodo");

/*
      String usuario = mAuth.getCurrentUser().getUid();

      Income result = dao.findByUserUID(usuario);
      String validar = result.userUID;
      if(validar != null){
          Intent i = new Intent(this,MainActivity.class);
          startActivity(i);
      }

*/


      if(!isUserSignedIn()){
            goTo(LoginActivity.class);
        } else
            Toast.makeText(this,"Hello"+mAuth.getCurrentUser().getDisplayName(),Toast.LENGTH_LONG);




    }
    public void Registrar(View view){
        //Id Usuario
        String usuario =  mAuth.getCurrentUser().getUid();

        //Periodo
       String selecion = periodo.getSelectedItem().toString();

       //Aun no ingresare una fecha, solo estoy tomando la fecha del dia de hoy
        String  fechaInicio = txtFechaInicio.getText().toString();
        // tomando la fecha del dia de hoy
        Date fecha = java.util.Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.format(fecha);

        // monto o sueldo a ingresar
        String monto = txtMonto.getText().toString();
        double monto_Double = Double.parseDouble(monto);

        Toast.makeText(this ,"LLEGUE AQUI ",Toast.LENGTH_SHORT).show();

        if(!selecion.isEmpty()  && !monto.isEmpty()  ){

            Income income = new Income();
            income.userUID = usuario;
            income.amount = monto_Double;
            income.period = selecion;
            income.startDate = fecha;
            income.payDate = fecha;
            /*
            income.setUserUID(usuario);
            income.setAmount(monto_Double);
            income.setPeriod(selecion);
            income.setStartDate(fecha);
            income.setPayDate(fecha);

             */

// mi error llegaba* aqui .

            repository.save(income)
            .addCallback(new FutureCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(MontoActivity.this ,"Se Almaceno tu informacion ",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MontoActivity.this ,"No Se Almaceno tu informacion :(",Toast.LENGTH_SHORT).show();
                }
            });





        } else {
            Toast.makeText(this ,"Debes llenar todas los campos",Toast.LENGTH_SHORT).show();

        }



     /*   Intent i = new Intent(this, MainActivity.class);

        startActivity(i); */
    }

    public void  signOut(View view){
        mAuth.signOut();
        goTo(LoginActivity.class);
    }
}
