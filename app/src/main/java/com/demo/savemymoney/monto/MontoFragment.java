package com.demo.savemymoney.monto;



import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseActivity;
import com.demo.savemymoney.common.BaseFragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MontoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MontoFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private  MontoActivity montoActivity;


    private TextView tvMonto;
    private EditText txtMonto;
    private EditText txtFechaInicio;
    private Spinner periodo;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private IncomeRepository repository;



    public MontoFragment() {
        // Required empty public constructor
    }






    // TODO: Rename and change types and number of parameters
    public static MontoFragment newInstance(String param1, String param2) {
        MontoFragment fragment = new MontoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        tvMonto = (TextView)getView().findViewById(R.id.tv_Monto);
        txtMonto = (EditText)getView().findViewById(R.id.txt_Monto);
        txtFechaInicio =(EditText)getView().findViewById(R.id.txt_Fecha);
        periodo = (Spinner)getView().findViewById(R.id.spinner);

        String[] opciones = {"Mensual","Quincenal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item_periodo_monto,opciones);
        periodo.setAdapter(adapter);



        Locale locale = new Locale("ES");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        montoActivity.getApplicationContext().getResources().updateConfiguration(config, null);
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
                        getContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth , mDateSetListener
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

        repository = new IncomeRepository(getContext());

    }



    @SuppressLint("MissingSuperCall")
    @Override
    public   void  onStart() {


        super.onStart();
        String nombre = mAuth.getCurrentUser().getDisplayName();
        tvMonto.setText("Hola Bienvenido " + nombre + ", Ingrese el monto por Periodo");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int calendarTime = Calendar.DAY_OF_MONTH;
        int temp = calendar.get(calendarTime);
        calendar.set(calendarTime, temp);
        SimpleDateFormat dateformart = new SimpleDateFormat();
        dateformart.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        Date fecha = calendar.getTime();
        dateformart.applyPattern("dd/MM/yyyy");
        String fecha_Actual = dateformart.format(fecha);
        txtFechaInicio.setText(fecha_Actual);


        if (!isUserSignedIn()) {
         goTo(LoginActivity.class);
        } else
            Toast.makeText(getContext(), "Hello" +mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG);


    }







    public void  singOut(View view){
        montoActivity.signOut(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monto, container, false);
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
                            Toast.makeText(getContext() ,"Se Almaceno tu informacion ",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getContext() ,"No Se Almaceno tu informacion :(",Toast.LENGTH_SHORT).show();
                        }
                    });





        } else {
            Toast.makeText(getContext() ,"Debes llenar todas los campos",Toast.LENGTH_SHORT).show();

        }



     /*   Intent i = new Intent(this, MainActivity.class);

        startActivity(i); */
    }

    public void  signOut(View view){
       mAuth.signOut();
    goTo(LoginActivity.class);
    }




}
