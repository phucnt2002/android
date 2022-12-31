package com.example.sampleproject.sql.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter {
    public class UserViewHolder extends RecyclerView {

        public UserViewHolder(@NonNull Context context) {
            super(context);
        }

        public UserViewHolder(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public UserViewHolder(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }
}
