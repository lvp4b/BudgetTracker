package edu.umkc.lvp4b.budgettracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TransactionEntity.class, LineItemEntity.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransactionDao transactionDao();

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
