package com.cara2v.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cara2v.R;
import com.cara2v.ui.activity.consumer.AddVehicleInfoActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropActivity extends Activity implements View.OnClickListener {

    private CropImageView cropImageView;
    private String imagePath;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop);
        findViewById(R.id.crop).setOnClickListener(this);
        cropImageView = findViewById(R.id.cropImageView);
        progressBar =  findViewById(R.id.progressBar);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            imagePath = b.getString("Image");
            String from = b.getString("FROM");
            if (from.equals("camera")) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);/*BitmapFactory.decodeFile(filepath);*/
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                cropImageView.setImageBitmap(myBitmap);
            } else {
                Uri selectedImageUri = Uri.parse(imagePath);
                cropImageView.setImageUriAsync(selectedImageUri);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crop:
                progressBar.setVisibility(View.VISIBLE);
                Bitmap bitmap = cropImageView.getCroppedImage();
                File path = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                File file = saveBitmap(bitmap, path.getPath());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("ImageURI", file.getPath().toString());
                setResult(111, returnIntent);
                progressBar.setVisibility(View.GONE);
                finish();
                break;
        }
    }

    private File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(222, returnIntent);
        finish();
    }
}
