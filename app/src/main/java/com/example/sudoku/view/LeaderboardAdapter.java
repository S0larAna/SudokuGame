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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            holder.bind(cursor, position);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPosition;
        private final TextView tvUsername;

        ViewHolder(View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            tvUsername = itemView.findViewById(R.id.tv_username);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && cursor.moveToPosition(pos) && cursor != null) {
                    @SuppressLint("Range") String username = cursor.getString(
                            cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)
                    );
                    showPlayerDetails(username);
                }
            });
        }

        void bind(Cursor cursor, int position) {
            tvPosition.setText(String.valueOf(position + 1));

            int usernameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME);
            if (usernameIndex != -1) {
                tvUsername.setText(cursor.getString(usernameIndex));
            }
        }

        private void showPlayerDetails(String username) {
            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();

            PlayerDetailsFragment fragment = PlayerDetailsFragment.newInstance(username);

            fm.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}



