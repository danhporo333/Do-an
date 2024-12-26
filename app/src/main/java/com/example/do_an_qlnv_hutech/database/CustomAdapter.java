package com.example.do_an_qlnv_hutech.database;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.admin.NhapGv;
import com.example.do_an_qlnv_hutech.model.NhanVien;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<NhanVien> {
    private ArrayList<NhanVien> arrnv;
    private final Activity context;
    private RelativeLayout relativeLayout;

    public CustomAdapter(Activity context, ArrayList<NhanVien> arrnv) {
        super(context, R.layout.list_item, arrnv);
        Log.d("CUSTOM_ADAPTER", "CustomAdapter khởi tạo với dữ liệu: " + arrnv.size());
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arrnv = arrnv;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
//        View rowView2 = inflater.inflate(R.layout.activity_listview, null, true);
        NhanVien nv = arrnv.get(position);

        Log.d("CUSTOM_ADAPTER", "Hiển thị: " + nv.getHovaten() + ", " + nv.getGioitinh());

        //ánh xạ
        TextView txttennv = rowView.findViewById(R.id.txttennv);
        TextView txtgioitinh = rowView.findViewById(R.id.txtgioitinh);
        RelativeLayout relativeLayout  = rowView.findViewById(R.id.listNhanvien);
//        relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getApplicationContext(), NhapGv.class);
//                startActivity(in);
//            }
//        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NhapGv.class);
                context.startActivity(intent);
            }
        });


        txttennv.setText(nv.getHovaten());
        txtgioitinh.setText(nv.getGioitinh());

        return rowView;
    }
}
