package com.example.sudoku.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sudoku.R;
import com.example.sudoku.game.Cell;
import com.example.sudoku.view.custom.SudokuBoardView;
import com.example.sudoku.viewmodel.PlaySudokuViewModel;
import android.os.SystemClock;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuActivity extends AppCompatActivity implements SudokuBoardView.OnTouchListener {

    private PlaySudokuViewModel viewModel;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_activity);

        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        SudokuBoardView sudokuBoardView = findViewById(R.id.sudokuBoardView);
        sudokuBoardView.registerListener(this);

        viewModel = new ViewModelProvider(this).get(PlaySudokuViewModel.class);
        viewModel.sudokuGame.getSelectedCellLiveData().observe(this, new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(Pair<Integer, Integer> cell) {
                updateSelectedCellUI(cell);
            }
        });

        viewModel.sudokuGame.getCellsLiveData().observe(this, new Observer<List<Cell>>() {
            @Override
            public void onChanged(List<Cell> cells) {
                updateCells(cells);
            }
        });

        List<Button> buttons = Arrays.asList(
                findViewById(R.id.oneButton),
                findViewById(R.id.twoButton),
                findViewById(R.id.threeButton),
                findViewById(R.id.fourButton),
                findViewById(R.id.fiveButton),
                findViewById(R.id.sixButton),
                findViewById(R.id.sevenButton),
                findViewById(R.id.eightButton),
                findViewById(R.id.nineButton),
                findViewById(R.id.deleteButton)
        );

        for (int i = 0; i < buttons.size()-1; i++) {
            final int index = i;
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.sudokuGame.handleInput(index + 1);
                }
            });
        }
        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.sudokuGame.delete();
            }
        });

        Button doneButton = findViewById(R.id.checkButton);
        doneButton.setOnClickListener(v -> {
            if (viewModel.sudokuGame.checkSolution()) {
                Intent intent = new Intent(SudokuActivity.this, SubmitActivity.class);
                intent.putExtra("TIME_SPENT", chronometer.getText().toString());
                startActivityForResult(intent, 1);
            } else {
                showErrorDialog();
            }
        });

        Button leaderboardButton = findViewById(R.id.leaders);
        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(SudokuActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Неправильное решение!")
                .setMessage("Пожалуйста, попробуйте снова")
                .setPositiveButton("OK", null)
                .show();
    }

    public void updateCells(List<Cell> cells){
        if (cells != null){
            SudokuBoardView sudokuBoardView = findViewById(R.id.sudokuBoardView);
            sudokuBoardView.updateCells(cells);
        }
    }

    private void updateSelectedCellUI(Pair<Integer, Integer> cell){
        if(cell!=null){
            SudokuBoardView sudokuBoardView = findViewById(R.id.sudokuBoardView);
            sudokuBoardView.updateSelectedCellUI(cell.first, cell.second);
        }
    }

    @Override
    public void onCellTouched(int row, int col) {
        viewModel.sudokuGame.updateSelectedCell(row, col);
    }
}
