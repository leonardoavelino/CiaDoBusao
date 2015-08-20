package com.example.snakechild.ciadobusao;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.snakechild.ciadobusao.util.ControladorEncontroRecorrente;
import com.example.snakechild.ciadobusao.util.CustomizeFont;
import com.example.snakechild.ciadobusao.util.EncontroRecorrente;
import com.example.snakechild.ciadobusao.util.ValidateInput;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NovoEncontroActivity extends Activity {

    private ControladorEncontroRecorrente controladorEncontroRecorrente;

    private static TextView nomeText, linhaText, pontoRefText, editDataText, editHoraText, editLocalMapaText, criarText, cancelarText, dataText, horaText, mapaText;
    private static EditText nomeEncEditText, linhaEncEditTex, refEncEditText;
    private CheckBox repertirCheck, checkDom, checkSeg, checkTer, checkQua, checkQui, checkSex, checkSab;
    private RadioButton radioTodoDia, radioTodaSemana, radioEscolherDias;
    private RadioGroup radioGroup;
    private RelativeLayout relativeDias;
    public static double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novoencontro);

        controladorEncontroRecorrente = ControladorEncontroRecorrente.getInstance(getApplicationContext());

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
        mapaText = (TextView) this.findViewById(R.id.idLocalMapaTextView);

        nomeEncEditText = (EditText) this.findViewById(R.id.idNomeEdit);
        linhaEncEditTex = (EditText) this.findViewById(R.id.idLinhaEdit);
        refEncEditText = (EditText) this.findViewById(R.id.idPontoEdit);

        radioTodoDia = (RadioButton) this.findViewById(R.id.radioTodoDia);
        radioTodoDia.setChecked(true);
        radioTodaSemana = (RadioButton) this.findViewById(R.id.radioTodaSemana);
        radioEscolherDias = (RadioButton) this.findViewById(R.id.radioEscolherDias);

        repertirCheck = (CheckBox) this.findViewById(R.id.repertirText);
        checkDom = (CheckBox) this.findViewById(R.id.checkDom);
        checkSeg = (CheckBox) this.findViewById(R.id.checkSeg);
        checkTer = (CheckBox) this.findViewById(R.id.checkTer);
        checkQua = (CheckBox) this.findViewById(R.id.checkQua);
        checkQui = (CheckBox) this.findViewById(R.id.checkQui);
        checkSex = (CheckBox) this.findViewById(R.id.checkSex);
        checkSab = (CheckBox) this.findViewById(R.id.checkSab);

        radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
        relativeDias = (RelativeLayout) this.findViewById(R.id.relativeDiasSemana);
        radioGroup.setVisibility(View.INVISIBLE);
        relativeDias.setVisibility(View.INVISIBLE);

        createListenersRecorrenteButtons();

        customizeItems();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void createListenersRecorrenteButtons() {
        repertirCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getVisibility() == View.VISIBLE) {
                    radioGroup.setVisibility(View.INVISIBLE);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        radioEscolherDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeDias.setVisibility(View.VISIBLE);
            }
        });
        radioTodoDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeDias.setVisibility(View.INVISIBLE);
            }
        });
        radioTodaSemana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeDias.setVisibility(View.INVISIBLE);
            }
        });


    }

    public void customizeItems() {
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

    public void onBack(View v) {
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

            String myFormat = "dd/MM/yyyy"; //In which format you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            dataText.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void onLocalPressed(View v) {
        Intent i = new Intent();
        i.setClass(getApplicationContext(), MapActivity.class);
        i.putExtra("nomeEnc", nomeEncEditText.getText().toString());
        MapActivity.novoEncontro = true;
        startActivity(i);
    }

    public void onDatePressed(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
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

    public static boolean validateParams() {
        boolean result = true;
        if (!ValidateInput.isNome(nomeEncEditText.getText().toString())) {
            nomeEncEditText.setError("Nome Inválido!");
            nomeEncEditText.setText("");
            result = false;
        }
        if (!ValidateInput.isPonto(refEncEditText.getText().toString())) {
            refEncEditText.setError("Ponto Inválido!");
            refEncEditText.setText("");
            result = false;
        }
        if (!ValidateInput.isLinha(linhaEncEditTex.getText().toString())) {
            linhaEncEditTex.setError("Linha Inválida!");
            linhaEncEditTex.setText("");
            result = false;
        }
        if (!ValidateInput.isDataHora(dataText.getText().toString(), horaText.getText().toString())) {
            horaText.setError("Horário Inválido!");
            horaText.setText("");
            dataText.setError("Data Inválida!");
            dataText.setText("");
            result = false;
        }
        /*if(mapaText.getText().toString().isEmpty()){
            mapaText.setError("Localização Inválida!");
            mapaText.setText("");
            result = false;
        }*/
        return result;
    }

    public void saveEncontro(View v) {
        if (validateParams()) {
            ParseObject encontro = new ParseObject("Encontro");
            String latitudeString = String.valueOf(latitude);
            String longitudeString = String.valueOf(longitude);

            encontro.put("data", dataText.getText().toString());
            encontro.put("horario", horaText.getText().toString());
            encontro.put("idDono", PerfilActivity.idUsuario);
            encontro.put("latitude", latitudeString);
            encontro.put("longitude", longitudeString);
            encontro.put("linha", linhaEncEditTex.getText().toString());
            encontro.put("nome", nomeEncEditText.getText().toString());
            encontro.put("referencia", refEncEditText.getText().toString());
            encontro.saveInBackground();
            Toast.makeText(getApplicationContext(), "Encontro criado com sucesso!", Toast.LENGTH_SHORT).show();
            criaRecorrentes();


            ParsePush push = new ParsePush();
            String message = "Novo encontro criado";
            push.setMessage(message);
            push.sendInBackground();

            ParseQuery query = new ParseQuery("Encontro");
            query.whereEqualTo("idDono", PerfilActivity.idUsuario);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (parseObjects != null) {
                        final ParseObject usuario = new ParseObject("PerfisConfirmaram");
                        usuario.put("idUsuario", PerfilActivity.idUsuario);
                        usuario.put("idEncontro", parseObjects.get(parseObjects.size() - 1).getObjectId());
                        usuario.saveInBackground();
                        Log.d("ID", parseObjects.get(parseObjects.size() - 1).getObjectId());
                    }
                }
            });

            onBack(v);
        } else {
            Toast.makeText(getApplicationContext(), "Preencha novamente o que está errado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void criaRecorrentes() {
        if (repertirCheck.isChecked()) {
            boolean[] dias = new boolean[7];
            dias[0] = false;
            dias[1] = false;
            dias[2] = false;
            dias[3] = false;
            dias[4] = false;
            dias[5] = false;
            dias[6] = false;
            if (radioTodoDia.isChecked()) {
                dias[0] = true;
                dias[1] = true;
                dias[2] = true;
                dias[3] = true;
                dias[4] = true;
                dias[5] = true;
                dias[6] = true;
            } else if (radioTodaSemana.isChecked()) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(dataText.getText().toString());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    dias[calendar.get(Calendar.DAY_OF_WEEK)-1] = true;
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            } else if (radioEscolherDias.isChecked()) {
                dias[0] = checkDom.isChecked();
                dias[1] = checkSeg.isChecked();
                dias[2] = checkTer.isChecked();
                dias[3] = checkQua.isChecked();
                dias[4] = checkQui.isChecked();
                dias[5] = checkSex.isChecked();
                dias[6] = checkSab.isChecked();
            }

            EncontroRecorrente encontroRecorrente =
                    new EncontroRecorrente(nomeEncEditText.getText().toString(),
                            horaText.getText().toString(), dias);
            controladorEncontroRecorrente.addEncontro(encontroRecorrente);

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nomeEnc", nomeEncEditText.getText().toString());
        outState.putString("linhaEnc", linhaEncEditTex.getText().toString());
        outState.putString("refEnc", refEncEditText.getText().toString());
        outState.putString("dataEnc", dataText.getText().toString());
        outState.putString("horaEnc", horaText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nomeEncEditText.setText(savedInstanceState.getString("nomeEnc"));
        linhaEncEditTex.setText(savedInstanceState.getString("linhaEnc"));
        refEncEditText.setText(savedInstanceState.getString("refEnc"));
        dataText.setText(savedInstanceState.getString("dataEnc"));
        horaText.setText(savedInstanceState.getString("horaEnc"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (latitude != 0) {
            mapaText.setText("Local Definido!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
