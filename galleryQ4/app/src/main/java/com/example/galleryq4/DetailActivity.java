package com.example.galleryq4;

import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.Date;
import com.example.galleryq4.R;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String path = getIntent().getStringExtra("path");
        File file = new File(path);

        ImageView img = findViewById(R.id.detailImage);
        TextView txt = findViewById(R.id.txtDetails);
        Button del = findViewById(R.id.btnDelete);

        img.setImageBitmap(BitmapFactory.decodeFile(path));

        txt.setText("Name: " + file.getName() +
                "\nSize: " + (file.length() / 1024) + " KB" +
                "\nDate: " + new Date(file.lastModified()));

        del.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Delete this photo forever?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (file.delete()) {
                            // THIS TELLS MAIN ACTIVITY TO REFRESH THE GRID
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}