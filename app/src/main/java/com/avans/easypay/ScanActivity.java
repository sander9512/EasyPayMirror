package com.avans.easypay;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.easypay.ASyncTasks.CheckOrderStatusTask;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.HCE.AccountStorage;

import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;


/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity implements CheckOrderStatusTask.OnStatusAvailable {

    private String TAG = this.getClass().getSimpleName();

    private Button button;
    private TextView messageOutput;

    private ImageView scanImage1, scanImage2, checkmarkImage;
    private Order order;
    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/";
    private String currentOrderStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //initialise elements
        messageOutput = (TextView) findViewById(R.id.message_textview);
        button = (Button) findViewById(R.id.button_scan);

        //initialise scan images
        scanImage1 = (ImageView) findViewById(R.id.scan_indicator_imageview1);
        scanImage2 = (ImageView) findViewById(R.id.scan_indicator_imageview2);
        checkmarkImage = (ImageView) findViewById(R.id.checkmark_imageview);
        //start infinite animation, until NFC succeeds
        Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse);
        scanImage1.startAnimation(pulse);
        scanImage2.startAnimation(pulse);

        //this is where intent data from previous activity should be called and inserted in a new Order object
//        Bundle bundle = getIntent().getExtras();
//        order = (Order) bundle.get(ORDER);
//        order.setCustomerId(5);
//        order.setOrderNumber(1235);
//        Log.i("ORDER", order.toString());

        //--TEST DATA--
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Schoenzool", 3.20, 1));
        products.add(new Product("Oreo", 1.40, 2));
        order = new Order(4, 4, new Date(), "Pizzahut", products, 8, "WAITING");
        //------------

        //add listener to cancel button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update database, so that the order has a status of 'CANCELED'
                new EasyPayAPIPUTConnector().execute(URL + order.getOrderNumber());

                //go back to MainActivity and close this activity
                Intent i = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(i);
                //close activity
                finish();
            }
        });

        //send order number to EasyPayKassa (phase 1/2)
        sendOrderNumber();
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

    //NFC phase 1/2
    public void sendOrderNumber() {
        //send order number
        Log.i(TAG, "Sending order number: " + order.getOrderNumber());
        AccountStorage.SetAccount(getApplicationContext(), "" + order.getOrderNumber());

        //check the status of the order payment every x seconds
        new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "TICK: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                new CheckOrderStatusTask(ScanActivity.this).execute(URL + order.getOrderNumber());
                Log.i(TAG, "FINISHED SENDING ORDER");
                if (currentOrderStatus.equals("RECEIVED")) {
                    //give user feedback
                    messageOutput.setText(getResources().getString(R.string.instructions_scan2));
                    //send string 'PAID' to EasyPayKassa (phase 2/2)
                    completePayment();
                } else {
                    this.start();
                }
            }
        }.start();
    }

    //NFC phase 2/2
    public void completePayment() {
        //sending string 'PAID'. When EasyPayKassa receives this string, it will handle the payment in the DB, as well as decreasing the customer balance.
        AccountStorage.SetAccount(getApplicationContext(), "PAID");

        //check the status of the order payment every x seconds
        new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                new CheckOrderStatusTask(ScanActivity.this).execute(URL + order.getOrderNumber());
                if (currentOrderStatus.equals("PAID")) {
//                    Intent i = new Intent(ScanActivity.this, OrderOverviewDetail.class);
//                    startActivity(i);
                    finish();
                } else {
                    this.start();
                }
            }
        }.start();
    }

    //after NFC connection was made...
    @Override
    public void onStatusAvailable(String status) {
        this.currentOrderStatus = status;
        Log.i("onStatusAvailable", "--CURRENT STATUS: " + status + "--");
        if (currentOrderStatus.equals("RECEIVED")) {
            Toasty.success(this, "Bestelling is ontvangen (1/2).", Toast.LENGTH_SHORT).show();

            //show animation to give user feedback that the transaction is successful
        } else if (currentOrderStatus.equals("PAID")) {
            Toasty.success(this, "Bestelling is betaald (2/2).", Toast.LENGTH_SHORT).show();
            checkMarkAnimFeedback();
        }
    }

    public void checkMarkAnimFeedback() {
        //fade out scan image
        Animation fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_out);
        scanImage1.setAnimation(fadeOut);
        scanImage2.setAnimation(fadeOut);

        //fade in check mark image
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_fade_in);
        checkmarkImage.startAnimation(fadeIn);
        checkmarkImage.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //animate check mark image
                scanImage1.setVisibility(View.GONE);
                scanImage2.setVisibility(View.GONE);
                Animation animationClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_clockwise);
                checkmarkImage.startAnimation(animationClockwise);
            }
        }, 500);
    }
}
