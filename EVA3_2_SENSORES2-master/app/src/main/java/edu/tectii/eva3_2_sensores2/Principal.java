package edu.tectii.eva3_2_sensores2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Principal extends AppCompatActivity implements SensorEventListener{
    TextView txtVwDatos;
    SensorManager snmAdminSensor;
    Sensor snAcel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtVwDatos = (TextView)findViewById(R.id.txtVwDatos);
        snmAdminSensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        snAcel = snmAdminSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        snmAdminSensor.registerListener(this, snAcel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            txtVwDatos.setText("X = " + sensorEvent.values[0] + "\n" +
                    "X = " + sensorEvent.values[1] + "\n" +
                    "X = " + sensorEvent.values[2] + "\n");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
