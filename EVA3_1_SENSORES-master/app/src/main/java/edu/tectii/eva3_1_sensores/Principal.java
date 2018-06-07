package edu.tectii.eva3_1_sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Principal extends AppCompatActivity implements ListView.OnItemClickListener{
    TextView txtVwDatos;
    ListView lstVwSensores;
    SensorManager smAdminSensor;
    List<Sensor> lstSensores;
    String [] asSensores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        txtVwDatos = (TextView)findViewById(R.id.txtVwDatos);
        lstVwSensores = (ListView)findViewById(R.id.lstVwSensores);

        //hay que acceder al gestor de servicios
        smAdminSensor = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lstSensores = smAdminSensor.getSensorList(Sensor.TYPE_ALL);
        //vamos a leer los nombres de cada sensor y ponerlo en un arreglo de strings para poner la lista
        asSensores = new String[lstSensores.size()];
        int i = 0;
        for (Sensor sSensor: lstSensores){
            asSensores[i] = sSensor.getName();
            i++;
        }
        lstVwSensores.setAdapter(new ArrayAdapter<String>(Principal.this,
                android.R.layout.simple_list_item_1,
                asSensores));
        lstVwSensores.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Sensor sSensor = lstSensores.get(i);
        txtVwDatos.setText("Rango Mas: " + sSensor.getMaximumRange() + "\n" +
                "Demora: " + sSensor.getMinDelay() + "\n" +
        "Resolución: " + sSensor.getResolution());
    }
}
