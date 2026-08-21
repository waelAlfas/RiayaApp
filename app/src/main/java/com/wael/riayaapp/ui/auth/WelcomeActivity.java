package com.wael.riayaApp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wael.riayaapp.R;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnCreateAccount, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // ربط العناصر مع الـ XML
        btnCreateAccount = findViewById(R.id.btnCreateAccount); // أو معرف الزر الخاص بك في XML
        btnLogin = findViewById(R.id.btnLogin);

        // الانتقال عند النقر على "إنشاء حساب جديد"
        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // الانتقال عند النقر على "لديك حساب؟ تسجيل الدخول"
        btnLogin.setOnClickListener(v -> {
            // Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            // startActivity(intent);
        });
    }
}