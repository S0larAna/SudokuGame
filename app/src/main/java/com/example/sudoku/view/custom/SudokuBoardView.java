package com.example.sudoku.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;


public class SudokuBoardView extends View {
    private final Paint thickLinePaint;
    private final Paint thinLinePaint;
    private int sqrtSize = 3;
    private int size = 9;
    private float cellSizePixels = 0F;
    private int selectedRow = 0;
    private int selecetedCol = 0;
    private Paint selectedCellPaint;

    private Paint conflictingCellPaint;

    private SudokuBoardView.OnTouchListener listener = null;

    public SudokuBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        thickLinePaint = new Paint();
        thickLinePaint.setStyle(Paint.Style.STROKE);
        thickLinePaint.setColor(Color.BLACK);
        thickLinePaint.setStrokeWidth(4F);

        thinLinePaint = new Paint();
        thinLinePaint.setStyle(Paint.Style.STROKE);
        thinLinePaint.setColor(Color.BLACK);
        thinLinePaint.setStrokeWidth(2F);

        selectedCellPaint = new Paint();
        selectedCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        selectedCellPaint.setColor(Color.parseColor("#6ead3a"));
        selectedCellPaint.setStrokeWidth(2F);

        conflictingCellPaint = new Paint();
        conflictingCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        conflictingCellPaint.setColor(Color.parseColor("#efedef"));
        conflictingCellPaint.setStrokeWidth(2F);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas){
        super.onDraw(canvas);
        cellSizePixels = ((float) getWidth() / size);
        canvas.drawRect(0F, 0F, getWidth(), getHeight(), thickLinePaint);
        fillCells(canvas);
        drawLines(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        var sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixels, sizePixels);
    }

    private void drawLines(Canvas canvas){
        canvas.drawRect(0F, 0F, (float)getWidth(), (float)getHeight(), thickLinePaint);

        for (int i = 1; i<size; i++){
            Paint paintToUse;
            if (i%sqrtSize==0) paintToUse = thickLinePaint;
            else paintToUse = thinLinePaint;

            canvas.drawLine(i* cellSizePixels, 0F, i*cellSizePixels, (float)getHeight(), paintToUse);
            canvas.drawLine(
                    0F,
                    i*cellSizePixels,
                    (float)getWidth(),
                    i*cellSizePixels,
            paintToUse
            );
        }
    }

    // TODO: make conflicting cells fill toggleable for medium and high difficulties
    private void fillCells(Canvas canvas){
        if (selectedRow == -1 || selecetedCol ==-1) return;
        for (int row=0; row<size; row++){
            for (int col=0; col<size; col++){
                if (row == selectedRow && col == selecetedCol){
                    fillCell(canvas, row, col, selectedCellPaint);
                }
                else if (row==selectedRow || col == selecetedCol){
                    fillCell(canvas, row, col, conflictingCellPaint);
                }
                else if (row / sqrtSize == selectedRow / sqrtSize && col / sqrtSize == selecetedCol / sqrtSize){
                    fillCell(canvas, row, col, conflictingCellPaint);
                }
            }
        }
    }

    private void fillCell(Canvas canvas, int row, int col, Paint paint){
        canvas.drawRect(col*cellSizePixels, row*cellSizePixels, (col+1)*cellSizePixels, (row+1)*cellSizePixels, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            handleTouchEvent(event.getX(), event.getY());
            return true;
        }
        else return false;
    }

    private void handleTouchEvent(float x, float y) {
        int possibleSelectedRow = (int)(y/cellSizePixels);
        int possibleSelectedCol = (int)(x/cellSizePixels);
        listener.onCellTouched(possibleSelectedRow, possibleSelectedCol);
    }

    public void updateSelectedCellUI(int row, int col){
        selecetedCol = col;
        selectedRow = row;
        invalidate();
    }

    public void registerListener(SudokuBoardView.OnTouchListener listener){
        this.listener = listener;
    }

    public interface OnTouchListener {
        public default void onCellTouched(int row, int col) {
        }
    }
}
