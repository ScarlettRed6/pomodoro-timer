package com.example.pomodoro_timer.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.pomodoro_timer.data.data_access_objects.CategoryDao;
import com.example.pomodoro_timer.data.data_access_objects.PomodoroLogDao;
import com.example.pomodoro_timer.data.data_access_objects.StatsDao;
import com.example.pomodoro_timer.data.data_access_objects.TaskDao;
import com.example.pomodoro_timer.data.data_access_objects.UserDao;
import com.example.pomodoro_timer.model.CategoryModel;
import com.example.pomodoro_timer.model.PomodoroLogModel;
import com.example.pomodoro_timer.model.StatsModel;
import com.example.pomodoro_timer.model.TaskModel;
import com.example.pomodoro_timer.model.UserModel;

@Database(
        entities = { CategoryModel.class, TaskModel.class, UserModel.class, StatsModel.class, PomodoroLogModel.class },
        version = 9
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "pomodoro_timer_db";
    private static AppDatabase INSTANCE;
    public abstract CategoryDao categoryDao();
    public abstract TaskDao taskDao();
    public abstract UserDao userDao();
    public abstract StatsDao statsDao();
    public abstract PomodoroLogDao pomodoroLogDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DB_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }//End of getInstance method
}
