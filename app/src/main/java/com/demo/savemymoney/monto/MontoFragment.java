package com.demo.savemymoney.monto;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.demo.savemymoney.R;
import com.demo.savemymoney.common.BaseFragment;
import com.demo.savemymoney.common.dto.ErrorMessage;
import com.demo.savemymoney.data.entity.Income;
import com.demo.savemymoney.databinding.MontoFragmentBinding;
import com.demo.savemymoney.main.MainActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.kolotnev.formattedittext.CurrencyEditText;


public class MontoFragment extends BaseFragment implements MontoFragmentPresenter.View {

    @BindView(R.id.monto_textInputLayout)
    TextInputLayout montoTil;
    @BindView(R.id.txt_Monto)
    CurrencyEditText txtMonto;
    @BindView(R.id.txt_Fecha)
    EditText txtFechaInicio;

    @BindView(R.id.monto_mensual_radio)
    RadioButton mensualRadio;

    @BindView(R.id.monto_quincenal_radio)
    RadioButton QuincenalRadio;

    @BindView(R.id.monto_frequency_radiogroup)
    RadioGroup frequencyGroup;


    private DatePickerDialog.OnDateSetListener mDateSetListener;

    MontoFragmentPresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static MontoFragment newInstance() {
        return new MontoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MontoFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.monto_fragment, container, false);
        presenter = new MontoFragmentPresenter(this, getContext());
        binding.setPresenter(presenter);

