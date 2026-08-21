package com.wael.riayaapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wael.riayaapp.data.local.dao.UserDao;
import com.wael.riayaapp.data.local.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // اسم قاعدة البيانات في النظام المحلي
    private static final String DATABASE_NAME = "riaya_database";

    // متغيرةSingleton لقاعدة البيانات
    private static volatile AppDatabase INSTANCE;

    // ربط الـ DAO بقاعدة البيانات
    public abstract UserDao userDao();

    // دالة للحصول على نسخة قاعدة البيانات (Thread-Safe)
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            // نقل الترحيلات (Migrations) في حال تغيير الإصدار
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}