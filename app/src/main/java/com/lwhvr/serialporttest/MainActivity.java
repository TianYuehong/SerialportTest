package com.lwhvr.serialporttest;

import androidx.appcompat.app.AppCompatActivity;
import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    private SerialPortFinder mSerialPortFinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSerialPortFinder = new SerialPortFinder();
        String[] devices = mSerialPortFinder.getAllDevices();

        for(String d:devices)
        {
            Log.d("devices", d);
        }

    }
}