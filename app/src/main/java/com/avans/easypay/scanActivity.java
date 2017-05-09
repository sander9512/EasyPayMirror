package com.avans.easypay;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;

/**
 * Created by TB on 5/6/2017.
 */

public class ScanActivity extends AppCompatActivity {
    private Button button;

    public static final String EXTRA_BESTELLING_ID = "res_id";

    private NfcAdapter nfcAdapter;
    private int bestellingId;

    //for test purposes only (normally this is in kassa-app)
    private Boolean receiving = false;
    private Button receiveBtn;
    private TextView messageOutput;

    //same
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //for test purposes only (normally this is in kassa-app)
        receiveBtn = (Button) findViewById(R.id.receive_nfc_button);
        messageOutput = (TextView) findViewById(R.id.message_textview);
        bestellingId = getIntent().getIntExtra(EXTRA_BESTELLING_ID, 0);
        Log.i(this.getClass().getSimpleName(), "RESULT VAN INTENT = " + bestellingId);

//        bestellingId = 1337;

        button = (Button) findViewById(R.id.button_scan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (!receiving) {
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
            } else {
                //Set sending
                nfcAdapter.setNdefPushMessage(getIntAsNdef(bestellingId), ScanActivity.this);
            }
        } else {
            mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // Intent filters for exchanging over p2p.
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try {
                ndefDetected.addDataType("text/plain");
            } catch (Exception e) {
                e.printStackTrace();
            }

            mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

            //set receiving
            nfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
        }
    }

    public void onPause() {
        super.onPause();
    }

    private NdefMessage getIntAsNdef(int pInt) {
        byte[] textBytes = (pInt + "").getBytes(Charset.forName("UTF8"));
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(), new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{
                textRecord
        });
    }

    public void toggleNFCMode(View v) {
        messageOutput.setText("Now receiving NFC...");
        receiving = true;
        onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            if (msgs.length > 0) {
                try {
                    NdefRecord firstRecord = msgs[0].getRecords()[0];
                    byte[] payload = firstRecord.getPayload();
                    String str = new String(payload, Charset.forName("UTF8"));

                    int reservServerId = Integer.parseInt(str);
                    returnResult(reservServerId);
                } catch (Exception e) {
                    Toast.makeText(this, "Error tijdens het maken van een NFC transfer", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void returnResult(int pResServerId) {
        Intent result = new Intent();
        result.putExtra(EXTRA_BESTELLING_ID, pResServerId);
        setResult(RESULT_OK, result);
        finish();
    }
}