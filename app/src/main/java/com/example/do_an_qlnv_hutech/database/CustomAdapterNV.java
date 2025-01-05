package com.example.do_an_qlnv_hutech.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.admin.NhapGv;
import com.example.do_an_qlnv_hutech.model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomAdapterNV extends ArrayAdapter<NhanVien> {
    private ArrayList<NhanVien> arrnv;
    private final Activity context;
    Connection conn;
    public CustomAdapterNV(Activity context, ArrayList<NhanVien> arrnv) {
        super(context, R.layout.list_item, arrnv);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arrnv = arrnv;
        ConnectionDB connectionDB = new ConnectionDB();
        conn = connectionDB.Conn();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        NhanVien nv = arrnv.get(position);

        Log.d("CUSTOM_ADAPTER1", "Hiển thị: " + nv.getHovaten() + ", " + nv.getGioitinh());

        //ánh xạ
        TextView txttennv = rowView.findViewById(R.id.txttennv);
        TextView txtgioitinh = rowView.findViewById(R.id.txtgioitinh);
        ImageView imgnv = (ImageView) rowView.findViewById(R.id.imgnv);
        // lấy tên và giới tính xuất ra listview
        txttennv.setText(nv.getHovaten());
        txtgioitinh.setText(nv.getGioitinh());
        imgnv.setImageBitmap(StringToBitMap(nv.getAnhsnv()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NhapGv.class);
                intent.putExtra("Object",nv);
                context.startActivity(intent);
                Log.d("DEBUG_CUSTOM_ADAPTER", "manv: " + nv.getManv() + ", Họ tên: " + nv.getHovaten() + ", số điện thoại: " + nv.getPhone());

            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy mã nhân viên
                        String manv = nv.getManv();

                        // Log kiểm tra mã nhân viên
                        if (manv == null || manv.isEmpty()) {
                            Log.e("DEBUG_MANV", "Không lấy được mã nhân viên! Giá trị: " + manv);
                            Toast.makeText(context, "Không lấy được mã nhân viên!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("DEBUG_MANV", "Mã nhân viên cần xóa: " + manv);

                        try {
                            // Kết nối cơ sở dữ liệu
                            ConnectionDB connectionDB = new ConnectionDB();
                            Connection conn = connectionDB.Conn();

                            // Câu lệnh xóa nhân viên
                            String deleteQuery = "DELETE FROM tb_NhanVien WHERE manv = ?";
                            PreparedStatement stmt = conn.prepareStatement(deleteQuery);

                            // Gắn mã nhân viên vào câu lệnh SQL
                            stmt.setString(1, manv);

                            // Thực thi lệnh xóa
                            int affectedRows = stmt.executeUpdate();
                            if (affectedRows > 0) {
                                // Xóa khỏi danh sách và cập nhật lại ListView
                                arrnv.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóa nhân viên thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Không thể xóa nhân viên!", Toast.LENGTH_SHORT).show();
                            }

                            // Đóng kết nối
                            stmt.close();
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Log.e("DEBUG_ERROR", "Lỗi xóa nhân viên: " + e.getMessage());
                            Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });

        return rowView;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
