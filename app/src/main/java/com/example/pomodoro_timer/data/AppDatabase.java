package com.example.pomodoro_timer.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pomodoro_timer.data.data_access_objects.CategoryDao;
import com.example.pomodoro_timer.data.data_access_objects.TaskDao;
import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.model.TaskModel;

@Database(
        entities = { CategoryModel.class, TaskModel.class },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "pomodoro_timer_db";
    private static AppDatabase INSTANCE;
    public abstract CategoryDao categoryDao();
    public abstract TaskDao taskDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
