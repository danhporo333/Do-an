package com.example.do_an_qlnv_hutech.admin;

import static android.Manifest.permission_group.CAMERA;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an_qlnv_hutech.R;
import com.example.do_an_qlnv_hutech.database.ConnectionDB;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddGV extends AppCompatActivity {
    Connection conn; // Kết nối cơ sở dữ liệu
    EditText etHoTenGV,etngaysinh,etSoDienThoai,etcancuoc,etdiachi;
    RadioButton rbNam,rbNu;
    String anhnv = "";
    ImageView imgnv;
    Button btnLuu2;
    Bitmap myBitmap;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

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
        imgnv = findViewById(R.id.imgnv);
        btnLuu2 = findViewById(R.id.btnLuu2);

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
        btnLuu2.setOnClickListener(new View.OnClickListener() {
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
                String str="";
                if( myBitmap!=null){
                    str= BitMapToString(myBitmap);
                    Log.d("anhchuoi",str);
                }else {
                    str="";
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
                            String sql = "INSERT INTO tb_NHANVIEN (HOTEN, GIOITINH, NGAYSINH, DIENTHOAI, CCCD, DIACHI, HINHANH) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement st = conn.prepareStatement(sql);
                            st.setString(1, hovaten);
                            st.setString(2, gt);
                            st.setString(3, datetime); // Sử dụng datetime đã chuyển đổi
                            st.setString(4, sodienthoai);
                            st.setString(5, CCCD);
                            st.setString(6, diachi);
                            st.setString(7,str);
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

    // khởi tạo icon camera
    @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == R.id.camera) {
            String masv = etHoTenGV.getText().toString();
            String tensv = etngaysinh.getText().toString();
//            if(!masv.equals("") || !tensv.equals("")) {
//
//            } else {
//                Toast.makeText(getApplicationContext(), "vui lòng điền dữ liệu trước khi chụp hình", Toast.LENGTH_SHORT).show();
//            }
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
            permissions.add(CAMERA);
            permissionsToRequest = findUnAskedPermissions(permissions);
            startActivityForResult(getPickImageChooserIntent(), 200);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }

        }
        allIntents.add(0,captureIntent);

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);

        }
        Intent chooserIntent = Intent.createChooser(captureIntent, "Select source");
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, captureIntent);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        Log.v("allIntents",""+allIntents.size());
        return chooserIntent;
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public Uri getPickImageResultUri(Intent data) {
        Uri outputFileUri = null;
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return outputFileUri;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    // nhận hình ảnh trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "requestCode: " + requestCode + ", resultCode: " + resultCode);
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    imgnv.setImageBitmap(myBitmap);
                    String chuoi = BitMapToString(myBitmap);
                    Log.d("chuoimahoa",chuoi);
                    anhnv = chuoi;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                String chuoi = BitMapToString(myBitmap);
                Log.d("chuoimahoa",chuoi);
                imgnv.setImageBitmap(myBitmap);
            }
        }
    }
}