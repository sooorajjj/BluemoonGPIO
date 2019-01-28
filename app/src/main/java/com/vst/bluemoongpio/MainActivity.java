package com.vst.bluemoongpio;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private File file;
    final String gpioPin = "938";
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        text = findViewById(R.id.text);

//        text.setText("Hello Gpio  value : "+sCurrentValue.toString());
//        btn = findViewById(R.id.button);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                text.setText("Hello Gpio value : "+sCurrentValue.toString());
//            }
//        });

        blink();



    }

    @Override
    protected void onDestroy() {
//        sBackground = null;
        super.onDestroy();
    }

    public void blink(){//blink led

        while (true){
            try {
                WriteGPIO(gpioPin, "1");
                text.setText(R.string.gpioHigh);
                sleep(5000);
                WriteGPIO(gpioPin, "0");
                text.setText(R.string.gpioLow);
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void WriteGPIO(String gpioPin, String value) {


        file = new File("/sys/class/gpio/gpio" + gpioPin + "/value");


        Log.d("LOG", gpioPin + " " + value );



        try {
            FileOutputStream fos = new FileOutputStream (file.getAbsolutePath());

            OutputStreamWriter myOutWriter = new OutputStreamWriter(fos);
            myOutWriter.write(value);
            myOutWriter.close();



            fos.close();

        } catch (FileNotFoundException e) {
            Log.i("LOG", "write failed 1 : " +e.getMessage());
        } catch (IOException e) {
            Log.i("LOG", "write failed 2 : " +e.getMessage());
        }
    }

}
