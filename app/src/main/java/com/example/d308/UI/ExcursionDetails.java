package com.example.d308.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    String name;
    int excursionID;
    int vacationID;

    EditText editName;
    TextView editDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    Excursion currentExcursion;

    int selectedVacationID = -1;

    final Calendar calendarStart = Calendar.getInstance();
    private ExcursionAdapter excursionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository = new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionName);
        editName.setText(name);
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", selectedVacationID);
        editDate = findViewById(R.id.date);
        String dateFormat = "E MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);

        ArrayList<Vacation> vacationList= new ArrayList<>();
        vacationList.addAll(repository.getmAllVacations());
        ArrayList<Integer> vacationIDList = new ArrayList<>();

        for (Vacation vacation:vacationList) {
            vacationIDList.add(vacation.getVacationID());
        }
        ArrayAdapter<Integer> vacationIdAdapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,vacationIDList);
        Spinner spinner=findViewById(R.id.spinner);
        spinner.setAdapter(vacationIdAdapter);

            startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, day);

                updateStartDate();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String startDateInfo = getIntent().getStringExtra("startDate");

                if (startDateInfo == "" || startDateInfo == null) {
                    startDateInfo = formatter.format(new Date());
                }

                try {
                    calendarStart.setTime(formatter.parse(startDateInfo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(ExcursionDetails.this, startDate, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedVacationID = (int) parentView.getItemAtPosition(position);
                Log.d("id", "vacation id is set" + selectedVacationID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }



    private void updateStartDate() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tag", calendarStart.getTime().toString());
        editDate.setText(sdf.format(calendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== android.R.id.home){
            this.finish();
            return true;
        }

        if (item.getItemId()== R.id.excursionSave){
            Excursion excursion;
            if (excursionID == -1) {
                if (repository.getmAllExcursions().size() == 0) {
                    excursionID = 1;
                }
                else {
                    excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                }
                excursion = new Excursion(excursionID, editName.getText().toString(), calendarStart.getTime(), selectedVacationID);
                repository.insert(excursion);
                this.finish();
            }
            else {
                excursion = new Excursion(excursionID, editName.getText().toString(), calendarStart.getTime(), selectedVacationID);
                repository.update(excursion);
                this.finish();
            }

            return true;
        }

        if (item.getItemId() == R.id.excursionDelete) {
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) currentExcursion = excursion;
            }

            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted.", Toast.LENGTH_LONG).show();
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}

//e.  Include validation that the excursion date is during the associated vacation. - after start date before end date?
