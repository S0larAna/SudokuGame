package com.example.sudoku.game;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SudokuGame {

    private MutableLiveData<Pair<Integer, Integer>> selectedCellLiveData = new MutableLiveData<>();

    private int selectedRow = -1;
    private int selectedCol = -1;
    private Board board = new Board();

    public SudokuGame(){
        selectedCellLiveData.postValue(new Pair<>(selectedRow, selectedCol));
    }

    public MutableLiveData<Pair<Integer, Integer>> getSelectedCellLiveData(){
        return selectedCellLiveData;
    }

    public void updateSelectedCell(int row, int col){
        selectedCol = col;
        selectedRow = row;
        selectedCellLiveData.postValue(new Pair<>(row, col));
    }
}

