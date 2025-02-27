package com.example.do_an_qlnv_hutech.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.admin.XuatLich;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;

public class UserMain extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    Connection conn;
    LinearLayout book2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usermain);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //kết nối database
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        Intent intent = getIntent();
        int idNhanVien = intent.getIntExtra("id_nhanvien", -1);  // Lấy id_nhanvien
        int idRole = intent.getIntExtra("id_role", -1);  // Lấy id_role

        Log.d("DEBUG_USERMAIN", "ID Nhân viên: " + idNhanVien);  // Log giá trị để kiểm tra
        Log.d("DEBUG_USERMAIN", "ID Vai trò: " + idRole);  // Log giá trị để kiểm tra

        //ánh xạ
        book2 = findViewById(R.id.book2);
        book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), XuatLichUser.class);
                in.putExtra("id_nhanvien", idNhanVien);
                in.putExtra("id_role", idRole);
                startActivity(in);
            }
        });

        // Thiết lập DrawerLayout và NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Thêm nút mở Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        //bắt sự kiện trong bottomsheet
        LinearLayout layoutVideo = dialog.findViewById(R.id.layoutVideo);
        ImageView cancelButton  = dialog.findViewById(R.id.cancelButton);

        layoutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(UserMain.this,"ok",Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

}