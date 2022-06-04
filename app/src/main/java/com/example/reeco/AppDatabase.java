package com.example.reeco;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Server.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // 싱글톤 패턴 구현
    private static AppDatabase INSTANCE;

    // TODO: java.util.concurrent 이용해서 다시 짜기. 메인스레드에서 돌리는건 문제 소지가 있음.
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "reeco_db").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public abstract ServerDao serverDao();
}
