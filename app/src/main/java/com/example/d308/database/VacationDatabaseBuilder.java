package com.example.d308.database;

import androidx.room.Database;

import com.example.d308.entities.Excursion;
import com.example.d308.entities.Vacation;

@Database(entities = {Excursion.class, Vacation.class}, version=1, exportSchema = false)
public class VacationDatabaseBuilder {
}
