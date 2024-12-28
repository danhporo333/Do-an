package com.example.do_an_qlnv_hutech.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddGV extends AppCompatActivity {
    Connection conn; // Kết nối cơ sở dữ liệu
    EditText etHoTenGV,etngaysinh,etSoDienThoai,etcancuoc,etdiachi;
    RadioButton rbNam,rbNu;
    Button btnLuu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_gv);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nhập thông tin giảng viên");
        setSupportActionBar(toolbar);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        //ánh xạ
        etHoTenGV = findViewById(R.id.etHoTenGV);
        etngaysinh =findViewById(R.id.etngaysinh);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        etcancuoc = findViewById(R.id.etcancuoc);
        etdiachi = findViewById(R.id.etdiachi);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        btnLuu = findViewById(R.id.btnLuu);

        rbNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbNu.setChecked(false);
            }
        });
        rbNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbNam.setChecked(false);

            }
        });

        //nút lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hovaten = etHoTenGV.getText().toString();
                String ngaysinh = etngaysinh.getText().toString();
                String sodienthoai = etSoDienThoai.getText().toString();
                String CCCD = etcancuoc.getText().toString();
                String diachi = etdiachi.getText().toString();
                String gt = "Nam";
                if (rbNu.isChecked()) {
                    gt = "Nữ";
                }
                if (hovaten.isEmpty() || ngaysinh.isEmpty() || sodienthoai.isEmpty() || CCCD.isEmpty() || diachi.isEmpty()) {
                    Toast.makeText(AddGV.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Chuyển đổi ngày sinh
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String datetime = null;
                    try {
                        Date date = inputFormat.parse(ngaysinh);
                        datetime = outputFormat.format(date);
                    } catch (ParseException e) {
                        Toast.makeText(AddGV.this, "Ngày sinh không đúng định dạng dd/MM/yyyy!", Toast.LENGTH_SHORT).show();
                        return; // Dừng thực thi nếu ngày không hợp lệ
                    }

                    // lưu dữ liệu vào database
                    try {
                        if (conn != null) {
                            String sql = "INSERT INTO tb_NHANVIEN (HOTEN, GIOITINH, NGAYSINH, DIENTHOAI, CCCD, DIACHI) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1, hovaten);
                            st.setString(2, gt);
                            st.setString(3, datetime); // Sử dụng datetime đã chuyển đổi
                            st.setString(4, sodienthoai);
                            st.setString(5, CCCD);
                            st.setString(6, diachi);
                            int rowInserted = st.executeUpdate();
                            if (rowInserted > 0) {
                                Toast.makeText(AddGV.this, "Thêm giảng viên thành công!", Toast.LENGTH_SHORT).show();
                                String manv = ""; //lấy mã tự động trong sql
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("MANV", manv);
                                resultIntent.putExtra("HOTEN", hovaten);
                                resultIntent.putExtra("GIOITINH", gt);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            } else {
                                Toast.makeText(AddGV.this, "Thêm giảng viên thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddGV.this, "Kết nối cơ sở dữ liệu thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddGV.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}