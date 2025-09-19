package com.example.listycitylab3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.AddCityDialogListener {
    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;
    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };
        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }
        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> {
            new AddCityFragment().show(getSupportFragmentManager(), "Add City");
        });

        cityList.setOnItemClickListener((parent, view, position, id) -> showEditDialog(position));
    }



    private void showEditDialog(int position) {
        City city = dataList.get(position); //city we are editing getting position with arraylist containing cities

        View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_city, null);
        EditText nameEt = dialogView.findViewById(R.id.edit_text_city_text);
        EditText provEt = dialogView.findViewById(R.id.edit_text_province_text);

        nameEt.setText(city.getName());
        provEt.setText(city.getProvince());

        new AlertDialog.Builder(this) //modal edit
                .setTitle("Edit City")
                .setView(dialogView)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Ok", (d,w)->{
                    //set new cities and update text of new cities

                    String newname = nameEt.getText().toString().trim();
                    String newprov = provEt.getText().toString().trim();

                    if (newname.isEmpty() || newprov.isEmpty() ) {
                        Toast.makeText(this,"Both fields required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //if not empty, update
                    city.setName(newname);
                    city.setProvince(newprov);

                    cityAdapter.notifyDataSetChanged();//refresh row
                })
                .show();

    }
}