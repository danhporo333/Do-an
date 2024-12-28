package com.example.do_an_qlnv_hutech.database;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.model.LichGV;
import com.example.do_an_qlnv_hutech.model.NhanVien;

import java.sql.Connection;
import java.util.ArrayList;

public class AdapterLich extends ArrayAdapter<LichGV> {
    private ArrayList<LichGV> arrlich;
    private final Activity context;
    Connection conn;

    public AdapterLich(Activity context, ArrayList<LichGV> arrlich) {
        super(context, R.layout.list_time_table, arrlich);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arrlich = arrlich;
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

        //ánh xạ
        TextView txtthu = rowView.findViewById(R.id.tvThu);
        TextView txttiethoc = rowView.findViewById(R.id.tvTietValue);
        TextView txtMonhoc = rowView.findViewById(R.id.tvMonHocValue);
        TextView txtphong = rowView.findViewById(R.id.tvPhongHocValue);
        TextView txtlop = rowView.findViewById(R.id.tvLopHocValue);
        //  xuất ra listview
        txtthu.setText(lich.getThu());
        txttiethoc.setText(lich.getCahoc());
        txtMonhoc.setText(lich.getMonhoc());
        txtlop.setText(lich.getLop());
        return rowView;
    }
}
