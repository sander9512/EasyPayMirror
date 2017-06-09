package com.avans.easypay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
import com.avans.easypay.DomainModel.Balance;
import com.avans.easypay.DomainModel.Order;
import com.avans.easypay.DomainModel.Product;
import com.avans.easypay.HCE.AccountStorage;
import com.avans.easypay.SQLite.BalanceDAO;
import com.avans.easypay.SQLite.DAOFactory;
import com.avans.easypay.SQLite.SQLiteDAOFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

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

    private boolean orderReceived = false;
    private boolean paymentReceived = false;

    private String currentStatus = "";

    private Context context = this;

    public CountDownTimer sendOrderTimer, completePaymentTimer;

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

//        this is where intent data from previous activity should be called and inserted in a new Order object
        order = (Order) getIntent().getSerializableExtra("ORDER");
        currentStatus = order.getStatus();
        Log.i(this.getClass().getSimpleName() + "order = ", order.toString());
        Log.i(this.getClass().getSimpleName(), order.getStatus() + "");
        Log.i(this.getClass().getSimpleName(), currentStatus);

        //add listener to cancel button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Weet u zeker dat u de bestelling wilt annuleren?")
                        .setTitle("Annulering bestelling")
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //update database, so that the order has a status of 'CANCELED'
                                String deleteOrderURL = "https://easypayserver.herokuapp.com/api/bestelling/delete/" + order.getOrderNumber();
                                new EasyPayAPIDELETEConnector().execute(deleteOrderURL);

                                sendOrderTimer.cancel();

                                //go back to MainActivity and close this activity
                                Intent i = new Intent(ScanActivity.this, MainActivity.class);
                                finish();
                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //does nothing other than closing the alert dialog
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //send order number to EasyPayKassa (phase 1/2)
        sendOrderNumber();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //NFC phase 1/2
    public void sendOrderNumber() {
        //send order number
        Log.i(TAG, "Sending order number: " + order.getOrderNumber());
        AccountStorage.SetAccount(getApplicationContext(), "" + order.getOrderNumber());

        //check the status of the order payment every x seconds
        sendOrderTimer = new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "TICK: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                new CheckOrderStatusTask(ScanActivity.this).execute(URL + order.getOrderNumber());
                if (currentStatus.equals("RECEIVED")) {
                    Log.i(TAG, "FINISHED SENDING ORDER");
                    //give user feedback
                    messageOutput.setText(getResources().getString(R.string.instructions_scan2));
                    //send string 'PAID' to EasyPayKassa (phase 2/2)
                    completePayment();
                    sendOrderTimer.cancel();
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
        completePaymentTimer = new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, millisUntilFinished + "");
            }

            @Override
            public void onFinish() {
                new CheckOrderStatusTask(ScanActivity.this).execute(URL + order.getOrderNumber());
                if (currentStatus.equals("PAID")) {
                    this.cancel();

                    //decrease balance in SQLite DB
                    decreaseBalanceLocally();

                    //start new activity
                    Intent i = new Intent(ScanActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    completePaymentTimer.cancel();
                } else {
                    this.start();
                }
            }
        }.start();

        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        nfc.setNdefPushMessage(null, this);
    }

    //after NFC connection was made...
    @Override
    public void onStatusAvailable(String status) {
        currentStatus = status;
        if (currentStatus.equals("RECEIVED")) {
            if (!orderReceived) {
                Toasty.success(this, "Bestelling is ontvangen (1/2).", Toast.LENGTH_SHORT).show();
                orderReceived = true;
            }

            //show animation to give user feedback that the transaction is successful
        } else if (currentStatus.equals("PAID")) {
            if (!paymentReceived) {
                Toasty.success(this, "Bestelling is betaald (2/2).", Toast.LENGTH_SHORT).show();
                checkMarkAnimFeedback();
                paymentReceived = true;
            }
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

    public void decreaseBalanceLocally() {
        //calc price
        float price = 0;
        for (int i = 0; i < order.getProducts().size(); i++) {
            price += order.getProducts().get(i).getProductPrice();
        }

        Log.i(TAG, "Calculated price = " + price);

        //get current balance
        DAOFactory factory = new SQLiteDAOFactory(getApplicationContext());
        BalanceDAO balanceDAO = factory.createBalanceDAO();
        float currentBalance = balanceDAO.selectData().get(balanceDAO.selectData().size() - 1).getAmount();
        Log.i(TAG, "Current balance = " + currentBalance);
        balanceDAO.insertData(new Balance(currentBalance - price, new Date()));
        Log.i(TAG, "New balance = " + (currentBalance - price));
    }
}
