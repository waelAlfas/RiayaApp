package com.wael.riayaApp.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateUser(UserEntity user);

    // البحث عن المستخدم للتحقق عند الدخول
    @Query("SELECT * FROM users WHERE emailOrPhone = :identifier LIMIT 1")
    UserEntity getUser(String identifier);

    // جلب الجلسة المحفوظة عند تفعيل "تذكرني"
    @Query("SELECT * FROM users WHERE isRememberMe = 1 LIMIT 1")
    UserEntity getRememberedUser();

    // مسح الجلسة (تسجيل الخروج)
    @Query("DELETE FROM users")
    void clearAllUsers();
}