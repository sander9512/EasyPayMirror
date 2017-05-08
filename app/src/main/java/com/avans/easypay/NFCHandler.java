package com.avans.easypay;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

/**
 * Created by Felix on 8-5-2017.
 */

public class NFCHandler implements NfcAdapter.CreateNdefMessageCallback {

    private String TAG = this.getClass().getSimpleName();

    public void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null) {
                Log.i(TAG, "Tag is not ndef formatible!");
                return;
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                Log.i(TAG, "Tag object cannot be null!");
                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()) {
                    ndef.close();
                    Log.i(TAG, "Tag is not writable!");
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Log.i(TAG, "Tag written!");
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }


}
