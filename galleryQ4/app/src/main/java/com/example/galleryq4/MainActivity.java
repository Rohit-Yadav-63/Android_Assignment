package com.example.galleryq4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int FOLDER_REQUEST = 2;

    Uri imageUri; // store image path

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnGallery = findViewById(R.id.btnGallery);

        checkPermissions();

        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openFolder());
    }

    //  Request permissions
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 100);
        }
    }

    //  Open camera
    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(getExternalFilesDir("Pictures"),
                "IMG_" + System.currentTimeMillis() + ".jpg");

        imageUri = FileProvider.getUriForFile(this,
                "com.example.photogallery.provider", file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    //  Open folder picker
    private void openFolder() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, FOLDER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == FOLDER_REQUEST && resultCode == RESULT_OK) {

            Uri folderUri = data.getData();

            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("folderUri", folderUri.toString());
            startActivity(intent);
        }
    }
}