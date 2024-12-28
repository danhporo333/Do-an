package com.example.do_an_qlnv_hutech;

import static android.system.Os.connect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an_qlnv_hutech.admin.AdminMain;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.user.UserMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText edttendangnhap, edtpassword; // Tên biến khớp với layout
    TextView signup; // Biến để hiển thị thông báo
    Button btnLogin; // Nút đăng nhập
    Connection conn; // Kết nối cơ sở dữ liệu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        // Ánh xạ các view
        edttendangnhap = findViewById(R.id.edtdangky);
        edtpassword = findViewById(R.id.edtpassword);
        btnLogin = findViewById(R.id.btnregister);
        signup = findViewById(R.id.signup);

        // Xử lý sự kiện nhấn nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edttendangnhap.getText().toString();
                String password = edtpassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                try {
                    String query = "SELECT id_nhanvien, id_role FROM login WHERE username = ? AND password = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int idNhanVien = rs.getInt("id_nhanvien");
                        int idRole = rs.getInt("id_role");
                        Log.d("DEBUG_LOGIN", "id_nhanvien: " + idNhanVien + ", id_role: " + idRole);
                        Intent intent;
                        // Điều hướng dựa trên vai trò
                        if (idRole == 1) { // 1 = Admin
                            intent = new Intent(LoginActivity.this, AdminMain.class);
                        } else if (idRole == 2) { // 2 = User
                            intent = new Intent(LoginActivity.this, UserMain.class);
                        } else {
                            Toast.makeText(LoginActivity.this, "Vai trò không xác định", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Chuyển tới màn hình chính
                        intent.putExtra("id_nhanvien", idNhanVien);
                        intent.putExtra("id_role", idRole);
                        Log.d("DEBUG_LOGIN", "Dữ liệu đã được thêm vào Intent");
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
