package com.example.ju.eva3_3_bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adNuesvos;
    ListView conectados, nuevos;
    BluetoothAdapter btAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conectados = (ListView)findViewById(R.id.lstconectados);
        nuevos = (ListView)findViewById(R.id.lstnuevos);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void onClickBuscar(View v) {
        //verificar si no hay una busquedad en proceso
        if (btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();

        }else {
            btAdapter.startDiscovery();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //verificar si esta encendidoo, si no
        //Pedirle al usuario que lo active
        if (!btAdapter.isEnabled()){
            Intent inActivaBluet = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(inActivaBluet,1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){//Pantalla de activacion del BlueTooth
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Activado", Toast.LENGTH_SHORT).show();
                //LLenar una lista con los dispositivos conectados
                LlenarDisp();
            }else {
                Toast.makeText(this, "Sigue desactivado", Toast.LENGTH_SHORT).show();
                    finish();
            }
        }
    }
    public void LlenarDisp(){
        ArrayAdapter<String> aaListaPaired =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        conectados.setAdapter(aaListaPaired);

        //PARA LOS NO COONECTADOS Y QUE VAMOS A BUSCAR
        //REQUERIMOS ESCUCHAR AL BLUETOOTH
        //CON BROOADCAST RECIVER
        adNuesvos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        conectados.setAdapter(adNuesvos);
        IntentFilter infFiltro = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, infFiltro);
        IntentFilter infFiltro2 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, infFiltro2);


        Set<BluetoothDevice> stConectados = btAdapter.getBondedDevices();
        if (stConectados.size() >0){
           for (BluetoothDevice btDispo: stConectados){
                aaListaPaired.add(btDispo.getName() + "\n");
            }
        }else {
            aaListaPaired.add("No se encontraron dispositivos");
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //encontrar algún dispositivo
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //dispositivo que no ah sido vinculado
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    adNuesvos.add(device.getName() + "\n" + device.getAddress());
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                if (adNuesvos.getCount() == 0){
                    adNuesvos.add("No hay dipositivos");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btAdapter !=null){
            btAdapter.cancelDiscovery();
        }
        unregisterReceiver(mReceiver);
    }
}

