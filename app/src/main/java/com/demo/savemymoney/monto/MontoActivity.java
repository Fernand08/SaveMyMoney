package com.demo.savemymoney.monto;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;


import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MontoActivity extends BaseActivity {

    private TextView tvMonto;
    private EditText txtMonto;
    private EditText txtFechaInicio;
    private Spinner periodo;


    private DatePickerDialog.OnDateSetListener mDateSetListener;





    private IncomeRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monto);

        MontoFragment montoFragment = new MontoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.id_montoactivity,montoFragment,null);
        fragmentTransaction.commit();





        tvMonto = (TextView)findViewById(R.id.tv_Monto);
        txtMonto = (EditText)findViewById(R.id.txt_Monto);
        txtFechaInicio =(EditText)findViewById(R.id.txt_Fecha);
        periodo = (Spinner)findViewById(R.id.spinner);

        String[] opciones = {"Mensual","Quincenal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_periodo_monto,opciones);
        periodo.setAdapter(adapter);



        txtFechaInicio =(EditText)findViewById(R.id.txt_Fecha);

        Locale locale = new Locale("ES");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config, null);
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                Date today = cal.getTime();
                SimpleDateFormat dateformart = new SimpleDateFormat();
                dateformart.setTimeZone(TimeZone.getTimeZone("GMT-5"));
                dateformart.applyPattern("dd/MM/yyyy");

                String fecha =  dateformart.format(today);

                int year  = cal.get(cal.YEAR);
                int month = cal.get(cal.MONTH);
                int day = cal.get(cal.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        MontoActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth , mDateSetListener
                        ,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(89, 89, 89)));

                dialog.setTitle("Seleccione Fecha de Ingreso del Sueldo");
                dialog.getWindow();
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mouth, int day) {
                mouth = mouth+1;
                String  fecha = day+"/"+mouth+"/"+year;
                txtFechaInicio.setText(fecha);
            }
        };



        ////////////////////////////////////////////
        repository = new IncomeRepository(this);

    }
  @Override
    protected  void  onStart() {


        super.onStart();
      String nombre = mAuth.getCurrentUser().getDisplayName();
      tvMonto.setText("Hola Bienvenido "+ nombre + ", Ingrese el monto por Periodo");

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      int calendarTime = Calendar.DAY_OF_MONTH;
      int temp = calendar.get(calendarTime);
      calendar.set(calendarTime,temp);
      SimpleDateFormat dateformart = new SimpleDateFormat();
      dateformart.setTimeZone(TimeZone.getTimeZone("GMT-5"));
      Date fecha = calendar.getTime();
      dateformart.applyPattern("dd/MM/yyyy");
      String fecha_Actual = dateformart.format(fecha);
      txtFechaInicio.setText(fecha_Actual);

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
    public void Registrar(View view) throws ParseException {

        String usuario =  mAuth.getCurrentUser().getUid();


       String selecion = periodo.getSelectedItem().toString();


        String  fechaInicio = txtFechaInicio.getText().toString();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio_Date =  sdf.parse(fechaInicio);
        String monto = txtMonto.getText().toString();
        double monto_Double = Double.parseDouble(monto);



        if(!selecion.isEmpty()  && !monto.isEmpty()  ){

            Income income = new Income();
            income.userUID = usuario;
            income.amount = monto_Double;
            income.period = selecion;
            income.startDate = fechaInicio_Date;
            income.payDate = fechaInicio_Date;


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
