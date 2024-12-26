package com.example.do_an_qlnv_hutech;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an_qlnv_hutech.database.ConnectionDB;

import java.sql.Connection;

public class Nav_Header extends AppCompatActivity {
    TextView nameheader, email;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.nav_header);

        //kết nối database
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        //ánh xạ
        nameheader = findViewById(R.id.namehd);
        email = findViewById(R.id.email);


    }
}