package com.lwhvr.serialporttest;

import androidx.appcompat.app.AppCompatActivity;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;


public class MainActivity extends AppCompatActivity {
    private SerialPortFinder mSerialPortFinder;
    private SerialPort mSerialPort = null;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private ReadThread mReadThread;
    private TextView recTextView;

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            Log.e("Read", "Read Thread is open");
            //Log.e(tag:"Read", "Read thread is open.");
            while (!isInterrupted()) {
                if (mInputStream == null)
                    return;

                try {
                    byte[] buffer = new byte[64];
                    int size = 0;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSerialPortFinder = new SerialPortFinder();
        String[] devices = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();

        for (String d : devices) {
            Log.d("devices", d);
        }

        for (String d : entryValues) {
            Log.d("devicesPath", d);
        }

        final Button buttonQuit = findViewById(R.id.quitButton);
        buttonQuit.setOnClickListener(view -> MainActivity.this.finish());

        recTextView = findViewById(R.id.textView);

        final Button buttonSend = findViewById(R.id.sendMessageButton);
        buttonSend.setOnClickListener(view -> {
            try {
                byte[] sendData = {(byte) 0xff, 0x26, 0x00, 0x00, (byte) 0xee};
                mOutputStream.write(sendData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mReadThread != null) {
            mReadThread.interrupt();
        }

        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;

            try {
                mOutputStream.close();
                mOutputStream = null;
                mInputStream.close();
                mInputStream = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    public void OpenSerialPort(View view) {
        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS0"), 9600, 0);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();

            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(() -> {
            //String receivedMessage = new String(buffer, 0, size);
            String receivedMessage = hex(buffer,size);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            recTextView.setText(String.format("%s:%s", timestamp, receivedMessage));
            Log.d("rec", "Received message: " + receivedMessage);
        });
    }

    public static String hex(byte[] bytes, int size) {
        StringBuilder result = new StringBuilder();
        for(int i=0;i<size;i++)
        {
            result.append(String.format("%02x", bytes[i]));
        }
//        for (byte aByte : bytes) {
//            result.append(String.format("%02x", aByte));
//        }
        return result.toString();
    }

}