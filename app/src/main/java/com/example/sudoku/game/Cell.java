package com.example.sudoku.game;
// фрагменты добавить лидерборд - открывать окно по нажатию на лидерборд
public class Cell {
    public int row;
    public int col;
    private int value;

    public Cell(int row, int col, int value){
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