        View view = binding.getRoot();

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initUI();
        presenter.loadIncome();
        getActivity().setTitle(R.string.income_update_title);
    }

    private void initUI() {

        frequencyGroup.check(R.id.monto_mensual_radio);

        txtFechaInicio.setOnClickListener(view12 -> {

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            int year = cal.get(cal.YEAR);
            int month = cal.get(cal.MONTH);
            int day = cal.get(cal.DAY_OF_MONTH);
            new DatePickerDialog(
                    getContext(), mDateSetListener
                    , year, month, day).show();
        });

        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String fecha = day + "/" + month + "/" + year;
            txtFechaInicio.setText(fecha);
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int calendarTime = Calendar.DAY_OF_MONTH;
        int temp = calendar.get(calendarTime);
        calendar.set(calendarTime, temp);

        Date fecha = calendar.getTime();
        String fecha_Actual = dateFormat.format(fecha);
        txtFechaInicio.setText(fecha_Actual);
    }


    @Override
    public void showErrorMessages(List<ErrorMessage> errors) {

        for (ErrorMessage error : errors) {
            if (error.getInputId() == null)
                showError(error.getMessage());
            else {
                TextInputLayout input = getActivity().findViewById(error.getInputId());
                input.setErrorEnabled(true);
                input.setError(error.getMessage());
            }
        }
    }

    @Override
    public void clearErrorMessages() {
        montoTil.setErrorEnabled(false);
        montoTil.setError("");
    }

    @Override
    public void showProgress() {
        showProgressDialog(R.string.income_loading);
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public Income getIncome() {
        BigDecimal monto = txtMonto.getValue();
        String usuario = mAuth.getCurrentUser().getUid();
        String selecion = frequencyGroup.getCheckedRadioButtonId() == mensualRadio.getId() ? "MENSUAL" : "QUINCENAL";
        String fechaInicio = txtFechaInicio.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio_Date = null;
        try {
            fechaInicio_Date = sdf.parse(fechaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Income income = new Income();
        income.userUID = usuario;

        income.period = selecion;
        income.startDate = fechaInicio_Date;
        income.payDate = fechaInicio_Date;


        //Fecha Actual
        Calendar calendarioActual= Calendar.getInstance();

        Date fechaActual = new Date();
       String fechaActual_String = sdf.format(fechaActual);
        Date   fecha_Actual_Parse =null;
        try {
             fecha_Actual_Parse = sdf.parse(fechaActual_String);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarioActual.setTime(fecha_Actual_Parse);

        //Fecha Seleccionada
        Calendar calendarSeleccionado = Calendar.getInstance();
        calendarSeleccionado.setTime(fechaInicio_Date);




        // Validar Registro de Sueldo Por Fecha


        int diasAtrasAcumulado = -1;
        int diasDespuesAcumulado= -1;
        int diasfaltantes = 0;
        int paydate = 0 ;
        int cantPeriodo = 0;
        int contador = 1;
        int maxContador = 0 ;


        //Asignar valores por Periodo seleccionado
        if ("MENSUAL".equals(selecion)){
            cantPeriodo = 30;
        }else{
            cantPeriodo = 15;
        }





        if(calendarSeleccionado != null && calendarioActual != null){
            if(calendarSeleccionado.before(calendarioActual)){// Cuando se selecciona fecha Antes de Fecha actual

                // Sueldo acumulado si selecciona dias antes de la fecha actual
                double sueldoAcumulado = monto.doubleValue();

                // Contador de dias de fechaSeleccionada y FechaActual
                while (calendarSeleccionado.before(calendarioActual)|| calendarSeleccionado.equals(calendarioActual)){


                    diasAtrasAcumulado++;
                    calendarSeleccionado.add(Calendar.DATE,1);
                }

                maxContador = diasAtrasAcumulado / cantPeriodo;

                sueldoAcumulado = sueldoAcumulado * maxContador;
                income.amount = sueldoAcumulado;

                //Settear la fecha de pago

                //dias que sobran desde el ultimo dia de pago
                diasfaltantes = diasAtrasAcumulado % cantPeriodo;
                //dias que falta para obtener tu sgte pago.
                paydate = cantPeriodo -  diasfaltantes;


                // PayDate , fechaactual mas el numero de dias.
                Calendar calendarioPagoAntes= Calendar.getInstance();
                calendarioPagoAntes.add(Calendar.DAY_OF_MONTH,paydate);
                Date diaPago = calendarioPagoAntes.getTime();


                String diaPago_String = sdf.format(diaPago);
                Date diaPago_parse = null ;
                try {
                    diaPago_parse = sdf.parse(diaPago_String);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                income.payDate = diaPago_parse;




            }
            else  if (calendarSeleccionado.after(calendarioActual)){ // Cuando se selecciona fecha Despues de Fecha actual

                // Contador de dias de fechaSeleccionada y FechaActual
                while (calendarioActual.before(calendarSeleccionado)|| calendarioActual.equals(calendarSeleccionado)){


                    diasDespuesAcumulado++;
                    calendarioActual.add(Calendar.DATE,1);
                }

                income.amount = null;

                // PayDate , fechaactual mas el numero de dias.
                Calendar calendarioPagoDespues= Calendar.getInstance();
                calendarioPagoDespues.add(Calendar.DAY_OF_MONTH,diasDespuesAcumulado);
                Date diaPago = calendarioPagoDespues.getTime();


                String diaPago_String = sdf.format(diaPago);
                Date diaPago_parse = null ;
                try {
                    diaPago_parse = sdf.parse(diaPago_String);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                income.payDate = diaPago_parse;

            }
            else if (calendarSeleccionado.equals(calendarioActual)){ //Cuando se Selecciona la fecha Actual

                income.amount = monto.doubleValue();

            }

        }
        return income;
    }

    @Override
    public void notifyIncomeSaved() {
        SweetAlertDialog alert = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.success_title));
        if (getActivity().getClass().equals(MontoActivity.class)) {
            alert.setContentText(getString(R.string.income_succes_save))
                    .setConfirmText(getString(R.string.income_confirm_save))
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().startActivity(intent);
                        getActivity().finish();
                    });
        } else {
            alert.setContentText(getString(R.string.income_succes_save_main))
                    .setConfirmClickListener(sDialog -> getActivity().onBackPressed());
        }
        alert.setCancelable(false);
        alert.show();
    }

    @Override
    public void loadValues(Income result) {
        txtMonto.setValue(BigDecimal.valueOf(result.getAmount()));
        txtFechaInicio.setText(dateFormat.format(result.payDate));
        if ("MENSUAL".equals(result.period))
            frequencyGroup.check(R.id.monto_mensual_radio);
        else
            frequencyGroup.check(R.id.monto_quincenal_radio);

    }

}
