package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sampleproject.sql.DBManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavController navController;
    TextView textName;
    TextView textEmail;
    ImageView imgAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar calendar = Calendar.getInstance();
        DBManager.initializeInstance(this);


        drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.img_Menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        textName = navigationView.getHeaderView(0).findViewById(R.id.nameUser);
        textEmail = navigationView.getHeaderView(0).findViewById(R.id.emailUser);
        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.imageProfile);
        showInfoUser();
    }

    private void showInfoUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri uri = user.getPhotoUrl();

        if(name == null){
            textName.setText("Chưa có thông tin");
        }else{
            textName.setText(name);
        }
        textEmail.setText(email);
        Glide.with(this).load(uri).error(R.drawable.profile_picture).into(imgAvatar);
    }
}