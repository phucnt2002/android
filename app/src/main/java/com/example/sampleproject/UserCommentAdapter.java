package com.example.sampleproject;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleproject.Model.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserCommentViewHolder>{
    private List<User> mListUsers;

    public UserCommentAdapter(List<User> mListUsers) {
        this.mListUsers = mListUsers;
    }

    @NonNull
    @Override
    public UserCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new UserCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentViewHolder holder, int position) {
        User user = mListUsers.get(position);
        if(user ==null){
            return;
        }
        holder.email.setText("Email: "+user.email);
        holder.name.setText("Tên: "+user.name);
        holder.total.setText("Thời tiết tổng quan: "+user.totalWeather);
        holder.temp.setText("Nhiệt độ thực tế: "+user.temp);
        holder.wind.setText("Tốc độ gió thực tế: "+user.wind);
        holder.other.setText("Điều kiện thời tiết khác: "+user.ortherWeather);
        holder.description.setText("Mô tả: "+user.describe);
        holder.time.setText("Thời gian phản hồi: "+ formatLongToDate(user.time));
    }

    @Override
    public int getItemCount() {
        if(mListUsers!=null){
            return mListUsers.size();
        }
        return 0;
    }

    public class UserCommentViewHolder extends RecyclerView.ViewHolder {

        private TextView email;
        private TextView name;
        private TextView total;
        private TextView temp;
        private TextView wind;
        private TextView other;
        private TextView description;
        private TextView time;

        public UserCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
            name = itemView.findViewById(R.id.name);
            total = itemView.findViewById(R.id.total);
            temp = itemView.findViewById(R.id.temp);
            wind = itemView.findViewById(R.id.wind);
            other = itemView.findViewById(R.id.other);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
        }
    }

    @SuppressLint("NewApi")
    public static String formatLongToDate(long epochMilliUtc) {
        @SuppressLint({"NewApi", "LocalSuppress"}) Instant instant = Instant.ofEpochMilli(epochMilliUtc);
        @SuppressLint({"NewApi", "LocalSuppress"}) DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
