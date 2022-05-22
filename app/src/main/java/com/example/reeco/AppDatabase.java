package com.example.reeco;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Server.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // 싱글톤 패턴 구현
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "reeco_db").build();
        }
        return INSTANCE;
    }

    public abstract ServerDao serverDao();
}
