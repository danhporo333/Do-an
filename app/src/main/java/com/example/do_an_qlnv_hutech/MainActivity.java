package com.example.do_an_qlnv_hutech;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an_qlnv_hutech.database.ConnectionDB;

import java.sql.Connection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    Connection con; // Thay ConnectionDB bằng Connection
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kết nối đến cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        con = connectionDB.Conn();
        connect();
    }

    public void connect() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                if (con == null) {
                    str = "Lỗi kết nối";
                } else {
                    str = "Kết nối thành công";
                }
            } catch (Exception e) {
                str = "Lỗi kết nối: " + e.getMessage();
            }

            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                TextView txtList = findViewById(R.id.txtmsg);
                txtList.setText(str);
            });
        });
    }
}