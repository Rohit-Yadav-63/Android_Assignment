package com.example.sensor;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public SensorManager sensormanager;
    public Sensor accelerometer;
    public Sensor light;
    public Sensor proximity;

    private TextView accelerometerText, lightText, proximityText;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        accelerometerText = findViewById(R.id.accmeter);
        lightText = findViewById(R.id.light);
        proximityText = findViewById(R.id.proximity);

        // sensor init
        sensormanager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximity = sensormanager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (accelerometer == null) {
            accelerometerText.setText("Accelerometer: not available");
        }
        if (light == null) {
            lightText.setText("Light: not available");
        }
        if (proximity == null) {
            proximityText.setText("Proximity: not available");
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensormanager == null) return;
        if (accelerometer != null) {
            sensormanager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (light != null) {
            sensormanager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximity != null) {
            sensormanager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensormanager != null) {
            sensormanager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.sensor == null) return;
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values.length > 0 ? event.values[0] : 0f;
            float y = event.values.length > 1 ? event.values[1] : 0f;
            float z = event.values.length > 2 ? event.values[2] : 0f;
            accelerometerText.setText("Accelerometer\nx: " + x + "\ny: " + y + "\nz: " + z);
        } else if (type == Sensor.TYPE_LIGHT) {
            float lux = event.values.length > 0 ? event.values[0] : 0f;
            lightText.setText("Light\nlux: " + lux);
        } else if (type == Sensor.TYPE_PROXIMITY) {
            float distance = event.values.length > 0 ? event.values[0] : 0f;
            float max = event.sensor.getMaximumRange();
            String state = distance < max ? "Near" : "Far";
            proximityText.setText("Proximity\n" + state + " (" + distance + ")");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no-op
    }
}