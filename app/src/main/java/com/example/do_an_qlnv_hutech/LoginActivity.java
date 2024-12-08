package com.example.do_an_qlnv_hutech;

import static android.system.Os.connect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an_qlnv_hutech.database.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText edttendangnhap, edtpassword; // Tên biến khớp với layout
    TextView signup; // Biến để hiển thị thông báo
    Button btnLogin; // Nút đăng nhập
    Connection conn; // Kết nối cơ sở dữ liệu
    SharedPreferences settings; // Lưu trữ thông tin người dùng


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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(in);
            }
        });

        // Xử lý sự kiện nhấn nút Đăng nhập
        btnLogin.setOnClickListener(view -> {
            String username = edttendangnhap.getText().toString().trim();
            String password = edtpassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                checkLogin(username, password);
            }
        });
    }

    // Hàm kiểm tra đăng nhập
    public void checkLogin(String user, String pass) {
        String query = "SELECT * FROM Login WHERE username = ? AND password = ?";

        Log.d("DEBUG", "SQL Query: " + query); // Debug để kiểm tra truy vấn SQL
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user); // Gán giá trị cho tham số đầu tiên
            stmt.setString(2, pass); // Gán giá trị cho tham số thứ hai

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Log.d("DEBUG", "Login successful for user: " + user); // Ghi log khi đăng nhập thành công
                    Toast.makeText(this, "Đăng nhập thành công! Chào mừng " + user, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("DEBUG", "Login failed for user: " + user); // Ghi log khi đăng nhập thất bại
                    runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show());
                }
            }
        } catch (SQLException e) {
            Log.e("DEBUG", "SQL Error: " + e. getMessage(), e); // Ghi log lỗi SQL
            Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
