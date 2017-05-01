package com.avans.easypay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RefundBalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_refund);

        Button CancelButton = (Button) findViewById(R.id.balance_refund_cancel);
        Button ConfirmButton = (Button) findViewById(R.id.balance_refund_confirm);
        CancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefundBalanceActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });

        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefundBalanceActivity.this, BalanceActivity.class);
                startActivity(intent);
            }
        });
    }
}
