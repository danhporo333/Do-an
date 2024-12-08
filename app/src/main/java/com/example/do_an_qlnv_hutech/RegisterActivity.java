package com.example.do_an_qlnv_hutech;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;

public class RegisterActivity extends AppCompatActivity {
    EditText edtdangky, edtpassword, conformpassword;
    Button btnregister; // Nút đăng ký
    Connection conn; // Kết nối cơ sở dữ liệu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        
    }
}