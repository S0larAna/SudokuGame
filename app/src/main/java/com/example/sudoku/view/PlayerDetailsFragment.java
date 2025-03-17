package com.example.sudoku.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sudoku.R;
import com.example.sudoku.dbHelper.DatabaseHelper;

public class PlayerDetailsFragment extends Fragment {
    private static final String ARG_USERNAME = "username";

    public static PlayerDetailsFragment newInstance(String username) {
        PlayerDetailsFragment fragment = new PlayerDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_details, container, false);
    }

    @SuppressLint("Range")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String username = getArguments().getString(ARG_USERNAME);
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        Cursor cursor = dbHelper.getPlayerDetails(username);

        if (cursor.moveToFirst()) {
            TextView tvUsername = view.findViewById(R.id.tv_username);
            TextView tvCountry = view.findViewById(R.id.tv_country);
            TextView tvTime = view.findViewById(R.id.tv_time);
            if (cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)!=-1){
                tvUsername.setText(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME)));
                tvCountry.setText(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_COUNTRY)));
                tvTime.setText(cursor.getString(
                        cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME)));
            }
        }
        cursor.close();

        // Кнопка закрытия фрагмента
        Button closeButton = view.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });
    }
}


