package com.avans.easypay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avans.easypay.HCE.CardService;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity {
    private Button button;

    private NfcAdapter nfcAdapter;
    private int bestellingId;

    private TextView messageOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        final Intent cardService = new Intent(this, CardService.class);

        messageOutput = (TextView) findViewById(R.id.message_textview);

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(cardService);
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!nfcAdapter.isEnabled()) {
            new AlertDialog.Builder(this).setCancelable(true).setMessage("NFC staat momenteel uit. Aanzetten?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(settingsIntent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create().show();
        }
    }

    public void onPause() {
        super.onPause();
    }
}