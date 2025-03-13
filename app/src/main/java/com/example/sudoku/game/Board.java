package com.example.sudoku.game;

import java.util.List;

public class Board {
    private int size;
    private List<Cell> cells;

    public Cell getCell(int row, int col){
        return cells.get(row * size + col);
    }
}
