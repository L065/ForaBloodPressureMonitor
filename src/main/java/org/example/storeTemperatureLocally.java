package org.example;

public class storeTemperatureLocally (double temperature)
{
    DatabaseHelper dbHelper = new DatabaseHelper(context);
    dbHelper.insertTemperature(temperature);
}
