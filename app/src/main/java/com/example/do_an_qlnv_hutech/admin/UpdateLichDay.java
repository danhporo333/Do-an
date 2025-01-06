package com.example.do_an_qlnv_hutech.admin;

import static android.Manifest.permission_group.CAMERA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.AdapterLich;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.model.LichGV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateLichDay extends AppCompatActivity {

    Connection conn; // Kết nối cơ sở dữ liệu
    Spinner spthu, spcahoc ,spmonhoc;
    ArrayList<String> arrayListthu, arrayListcahoc, arrayListmonhoc;
    EditText etclass, etphongday;
    Button btnupdate;
    ArrayList<LichGV> arrlich;

    AdapterLich adapterLich;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_lich_day);

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chỉnh sửa lịch dạy");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // anh xa
        etclass = findViewById(R.id.etclass);
        etphongday = findViewById(R.id.etphongday);
        btnupdate = findViewById(R.id.btnupdate);

        //ánh xa spinner
        spthu = findViewById(R.id.spthu);
        spcahoc = findViewById(R.id.spCaHoc);
        spmonhoc =findViewById(R.id.spMonHoc);

        // Khởi tạo ArrayLists cho Thứ và Ca học
        arrayListthu = new ArrayList<>();
        arrayListcahoc = new ArrayList<>();
        arrayListmonhoc = new ArrayList<>();

        // Thiết lập spinner cho Thứ
        for (int i = 2; i <= 7; i++) {
            arrayListthu.add("Thứ " + i);
        }

        // Nếu bạn sử dụng ArrayAdapter thay vì CustomAdapterNV
        ArrayAdapter<String> adapterThu = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListthu);
        adapterThu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spthu.setAdapter(adapterThu);

        // Thiết lập spinner cho Ca học
        arrayListcahoc.add("Ca 1 (tiết 1 , 2 , 3)");
        arrayListcahoc.add("Ca 2 (tiết 4 , 5 , 6)");
        arrayListcahoc.add("Ca 3 (tiết 7 , 8 , 9)");
        arrayListcahoc.add("Ca 4 (tiết 10 , 11 , 12)");
        arrayListcahoc.add("Ca 4 (tiết 13 , 14 , 15)");

        ArrayAdapter<String> adapterCaHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListcahoc);
        adapterCaHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcahoc.setAdapter(adapterCaHoc);

        //load môn học ra spinner
        showMonhoc();

        // nhận ve lich day
        LichGV lich = (LichGV) getIntent().getSerializableExtra("ObjectLich");
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d("nhanid", "id  " + id);
        if (lich != null) {
            Log.d("UpdateLichDay", "ObjectLich nhận được: " + lich.getMonhoc());
            etclass.setText(lich.getLop());
            etphongday.setText(lich.getPhong_hoc());
            // Gán giá trị cho Spinner Thứ
            int thuPosition = arrayListthu.indexOf(lich.getThu());
            if (thuPosition >= 0) {
                spthu.setSelection(thuPosition);
            }

            // Gán giá trị cho Spinner Ca học
            int caHocPosition = arrayListcahoc.indexOf(lich.getCahoc());
            if (caHocPosition >= 0) {
                spcahoc.setSelection(caHocPosition);
            }

            // Gán giá trị cho Spinner Môn học
            int monHocPosition = arrayListmonhoc.indexOf(lich.getMonhoc());
            if (monHocPosition >= 0) {
                spmonhoc.setSelection(monHocPosition);
            }
        } else {
            Log.e("DATA_ERROR", "Không nhận được đối tượng NhanVien");
        }

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ giao diện
                String lop = etclass.getText().toString();
                String phong = etphongday.getText().toString();
                String thu = spthu.getSelectedItem().toString();
                String cahoc = spcahoc.getSelectedItem().toString();
                String monhoc = spmonhoc.getSelectedItem().toString();


                // Tạo câu lệnh SQL để lấy id_monhoc từ tên môn học
                String sqlMonHoc = "SELECT id FROM tb_MONHOC WHERE monhoc = ?";
                try {
                    PreparedStatement psMonHoc = conn.prepareStatement(sqlMonHoc);
                    psMonHoc.setString(1, monhoc);
                    ResultSet rs = psMonHoc.executeQuery();

                    if (rs.next()) {
                        int monhocId = rs.getInt("id"); // Lấy id_monhoc từ kết quả truy vấn

                        // Cập nhật lịch dạy
                        String sql = "UPDATE LichDay SET thu = ?, cahoc = ?, Lop = ?, phong_hoc = ?, id_monhoc = ? WHERE id = ?";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, thu);
                        ps.setString(2, cahoc);
                        ps.setString(3, lop);
                        ps.setString(4, phong);
                        ps.setInt(5, monhocId); // Sử dụng id_monhoc thay vì tên môn học
                        ps.setString(6, id); // Sử dụng id là String để cập nhật đúng bản ghi

                        int result = ps.executeUpdate();
                        if (result > 0) {
                            Toast.makeText(UpdateLichDay.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                            finish(); // Đóng Activity sau khi cập nhật
                        } else {
                            Toast.makeText(UpdateLichDay.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UpdateLichDay.this, "Không tìm thấy môn học", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateLichDay.this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMonhoc() {
        try {
            String query = "SELECT monhoc FROM tb_MONHOC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Duyệt qua kết quả và thêm vào ArrayList
            while (rs.next()) {
                String monhoc = rs.getString("monhoc");
                Log.d("DATA_MONHOC", "môn học: " + monhoc);
                arrayListmonhoc.add(monhoc);

            }
            // Thiết lập Adapter cho Spinner MonHoc
            ArrayAdapter<String> adapterMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListmonhoc);
            adapterMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spmonhoc.setAdapter(adapterMonHoc);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lấy dữ liệu môn học", Toast.LENGTH_SHORT).show();
        }
    }


}