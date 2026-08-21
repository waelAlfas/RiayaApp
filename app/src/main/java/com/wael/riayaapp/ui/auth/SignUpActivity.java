package com.wael.riayaapp.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wael.riayaapp.R;
import com.wael.riayaapp.data.local.AppDatabase;
import com.wael.riayaapp.data.local.dao.UserDao;
import com.wael.riayaapp.data.local.entity.User;

import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    // العناصر البرمجية
    private Button btnTabEmail, btnTabPhone, btnRegister;
    private TextInputLayout layoutFullName, layoutEmail, layoutPhone, layoutPassword, layoutConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private MaterialCheckBox cbTerms;
    private TextView tvLoginLink;
    private ProgressBar progressBar;
    private ViewGroup mainContainer;

    // قاعدة البيانات
    private UserDao userDao;

    // متغير لتحديد الوضع الحالي (الافتراضي يكون البريد الإلكتروني)
    private boolean isEmailMode = true;

    // نمط التحقق من الاسم (أحرف مسافات فقط)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} ]+$");

    // نمط كلمة المرور (8 أحرف + حرف كبير + حرف صغير + رقم)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    // نمط رقم الجوال اليمني (يتكون من 9 أرقام ويبدأ بـ 7)
    private static final Pattern YEMEN_PHONE_PATTERN = Pattern.compile("^(71|73|77|78|70)\\d{7}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // تهيئة DAO
        userDao = AppDatabase.getInstance(this).userDao();

        initViews();
        setupListeners();
    }

    private void initViews() {
        mainContainer = findViewById(R.id.mainContainer);

        btnTabEmail = findViewById(R.id.btnTabEmail);
        btnTabPhone = findViewById(R.id.btnTabPhone);
        btnRegister = findViewById(R.id.btnRegister);

        layoutFullName = findViewById(R.id.layoutFullName);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword = findViewById(R.id.layoutConfirmPassword);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        cbTerms = findViewById(R.id.cbTerms);
        tvLoginLink = findViewById(R.id.tvLoginLink);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnTabEmail.setOnClickListener(v -> switchMode(true));
        btnTabPhone.setOnClickListener(v -> switchMode(false));
        btnRegister.setOnClickListener(v -> performValidation());

        tvLoginLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

// 👁 التحكم في إظهار وإخفاء كلمة المرور يدويًا
        layoutPassword.setEndIconOnClickListener(v -> {
            if (etPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                layoutPassword.setEndIconDrawable(com.google.android.material.R.drawable.design_ic_visibility);
            } else {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                layoutPassword.setEndIconDrawable(com.google.android.material.R.drawable.design_ic_visibility_off);
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        // 👁 التحكم في إظهار وإخفاء تأكيد كلمة المرور يدويًا
        layoutConfirmPassword.setEndIconOnClickListener(v -> {
            if (etConfirmPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                layoutConfirmPassword.setEndIconDrawable(com.google.android.material.R.drawable.design_ic_visibility);
            } else {
                etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                layoutConfirmPassword.setEndIconDrawable(com.google.android.material.R.drawable.design_ic_visibility_off);
            }
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });
    }

    private void switchMode(boolean isEmail) {
        if (isEmailMode == isEmail) return;
        isEmailMode = isEmail;
        clearErrors();

        if (mainContainer != null) {
            AutoTransition transition = new AutoTransition();
            transition.setDuration(100);
            TransitionManager.beginDelayedTransition(mainContainer, transition);
        }

        if (isEmail) {
            layoutEmail.setVisibility(View.VISIBLE);
            layoutPhone.setVisibility(View.GONE);

            btnTabEmail.setBackgroundResource(R.drawable.btn_primary_bg);
            btnTabEmail.setTextColor(ContextCompat.getColor(this, R.color.surface_white));

            btnTabPhone.setBackgroundColor(Color.TRANSPARENT);
            btnTabPhone.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else {
            layoutPhone.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.GONE);

            btnTabPhone.setBackgroundResource(R.drawable.btn_primary_bg);
            btnTabPhone.setTextColor(ContextCompat.getColor(this, R.color.surface_white));

            btnTabEmail.setBackgroundColor(Color.TRANSPARENT);
            btnTabEmail.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }
    }

    private void performValidation() {
        clearErrors();

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean isValid = true;

        // 1. التحقق من الاسم الكامل
        if (TextUtils.isEmpty(fullName)) {
            layoutFullName.setError("الاسم الكامل مطلوب");
            isValid = false;
        } else if (!NAME_PATTERN.matcher(fullName).matches()) {
            layoutFullName.setError("يرجى إدخال اسم صحيح بدون أرقام أو رموز");
            isValid = false;
        }

        // 2. التحقق من البريد أو الجوال
        if (isEmailMode) {
            if (TextUtils.isEmpty(email)) {
                layoutEmail.setError("البريد الإلكتروني مطلوب");

                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                layoutEmail.setError("صيغة البريد الإلكتروني غير صحيحة");
                isValid = false;
            }
        } else {
            if (TextUtils.isEmpty(phone)) {
                layoutPhone.setError("رقم الجوال مطلوب");
                isValid = false;
            } else if (!YEMEN_PHONE_PATTERN.matcher(phone).matches()) {
                layoutPhone.setError("يرجى إدخال رقم جوال صحيح مكون من 9 أرقام يبدأ بـ 7 (مثال: 714531842)");
                isValid = false;
            }
        }

        // 3. التحقق من كلمة المرور
        if (TextUtils.isEmpty(password)) {
            layoutPassword.setError("كلمة المرور مطلوبة");
            isValid = false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            layoutPassword.setError("كلمة المرور يجب أن تتكون من 8 أحرف على الأقل، وتحتوي على حرف كبير، حرف صغير، ورقم");
            isValid = false;
        }

        // 4. التحقق من تأكيد كلمة المرور
        if (TextUtils.isEmpty(confirmPassword)) {
            layoutConfirmPassword.setError("تأكيد كلمة المرور مطلوب");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            layoutConfirmPassword.setError("كلمة المرور غير متطابقة");
            isValid = false;
        }

        // 5. التحقق من الموافقة على الشروط
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "يجب الموافقة على الشروط والأحكام لمتابعة التسجيل", Toast.LENGTH_LONG).show();
            isValid = false;
        }

        if (isValid) {
            executeRegistration(fullName, isEmailMode ? email : null, !isEmailMode ? phone : null, password);
        }
    }

    private void clearErrors() {
        if (layoutFullName != null) layoutFullName.setError(null);
        if (layoutEmail != null) layoutEmail.setError(null);
        if (layoutPhone != null) layoutPhone.setError(null);
        if (layoutPassword != null) layoutPassword.setError(null);
        if (layoutConfirmPassword != null) layoutConfirmPassword.setError(null);
    }

    private void executeRegistration(String fullName, String email, String phone, String password) {
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        Executors.newSingleThreadExecutor().execute(() -> {
            // فحص عدم تكرار البريد الإلكتروني أو رقم الهاتف في Room
            boolean isEmailTaken = email != null && userDao.isEmailExists(email);
            boolean isPhoneTaken = phone != null && userDao.isPhoneExists(phone);

            runOnUiThread(() -> {
                if (isEmailTaken) {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    layoutEmail.setError("البريد الإلكتروني مستخدم مسبقاً");
                } else if (isPhoneTaken) {
                    progressBar.setVisibility(View.GONE);
                    btnRegister.setEnabled(true);
                    layoutPhone.setError("رقم الجوال مستخدم مسبقاً");
                } else {
                    // إنشاء كائن المستخدم وحفظه مباشرة في قاعدة البيانات
                    User newUser = new User(fullName, email, phone, password);

                    Executors.newSingleThreadExecutor().execute(() -> {
                        userDao.insertUser(newUser);

                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(SignUpActivity.this, "تم إنشاء الحساب بنجاح!", Toast.LENGTH_SHORT).show();

                            // الانتقال المباشر إلى شاشة تسجيل الدخول
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }
            });
        });
    }
}