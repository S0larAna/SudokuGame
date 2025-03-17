package com.example.sudoku.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.sudoku.game.Cell;

import java.util.List;

public class SudokuBoardView extends View {

    private int sqrtSize = 3;
    private int size = 9;

    private float cellSizePixels = 0F;

    private int selectedRow = 0;
    private int selectedCol = 0;

    private OnTouchListener listener;

    private List<Cell> cells;

    private Paint thickLinePaint;
    private Paint thinLinePaint;
    private Paint selectedCellPaint;
    private Paint conflictingCellPaint;
    private Paint textPaint;

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

        conflictingCellPaint = new Paint();
        conflictingCellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        conflictingCellPaint.setColor(Color.parseColor("#efedef"));

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50F);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixels, sizePixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellSizePixels = (float) (getWidth() / size);
        fillCells(canvas);
        drawLines(canvas);
        drawText(canvas);
    }

    private void fillCells(Canvas canvas) {
        if (cells != null) {
            for (Cell cell : cells) {
                int r = cell.row;
                int c = cell.col;

                if (r == selectedRow && c == selectedCol) {
                    fillCell(canvas, r, c, selectedCellPaint);
                } else if (r == selectedRow || c == selectedCol) {
                    fillCell(canvas, r, c, conflictingCellPaint);
                } else if (r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize) {
                    fillCell(canvas, r, c, conflictingCellPaint);
                }
            }
        }
    }

    private void fillCell(Canvas canvas, int r, int c, Paint paint) {
        canvas.drawRect(c * cellSizePixels, r * cellSizePixels, (c + 1) * cellSizePixels, (r + 1) * cellSizePixels, paint);
    }

    private void drawLines(Canvas canvas) {
        canvas.drawRect(0F, 0F, (float) getWidth(), (float) getHeight(), thickLinePaint);

        for (int i = 1; i < size; i++) {
            Paint paintToUse = (i % sqrtSize == 0) ? thickLinePaint : thinLinePaint;

            canvas.drawLine(
                    i * cellSizePixels,
                    0F,
                    i * cellSizePixels,
                    (float) getHeight(),
                    paintToUse
            );

            canvas.drawLine(
                    0F,
                    i * cellSizePixels,
                    (float) getWidth(),
                    i * cellSizePixels,
                    paintToUse
            );
        }
    }

    private void drawText(Canvas canvas) {
        if (cells != null) {
            for (Cell cell : cells) {
                int row = cell.row;
                int col = cell.col;
                String valueString = String.valueOf(cell.getValue());

                Rect textBounds = new Rect();
                textPaint.getTextBounds(valueString, 0, valueString.length(), textBounds);
                float textWidth = textPaint.measureText(valueString);
                float textHeight = textBounds.height();
                if (cell.getValue()!=0) {

                    canvas.drawText(valueString, (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                            (float) ((row * cellSizePixels) + cellSizePixels / 1.2 - textHeight / 2), textPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleTouchEvent(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    private void handleTouchEvent(float x, float y) {
        int possibleSelectedRow = (int) (y / cellSizePixels);
        int possibleSelectedCol = (int) (x / cellSizePixels);
        listener.onCellTouched(possibleSelectedRow, possibleSelectedCol);
    }

    public void updateSelectedCellUI(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        invalidate();
    }

    public void updateCells(List<Cell> cells) {
        this.cells = cells;
        invalidate();
    }

    public void registerListener(OnTouchListener listener) {
        this.listener = listener;
    }

    public interface OnTouchListener {
        void onCellTouched(int row, int col);
    }
}
