package edu.umkc.lvp4b.budgettracker.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "transaction")
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public double amount;
}
