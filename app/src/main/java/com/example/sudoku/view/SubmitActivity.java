package com.example.sudoku.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku.R;
import com.example.sudoku.dbHelper.DatabaseHelper;

public class SubmitActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        String time = getIntent().getStringExtra("TIME_SPENT");
        TextView tvTime = findViewById(R.id.tv_time);
        tvTime.setText("Time: " + time);

        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            String username = ((EditText)findViewById(R.id.et_username)).getText().toString();
            String country = ((EditText)findViewById(R.id.et_country)).getText().toString();

            dbHelper = new DatabaseHelper(this);
            dbHelper.addScore(username, country, time);

            finish(); // Returns to Sudoku activity
        });
    }
}

