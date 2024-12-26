package com.example.do_an_qlnv_hutech.admin;

import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.database.CustomAdapter;
import com.example.do_an_qlnv_hutech.model.NhanVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Listview extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    ListView lvNhanVien;
    ArrayList<NhanVien> arrnv;
    CustomAdapter adapter;
    Connection conn; // Kết nối cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listview);

        //toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách lịch giảng dạy");
        setSupportActionBar(toolbar);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        if (conn == null) {
            Log.e("DB_CONNECTION", "Kết nối cơ sở dữ liệu thất bại");
        } else {
            Log.d("DB_CONNECTION", "Kết nối cơ sở dữ liệu thành công");
        }

        //ánh xạ
        lvNhanVien = findViewById(R.id.listView);
        arrnv = new ArrayList<>();
        if (conn != null) {
            showDataListView();
        } else {
            Log.e("DB_CONNECTION", "Kết nối cơ sở dữ liệu thất bại");
        }

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

    private void showDataListView(){
        if(conn != null){
            String query = "SELECT HOTEN, GIOITINH FROM tb_NHANVIEN;";
            try  {
                arrnv.clear();
                // Thực hiện truy vấn
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while (rs.next()){
                    count++;
                    String hovaten = rs.getString("hoten");
                    String gioitinh = rs.getString("GIOITINH");
                    Log.d("DATA_FETCHED", "Họ tên: " + hovaten + ", Giới tính: " + gioitinh);
                    arrnv.add(new NhanVien(hovaten,gioitinh));
                }
//                adapter.notifyDataSetChanged();
                // Chỉ gọi setAdapter khi có dữ liệu
                if (arrnv.size() > 0) {
                    adapter = new CustomAdapter(this, arrnv);
                    lvNhanVien.setAdapter(adapter);
                } else {
                    Log.d("DATA_ERROR", "Không có dữ liệu trong arrnv");
                }
                Log.d("DATA_COUNT", "Dữ liệu truy vấn được: " + count);
                // Đóng tài nguyên
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                Log.e("SQL_ERROR", "Lỗi đọc dữ liệu: " + e.getMessage(), e);
            }

        } else {
            Toast.makeText(this, "Không thể kết nối cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(Listview.this,"ok",Toast.LENGTH_SHORT).show();
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