package com.example.do_an_qlnv_hutech.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;
import com.example.do_an_qlnv_hutech.model.LichGV;
import com.example.do_an_qlnv_hutech.model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NhapGv extends AppCompatActivity {
    EditText etHoTenGV, etSoDienThoai,etMonHoc,etLopHoc,etPhongDay;
    RadioButton rbNam,rbNu;
    Button btnLuu,btnShowall;
    Spinner spthu, spcahoc ,spmonhoc;
    ArrayList<String> arrayListThu, arrayListCaHoc, arrayListMonHoc;
    Connection conn; // Kết nối cơ sở dữ liệu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_gv);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Quản lý Giảng viên");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Kết nối cơ sở dữ liệu
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();

        //ánh xạ spinner
        spmonhoc = findViewById(R.id.spinnermonhoc);
        spthu = findViewById(R.id.spinnerThu);
        spcahoc = findViewById(R.id.spinnercahoc);

        // Khởi tạo ArrayLists cho Thứ và Ca học
        arrayListThu = new ArrayList<>();
        arrayListCaHoc = new ArrayList<>();
        arrayListMonHoc = new ArrayList<>();

        // Thiết lập spinner cho Thứ
        for (int i = 2; i <= 7; i++) {
            arrayListThu.add("Thứ " + i);
        }

        // Nếu bạn sử dụng ArrayAdapter thay vì CustomAdapterNV
        ArrayAdapter<String> adapterThu = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListThu);
        adapterThu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spthu.setAdapter(adapterThu);

        // Thiết lập spinner cho Ca học
        arrayListCaHoc.add("Ca 1 (tiết 1 , 2 , 3)");
        arrayListCaHoc.add("Ca 2 (tiết 4 , 5 , 6)");
        arrayListCaHoc.add("Ca 3 (tiết 7 , 8 , 9)");
        arrayListCaHoc.add("Ca 4 (tiết 10 , 11 , 12)");
        arrayListCaHoc.add("Ca 4 (tiết 13 , 14 , 15)");

        ArrayAdapter<String> adapterCaHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListCaHoc);
        adapterCaHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcahoc.setAdapter(adapterCaHoc);

        //load môn học ra spinner
        showMonhoc();

        // ánh xạ
        etHoTenGV = findViewById(R.id.etHoTenGV);
        etSoDienThoai =findViewById(R.id.etSoDienThoai);
        etLopHoc = findViewById(R.id.etLopHoc);
        etPhongDay = findViewById(R.id.etPhongDay);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        btnLuu = findViewById(R.id.btnLuu);
        btnShowall = findViewById(R.id.btnShowall);

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

        // nhận ve thong tin giang vien
        NhanVien nv = (NhanVien) getIntent().getSerializableExtra("Object");
        if (nv != null) {
            String hoten = nv.getHovaten();
            String gt = nv.getGioitinh();
            String phone = nv.getPhone();
            etHoTenGV.setText(hoten);
            etSoDienThoai.setText(phone);
            // Gán giá trị giới tính
            if ("Nam".equalsIgnoreCase(gt)) {
                rbNam.setChecked(true);
            } else if ("Nữ".equalsIgnoreCase(gt)) {
                rbNu.setChecked(true);
            }
            Log.e("DATA1", "manv: " +nv.getManv());
        } else {
            Log.e("DATA_ERROR", "Không nhận được đối tượng NhanVien");
        }


        //nút lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hovaten = etHoTenGV.getText().toString();
                String sodienthoai = etSoDienThoai.getText().toString();
                String lop = etLopHoc.getText().toString();
                String thu = spthu.getSelectedItem().toString();
                String CaHoc = spcahoc.getSelectedItem().toString();
                String phong = etPhongDay.getText().toString();
                String gt = "Nam";

                if (rbNu.isChecked()) {
                    gt = "Nữ";
                }

                // Lấy tên môn học đã chọn
                String monHocDaChon = spmonhoc.getSelectedItem().toString();

                if (hovaten.isEmpty() || sodienthoai.isEmpty() || lop.isEmpty()) {
                    Toast.makeText(NhapGv.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Lấy id của môn học từ tên môn học trong spinner
                        String getidmonhoc = "SELECT id FROM tb_MONHOC WHERE monhoc = ?";
                        PreparedStatement getIdStatement = conn.prepareStatement(getidmonhoc);
                        getIdStatement.setString(1, monHocDaChon);
                        ResultSet rs = getIdStatement.executeQuery();

                        if (rs.next()) {
                            int idMonHoc = rs.getInt("id");

                            // Lấy id nhân viên từ tên nhân viên (hovaten)
                            String getidNhanVien = "SELECT MANV FROM tb_NHANVIEN WHERE HOTEN = ?";
                            PreparedStatement getNhanVienStatement = conn.prepareStatement(getidNhanVien);
                            getNhanVienStatement.setString(1, hovaten);
                            ResultSet rsNhanVien = getNhanVienStatement.executeQuery();

                            if (rsNhanVien.next()) {
                                int idNhanVien = rsNhanVien.getInt("MANV");

                                // Thêm dữ liệu vào bảng Lịch Dạy
                                String addquery = "INSERT INTO LichDay (id_nhanvien, id_monhoc, thu, cahoc, Lop, phone, phong_hoc) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement st = conn.prepareStatement(addquery);
                                st.setInt(1, idNhanVien); // Gán id_nhanvien vào khóa ngoại
                                st.setInt(2, idMonHoc);   // Gán id_monhoc vào khóa ngoại
                                st.setString(3, thu);
                                st.setString(4, CaHoc);
                                st.setString(5, lop);
                                st.setString(6, sodienthoai);
                                st.setString(7,phong);

                                int ketqua = st.executeUpdate();
                                if (ketqua > 0) {
                                    Toast.makeText(NhapGv.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NhapGv.this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NhapGv.this, "Không tìm thấy nhân viên", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NhapGv.this, "Không tìm thấy môn học", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // In lỗi ra logcat
                        Toast.makeText(NhapGv.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnShowall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = etHoTenGV.getText().toString();
                String phone = etSoDienThoai.getText().toString();
                if(!hoten.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), XuatLich.class);
                    // Truyền dữ liệu vào Intent
                    intent.putExtra("HOTEN", hoten);
                    intent.putExtra("DIENTHOAI", phone);
                    // Gọi XuatLich và đợi kết quả trả lại
                    startActivityForResult(intent, 1);
                    Log.d("XuatLich1", "Tên giáo viên nhận từ Intent: " + hoten + ", phone: " + phone);
                } else {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập tên giáo viên", Toast.LENGTH_SHORT).show();
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
                Log.d("DATA_MONHOC", "môn học: " + monhoc );
                arrayListMonHoc.add(monhoc);

            }
            // Thiết lập Adapter cho Spinner MonHoc
            ArrayAdapter<String> adapterMonHoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListMonHoc);
            adapterMonHoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spmonhoc.setAdapter(adapterMonHoc);

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lấy dữ liệu môn học", Toast.LENGTH_SHORT).show();
        }
    }

}
