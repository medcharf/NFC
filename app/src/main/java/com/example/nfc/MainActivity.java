package com.example.nfc;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements NfcAdapter.ReaderCallback {

    private NfcAdapter nfcAdapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("Scan your NFC card...");
        textView.setTextSize(24f);
        setContentView(textView);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            // Enable Reader Mode with flags for IsoDep
            int flags = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
            nfcAdapter.enableReaderMode(this, this, flags, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        // Called on background thread, so switch to UI thread for UI update
        runOnUiThread(() -> textView.setText("NFC Tag detected!"));

        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep == null) {
            runOnUiThread(() -> {
                textView.setText("Tag does not support IsoDep");
                Toast.makeText(this, "Not an IsoDep tag", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        try {
            isoDep.connect();

            // Example APDU command: Select the Master File (you can customize)
            byte[] selectApdu = {
                    (byte) 0x00, // CLA
                    (byte) 0xA4, // INS
                    (byte) 0x04, // P1
                    (byte) 0x00, // P2
                    (byte) 0x00  // Lc = 0
            };

            byte[] response = isoDep.transceive(selectApdu);

            StringBuilder sb = new StringBuilder();
            for (byte b : response) {
                sb.append(String.format("%02X ", b));
            }
            String responseHex = sb.toString().trim();

            runOnUiThread(() -> {
                textView.setText("Response:\n" + responseHex);
                Toast.makeText(this, "APDU Response: " + responseHex, Toast.LENGTH_LONG).show();
            });

            isoDep.close();

        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                textView.setText("Error communicating with tag");
                Toast.makeText(this, "Communication error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }
}
