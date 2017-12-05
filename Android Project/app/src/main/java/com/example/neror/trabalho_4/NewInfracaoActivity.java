package com.example.neror.trabalho_4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class NewInfracaoActivity extends AppCompatActivity implements View.OnClickListener{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView img;
    private Bitmap bmp;
    private Button btnTakePicture;
    private Button btnFinish;
    private Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_infracao);

        img = (ImageView) findViewById(R.id.imgPhoto);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(this);

        btnFinish =(Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnTakePicture) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            }
        }
        else if(view.getId() == R.id.btnFinish){
            Intent intent = new Intent();
            MultiAutoCompleteTextView textView =(MultiAutoCompleteTextView) findViewById(R.id.txtDescrition);
            String description = textView.getText().toString();
            if(img == null || description == null || description.length() == 0) {
                Toast.makeText(getApplicationContext(),"É necessário selecionar uma imagem e preencher a descrição.",Toast.LENGTH_LONG).show();
                return;
            }

            intent.putExtra("ImageData", getImageArray());
            intent.putExtra("Description", description);
            setResult(RESULT_OK, intent);

            finish();
        }
        else{
            setResult(RESULT_CANCELED);
            Toast.makeText(getApplicationContext(),"Cadastro Cancelado.",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            this.bmp = (Bitmap) extras.get("data");
            ImageView img = (ImageView) findViewById(R.id.imgPhoto);
            img.setImageBitmap(this.bmp);
        }
    }

    private byte[] getImageArray(){
        if(bmp!= null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }
}
