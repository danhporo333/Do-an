package com.example.do_an_qlnv_hutech.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.admin.Listview;
import com.example.do_an_qlnv_hutech.database.AdapterLich;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.model.LichGV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class XuatLichUser extends AppCompatActivity {
    ArrayList<LichGV> arrlich;
    AdapterLich adapterLich;
    Connection conn; // Kết nối cơ sở dữ liệu
    ListView lvlichUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xuat_lich_user);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbaruser);
        toolbar.setTitle("Thời khóa biểu");
        setSupportActionBar(toolbar);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        lvlichUser = findViewById(R.id.lvlichUser);
        arrlich = new ArrayList<>();

        Intent intent = getIntent();
        int idNhanVien = intent.getIntExtra("id_nhanvien", -1); // Kiểm tra giá trị id_nhanvien
        int idRole = intent.getIntExtra("id_role", -1); // Kiểm tra giá trị id_role

        Log.d("DEBUG_XUATLICH", "ID Nhân viên: " + idNhanVien); // Log id_nhanvien
        Log.d("DEBUG_XUATLICH", "ID Vai trò: " + idRole); // Log id_role

        try {
            // Truy vấn lịch dạy của nhân viên
            String query = "SELECT LichDay.*, tb_MONHOC.monhoc " +
                    "FROM LichDay " +
                    "JOIN tb_MONHOC ON LichDay.id_monhoc = tb_MONHOC.id " +
                    "WHERE LichDay.id_nhanvien = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idNhanVien);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String thu = rs.getString("thu");
                String cahoc = rs.getString("cahoc");
                String monhoc = rs.getString("monhoc");
                String lop = rs.getString("Lop");
                Log.d("LichDayuser", "Môn học: " + monhoc + ", Lớp: " + lop + ", Thứ: " + thu + ", Ca học: " + cahoc);
                arrlich.add(new LichGV(thu, cahoc, lop, monhoc));
            }

            // Tạo adapter và gắn vào ListView
            adapterLich = new AdapterLich(this, arrlich);
            lvlichUser.setAdapter(adapterLich);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}