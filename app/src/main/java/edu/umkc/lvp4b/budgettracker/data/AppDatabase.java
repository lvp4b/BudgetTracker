package edu.umkc.lvp4b.budgettracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TransactionEntity.class, LineItemEntity.class, CategoryEntity.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();

    public abstract CategoryDao categoryDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context applicationContext) {
        if (instance != null){
            return instance;
        }

        instance = Room.databaseBuilder(applicationContext, AppDatabase.class, "budget-tracker")
                .fallbackToDestructiveMigration()
                .build();
        return instance;
    }
}
