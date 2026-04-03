

package com.example.currencyconverterq1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etAmount = findViewById(R.id.etAmount);
        Spinner spFrom = findViewById(R.id.spFrom);
        Spinner spTo = findViewById(R.id.spTo);
        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnSettings = findViewById(R.id.btnSettings);
        TextView tvResult = findViewById(R.id.tvResult);

        String[] cur = {"INR", "USD", "EUR", "JPY"};
        ArrayAdapter<String> ad = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cur);

        spFrom.setAdapter(ad);
        spTo.setAdapter(ad);

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, settings.class))
        );

        btnConvert.setOnClickListener(v -> {
            String val = etAmount.getText().toString();

            if (val.isEmpty()) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amt = Double.parseDouble(val);
            String from = spFrom.getSelectedItem().toString();
            String to = spTo.getSelectedItem().toString();

            double inr = 93.30, eur = 0.87, jpy = 159.60;
            double toUsd;

            if (from.equals("INR")) toUsd = amt / inr;
            else if (from.equals("EUR")) toUsd = amt / eur;
            else if (from.equals("JPY")) toUsd = amt / jpy;
            else toUsd = amt;

            double res;

            if (to.equals("INR")) res = toUsd * inr;
            else if (to.equals("EUR")) res = toUsd * eur;
            else if (to.equals("JPY")) res = toUsd * jpy;
            else res = toUsd;

            tvResult.setText(String.format("%.2f %s", res, to));
        });
    }
}