package com.example.sudoku.view;

import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sudoku.R;
import com.example.sudoku.view.custom.SudokuBoardView;
import com.example.sudoku.viewmodel.PlaySudokuViewModel;

public class SudokuActivity extends AppCompatActivity implements SudokuBoardView.OnTouchListener {

    private PlaySudokuViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_activity);

        SudokuBoardView sudokuBoardView = findViewById(R.id.sudokuBoardView);
        sudokuBoardView.registerListener(this);

        viewModel = new ViewModelProvider(this).get(PlaySudokuViewModel.class);
        viewModel.sudokuGame.getSelectedCellLiveData().observe(this, new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(Pair<Integer, Integer> cell) {
                updateSelectedCellUI(cell);
            }
        });
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
