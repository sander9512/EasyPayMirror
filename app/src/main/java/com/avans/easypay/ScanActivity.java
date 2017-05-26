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
import com.avans.easypay.HCE.AccountStorage;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity implements CheckOrderStatusTask.OnStatusAvailable {

    private String TAG = this.getClass().getSimpleName();

    private Button button;
    private TextView messageOutput;

    private ImageView scanImage1, scanImage2, checkmarkImage;

    private String URL = "https://easypayserver.herokuapp.com/api/bestelling/";
    private String currentOrderStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        scanImage1 = (ImageView) findViewById(R.id.scan_indicator_imageview1);
        scanImage2 = (ImageView) findViewById(R.id.scan_indicator_imageview2);
        //start infinite animation, until NFC succeeded
        Animation pulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse);
        scanImage1.startAnimation(pulse);
        scanImage2.startAnimation(pulse);
        checkmarkImage = (ImageView) findViewById(R.id.checkmark_imageview);
        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMarkAnimFeedback();
            }
        });

        //this is where intent data from previous activity should be called and inserted in a new Order object
//        Order order = getIntent().getSerializableExtra("ORDER");
        final Order order = new Order(4, 4, 14, 8, "WAITING");

        //HCE components
        messageOutput.setText(getResources().getString(R.string.instructions_scan));
        AccountStorage.SetAccount(getApplicationContext(), "" + order.getOrderNumber());

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
                    //start new activity after x seconds
                    Intent i = new Intent(ScanActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    this.start();
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
        this.currentOrderStatus = status;
        if (currentOrderStatus.equals("PAID")) {
            Toast.makeText(this, "Bestelling is betaald", Toast.LENGTH_SHORT).show();

            //show animation to give user feedback that the transaction is successful
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
