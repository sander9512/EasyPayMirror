package com.avans.easypay;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.CheckOrderStatusTask;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.HCE.AccountStorage;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity implements CheckOrderStatusTask.OnStatusAvailable {

    private String TAG = this.getClass().getSimpleName();

    private Button button;

    private TextView messageOutput;
    private int checkStatusDelay = 10000, nextActivityDelay = 3000;

    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/";
    private String currentOrderStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //this is where intent data from previous activity should be called and inserted in a new Order object
//        Order order = getIntent().getSerializableExtra("ORDER");
        final Order order = new Order(4, 4, 14, 8, "WAITING");

        //HCE components
        messageOutput.setText(getResources().getString(R.string.instructions_scan));
        AccountStorage.SetAccount(getApplicationContext(), "" + order.getOrderNumber());

        //check the status of the order payment every x seconds
        new CountDownTimer(10000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                new CheckOrderStatusTask(ScanActivity.this).execute(URL + order.getOrderNumber());
                if (currentOrderStatus.equals("PAID")) {

                    //start new activity after x seconds
                    Log.i(TAG, "1. Order status: " + currentOrderStatus);
//                    Intent i = new Intent(this, DeClassWaarOmidMeeBezigIs.class);
//                    startActivity(i);
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

//        if (!nfcAdapter.isEnabled()) {
//            new AlertDialog.Builder(this).setCancelable(true).setMessage("NFC staat momenteel uit. Aanzetten?")
//                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
//                            startActivity(settingsIntent);
//                        }
//                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .create().show();
//        }
    }

    @Override
    public void onStatusAvailable(String status) {
        Toast.makeText(this, "API GET request gedaan.", Toast.LENGTH_SHORT).show();
        this.currentOrderStatus = status;
        Log.i(TAG, "3. Order status: " + currentOrderStatus);
        if (currentOrderStatus.equals("PAID")) {
            Log.i(TAG, "4. Order status: " + currentOrderStatus);
            Toast.makeText(this, "Status is nu betaald", Toast.LENGTH_SHORT).show();

            //show check mark animation to give user feedback that the transaction is successful

        }
    }
}
