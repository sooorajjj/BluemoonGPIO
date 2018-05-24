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
    private static Thread sGpioThread7 ;
    private static final AtomicInteger sCurrentValue = new AtomicInteger(0);
    final String gpioPin = "142";
    TextView text;
    Button btn;
    private static View sBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sBackground = findViewById(android.R.id.content);

        if(sGpioThread7 == null){
            sGpioThread7 = new Thread(mGpioRunnable7, "GPIO-Thread");
            Log.d("thread7","start");
            sGpioThread7.start();

        }
        text = findViewById(R.id.text);

//        text.setText("Hello Gpio  value : "+sCurrentValue.toString());
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                text.setText("Hello Gpio value : "+sCurrentValue.toString());
            }
        });

//        blink()



    }

    @Override
    protected void onDestroy() {
        sBackground = null;
        super.onDestroy();
    }

    private void updateView(){
        runOnUiThread(mColorRunnable);
    }

    private final Runnable mColorRunnable = new Runnable() {
        private Random mRand = new Random();
        @Override
        public void run() {
            int r = mRand.nextInt(256);
            int g = mRand.nextInt(256);
            int b = mRand.nextInt(256);

            if(sBackground != null){
                sBackground.setBackgroundColor(Color.rgb(r, g, b));
                sBackground.postInvalidate();
                ((TextView)sBackground.findViewById(R.id.text)).setText(
                        String.format("GPIO value is: %d", sCurrentValue.get()));

            }
        }
    };

    public void blink(){//blink led

        while (true){
            try {
                WriteGPIO("142", "0");
                sleep(1000);
                WriteGPIO("142", "1");
                sleep(1000);
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

    private static int sInterruptCount = 0;
        final Runnable mGpioRunnable7 = new Runnable() {
            @Override
            public void run() {
                while(true){

                    NativeGpio.readGpio("/sys/class/gpio/gpio" + gpioPin + "/value", new NativeGpio.GpioInterruptCallback(){
                        @Override
                        public void onNewValue(int value) {
                            if (value == 1) {
                                Log.d("thread7","send");
                                Log.d("LOG: GPIO", gpioPin + " " + value );

                            } else {
                                Log.d("LOG: GPIO", gpioPin + " " + value );

                            }

                            sInterruptCount++;
                            sCurrentValue.set(value);
                            updateView();
                        }
                    });

                }
            }
        };

}
