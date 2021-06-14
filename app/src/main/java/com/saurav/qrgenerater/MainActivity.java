package com.saurav.qrgenerater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText1;
    EditText edittext2;
    Button btnGenerate,btnScan;
    ImageView imageView;
    private static final String TAG = "MyActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.edtText);
        editText1=findViewById(R.id.editTextNumber);
        edittext2= findViewById(R.id.editTextTextPersonName2);

        btnGenerate=findViewById(R.id.btnGenerate);
        imageView=findViewById(R.id.imageView);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data0 = editText.getText().toString();
                String data1 = editText1.getText().toString();
                String data2 = edittext2.getText().toString();
                String data3 =" ";
                String data = data0.concat(data3).concat(data1).concat(data3).concat(data2);
                if (data.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill the QR value", Toast.LENGTH_SHORT).show();
                }
                else {
                    QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);

                    Bitmap qrbits = qrgEncoder.getBitmap();
                    imageView.setImageBitmap(qrbits);
                }


            }
        });

    }
}