package com.example.snakechild.ciadobusao;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.parse.ParseObject;

import android.text.format.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NovoEncontroActivity extends FragmentActivity {

    private static TextView nomeText, linhaText, pontoRefText, editDataText, editHoraText, editLocalMapaText, criarText, cancelarText, dataText, horaText;
    private static EditText nomeEncEditText, linhaEncEditTex, refEncEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novoencontro);

        nomeText = (TextView) this.findViewById(R.id.idNomeEnc);
        linhaText = (TextView) this.findViewById(R.id.idLinhaOnibusText);
        pontoRefText = (TextView) this.findViewById(R.id.idPontoRefText);
        editDataText = (TextView) this.findViewById(R.id.idEditarDataTextView);
        editHoraText = (TextView) this.findViewById(R.id.idEditarHoraTextView);
        editLocalMapaText = (TextView) this.findViewById(R.id.idEditarMapaTextView);
        criarText = (TextView) this.findViewById(R.id.idCriarTextView);
        cancelarText = (TextView) this.findViewById(R.id.idCancelarTextView);
        dataText = (TextView) this.findViewById(R.id.idDateTextView);
        horaText = (TextView) this.findViewById(R.id.idHoraTextView);

        nomeEncEditText = (EditText) this.findViewById(R.id.idNomeEdit);
        linhaEncEditTex = (EditText) this.findViewById(R.id.idLinhaEdit);
        refEncEditText = (EditText) this.findViewById(R.id.idPontoEdit);

        customizeItems();
    }

    public void customizeItems(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", nomeText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", linhaText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", pontoRefText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editDataText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editHoraText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", editLocalMapaText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", criarText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", cancelarText);
        CustomizeFont.customizeFont(this, "Amaranth-Regular.otf", dataText);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBack(View v){
        onBackPressed();
        finish();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private String data;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "dd/MM/yy"; //In which format you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dataText.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void onDatePressed(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager() ,"datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.HOUR, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);

            String myFormat = "HH:mm"; //In which format you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            horaText.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void onTimePressed(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void saveEncontro(View v){
        ParseObject encontro = new ParseObject("Encontro");

        encontro.put("data", dataText.getText().toString());
        encontro.put("horario", horaText.getText().toString());
        encontro.put("idDono", PerfilActivity.idUsuario);
        encontro.put("latitude", "");
        encontro.put("longitude", "");
        encontro.put("linha", linhaEncEditTex.getText().toString());
        encontro.put("nome", nomeEncEditText.getText().toString());
        encontro.put("referencia", refEncEditText.getText().toString());
        encontro.saveInBackground();
        onBack(v);
    }
}
