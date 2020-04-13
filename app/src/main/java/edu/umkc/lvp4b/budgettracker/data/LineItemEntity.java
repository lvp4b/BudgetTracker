package edu.umkc.lvp4b.budgettracker.data;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(tableName = "lineItem", primaryKeys = {"transactionId", "order"})
public class LineItemEntity {
    @ColumnInfo(name = "transactionId")
    public int transactionId;

    @ColumnInfo(name = "order")
    public int order;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "categoryId")
    public int categoryId;

    public int getOrder() {return order;}
}
