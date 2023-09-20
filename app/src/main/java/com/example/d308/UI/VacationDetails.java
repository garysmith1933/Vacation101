package com.example.d308.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    int vacationID;
    DatePickerDialog.OnDateSetListener startDate;
    Date endDate;

    final Calendar myCalendarStart = Calendar.getInstance();

    EditText editName;
    EditText editHotelName;
    TextView editStartDate;
    CalendarView editEndDate;
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editName = findViewById(R.id.titletext);
        editHotelName = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);

        name = getIntent().getStringExtra("name");
        editName.setText(name);

        hotel = getIntent().getStringExtra("hotel");
        editHotelName.setText(hotel);

        vacationID = getIntent().getIntExtra("id", -1);

        long tempEndDate = getIntent().getLongExtra("endDate", -1);

        editEndDate.setDate(tempEndDate);

        startDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, day);

                updateStartDate();
            }
        };

        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //you get it from the intent, you pass the info down.

                //get value from other screen,but I'm going to hard code it right now
                String startDateInfo = editStartDate.getText().toString();
                if (startDateInfo.equals("")) startDateInfo = "02/10/22";

                try {
                    myCalendarStart.setTime(formatter.parse(startDateInfo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                endDate = calendar.getTime();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository((getApplication()));
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    private void updateStartDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tag", myCalendarStart.getTime().toString());
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            this.finish();
            return true;
        }
        if(item.getItemId() == R.id.vacationSave ) {
            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size()-1).getVacationID()+1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), myCalendarStart.getTime(), endDate);
                repository.insert(vacation);
            }

            else {
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotelName.getText().toString(), myCalendarStart.getTime(), endDate);
                repository.update(vacation);
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


//need to do the same thing for excursion, being able to add it and update it.