package com.saurav.qrgenerater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScannerActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    TextView textView;
    CodeScannerView scanView;
    EditText edtNo;
    String sMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scanView=findViewById(R.id.codeScannerView);
        codeScanner =new CodeScanner(this,scanView);
        textView=findViewById(R.id.txtView);
        edtNo=findViewById(R.id.edtNo);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sMessage=result.getText().toString();
                        textView.setText(sMessage);

                    }
                });
            }
        });
        scanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
     //   codeScanner.startPreview();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        codeScanner.startPreview();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(ScannerActivity.this, "Camera permission is required", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    public void btnSend(View view) {
        if (ContextCompat.checkSelfPermission(ScannerActivity.this,Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){

            sendMessage();
        }else {
            ActivityCompat.requestPermissions(ScannerActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
        }
    }

    private void sendMessage() {

        String sphone=edtNo.getText().toString().trim();
      //  String sMessage=textView.getText().toString().trim();

        if (!sphone.equals("")&&!sMessage.equals("")){

            SmsManager smsManager=SmsManager.getDefault();

            smsManager.sendTextMessage(sphone,null,sMessage,null,null);

            Toast.makeText(this, "SMS sent successfully....", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Enter value first...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100&&grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }else {
            Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
        }
    }
}