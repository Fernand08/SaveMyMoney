package com.demo.savemymoney.monto;

import android.arch.persistence.room.Room;

import android.content.Intent;

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

import com.demo.savemymoney.login.LoginActivity;
import com.demo.savemymoney.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MontoActivity extends BaseActivity {

    private TextView tvMonto;
    private EditText txtMonto;
    private EditText txtFechaInicio;
    private Spinner periodo;

    private IncomeDao dao ;
    private AppDatabase db;


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


        db = Room.inMemoryDatabaseBuilder(this, AppDatabase.class).build();
        dao = db.incomeDao();

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
        String usuario =  mAuth.getCurrentUser().getUid();
       String selecion = periodo.getSelectedItem().toString();
        String  fechaInicio = txtFechaInicio.getText().toString();
        Date fechaInicio_date=new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicio,null);

        String monto = txtMonto.getText().toString();
        double monto_Double = Double.parseDouble(monto);
     /*

        Income result = dao.findByUserUID(usuario);
        String validar = result.userUID;
*/


        if(!selecion.isEmpty() && !fechaInicio.isEmpty() && !monto.isEmpty()  ){
            Income income = new Income();
    /*      income.setUserUID(usuario);
            income.setAmount(monto_Double);
            income.setPeriod(selecion);
            income.setStartDate(fechaInicio_date);
            */
            income.userUID = usuario ;
            income.amount = monto_Double;
            income.period = selecion;
            income.startDate = fechaInicio_date;

            dao.saveIncome(income);

            Toast.makeText(this ,"Se Almaceno tu informacion ",Toast.LENGTH_SHORT).show();

            db.close();


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
