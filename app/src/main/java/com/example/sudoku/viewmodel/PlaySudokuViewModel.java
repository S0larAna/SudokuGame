package com.example.sudoku.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sudoku.game.SudokuGame;

public class PlaySudokuViewModel extends ViewModel {
    public SudokuGame sudokuGame = new SudokuGame();

}
