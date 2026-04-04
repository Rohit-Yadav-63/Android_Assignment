package com.example.galleryq4;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.util.Date;

public class ImageDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView details;
    Button btnDelete;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imageView = findViewById(R.id.imageView);
        details = findViewById(R.id.details);
        btnDelete = findViewById(R.id.btnDelete);

        imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));

        imageView.setImageURI(imageUri);

        showDetails();

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    //  Show image info
    private void showDetails() {

        File file = new File(imageUri.getPath());

        String info =
                "Name: " + file.getName() +
                        "\nPath: " + file.getPath() +
                        "\nSize: " + file.length() + " bytes" +
                        "\nDate: " + new Date(file.lastModified());

        details.setText(info);
    }

    //  Confirm delete
    private void confirmDelete() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Image")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> deleteImage())
                .setNegativeButton("No", null)
                .show();
    }

    //  Delete image
    private void deleteImage() {

        DocumentFile file = DocumentFile.fromSingleUri(this, imageUri);

        if (file != null && file.delete()) {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            finish(); // return to gallery
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }
}