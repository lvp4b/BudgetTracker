package edu.umkc.lvp4b.budgettracker.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    List<TransactionAndItems> getAll();

    @Query("SELECT * FROM lineItem WHERE transactionId = :transactionId")
    List<LineItemEntity> getAllLineItems(int transactionId);

    @Insert
    long insert(TransactionEntity transaction);

    @Update
    void update(TransactionEntity transaction);

    @Delete
    void delete(TransactionEntity transaction);

    @Insert
    void insert(List<LineItemEntity> lineItems);

    @Delete
    void delete(List<LineItemEntity> lineItems);
}
