package com.wael.riayaapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wael.riayaapp.data.local.entity.User;

@Dao
public interface UserDao {

    //  إدخال مستخدم جديد
    @Insert
    long insertUser(User user);

    //  فحص هل البريد الإلكتروني موجود مسبقاً في قاعدة البيانات
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email LIMIT 1)")
    boolean isEmailExists(String email);

    //  فحص هل رقم الهاتف موجود مسبقاً في قاعدة البيانات
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE phone = :phone LIMIT 1)")
    boolean isPhoneExists(String phone);

    //  الحصول على بيانات المستخدم بواسطة البريد الإلكتروني
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    //  الحصول على بيانات المستخدم بواسطة رقم الهاتف
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    User getUserByPhone(String phone);

    //  استعلام تسجيل الدخول (باستخدام البريد أو الهاتف مع كلمة المرور بشرط أن يكون الحساب مفصلاً/مفعلاً)
    @Query("SELECT * FROM users WHERE (email = :identifier OR phone = :identifier) AND password = :password AND isVerified = 1 LIMIT 1")
    User login(String identifier, String password);
}