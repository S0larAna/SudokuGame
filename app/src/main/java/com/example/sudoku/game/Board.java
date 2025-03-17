package com.example.sudoku.game;

import java.util.List;

public class Board {
    private int size;
    private List<Cell> cells;

    public Board(int size, List<Cell> cells){
        this.size = size;
        this.cells = cells;
    }

    public List<Cell> getCells(){
        return cells;
    }

    public Cell getCell(int row, int col){
        return cells.get(row * size + col);
    }
}
