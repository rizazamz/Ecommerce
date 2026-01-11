package com.riza.ecommerce.data.local.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.riza.ecommerce.data.local.dao.CartDao;
import com.riza.ecommerce.data.local.entity.CartItem;

@Database(entities = {CartItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract CartDao cartDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "ecommerce_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}