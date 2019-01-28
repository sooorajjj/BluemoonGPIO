package com.vst.bluemoongpio;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private File file;
    final String gpioPin = "938";
    TextView text;
    private Handler mHandler = new Handler();
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 5000;
    boolean gpioHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

        mHandler.post(mBlinkRunnable);



    }

    @Override
    protected void onDestroy() {
//        sBackground = null;
        mHandler.removeCallbacks(mBlinkRunnable);
        super.onDestroy();
    }


    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            // Toggle the GPIO state
            if (!gpioHigh) {
                WriteGPIO(gpioPin, "1");
                gpioHigh = true;
                text.setText(R.string.gpioHigh);
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } else {
                WriteGPIO(gpioPin, "0");
                gpioHigh = false;
                text.setText(R.string.gpioLow);
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            }

        }
    };




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
