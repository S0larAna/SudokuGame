package com.example.sudoku.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudoku.R;
import com.example.sudoku.dbHelper.DatabaseHelper;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private Cursor cursor;
    private final Context context;

    public LeaderboardAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_player_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }
        holder.bind(cursor, position);
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPosition;
        private final TextView tvUsername;
        private final TextView tvCountry;
        private final TextView tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvCountry = itemView.findViewById(R.id.tv_country);
            tvTime = itemView.findViewById(R.id.tv_time);

            if (tvPosition == null || tvUsername == null || tvCountry == null || tvTime == null) {
                throw new IllegalStateException("TextView not found in item layout");
            }
        }

        @SuppressLint("Range")
        void bind(Cursor cursor, int position) {
            int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            int countryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_COUNTRY);
            int timeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);

            if (usernameIndex == -1 || countryIndex == -1 || timeIndex == -1) {
                return;
            }

            tvPosition.setText(String.valueOf(position + 1));
            tvUsername.setText(cursor.getString(usernameIndex));
            tvCountry.setText(cursor.getString(countryIndex));
            tvTime.setText(cursor.getString(timeIndex));
        }
    }
}


