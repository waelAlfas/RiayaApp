package com.wael.riayaapp.data.repository;

import android.content.Context;
import com.wael.riayaapp.data.local.AppDatabase;
import com.wael.riayaapp.data.local.dao.UserDao;
import com.wael.riayaapp.data.local.entity.User;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
    }

    // التحقق من وجود المستخدم
    public void checkUserExists(String email, String phone, Callback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean exists = (email != null && userDao.isEmailExists(email)) ||
                    (phone != null && userDao.isPhoneExists(phone));
            callback.onResult(exists);
        });
    }

    // حفظ المستخدم الجديد
    public void registerUser(User user, Callback<Void> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            userDao.insertUser(user);
            callback.onResult(null);
        });
    }

    public interface Callback<T> {
        void onResult(T result);
    }
}