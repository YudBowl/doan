package com.example.ktraquanlymonan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivityThem extends AppCompatActivity {
    TextView ma,ten,gia;
    Button them;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_them);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ma=findViewById(R.id.edit_mamonan);
        ten=findViewById(R.id.edit_tenmonan);
        gia=findViewById(R.id.edit_gia);
        them=findViewById(R.id.btn_themmonan);

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityThem.this, MainActivity.class);
                String mamonan=ma.getText().toString().trim();
                String tenmonan=ten.getText().toString().trim();
                double giaban=Double.parseDouble(gia.getText().toString());
                intent.putExtra("ma_ma",mamonan);
                intent.putExtra("ten_ma",tenmonan);
                intent.putExtra("gia_ma",giaban);
                startActivity(intent);
            }
        });
    }
}