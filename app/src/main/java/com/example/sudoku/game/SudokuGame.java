package com.example.sudoku.game;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class SudokuGame {

    private MutableLiveData<Pair<Integer, Integer>> selectedCellLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Cell>> cellsLiveData = new MutableLiveData<>();

    private int selectedRow = -1;
    private int selectedCol = -1;
    private Board board;

    public SudokuGame() {
        SudokuConfig sudokuConfig = new SudokuConfig();
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 9 * 9; i++) {
            cells.add(new Cell(i / 9, i % 9, sudokuConfig.INITIAL_CONFIG[i / 9][i % 9]));
        }
        board = new Board(9, cells);

        selectedCellLiveData.postValue(new Pair<>(selectedRow, selectedCol));
        cellsLiveData.postValue(board.getCells());
    }

    public MutableLiveData<Pair<Integer, Integer>> getSelectedCellLiveData() {
        return selectedCellLiveData;
    }

    public MutableLiveData<List<Cell>> getCellsLiveData() {
        return cellsLiveData;
    }

    public void handleInput(int number) {
        if (selectedRow == -1 || selectedCol == -1) return;

        board.getCell(selectedRow, selectedCol).setValue(number);
        cellsLiveData.postValue(board.getCells());
    }

    public void delete() {
        if (selectedRow == -1 || selectedCol == -1) return;
        board.getCell(selectedRow, selectedCol).setValue(0);
        cellsLiveData.postValue(board.getCells());
    }

    public void updateSelectedCell(int row, int col) {
        selectedCol = col;
        selectedRow = row;
        selectedCellLiveData.postValue(new Pair<>(row, col));
    }

    public boolean checkSolution() {
        SudokuConfig sudokuConfig = new SudokuConfig();
        boolean flag = true;
        List<Cell> cells = board.getCells();
        for (int i = 0; i < 9 * 9; i++) {
            if (cells.get(i).getValue() != sudokuConfig.INITIAL_CONFIG[i / 9][i % 9]) flag = false;
        }
        return flag;
    }
}

