package com.example.do_an_qlnv_hutech.database;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.admin.NhapGv;
import com.example.do_an_qlnv_hutech.admin.UpdateLichDay;
import com.example.do_an_qlnv_hutech.admin.XuatLich;
import com.example.do_an_qlnv_hutech.model.LichGV;
import com.example.do_an_qlnv_hutech.model.NhanVien;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AdapterLich extends ArrayAdapter<LichGV> {
    private ArrayList<LichGV> arrlich;
    private final Activity context;
    private boolean isAdmin;  // Biến kiểm tra xem người dùng có phải là admin không
    Connection conn;


    public AdapterLich(Activity context, ArrayList<LichGV> arrlich, boolean isAdmin) {
        super(context, R.layout.list_time_table, arrlich);
        this.context = context;
        this.arrlich = arrlich  != null ? arrlich : new ArrayList<LichGV>();
        this.isAdmin = isAdmin; // Nhận giá trị isAdmin từ Activity
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();
    }



    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_time_table, null, true);

        LichGV lich = arrlich.get(position);
        Log.d("LichGV", "mon hoc : " + lich.getMonhoc() + ", lop: " + lich.getLop());

        // ánh xạ
        TextView txtthu = rowView.findViewById(R.id.tvThu);
        TextView txttiethoc = rowView.findViewById(R.id.tvTietValue);
        TextView txtMonhoc = rowView.findViewById(R.id.tvMonHocValue);
        TextView txtlop = rowView.findViewById(R.id.tvLopHocValue);
        TextView txtphong = rowView.findViewById(R.id.tvPhongHocValue);
        Button btnEdit = rowView.findViewById(R.id.btnEdit);  // Nút sửa

        // Xuất ra ListView
        txtthu.setText(lich.getThu());
        txttiethoc.setText(lich.getCahoc());
        txtMonhoc.setText(lich.getMonhoc());
        txtlop.setText(lich.getLop());
        txtphong.setText(lich.getPhong_hoc());



        // Hiển thị nút sửa nếu là admin
        Log.d("AdapterLich", "isAdmin: " + isAdmin);
        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);  // Hiển thị nút sửa
        } else {
            btnEdit.setVisibility(View.GONE);  // Ẩn nút sửa nếu là user
        }

        // Xử lý sự kiện khi nhấn nút sửa
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateLichDay.class);
                intent.putExtra("id", lich.getMalich());
                intent.putExtra("ObjectLich",lich);
                context.startActivity(intent);
                Log.d("EditButton", "Đang gửi ID: " + lich.getMalich());
            }
        });

        return rowView;
    }
}

