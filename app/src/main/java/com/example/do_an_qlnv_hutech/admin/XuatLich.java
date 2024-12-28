package com.example.do_an_qlnv_hutech.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.AdapterLich;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.model.LichGV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class XuatLich extends AppCompatActivity {
    ListView listViewlich;
    ArrayList<LichGV> arrlich;
    AdapterLich adapterLich;
    Connection conn; // Kết nối cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xuat_lich);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách Lịch giảng dạy");
        setSupportActionBar(toolbar);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        //ánh xạ
        listViewlich = findViewById(R.id.listViewlich);
        arrlich = new ArrayList<>();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String hovaten = intent.getStringExtra("HOTEN");
        Log.d("XuatLich", "Tên giáo viên nhận từ Intent: " + hovaten);

        if (hovaten != null && !hovaten.isEmpty()) {
            showLichDay(hovaten);
        }
    }

    private void showLichDay(String hovaten) {
        try {
            // Tạo câu truy vấn lấy lịch dạy theo tên giáo viên
            String query = "SELECT LichDay.*, tb_MONHOC.monhoc, LichDay.Lop " +
                    "FROM LichDay " +
                    "JOIN tb_MONHOC ON LichDay.id_monhoc = tb_MONHOC.id " +
                    "JOIN tb_NHANVIEN ON LichDay.id_nhanvien = tb_NHANVIEN.MANV " +
                    "WHERE tb_NHANVIEN.HOTEN = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, hovaten);
            ResultSet rs = ps.executeQuery();

            // Xử lý kết quả và hiển thị lịch dạy

            while (rs.next()) {
                String thu = rs.getString("thu");
                String cahoc = rs.getString("cahoc");
                String monhoc = rs.getString("monhoc");
                String lop = rs.getString("Lop");
                Log.d("LichDay", "Môn học: " + monhoc + ", Lớp: " + lop + ", Thứ: " + thu + ", Ca học: " + cahoc);
                arrlich.add(new LichGV(thu, cahoc, lop, monhoc));
            }
            // Sử dụng AdapterLich để hiển thị dữ liệu
            adapterLich = new AdapterLich(this, arrlich);
            listViewlich.setAdapter(adapterLich);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(XuatLich.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}