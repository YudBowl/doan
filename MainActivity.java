package com.example.ktraquanlymonan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> dsmonan;
    ArrayAdapter<String> adapter;
    SQLiteDatabase mydata;

    ListView hienthi;
    Button them, xoa, sua, tim;
    TextView mamm, tenmm, giamm;
    SearchView timkiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dsmonan = new ArrayList<>();

        timkiem = findViewById(R.id.timkiem);
        hienthi = findViewById(R.id.hienthinha);
        them = findViewById(R.id.btn_them);
        xoa = findViewById(R.id.btn_xoa);
        sua = findViewById(R.id.btn_sua);
        tim = findViewById(R.id.btn_tim);

        mamm = findViewById(R.id.edit_nhapma);
        tenmm = findViewById(R.id.edit_nhapten);
        giamm = findViewById(R.id.edit_nhapgia);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dsmonan
        );

        hienthi.setAdapter(adapter);

        mydata = openOrCreateDatabase("QLMA.db", MODE_PRIVATE, null);

        mydata.execSQL(
                "CREATE TABLE IF NOT EXISTS tblmonan(" +
                        "mamonan TEXT PRIMARY KEY," +
                        "tenmonan TEXT," +
                        "giaban REAL)"
        );

        // Nhận dữ liệu từ Activity thêm
        Intent intent = getIntent();
        String ma = intent.getStringExtra("ma_ma");
        String ten = intent.getStringExtra("ten_ma");
        double gia = intent.getDoubleExtra("gia_ma", -1);

        if (ma != null && ten != null && gia >= 0) {
            mydata.execSQL(
                    "INSERT OR REPLACE INTO tblmonan VALUES(?,?,?)",
                    new Object[]{ma, ten, gia}
            );
        }

        hienThiDuLieu();

        them.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivityThem.class))
        );

        tim.setOnClickListener(v ->
                adapter.getFilter().filter(timkiem.getQuery().toString())
        );

        xoa.setOnClickListener(v -> {
            String maXoa = mamm.getText().toString().trim();

            if (maXoa.isEmpty()) {
                Toast.makeText(this, "Chọn món ăn cần xóa", Toast.LENGTH_SHORT).show();
                return;
            }

            mydata.execSQL(
                    "DELETE FROM tblmonan WHERE mamonan=?",
                    new Object[]{maXoa}
            );

            clearInput();
            hienThiDuLieu();

            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
        });

        sua.setOnClickListener(v -> {
            String maSua = mamm.getText().toString().trim();
            String tenSua = tenmm.getText().toString().trim();
            String giaSua = giamm.getText().toString().trim();

            if (maSua.isEmpty() || tenSua.isEmpty() || giaSua.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            mydata.execSQL(
                    "UPDATE tblmonan SET tenmonan=?, giaban=? WHERE mamonan=?",
                    new Object[]{tenSua, Double.parseDouble(giaSua), maSua}
            );

            hienThiDuLieu();
            Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT).show();
        });

        hienthi.setOnItemClickListener((parent, view, position, id) -> {
            String item = dsmonan.get(position);

            String[] arr = item.split("\\|");

            String maClick = arr[0].replace("Mã món ăn:", "").trim();
            String tenClick = arr[1].replace("Tên món ăn:", "").trim();
            String giaClick = arr[2].replace("Giá:", "").trim();

            mamm.setText(maClick);
            tenmm.setText(tenClick);
            giamm.setText(giaClick);
        });
    }

    private void hienThiDuLieu() {
        dsmonan.clear();

        Cursor c = mydata.rawQuery("SELECT * FROM tblmonan", null);

        while (c.moveToNext()) {
            String ma = c.getString(0);
            String ten = c.getString(1);
            double gia = c.getDouble(2);

            dsmonan.add(
                    "Mã món ăn: " + ma +
                            " | Tên món ăn: " + ten +
                            " | Giá: " + gia
            );
        }

        c.close();
        adapter.notifyDataSetChanged();
    }

    private void clearInput() {
        mamm.setText("");
        tenmm.setText("");
        giamm.setText("");
    }
}